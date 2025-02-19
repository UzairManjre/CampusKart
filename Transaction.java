import java.io.*;
import java.util.*;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int trackTransactionIds = 1000;
    private int transactionId;
    private String buyer;
    private String seller;
    private int productId;
    private double amount;
    private Date transactionDate;

    public Transaction(String buyer, String seller, int productId, double amount) {
        this.transactionId = trackTransactionIds++;
        this.buyer = buyer;
        this.seller = seller;
        this.productId = productId;
        this.amount = amount;
        this.transactionDate = new Date();
    }

    public void displayTransaction() {
        System.out.println(transactionId + " - Buyer: " + buyer + " - Seller: " + seller +
                " - Product ID: " + productId + " - Amount: " + amount +
                " - Date: " + transactionDate);
    }

    public static void recordTransaction(Transaction transaction) {
        List<Transaction> transactions = loadTransactionsFromFile();
        transactions.add(transaction);
        saveTransactionsToFile(transactions);
    }

    private static void saveTransactionsToFile(List<Transaction> transactions) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("transactions.dat"))) {
            out.writeObject(transactions);
            out.writeInt(trackTransactionIds);
        } catch (IOException e) {
            System.out.println("Error saving transactions.");
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Transaction> loadTransactionsFromFile() {
        List<Transaction> transactions = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("transactions.dat"))) {
            Object obj = in.readObject();
            if (obj instanceof List<?>) {
                transactions = (List<Transaction>) obj;
            }
            trackTransactionIds = in.readInt();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No previous transactions found, starting fresh.");
        }
        return transactions;
    }

    public static void displayAllTransactions() {
        List<Transaction> transactions = loadTransactionsFromFile();
        if (transactions.isEmpty()) {
            System.out.println("No transactions recorded yet.");
        } else {
            transactions.forEach(Transaction::displayTransaction);
        }
    }
}
