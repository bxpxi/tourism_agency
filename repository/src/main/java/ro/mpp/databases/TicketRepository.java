package ro.mpp.databases;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp.Flight;
import ro.mpp.Ticket;
import ro.mpp.JdbcUtils;
import ro.mpp.interfaces.ITicketRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TicketRepository implements ITicketRepository {
    private JdbcUtils dbUtils;
    private FlightRepository flightRepo;

    private static final Logger logger = LogManager.getLogger();

    public TicketRepository(Properties props, FlightRepository flightRepo) {
        logger.info("Initializing TicketRepository with properties: " + props);
        dbUtils = new JdbcUtils(props);
        this.flightRepo = flightRepo;
    }

    @Override
    public Iterable<Ticket> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Ticket> tickets = new ArrayList<>();

        try(PreparedStatement ps = con.prepareStatement("SELECT * FROM tickets1")) {
            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String client = rs.getString("clientId");
                    int flightId = rs.getInt("flightId");
                    int noOfSeats = rs.getInt("noOfSeats");
                    Flight flight = flightRepo.findById(flightId);
                    Ticket ticket = new Ticket(client, flight, noOfSeats);
                    ticket.setId(id);
                    tickets.add(ticket);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Database error: " + e.getMessage());
        }

        logger.traceExit();
        return tickets;
    }

    @Override
    public Ticket findById(Integer id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        Ticket ticket = null;

        try(PreparedStatement ps = con.prepareStatement("SELECT * FROM tickets1 WHERE id=?")) {
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id1 = rs.getInt("id");
                    String client = rs.getString("clientId");
                    int flightId = rs.getInt("flightId");
                    int noOfSeats = rs.getInt("noOfSeats");
                    Flight flight = flightRepo.findById(flightId);
                    ticket = new Ticket(client, flight, noOfSeats);
                    ticket.setId(id);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Database error: " + e.getMessage());
        }

        logger.traceExit();
        return ticket;
    }

    @Override
    public void save(Ticket ticket) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();

        try(PreparedStatement ps = con.prepareStatement("INSERT INTO tickets1(clientId, flightId, noOfSeats) VALUES (?,?,?)")) {
            ps.setString(1, ticket.getClient());
            ps.setInt(2, ticket.getFlight().getId());
            ps.setInt(3, ticket.getNoOfSeats());
            int result = ps.executeUpdate();
            logger.trace("Saved {} tickets", result);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            System.err.println("Database error" + e.getMessage());
        }

        logger.traceExit();
    }

    @Override
    public void update(Ticket ticket) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();

        try(PreparedStatement ps = con.prepareStatement("UPDATE tickets1 SET clientId = ?, flightId = ?, noOfSeats = ? WHERE id = ?")) {
            ps.setString(1, ticket.getClient());
            ps.setInt(2, ticket.getFlight().getId());
            ps.setInt(3, ticket.getNoOfSeats());
            ps.setInt(4, ticket.getId());
            int result = ps.executeUpdate();
            logger.trace("Updated {} tickets", result);
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Database error: " + e.getMessage());
        }

        logger.traceExit();
    }

    @Override
    public void delete(Integer id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();

        try(PreparedStatement ps = con.prepareStatement("DELETE FROM tickets1 WHERE id = ?")) {
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            logger.trace("Deleted {} tickets", result);
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Database error: " + e.getMessage());
        }

        logger.traceExit();
    }

    @Override
    public Iterable<Ticket> findByFlight(Flight flight) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();

        List<Ticket> tickets = new ArrayList<>();

        try(PreparedStatement ps = con.prepareStatement("SELECT * FROM tickets1 WHERE flightId = ?")) {
            ps.setInt(1, flight.getId());
            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String client = rs.getString("clientId");
                    int flightId = rs.getInt("flightId");
                    int noOfSeats = rs.getInt("noOfSeats");
                    Flight flight1 = flightRepo.findById(flightId);
                    Ticket ticket = new Ticket(client, flight1, noOfSeats);
                    ticket.setId(id);
                    tickets.add(ticket);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Database error: " + e.getMessage());
        }

        logger.traceExit();
        return tickets;
    }

}
