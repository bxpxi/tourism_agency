package ro.mpp.databases;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp.Flight;
import ro.mpp.JdbcUtils;
import ro.mpp.interfaces.IFlightRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FlightRepository implements IFlightRepository {
    private JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger();

    public FlightRepository(Properties props) {
        logger.info("Initializing FlightRepository with properties: " + props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Iterable<Flight> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Flight> flights = new ArrayList<>();

        try(PreparedStatement ps = con.prepareStatement("SELECT * FROM flights WHERE availableSeats > 0")) {
            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String destination = rs.getString("destination");
                    String departureDate = rs.getString("departureDate");
                    String departureTime = rs.getString("departureTime");
                    String airport = rs.getString("airport");
                    int availableSeats = rs.getInt("availableSeats");
                    Flight flight = new Flight(destination, departureDate, departureTime, airport, availableSeats);
                    flight.setId(id);
                    flights.add(flight);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Database error: " + e.getMessage());
        }

        logger.traceExit();
        return flights;
    }

    @Override
    public Flight findById(Integer id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        Flight flight = null;

        try(PreparedStatement ps = con.prepareStatement("SELECT * FROM flights WHERE id=?")) {
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id1 = rs.getInt("id");
                    String destination = rs.getString("destination");
                    String departureDate = rs.getString("departureDate");
                    String departureTime = rs.getString("departureTime");
                    String airport = rs.getString("airport");
                    int availableSeats = rs.getInt("availableSeats");
                    flight = new Flight(destination, departureDate, departureTime, airport, availableSeats);
                    flight.setId(id);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Database error: " + e.getMessage());
        }

        logger.traceExit();
        return flight;
    }

    @Override
    public void save(Flight flight) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();

        try(PreparedStatement ps = con.prepareStatement("INSERT INTO flights(destination, departureDate, departureTime, airport, availableSeats) VALUES (?,?,?,?,?)")) {
            ps.setString(1, flight.getDestination());
            ps.setString(2, flight.getDepartureDate());
            ps.setString(3, flight.getDepartureTime());
            ps.setString(4, flight.getAirport());
            ps.setInt(5, flight.getAvailableSeats());
            int result = ps.executeUpdate();
            logger.trace("Saved {} flights", result);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            System.err.println("Database error" + e.getMessage());
        }

        logger.traceExit();
    }

    @Override
    public void update(Flight flight) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();

        try(PreparedStatement ps = con.prepareStatement("UPDATE flights SET destination = ?, departureDate = ?, departureTime = ?, airport = ?, availableSeats = ? WHERE id = ?")) {
            ps.setString(1, flight.getDestination());
            ps.setString(2, flight.getDepartureDate());
            ps.setString(3, flight.getDepartureTime());
            ps.setString(4, flight.getAirport());
            ps.setInt(5, flight.getAvailableSeats());
            ps.setInt(6, flight.getId());
            int result = ps.executeUpdate();
            logger.trace("Updated {} flights", result);
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

        try(PreparedStatement ps = con.prepareStatement("DELETE FROM flights WHERE id = ?")) {
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            logger.trace("Deleted {} flights", result);
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Database error: " + e.getMessage());
        }

        logger.traceExit();
    }

    @Override
    public Iterable<Flight> findByDestinationAndDepartureDate(String destination, String departureDate) {
        logger.traceEntry();

        Connection con = dbUtils.getConnection();
        List<Flight> flights = new ArrayList<>();

        try(PreparedStatement ps = con.prepareStatement("SELECT * FROM flights WHERE destination= ? AND departureDate = ?")) {
            ps.setString(1, destination);
            ps.setString(2, departureDate);
            try(ResultSet rs = ps.executeQuery()) {
                System.out.println("Checking params before SQL: |" + destination + "| , |" + departureDate + "|");
                System.out.println(destination.length() + ", " + departureDate.length());
                System.out.println("Checking if ResultSet has rows: " + rs.isBeforeFirst());
                while (rs.next()) {
                    System.out.println("Am intrat in while(rs.next())");
                    int id = rs.getInt("id");
                    String destination1 = rs.getString("destination");
                    String departureDate1 = rs.getString("departureDate");
                    String departureTime = rs.getString("departureTime");
                    String airport = rs.getString("airport");
                    int availableSeats = rs.getInt("availableSeats");
                    Flight flight = new Flight(destination, departureDate, departureTime, airport, availableSeats);
                    flight.setId(id);
                    flights.add(flight);
                    System.out.println("Flights found in DB: " + flights.size());

                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Database error: " + e.getMessage());
        }

        logger.traceExit();
        return flights;
    }
}
