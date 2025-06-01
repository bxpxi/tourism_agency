package ro.mpp.databases;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp.Employee;
import ro.mpp.JdbcUtils;
import ro.mpp.interfaces.IEmployeeRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class EmployeeRepository implements IEmployeeRepository {
    private JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger();

    public EmployeeRepository(Properties props) {
        logger.info("Initializing EmployeeRepository with properties: " + props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Iterable<Employee> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Employee> employees = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM employees")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String agencyName = rs.getString("agencyName");
                    String password = rs.getString("password");
                    Employee employee = new Employee(agencyName, password);
                    employee.setId(id);
                    employees.add(employee);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Database error: " + e.getMessage());
        }

        logger.traceExit();
        return employees;
    }

    @Override
    public Employee findById(Integer id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        Employee employee = null;

        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM employees WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id1 = rs.getInt("id");
                    String agencyName = rs.getString("agencyName");
                    String password = rs.getString("password");
                    employee = new Employee(agencyName, password);
                    employee.setId(id1);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Database error: " + e.getMessage());
        }

        logger.traceExit();
        return employee;
    }

    @Override
    public void save(Employee employee) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();

        try (PreparedStatement ps = con.prepareStatement("INSERT INTO employees(agencyName, password) VALUES (?,?)")) {
            ps.setString(1, employee.getAgencyName());
            ps.setString(2, employee.getPassword());
            int result = ps.executeUpdate();
            logger.trace("Saved {} employees", result);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            System.err.println("Database error" + e.getMessage());
        }

        logger.traceExit();
    }

    @Override
    public void update(Employee employee) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();

        try (PreparedStatement ps = con.prepareStatement("UPDATE employees SET agencyName = ?, password = ? WHERE id = ?")) {
            ps.setString(1, employee.getAgencyName());
            ps.setString(2, employee.getPassword());
            ps.setInt(3, employee.getId());
            int result = ps.executeUpdate();
            logger.trace("Updated {} employees", result);
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

        try (PreparedStatement ps = con.prepareStatement("DELETE FROM employees WHERE id = ?")) {
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            logger.trace("Deleted {} employees", result);
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Database error: " + e.getMessage());
        }

        logger.traceExit();
    }

    @Override
    public Employee findByAgencyNameAndPassword(String agencyName, String password) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        Employee employee = null;

        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM employees WHERE agencyName=? AND password=?")) {
            ps.setString(1, agencyName);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id1 = rs.getInt("id");
                    String agencyName1 = rs.getString("agencyName");
                    String password1 = rs.getString("password");
                    employee = new Employee(agencyName1, password1);
                    employee.setId(id1);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Database error: " + e.getMessage());
        }

        logger.traceExit();
        return employee;
    }
}
