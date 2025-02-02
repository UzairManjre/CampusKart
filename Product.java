import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Product {
//    •	price: double
//•	description: String
//•	sellerUsername: String
//•	status: String (available/sold)
static int trackIds = 230010;
    String title;
    String description;
    String sellerUsername;
    boolean status;
    String category;
    private int productid;
    private double price;
//    Methods:
//• postProduct(String title, double price, String description, String sellerUsername): Saves a new product in products.txt.
//•	searchProduct(String query): Searches for products by title or category.
//•	buyProduct(int productID, String buyerUsername): Marks a product as sold and updates files accordingly.

    public void postProduct(String title, double price, String category, String description, String seller) {
        this.productid = trackIds;
        this.title = title;
        this.price = price;
        this.category = category;
        this.description = description;
        this.sellerUsername = seller;
        this.status = true;
        trackIds ++;


    }

    void displayProduct() {

        System.out.println(this.productid);
        System.out.println(this.title);
        System.out.println(this.price);
        System.out.println(this.category);
        System.out.println(this.description);
        System.out.println(this.status);
    }

    public Product searchProduct(String title, String category, int productid, List<Product> Products) {
        return Products.stream()
                .filter(p -> (title == null || p.title.equalsIgnoreCase(title)) &&
                        (category == null || p.category.equalsIgnoreCase(category)) &&
                        (productid == -1 || p.productid == productid))
//                .peek(p -> System.out.println("Product found: ID " + p.productid))
                .findFirst()
                .orElse(null);
    }


    void buyProduct(String title, List<Product> Products) {
        if (searchProduct(title, null, -1, Products) == null) {
            System.out.println("the product doest exist");
            return;
        }


        for (Product p : Products) {
            if (p.title.equalsIgnoreCase(title)) {
                if (!p.status) {
                    System.out.println("Product not available for sale ");
                    return;
                }

                System.out.println("do you wish to buy " + title + " ?");
                System.out.println("Press 'y' to continue press 'n' to cancel");
                Scanner scanner = new Scanner(System.in);
                char ch = scanner.next().charAt(0);
                if (ch == 'y' || ch == 'Y') {
                    System.out.println("enter y if money is sent or enter n if the money is not sent");
                    char ch2 = scanner.next().charAt(0);
                    if (ch2 == 'y' || ch2 == 'Y') {
                        p.status = false;
                        System.out.println("Product purchase successfully");

                    }
                    if (ch2 == 'n' || ch2 == 'N'){
                        System.out.println("payment failed please try again later ");
                    }

                }
                else {
                    return;
                }
            }
        }
    }
    public static void main(String[] args) {
        // Create a list of products
        List<Product> Products = new ArrayList<>();

        // Create some products
        Product product1 = new Product();
        product1.postProduct("Laptop", 1000.00, "Electronics", "High-performance laptop", "seller123");
        Products.add(product1);

        Product product2 = new Product();
        product2.postProduct("Phone", 500.00, "Electronics", "Smartphone with great camera", "seller456");
        Products.add(product2);

        Product product3 = new Product();
        product3.postProduct("Shoes", 50.00, "Fashion", "Comfortable running shoes", "seller789");
        Products.add(product3);

        // Display all products
        System.out.println("Available Products:");
        for (Product p : Products) {
            p.displayProduct();
            System.out.println();
        }

        // Search for a product
        String searchQuery = "Laptop";
        System.out.println("Searching for product: " + searchQuery);
        Product searchedProduct = product1.searchProduct(searchQuery, null, -1, Products);
        if (searchedProduct != null) {
            System.out.println("Found product: " + searchedProduct.title);
        } else {
            System.out.println("Product not found.");
        }

        // Attempt to buy a product
        String buyProductTitle = "Laptop";
        product1.buyProduct(buyProductTitle, Products); // Test purchasing a product

        // Check if product status is updated
        System.out.println("\nUpdated Products:");
        for (Product p : Products) {
            p.displayProduct();
            System.out.println();
        }
    }
}
