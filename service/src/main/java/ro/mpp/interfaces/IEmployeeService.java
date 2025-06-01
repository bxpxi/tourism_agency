package ro.mpp.interfaces;

import ro.mpp.Employee;
import ro.mpp.IService;
import ro.mpp.ValidationException;

public interface IEmployeeService extends IService<Integer, Employee> {
    Employee findByAgencyNameAndPassword(String agencyName, String password) throws ValidationException;
}