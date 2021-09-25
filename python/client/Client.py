import platform
import random

from getmac import get_mac_address as gma

from SocketWorker import SocketWorker
from log_system import Logger


class Client:
    def __init__(self, name: str, debug: bool = False):
        self.name = name
        self.version = "1.1.0"
        self.pythonVersion = "python-" + str(platform.python_version())
        self.macAddress = self.getMacAddress()
        self.debug = debug
        self.socketWorkerList = []
        self.logger = Logger.Logger(self)

    def getMacAddress(self):
        macAddress = gma()
        if macAddress == None:
            return self.randomMacAddress()
        return macAddress

    def randomMacAddress(self):
        macAddr = []
        for i in range(6):
            randStr = "".join(random.sample("0123456789abcdef", 2))
            macAddr.append(randStr)
        return ":".join(macAddr)

    def addSocketWorker(self, ip: str, port: int):
        socketWorkerObj = SocketWorker(self, ip, port)
        self.socketWorkerList.append(socketWorkerObj)
        return socketWorkerObj

    def getVersion(self):
        return self.version

    def getPythonVersion(self):
        return self.pythonVersion


client = Client("Main Client", True)
socketWorker = client.addSocketWorker("localhost", 40000)
socketWorker.startWorker()

# connectSocket("localhost", 40000)

# msgsend = jpysocket.jpyencode("version:1.1.0")  # Encript The Msg
# socket.send(msgsend)  # Send Msg
# msgrecv = socket.recv(1024)  # Recieve msg
# msgrecv = jpysocket.jpydecode(msgrecv)  # Decript msg
# print("From Server: ", msgrecv)
# socket.close()  # Close connection
# print("Connection Closed.")
