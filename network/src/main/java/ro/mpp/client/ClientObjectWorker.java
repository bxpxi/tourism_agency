package ro.mpp.client;

import ro.mpp.Flight;
import ro.mpp.IAppService;
import ro.mpp.ValidationException;
import ro.mpp.dto.DTOFactory;
import ro.mpp.objectprotocol.*;
import ro.mpp.observer.Observer;
import ro.mpp.utils.Stringifier;

import java.io.*;
import java.net.Socket;
import java.rmi.ServerException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.stream.StreamSupport;

public class ClientObjectWorker implements Observer, Runnable {
    private final IAppService server;
    private final Socket connection;
    private final OutputStream output;
    private final InputStream input;
    private volatile boolean connected;

    public ClientObjectWorker(IAppService server, Socket connection) {
        this.server = server;
        this.connection = connection;
        InputStream in = null;
        OutputStream out = null;

        try {
            out = connection.getOutputStream();
            out.flush();
            in = connection.getInputStream();
            connected = true;
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        input = in;
        output = out;
    }

    byte[] intToByteArray(int value) {
        byte[] result = new byte[4];
        result[0] = (byte) (value & 0xFF); value>>=8;
        result[1] = (byte) (value & 0xFF); value>>=8;
        result[2] = (byte) (value & 0xFF); value>>=8;
        result[3] = (byte) (value & 0xFF);
        return result;
    }

    private int byteArrayToInt(byte[] arr) {
        System.out.println("ARR = "+arr[0]+" "+arr[1]+" "+arr[2]+" "+arr[3]);
        return ((arr[0]&0xFF) | (((arr[1]&0xFF))<<8)
                | ((arr[2]&0xFF)<<16) | ((arr[3]&0xFF)<<24));
    }

    private void writeInt(int value) throws IOException {
        output.write(intToByteArray(value));
    }

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    private void writeString(String str) throws IOException {
        str=str.replace("\0","");
        var buffer = StandardCharsets.UTF_8.encode(str).array();

        System.out.println(buffer.length+" "+ str);
        System.out.println(bytesToHex(buffer));
        writeInt(buffer.length);
        output.write(buffer);
    }

    private String readString() throws IOException {
        int len = byteArrayToInt(input.readNBytes(4));
        byte[] buffer = input.readNBytes(len);
        return StandardCharsets.UTF_8.decode(ByteBuffer.wrap(buffer))
                .toString().replace("\0","");
    }

    @Override
    public void run() {
        while(connected) {
            try {
                String reqStr = readString();
                System.out.println("Received request: " + reqStr);
                IRequest request = Stringifier.decode(reqStr, IRequest.class);
                IResponse response = handleRequest(request);
                if(response != null) {
                    sendResponse(response);
                }
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            input.close();
            output.close();
            connection.close();
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    private IResponse handleRequest(IRequest request) {
        System.out.println("Handling request: " + request);

        IResponse response = null;
        if(request instanceof GetAllFlightsRequest) {
            System.out.println("Handling GetAllFlightsRequest");
            try {
                response = new GetAllFlightsResponse(StreamSupport.stream(server.getAllFlights().spliterator(), false).toArray(Flight[]::new));
            } catch (Exception e) {
                response = new ErrorResponse(e.getMessage());
            }
        }

        if(request instanceof FilterFlightsRequest) {
            System.out.println("Handling FilterFlightsRequest");
            try {
                var flights = server.findByDestinationAndDepartureDate(((FilterFlightsRequest) request).getDestination(), ((FilterFlightsRequest) request).getDate());
                response = new FilterFlightsResponse(StreamSupport.stream(flights.spliterator(), false).toArray(Flight[]::new));
            } catch(Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                response = new ErrorResponse(e.getMessage());
            }
        }

        if(request instanceof LoginEmployeeRequest) {
            System.out.println("Handling LoginEmployeeRequest");
            try {
                var employee = server.login(((LoginEmployeeRequest) request).getAgencyName(), ((LoginEmployeeRequest) request).getPassword(), this);
                response = new LoginEmployeeResponse(DTOFactory.getDTO(employee));
            } catch(Exception e) {
                response = new ErrorResponse(e.getMessage());
            }
        }

        if(request instanceof BuyTicketRequest) {
            System.out.println("Handling BuyTicketRequest");
            try {
                var flight = DTOFactory.fromDTO(((BuyTicketRequest) request).getTicket().getFlight());
                var client = ((BuyTicketRequest) request).getTicket().getClient();
                var noOfSeats = ((BuyTicketRequest) request).getTicket().getNoOfSeats();
                System.out.println(server.getClass());
                server.buyTicket(client, flight, noOfSeats);
                response = new BuyTicketResponse();
            } catch (ValidationException e) {
                response = new ErrorResponse(e.getMessage());
            }

            if(response == null) {
                response = new ErrorResponse("Invalid request");
            }
        }

        return response;
    }

    private void sendResponse(IResponse response) throws IOException {
        System.out.println("Sending response: " + response);
        String strigifiedResponse = Stringifier.encode(response);
        synchronized (output) {
            writeString(strigifiedResponse);
            output.flush();
        }
    }

    @Override
    public void updatedFlight(Flight flight) {
        System.out.println("Worker: Updating flight: " + flight);
        var flightDTO = DTOFactory.getDTO(flight);
        try {
            sendResponse(new UpdatedFlightResponse(flightDTO));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
