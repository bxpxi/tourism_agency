package ro.mpp.interfaces;

import ro.mpp.Employee;
import ro.mpp.IRepository;

public interface IEmployeeRepository extends IRepository<Integer, Employee> {
    Employee findByAgencyNameAndPassword(String agencyName, String password);
}