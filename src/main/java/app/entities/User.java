package app.entities;

public class User {
    int userId;
    String username;
    String password;
    float balance;
    boolean isAdmin = false;

    public User(int userId, String username, String password, float balance, boolean isAdmin) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.balance = balance;
        this.isAdmin = isAdmin;
    }

    public String getUsername() {
        return username;
    }

    public int getUserId() {
        return userId;
    }

    public boolean isAdmin() { return isAdmin; }

    public String getPassword() { return password;}

    public float getBalance() { return balance;}
}
