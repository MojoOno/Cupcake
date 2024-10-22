package app.entities;

public class User {
    int userId;
    String username;
    String password;
    float balance;
    String role;

    public User(String username, String password, float balance, String role) {
        this.username = username;
        this.password = password;
        this.balance = balance;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public int getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }

    public float getBalance() {
        return balance;
    }
}
