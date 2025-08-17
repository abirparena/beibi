package mother.model;

import java.io.Serializable;


public abstract class User implements Serializable {
    private String username;
    private String nid;

    public User(String username, String nid) {
        this.username = username;
        this.nid = nid;
    }

    // Getter methods (Encapsulation)
    public String getUsername() { return username; }
    public String getNid() { return nid; }

    // Abstract method (Abstraction)
    public abstract String getRole();
}
