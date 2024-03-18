package copypaste.ticketguru.domain;

public class JwtResponse {

    private String token;
    private String type = "Bearer";

    // Include other fields if necessary, such as expiration time
    // private long expiresIn;

    public JwtResponse(String token) {
        this.token = token;
    }

    // Getter and Setter for the token
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    // Getter for the type
    public String getType() {
        return type;
    }

    // Optionally, include getters and setters for other fields if you added them

}
