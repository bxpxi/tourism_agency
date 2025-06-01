package ro.mpp.services;

import ro.mpp.AbstractService;
import ro.mpp.Employee;
import ro.mpp.ValidationException;
import ro.mpp.interfaces.IEmployeeRepository;
import ro.mpp.interfaces.IEmployeeService;

public class EmployeeService extends AbstractService<Integer, Employee, IEmployeeRepository> implements IEmployeeService {
    public EmployeeService(IEmployeeRepository repository) {
        super(repository);
    }

    @Override
    public Employee findByAgencyNameAndPassword(String agencyName, String password) throws ValidationException {
        return getRepository().findByAgencyNameAndPassword(agencyName, password);
    }
}