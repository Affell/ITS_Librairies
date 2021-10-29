package net.kio.its.server.events;

import net.kio.its.server.ServerWorker;
import net.kio.its.event.Event;

public class ConnectionProtocolSuccessEvent extends Event {

    private final ServerWorker serverWorker;

    public ConnectionProtocolSuccessEvent(ServerWorker serverWorker) {
        this.serverWorker = serverWorker;
    }

    public ServerWorker getServerWorker() {
        return serverWorker;
    }
}
