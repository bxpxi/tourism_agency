package ro.mpp.objectprotocol;

import ro.mpp.dto.EmployeeDTO;

public class LoginEmployeeResponse implements IResponse{
    private EmployeeDTO employee;

    public LoginEmployeeResponse(EmployeeDTO employee) {
        this.employee = employee;
    }

    public EmployeeDTO getEmployee() {
        return employee;
    }

    @Override
    public String toString() {
        return "LoginEmployeeResponse [employee=" + employee + "]";
    }
}
