import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Users user = new Users();
        Product.loadProducts(); // Load products from file
        Transaction.loadTransactionsFromFile(); // Load transactions from file

        System.out.println("Welcome to the Online Store!");

        while (true) {
            if (user.getUsername() == null) { // User is not logged in
                System.out.println("\nChoose an option:");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("-1. Exit");
            } else { // User is logged in
                System.out.println("\nChoose an option:");
                System.out.println("3. View Products");
                System.out.println("4. Buy Product");
                System.out.println("5. View Purchase History");
                System.out.println("6. View All Transactions");
                System.out.println("7. Add Product");
                System.out.println("8. Logout");
                System.out.println("-1. Exit");
            }

            int choice = sc.nextInt();
            sc.nextLine(); // Consume the newline character

            switch (choice) {
                case 1: // Register
                    if (user.getUsername() == null) {
                        System.out.print("Enter your username: ");
                        String username = sc.nextLine();
                        System.out.print("Enter your password: ");
                        String password = sc.nextLine();
                        System.out.print("Enter your email: ");
                        String email = sc.nextLine();
                        user.registerUser(username, password, email);
                    } else {
                        System.out.println("You are already logged in.");
                    }
                    break;

                case 2: // Login
                    if (user.getUsername() == null) {
                        System.out.print("Enter your username: ");
                        String loginUsername = sc.nextLine();
                        System.out.print("Enter your password: ");
                        String loginPassword = sc.nextLine();
                        if (user.loginUser(loginUsername, loginPassword)) {
                            System.out.println("Login successful!");
                        }
                    } else {
                        System.out.println("You are already logged in.");
                    }
                    break;

                case 3: // View Products
                    Product.displayAllProducts();
                    break;

                case 4: // Buy Product
                    if (user.getUsername() != null) {
                        System.out.print("Enter the product id to purchase: ");

                        int productId = sc.nextInt();
                        Product.buyProduct(user, productId);
                    } else {
                        System.out.println("You must be logged in to buy products.");
                    }
                    break;

                case 5: // View Purchase History
                    if (user.getUsername() != null) {
                        user.viewPurchaseHistory();
                    } else {
                        System.out.println("You must be logged in to view purchase history.");
                    }
                    break;

                case 6: // View All Transactions
                    if (user.getUsername() != null) {
                        System.out.println("\nAll Transactions:");
                        Transaction.displayAllTransactions();
                    } else {
                        System.out.println("You must be logged in to view transactions.");
                    }
                    break;

                case 7: // Add Product
                    if (user.getUsername() != null) {
                        System.out.print("Enter product name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter product price: ");
                        double price = sc.nextDouble();
                        sc.nextLine(); // Consume newline
                        System.out.print("Enter product description: ");
                        String description = sc.nextLine();
                        System.out.print("Enter product category: ");
                        String category = sc.nextLine();
                        System.out.print("Enter product condition: ");
                        String condition = sc.nextLine();
                        Product.addProduct(name, price, description, category, condition);
                    } else {
                        System.out.println("You must be logged in to add products.");
                    }
                    break;

                case 8: // Logout
                    if (user.getUsername() != null) {
                        System.out.println("Logging out...");
                        user.logout();
                    } else {
                        System.out.println("No user is logged in.");
                    }
                    break;

                case -1: // Exit
                    System.out.println("Thank you for using the Online Store. Goodbye!");
                    sc.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}