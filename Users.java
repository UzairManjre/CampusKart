import java.io.*;
import java.util.*;

public class Users {
    private String username;
    private String password;
    private String email;
    private List<String> purchasedProducts = new ArrayList<>();

    public Users() {} // Default constructor

    public Users(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // Method to register a new user
    public void registerUser(String username, String password, String email) {
        if (isUserExists(username)) {
            System.out.println("User already exists. Try logging in.");
            return;
        }

        this.username = username;
        this.password = password;
        this.email = email;

        try (FileWriter writer = new FileWriter("users.txt", true)) {
            writer.write(username + "," + password + "," + email + "\n");
            System.out.println("User registered successfully!");
        } catch (IOException e) {
            System.out.println("An error occurred while registering the user.");
            e.printStackTrace();
        }
    }

    // Check if a user already exists in the file
    private boolean isUserExists(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading user file.");
        }
        return false;
    }

    // Method to log in a user
    public boolean loginUser(String username, String password) {
        boolean loggedIn = false;
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData[0].equals(username) && userData[1].equals(password)) {
                    loggedIn = true;
                    this.username = username;
                    this.email = userData[2];
                    loadPurchaseHistory();
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

    // Load purchase history from file
    private void loadPurchaseHistory() {
        purchasedProducts.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("purchases.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] purchaseData = line.split(",");
                if (purchaseData[0].equals(username)) {
                    purchasedProducts.add(purchaseData[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("No purchase history found.");
        }
    }

    // Save purchase to file
    public void addPurchase(String productName) {
        if (username == null) {
            System.out.println("You must be logged in to purchase products.");
            return;
        }

        purchasedProducts.add(productName);
        try (FileWriter writer = new FileWriter("purchases.txt", true)) {
            writer.write(username + "," + productName + "\n");
        } catch (IOException e) {
            System.out.println("Error saving purchase.");
        }
    }

    // View purchase history
    public void viewPurchaseHistory() {
        if (username == null) {
            System.out.println("No user is logged in.");
            return;
        }
        System.out.println("Purchase history for " + username + ":");
        if (purchasedProducts.isEmpty()) {
            System.out.println("No purchases made.");
        } else {
            for (String product : purchasedProducts) {
                System.out.println("- " + product);
            }
        }
    }

    // Getters for other classes to access user details
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void logout() {
        this.username = null;
        this.email = null;
        this.purchasedProducts.clear();
    }

    public List<String> getPurchasedProducts() {
        return purchasedProducts;
    }

    public static void main(String[] args) {
        Users user = new Users();
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to the Product Store!");

        int choice = 0;
        while (true) {
            if (user.username == null) {  // Only show login/register if not logged in
                System.out.println("\nChoose an option:");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("-1. Exit");
            } else {  // Show other options if logged in
                System.out.println("\nChoose an option:");
                System.out.println("3. View purchase history");
                System.out.println("4. Simulate Purchase");
                System.out.println("5. Logout");
                System.out.println("-1. Exit");
            }

            choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1 && user.username == null) {
                System.out.print("Enter your username: ");
                String username = sc.nextLine();
                System.out.print("Enter your password: ");
                String password = sc.nextLine();
                System.out.print("Enter your email: ");
                String email = sc.nextLine();
                user.registerUser(username, password, email);
            }

            if (choice == 2 && user.username == null) {
                System.out.print("Enter your username: ");
                String username = sc.nextLine();
                System.out.print("Enter your password: ");
                String password = sc.nextLine();
                if (user.loginUser(username, password)) {
                    System.out.println("Login successful!");
                }
            }

            if (choice == 3 && user.username != null) {
                user.viewPurchaseHistory();
            }

            if (choice == 4 && user.username != null) {
                System.out.print("Enter product name to purchase: ");
                String product = sc.nextLine();
                user.addPurchase(product);
                System.out.println("Product added to purchase history!");
            }

            if (choice == 5 && user.username != null) {
                System.out.println("Logging out...");
                user.username = null;
                user.email = null;
                user.purchasedProducts.clear();
            }

            if (choice == -1) {
                System.out.println("Goodbye!");
                break;
            }
        }
        sc.close();
    }
}
