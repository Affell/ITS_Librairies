package net.kio.its.client.events;

import net.kio.its.client.SocketWorker;

import java.util.UUID;

public class EncryptedDataReceivedEvent extends DataEvent {

    private String decryptedData;

    public EncryptedDataReceivedEvent(SocketWorker socketWorker, String data, String decryptedData, UUID messageUUID) {
        super(socketWorker, data, messageUUID);
    }

    public String getDecryptedData() {
        return decryptedData;
    }
}
