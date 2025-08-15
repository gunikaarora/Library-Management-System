import java.io.*;
import java.util.*;

class Book implements Serializable {
    private static final long serialVersionUID = 1L;
    int id;
    String title;
    String author;
    boolean isIssued;

    Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isIssued = false;
    }

    public String toString() {
        return id + " | " + title + " | " + author + " | " + (isIssued ? "Issued" : "Available");
    }
}

public class LibraryManagementSystem {
    private static final String FILE_NAME = "library.dat";
    private static List<Book> books = new ArrayList<>();

    public static void main(String[] args) {
        loadData();
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n===== Library Management System =====");
            System.out.println("1. Add Book");
            System.out.println("2. View All Books");
            System.out.println("3. Search Book");
            System.out.println("4. Issue Book");
            System.out.println("5. Return Book");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1: addBook(sc); break;
                case 2: viewBooks(); break;
                case 3: searchBook(sc); break;
                case 4: issueBook(sc); break;
                case 5: returnBook(sc); break;
                case 6: saveData(); System.out.println("Data saved. Exiting..."); break;
                default: System.out.println("Invalid choice!");
            }
        } while (choice != 6);
        sc.close();
    }

    private static void addBook(Scanner sc) {
        System.out.print("Enter Book ID: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Book Title: ");
        String title = sc.nextLine();
        System.out.print("Enter Author Name: ");
        String author = sc.nextLine();

        books.add(new Book(id, title, author));
        saveData();
        System.out.println("Book added successfully!");
    }

    private static void viewBooks() {
        if (books.isEmpty()) {
            System.out.println("No books available.");
            return;
        }
        for (Book b : books) {
            System.out.println(b);
        }
    }

    private static void searchBook(Scanner sc) {
        System.out.print("Enter Book Title to search: ");
        String title = sc.nextLine().toLowerCase();
        for (Book b : books) {
            if (b.title.toLowerCase().contains(title)) {
                System.out.println(b);
                return;
            }
        }
        System.out.println("Book not found!");
    }

    private static void issueBook(Scanner sc) {
        System.out.print("Enter Book ID to issue: ");
        int id = sc.nextInt();
        for (Book b : books) {
            if (b.id == id && !b.isIssued) {
                b.isIssued = true;
                saveData();
                System.out.println("Book issued successfully!");
                return;
            }
        }
        System.out.println("Book not available for issue.");
    }

    private static void returnBook(Scanner sc) {
        System.out.print("Enter Book ID to return: ");
        int id = sc.nextInt();
        for (Book b : books) {
            if (b.id == id && b.isIssued) {
                b.isIssued = false;
                saveData();
                System.out.println("Book returned successfully!");
                return;
            }
        }
        System.out.println("Book was not issued or doesn't exist.");
    }

    @SuppressWarnings("unchecked")
    private static void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            books = (List<Book>) ois.readObject();
        } catch (FileNotFoundException e) {
            books = new ArrayList<>();
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    private static void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(books);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
}
