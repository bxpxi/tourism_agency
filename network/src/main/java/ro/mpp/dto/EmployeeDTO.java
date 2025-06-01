package ro.mpp.dto;

import ro.mpp.Employee;

public class EmployeeDTO extends EntityDTO {
    private final String agencyName;
    private final String password;

    public EmployeeDTO(String agencyName, String password) {
        this.agencyName = agencyName;
        this.password = password;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public String getPassword() {
        return password;
    }

    public static EmployeeDTO fromEmployee(Employee emp) {
        var e = new EmployeeDTO(emp.getAgencyName(), emp.getPassword());
        e.setId(emp.getId());
        return e;
    }

    public Employee toEmployee() {
        var e = new Employee(getAgencyName(), getPassword());
        e.setId(e.getId());
        return e;
    }
}
