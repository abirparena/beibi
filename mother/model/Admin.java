package mother.model;

public class Admin extends User {
    public Admin(String username, String nid) {
        super(username, nid);
    }

    // Method Overriding (Polymorphism)
    @Override
    public String getRole() {
        return "Admin";
    }
}
