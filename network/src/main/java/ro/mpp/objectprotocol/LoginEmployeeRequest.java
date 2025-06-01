package ro.mpp.objectprotocol;

public class LoginEmployeeRequest implements IRequest {
    private String agencyName;
    private String password;

    public LoginEmployeeRequest() {}

    public String getAgencyName() {
        return agencyName;
    }

    public String getPassword() {
        return password;
    }

    public LoginEmployeeRequest(String agencyName, String password) {
        this.agencyName = agencyName;
        this.password = password;
    }
}
