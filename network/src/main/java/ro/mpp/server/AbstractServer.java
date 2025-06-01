package ro.mpp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.ServerException;

public abstract class AbstractServer {
    private final int port;
    private ServerSocket serverSocket = null;

    public AbstractServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                System.out.println("Waiting for clients...");
                Socket client = serverSocket.accept();
                System.out.println("Client connected");
                processRequest(client);
            }
        } catch (IOException e) {
            throw new ServerException("Starting server error");
        } finally {
            if (serverSocket != null) {
                stop();
            }
        }
    }

    public void stop() throws ServerException {
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new ServerException("Closing server error");
        }
    }

    protected abstract void processRequest(Socket client);
}
