import java.io.*;
import java.util.*;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int nextProductId = 24001; // Tracks the next product ID to assign
    private int productId;
    private String name;
    private double price;
    private String description;
    private String category;
    private String condition;

    public Product(String name, double price, String description, String category, String condition) {
        this.productId = nextProductId++;
        this.name = name;
        this.price = price;
        this.description = description;
        this.category = category;
        this.condition = condition;
    }

    // Getters
    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getCondition() {
        return condition;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    // Load Products from File
    @SuppressWarnings("unchecked")
    public static List<Product> loadProducts() {
        List<Product> products = new ArrayList<>();
        File file = new File("products.dat");

        if (!file.exists()) {
            System.out.println("products.dat does not exist. Creating a new file with sample data.");
            products.add(new Product("Laptop", 999.99, "High-performance laptop", "Electronics", "New"));
            products.add(new Product("Phone", 499.99, "Latest smartphone", "Electronics", "New"));
            products.add(new Product("Headphones", 99.99, "Noise-cancelling headphones", "Electronics", "Refurbished"));
            saveProducts(products);
        } else {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = in.readObject();
                if (obj instanceof List<?>) {
                    products = (List<Product>) obj;
                    // Update nextProductId to avoid duplicates
                    nextProductId = products.stream().mapToInt(Product::getProductId).max().orElse(0) + 1;
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error reading products.dat. Starting with an empty product list.");
                e.printStackTrace();
            }
        }
        return products;
    }

    // Save Products to File
    public static void saveProducts(List<Product> products) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("products.dat"))) {
            out.writeObject(products);
        } catch (IOException e) {
            System.out.println("Error saving products.dat.");
            e.printStackTrace();
        }
    }

    // Display All Products
    public static void displayAllProducts() {
        List<Product> products = loadProducts();
        if (products.isEmpty()) {
            System.out.println("No products available.");
        } else {
            System.out.println("\nAvailable Products:");
            for (Product p : products) {
                System.out.println("ID: " + p.getProductId() + " - Name: " + p.getName() + " - Price: $" + p.getPrice() +
                        " - Category: " + p.getCategory() + " - Condition: " + p.getCondition() +
                        " - Description: " + p.getDescription());
            }
        }
    }

    // Find Product by ID
    public static Product findProductById(int productId) {
        List<Product> products = loadProducts();
        for (Product p : products) {
            if (p.getProductId() == productId) {
                return p;
            }
        }
        return null;
    }

    // Add a new product
    public static void addProduct(String name, double price, String description, String category, String condition) {
        List<Product> products = loadProducts();
        Product newProduct = new Product(name, price, description, category, condition);
        products.add(newProduct);
        saveProducts(products);
        System.out.println("Product added successfully!");
    }

    // Remove a product by ID
    public static void removeProduct(int productId) {
        List<Product> products = loadProducts();
        products.removeIf(p -> p.getProductId() == productId);
        saveProducts(products);
        System.out.println("Product removed successfully!");
    }

    // Buy Product Method
    public static void buyProduct(Users user, int productId) {
        if (user.getUsername() == null) {
            System.out.println("You must be logged in to buy products.");
            return;
        }

        List<Product> products = loadProducts();
        Product selectedProduct = null;

        Iterator<Product> iterator = products.iterator();
        while (iterator.hasNext()) {
            Product p = iterator.next();
            if (p.getProductId() == productId) {
                selectedProduct = p;
                System.out.println("Do you wish to purchase " + selectedProduct.getName() + "? (y/n)");
                Scanner scanner = new Scanner(System.in);
                char ch = scanner.next().charAt(0);
                if (ch == 'n' || ch == 'N') {
                    return;
                }

                System.out.println("Money transferring...");
                user.addPurchase(selectedProduct.getName());

                iterator.remove(); // Removes the product from the list
                saveProducts(products); // Save the updated product list

                System.out.println("Purchase successful! " + selectedProduct.getName() + " has been removed from the listing.");
                return;
            }
        }

        System.out.println("Product not found.");
    }

}