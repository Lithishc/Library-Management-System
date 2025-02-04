package ProjectJava;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LibraryManagementSystem {
    public static void main(String[] args) {
    	Library library = new Library();
        Scanner scanner = new Scanner(System.in);
        String filePath = "users.txt";
        if (filecheck.Exists(filePath)) {
        	System.out.print("Library Management System ");
        	System.out.print("\nEnter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            if (library.login(username, password)) {
                System.out.println("Login successful. Welcome to the Library Management System!");
            } else {
                System.out.println("Login failed. Exiting...");
                System.exit(0);
                }
        } else {
            System.out.println("Initiating the Library Management system...");
            System.out.println("Register Master Login Details!!!!");
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            library.addUser(username,password);
        }
        
        while (true) {
        	System.out.println("\nLibrary Management System\n1. Add Book\n2. Delete Book\n3. Display Books\n" +
                    "4. Checkout Book\n5. Return Book\n6. Admin Options\n7. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                	System.out.print("Enter book id: ");
                    String id = scanner.nextLine();
                    System.out.print("Enter book title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter author name: ");
                    String author = scanner.nextLine();
                    System.out.print("Enter book publisher: ");
                    String publisher = scanner.nextLine();
                    library.addBook(id,title, author,publisher);
                    break;                  
                case 2:
                    System.out.print("Enter book id to delete: ");
                    String deleteid = scanner.nextLine();
                    library.deleteBook(deleteid);
                    break;
                case 3:
                    library.displayBooks();
                    break;
                case 4:
                    System.out.print("Enter book id to checkout: ");
                    String checkoutid = scanner.nextLine();
                    library.checkoutBook(checkoutid);
                    break;
                case 5:
                    System.out.print("Enter book id to return: ");
                    String returnid = scanner.nextLine();
                    library.returnBook(returnid);
                    break;
                case 6: admin.display();
                break;
                	
                case 7:
                    System.out.println("Exiting Library Management System. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
class Library {
	  private List<Book> books;
	  private static List<User> users;
	    public Library() {
	        books = readBooksFromFile();
	        users = new ArrayList<>();
	        loadUsersFromFile();
	        loadBooksFromFile();
	    }

	    public void addBook(String id,String title, String author,String publisher) {
	    		books.add(new Book(id,title, author,publisher,true));
	    		writeBooksToFile();
	    		System.out.println("Book added successfully.");	    	
	    }
	    public void deleteBook(String id) {
	        Book bookToDelete = findBook(id);
	        if (bookToDelete != null) {
	            books.remove(bookToDelete);
	            writeBooksToFile();
	            System.out.println("Book deleted successfully.");
	        } else {
	            System.out.println("Book not found in the library.");
	        }
	    }

	    public void displayBooks() {
	        System.out.println("Books in the library:");
	        System.out.println("Book id: Book Title by Book Author published by Book Publisher");
	        for (Book book : books) {
	            System.out.println(book.id+": "+book.title+" by "+book.author+" published by "+book.publisher+" - "+(book.available ? "Available" : "Not Available"));
	        }
	    }

	    public void checkoutBook(String id) {
	        Book bookToCheckout = findBook(id);
	        if (bookToCheckout != null && bookToCheckout.available) {
	            bookToCheckout.available = false;
	            writeBooksToFile();
	            System.out.println("Book Checked Out Successfully.");
	        } else if (bookToCheckout == null) {
	            System.out.println("Book not found in the Library.");
	        } else {
	            System.out.println("Book is not available for checkout.");
	        }
	    }

	    public void returnBook(String id) {
	        Book bookToReturn = findBook(id);
	        if (bookToReturn != null && !bookToReturn.available) {
	            bookToReturn.available = true;
	            writeBooksToFile();
	            System.out.println("Book Returned successfully.");
	        } else if (bookToReturn == null) {
	            System.out.println("Book not found in the Library.");
	        } else {
	            System.out.println(" Book is available in Library ");
	        }
	    }

	    private Book findBook(String id) {
	        for (Book book : books) {
	            if (book.id.equalsIgnoreCase(id)) {
	                return book;
	            }
	        }
	        return null;
	    }
	    
	    private void loadBooksFromFile() {
	        try (Scanner scanner = new Scanner(new File("librarybooks.txt"))) {
	            while (scanner.hasNextLine()) {
	                String line = scanner.nextLine();
	                String[] parts = line.split(",");
	                books.add(new Book(parts[0], parts[1],parts[2],parts[3], Boolean.parseBoolean(parts[4])));
	            }
	        } catch (FileNotFoundException e) {
	        	System.err.println("Error reading file: " + e.getMessage());
	        }
		}
	    private List<Book> readBooksFromFile() {
	        List<Book> bookList = new ArrayList<>();

	        try (Scanner fileScanner = new Scanner(new File("librarybooks.txt"))) {
	            while (fileScanner.hasNextLine()) {
	                String line = fileScanner.nextLine();
	                String[] parts = line.split(",");
	                if (parts.length == 6) {
	                    bookList.add(new Book(parts[0],parts[1],parts[2],parts[3], Boolean.parseBoolean(parts[4])));
	                }
	            }
	        } catch (FileNotFoundException e) {
	            System.err.println("Error reading file: " + e.getMessage());
	        }

	        return bookList;
	    }

	    private void writeBooksToFile() {
	        try (PrintWriter writer = new PrintWriter(new FileWriter("librarybooks.txt"))) {
	            for (Book book : books) {
	                writer.println(book);
	            }
	        } catch (IOException e) {
	            System.err.println("Error writing to file: " + e.getMessage());
	        }
	    }
	    
	    
	    private static void saveUsersToFile() {
	        try (PrintWriter writer = new PrintWriter(new FileWriter("users.txt"))) {
	            for (User user : users) {
	                writer.println(user.username + "," + user.password);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
		
		public void addUser(String username, String password) {
	        users.add(new User(username, password));
	        saveUsersToFile();
	        System.out.println("User added successfully.");
	    }
		
		public void displayUsers() {
			System.out.println("Admin Login Details");
			for (User user : users) {
               System.out.println("UserName: "+user.username + ", Password: " + user.password);
			}
		}
		public void deleteUser(String username) {
			 User Usertodelete = findUser(username);  
	         if (users!= null) {
	             users.remove(Usertodelete);
	             saveUsersToFile();
	             System.out.println("User deleted successfully.");
	         } else {
	             System.out.println("User not found in the library.");
	         }
		 }
	        
		
		private User findUser(String username) {
			for (User user : users) {
	            if (user.username.equalsIgnoreCase(username)) {
	                return user;
	            }
	        }
	        return null;
	    }

		private void loadUsersFromFile() {
	        try (Scanner scanner = new Scanner(new File("users.txt"))) {
	            while (scanner.hasNextLine()) {
	                String line = scanner.nextLine();
	                String[] parts = line.split(",");
	                users.add(new User(parts[0], parts[1]));
	            }
	        } catch (FileNotFoundException e) {
	            // Handle file not found exception (for the first run)
	        }
		}
	        
	        public boolean login(String username, String password) {
	            for (User user : users) {
	                if (user.username.equals(username) && user.password.equals(password)) {
	                    return true;
	                }
	            }
	            return false;
	        }

	
	
}
class Book {
	String id;
    String title;
    String author;
    String publisher;
    boolean available;
    
    public Book(String id,String title, String author,String publisher, boolean available) {
        this.id=id;
        this.title = title;
        this.author = author;
        this.publisher= publisher;
        this.available = available;
    }

    @Override
    public String toString() {
        return id+"," +title + "," + author + "," +publisher+ "," + (available ? "true" : "false");
    }
}

class filecheck{
	static boolean Exists(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.isFile();
	}
}

class User {
    String username;
    String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

class admin{
	public static void display() {
		Scanner scanner = new Scanner(System.in);
		Library library = new Library();
		System.out.println("ADMIN Options");
		System.out.println("1.Add Admin ");
		System.out.println("2.Delete Admin");
		System.out.println("3.Display Admin");
		System.out.println("Enter your choice: ");
		int ch = scanner.nextInt();
		scanner.nextLine();
		switch(ch)
		{
			case 1: System.out.println("Enter new username: ");
			String username = scanner.nextLine();
			System.out.println("Enter new password: ");
			String password = scanner.nextLine();
			library.addUser(username,password);
			break;
			case 2: System.out.println("Enter username to delete: ");
			String dusername = scanner.nextLine();
			library.deleteUser(dusername);
			break;	
			case 3: library.displayUsers();
			break;
			default:
				System.out.println("Invalid choice");
		}
	} 
}

