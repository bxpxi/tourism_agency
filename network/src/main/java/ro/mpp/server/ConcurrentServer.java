package ro.mpp.server;

import java.net.Socket;

public abstract class ConcurrentServer extends AbstractServer {
    public ConcurrentServer(int port) {
        super(port);
    }

    @Override
    protected void processRequest(Socket client) {
        Thread t = createWorker(client);
        t.start();
    }

    protected abstract Thread createWorker(Socket client);
}
