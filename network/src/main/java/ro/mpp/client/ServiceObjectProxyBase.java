package ro.mpp.client;

import ro.mpp.ValidationException;
import ro.mpp.objectprotocol.IRequest;
import ro.mpp.objectprotocol.IResponse;
import ro.mpp.objectprotocol.UpdateResponse;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class ServiceObjectProxyBase {
    private final String host;
    private final int port;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;
    private final BlockingQueue<IResponse> qresponses = new LinkedBlockingQueue<IResponse>();
    private volatile boolean finished;

    public ServiceObjectProxyBase(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void initializeConnection() {
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void startReader() {
        var readerThread = new Thread(new ServiceObjectProxyBase.ReaderThread());
        readerThread.start();
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Object response = input.readObject();
                    System.out.println("Response received: " + response);
                    if(response instanceof UpdateResponse) {
                        handleUpdate((UpdateResponse) response);
                    } else {
                        try {
                            qresponses.put((IResponse) response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (EOFException e) {
                    continue;
                } catch(IOException e) {
                    System.out.println("Reading error: " + e);
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error: " + e);
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Reader thread stopped");
        }
    }

    protected void sendRequest(IRequest request) throws SendRequestException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new SendRequestException("Error sending object " + e);
        }
    }

    protected IResponse readResponse() throws ReceiveResponseException {
        IResponse response = null;
        try {
            response = qresponses.take();
        } catch (InterruptedException e) {
            throw new ReceiveResponseException("Error receiving object " + e);
        }
        return response;
    }

    protected void testConnectionOpen() throws ValidationException {
        if(connection == null) {
            throw new ValidationException("Connection is not open");
        }
    }

    public void closeConnection() {
        System.out.println("Closing connection");
        finished = true;
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            input.close();
            output.close();
            connection.close();
            connection = null;
            System.out.println("Connection closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract void handleUpdate(UpdateResponse update);
}
