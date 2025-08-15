package mother.model;

public class Reporter extends User {
    public Reporter(String username, String nid) {
        super(username, nid);
    }

    // Method Overriding (Polymorphism)
    @Override
    public String getRole() {
        return "Reporter";
    }
}
