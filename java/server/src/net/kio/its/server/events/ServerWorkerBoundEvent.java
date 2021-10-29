package net.kio.its.server.events;

import net.kio.its.server.ServerWorker;
import net.kio.its.event.Cancellable;
import net.kio.its.event.Event;

public class ServerWorkerBoundEvent extends Event implements Cancellable {

    private final ServerWorker serverWorker;
    private boolean cancelled;

    public ServerWorkerBoundEvent(ServerWorker serverWorker) {
        this.serverWorker = serverWorker;
    }

    public ServerWorker getServerWorker() {
        return serverWorker;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
