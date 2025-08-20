
package bank;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileHandle implements constants,colors {
    static Scanner sc = new Scanner(System.in);
    static List<User> users = new ArrayList<>();

    static {
        try {
            loadUsers();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        public static boolean check(int userIndex, int inputPin) {
        return users.get(userIndex).accPin == inputPin;
    }

    public static int inputUserPin() {
        System.out.print(CYAN + "\tEnter your PIN: " + RESET);
        return sc.nextInt();
    }

    static void saveUsers() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE));
        writer.write(String.format("%-15s%-20s%-10s%-15s%n", "USERNAME", "ACCOUNTNUMBER", "PIN", "CURRENTBALANCE"));
        for (User user : users) {
            writer.write(String.format("%-15s%-20d%-10d%-15d%n", user.name, user.accNum, user.accPin, user.accBal));
        }
        writer.close();
    }

    static void loadUsers() throws IOException {
        File file = new File(USER_FILE);
        if (!file.exists()) return;
        BufferedReader reader = new BufferedReader(new FileReader(file));
        reader.readLine(); // Skip header
        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.trim().split("\\s+");
            if (data.length >= 4) {
                users.add(new User(
                        data[0],
                        Long.parseLong(data[1]),
                        Integer.parseInt(data[2]),
                        Integer.parseInt(data[3])
                ));
            }
        }
        reader.close();
    }

    static void processTransaction(String type, User user, int amount) throws IOException {
        File file = new File(TRANSACTION_FILE);
        boolean writeHeader = !file.exists() || file.length() == 0;
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        if (writeHeader) {
            writer.write(String.format("%-15s%-15s%-15s%-15s%-20s%n", "USERNAME", "BALANCE", "TYPE", "AMOUNT", "TIMESTAMP"));
        }
        writer.write(String.format("%-15s%-15d%-15s%-15d%-20s%n",
                user.name, user.accBal, type, Math.abs(amount), timeStamp));
        writer.close();
        System.out.println(GREEN + "Transaction " + type + " completed." + RESET);
    }

    public static int findUserIndex(long accNum) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).accNum == accNum) {
                return i;
            }
        }
        return -1;
    }

    public static void handleTransaction(int index) throws Exception {
        Screens.clearScreen();
        Screens.mainMenu();
        User user = users.get(index);
        System.out.println(GREEN + "\nWelcome, " + user.name + RESET);
        System.out.println(YELLOW + "\n1. Withdraw\n2. Deposit\n3. Check Balance\n4. View Transaction History\n5. Quit" + RESET);
        System.out.print(CYAN + "\nChoose option: " + RESET);
        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                System.out.print(PURPLE + "\nEnter withdraw amount (multiples of 100): " + RESET);
                int withdraw = sc.nextInt();
                if (withdraw % 100 == 0 && user.accBal - withdraw >= 1000) {
                    processTransaction("WITHDRAWN", user, -withdraw);
                    user.accBal -= withdraw;
                    saveUsers();
                } else {
                    System.out.println(RED + "Insufficient balance or invalid amount." + RESET);
                }
                break;
            case 2:
                System.out.print(PURPLE + "\nEnter deposit amount (multiples of 100): " + RESET);
                int deposit = sc.nextInt();
                if (deposit % 100 == 0) {
                    processTransaction("DEPOSITED", user, deposit);
                    user.accBal += deposit;
                    saveUsers();
                } else {
                    System.out.println(RED + "Amount must be in multiples of 100." + RESET);
                }
                break;
            case 3:
                System.out.println(GREEN + "\nCurrent Balance: ₹" + user.accBal + RESET);
                break;
            case 4:
                viewUserTransactions(user.name);
                break;
            case 5:
                return;
            default:
                System.out.println(RED + "Invalid choice." + RESET);
        }
        System.out.println(GREEN + "\nPress Enter to continue..." + RESET);
        sc.nextLine(); sc.nextLine();
    }

    public static void viewUserTransactions(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTION_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(username)) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            System.out.println(RED + "Error reading transaction file." + RESET);
        }
    }

    public static void adminLogin() throws Exception {
        Screens.clearScreen();
        Screens.mainMenu();
      

            while (true) {
                Screens.clearScreen();
                Screens.mainMenu();
                System.out.println(GREEN + "\nWelcome, Admin" + RESET);
                System.out.println(YELLOW +
                        "\n1. Add New User\n" +
                        "2. Delete User\n" +
                        "3. View All Users\n" +
                        "4. View All Transactions\n" +
                        "5. Logout" + RESET);
                System.out.print(CYAN + "\nChoose option: " + RESET);
                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        addNewUser();
                        break;
                    case 2:
                        deleteUser();
                        break;
                    case 3:
                        viewAllUsers();
                        Thread.sleep(5000);
                        break;
                    case 4:
                        viewAllTransactions();
                        Thread.sleep(5000);
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println(RED + "Invalid choice." + RESET);
                        Thread.sleep(2000);
                }

            


            }
   
    }

    public static void addNewUser() throws IOException {
        System.out.print(PURPLE + "\nEnter Name: " + RESET);
        String name = sc.next();
        System.out.print(PURPLE + "Enter Account Number: " + RESET);
        long accNum = sc.nextLong();
        System.out.print(PURPLE + "Enter PIN: " + RESET);
        int pin = sc.nextInt();
        System.out.print(PURPLE + "Enter Opening Balance: " + RESET);
        int balance = sc.nextInt();

        users.add(new User(name, accNum, pin, balance));
        saveUsers();
        System.out.println(GREEN + "\nUser added successfully!" + RESET);
    }

    public static void deleteUser() throws IOException {
        System.out.print(PURPLE + "\nEnter Account Number to Delete: " + RESET);
        long accNum = sc.nextLong();
        int index = findUserIndex(accNum);
        if (index != -1) {
            users.remove(index);
            saveUsers();
            System.out.println(GREEN + "\nUser deleted successfully!" + RESET);
        } else {
            System.out.println(RED + "\nUser not found!" + RESET);
        }
    }

    public static void viewAllUsers() {
        System.out.println(YELLOW + "\n%-15s%-20s%-10s%-15s".formatted("USERNAME", "ACCOUNTNUMBER", "PIN", "BALANCE") + RESET);
        for (User user : users) {
            System.out.println("%-15s%-20d%-10d%-15d".formatted(user.name, user.accNum, user.accPin, user.accBal));
        }
    }

    public static void viewAllTransactions() {
        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTION_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println(RED + "Error reading transaction file." + RESET);
        }
    }
}
