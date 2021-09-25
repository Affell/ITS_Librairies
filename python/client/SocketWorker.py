import socket as socketlib
import threading
import uuid

from response_system import IResponseWorker, Response
from SocketStreamReader import SocketStreamReader
from data_encryption import KeyGenerator, EncryptionManager


class SocketWorker(IResponseWorker.IResponseWorker):
    def __init__(self, client, ip, port):
        IResponseWorker.IResponseWorker.__init__(self)
        self.name = "Socket Worker #" + str(self.native_id)
        self.client = client
        self.ip = ip
        self.port = port
        self.socket = None
        self.connected = False
        self.reader = None
        self.keyGenerator = KeyGenerator.KeyGenerator()
        self.commandPrefix = None
        self.requestSeparator = None
        self.connectionProtocol = False

    def startWorker(self):
        self.keyGenerator.generateKeys(False, True)
        self.__connectSocket()
        self.start()

    def stopWorker(self):
        if self.connected:
            self.sendCommand("disconnect")
            self.interrupt()

    def __connectSocket(self):
        self.socket = socketlib.socket()
        self.socket.connect((self.ip, self.port))
        self.reader = SocketStreamReader(self.socket)
        self.connected = True
        self.client.logger.log("Socket is connected")
        # socket.send(bytes("env:" + self.client.getPythonVersion() + "\n", 'utf-8'))
        # socket.send(b"version:1.1.0\n")
        # socket.close()

    def __disconnectSocket(self):
        if self.socket is not None:
            self.socket.close()
            self.connected = False
            self.interrupt()
            self.socket = None

    def run(self) -> None:
        self.__handleServerSocket()

    def __handleServerSocket(self):
        self.__startConnectionProtocol()
        t = threading.currentThread()
        while getattr(t, "do_run", True):
            line = self.reader.readLine()
            if line is not None:
                self.__onLineRead(line)
            else:
                break
        self.__disconnectSocket()

    def __onLineRead(self, line: str):
        decryptedData = None
        if self.keyGenerator.secretKeyBytes is not None:
            decryptedData = EncryptionManager.decrypt(line, self.keyGenerator.getSecretKey())
        if decryptedData is not None:
            self.__onDataReceived([decryptedData, line], True)
        else:
            self.__onDataReceived([line], False)

    def __onDataReceived(self, data: list, encrypted: bool):
        if data[0].startswith("response:"):
            msgUUID = uuid.UUID(data[0][9:45])
            data[0] = data[0][45:]
            self.client.logger.log(self.getName() + " - Response received for UUID " + str(msgUUID))
            self.responseManager.onResponseReceived(msgUUID, data[0])
        else:
            msgUUID = uuid.UUID(data[0][:36])
            data[0] = data[0][36:]

        if self.commandPrefix is not None and str(data[0]).startswith(self.commandPrefix):
            self.__onCommandReceived(data[0], msgUUID, encrypted)
        else:
            self.client.logger.log(self.getName() +
                                   " - Data Received (encrypted=" + str(encrypted) + ", uuid=" + str(msgUUID) + ") : "
                                   + data[0])

            # todo event system

            if self.connectionProtocol and not (self.__onConnectionProtocolDataReceived(data[0])):
                self.client.logger.log(self.getName() + " - Unable to connect to the server")
                self.stopWorker()

    def __onCommandReceived(self, command: str, msgUUID: uuid.UUID, encrypted: bool):
        self.client.logger.log(self.getName() +
                               " - Command Received (encrypted=" + str(encrypted) + ", uuid=" + str(msgUUID) + ") : "
                               + command)

        # todo event system

    def __startConnectionProtocol(self):
        self.connectionProtocol = True
        self.sendData("version:" + self.client.getVersion(), encrypt=False)

    def __onConnectionProtocolDataReceived(self, data: str) -> bool:
        if data.startswith("version:"):
            version = bool(data[8:])
            if not version:
                return False
            self.sendData("macAddress:" + self.client.getMacAddress(), encrypt=False)
            self.sendData("publicKey:" + self.keyGenerator.getStringPublicKey(), encrypt=False)
        elif data.startswith("separator:"):
            self.requestSeparator = data[10:]
        elif data.startswith("commandPrefix:"):
            self.requestSeparator = data[14:]
        elif data.startswith("secretKey:"):
            self.keyGenerator.secretKeyBytes = EncryptionManager.decryptSecretKey([data[10:]],
                                                                                  self.keyGenerator.privateKey)
            self.keyGenerator.secretKeyNonce = b''
            connected = self.keyGenerator.secretKeyBytes is not None
            self.sendData("connected:" + str(connected), encrypt=False)
            self.connectionProtocol = not (connected)
            if connected:
                # todo event system
                pass
            return connected
        else:
            return False
        return True

    def sendData(self, data: str, msgUUID: uuid.UUID = None, encrypt: bool = True):
        rawData = str(data)
        response = Response.Response(self, msgUUID)
        if msgUUID is not None:
            data = "response:" + data
        data = str(response.msgUUID) + data
        if encrypt:
            data = EncryptionManager.encrypt(data, self.keyGenerator.getSecretKey())
        self.socket.send(bytes(data + "\n", 'utf-8'))
        self.client.logger.log(self.getName() +
                               " - Data sent (encrypted=" + str(encrypt) + ", uuid=" + str(response.msgUUID) + ") : "
                               + rawData)
        return response

    def sendCommand(self, command: str, args: tuple = None):
        return self.sendData(self.commandPrefix + command + self.requestSeparator + self.requestSeparator.join(args))

    def interrupt(self):
        setattr(threading.currentThread(), 'do_run', False)
