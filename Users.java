import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Users {
    private String username;
    private String password;
    private List<String> purchasedProducts = new ArrayList<>();
    private String email;

    // Method to register a new user
    public void RegisterUser(String username, String password, String email) {
        this.username = username;
        this.password = password; // Store password in plain text
        this.email = email;

        // Save user data to a text file
        try (FileWriter writer = new FileWriter("users.txt", true)) {
            writer.write(username + "," + this.password + "," + email + "\n");
            System.out.println("User registered successfully!");
        } catch (IOException e) {
            System.out.println("An error occurred while registering the user.");
            e.printStackTrace();
        }
    }

    // Method to log in a user
    public boolean LoginUser(String username, String password) {
        boolean loggedIn = false;
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData[0].equals(username) && userData[1].equals(password)) {
                    System.out.println("Login successful!");
                    loggedIn = true;
                    this.username = username; // Set the logged-in user
                    this.email = userData[2]; // Set the logged-in user's email
                    break;
                }
            }
            if (!loggedIn) {
                System.out.println("Invalid credentials!");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while logging in.");
            e.printStackTrace();
        }
        return loggedIn;
    }

    // Method to view purchase history (placeholder for now)
    public void viewPurchaseHistory() {
        if (username == null) {
            System.out.println("No user is logged in.");
            return;
        }
        System.out.println("Purchase history for " + username + ":");
        for (String product : purchasedProducts) {
            System.out.println("- " + product);
        }
    }

    public static void main(String[] args) {
        Users user = new Users();

        // Register a new user
        user.RegisterUser("john_doe", "password123", "john@example.com");

        // Test login with correct credentials
        boolean loginSuccess = user.LoginUser("john_doe", "password123");  // Should print "Login successful!"
        if (loginSuccess) {
            user.viewPurchaseHistory(); // View purchase history (empty for now)
        }

        // Test login with incorrect credentials
        user.LoginUser("john_doe", "wrongpassword");  // Should print "Invalid credentials!"
    }
}