package ro.mpp.server;

import ro.mpp.IAppService;
import ro.mpp.client.ClientObjectWorker;

import java.net.Socket;

public class ObjectConcurrentServer extends ConcurrentServer {
    private final IAppService server;

    public ObjectConcurrentServer(int port, IAppService server) {
        super(port);
        this.server = server;
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientObjectWorker worker = new ClientObjectWorker(server, client);
        return new Thread(worker);
    }
}
