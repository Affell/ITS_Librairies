package net.kio.its.server.events;

import net.kio.its.server.ServerWorker;
import net.kio.its.event.Event;

public class ClientDisconnectEvent extends Event {

    private final ServerWorker serverWorker;

    public ClientDisconnectEvent(ServerWorker serverWorker) {
        this.serverWorker = serverWorker;
    }

    public ServerWorker getServerWorker() {
        return serverWorker;
    }
}
