package bank;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Screens implements colors {

    static Scanner sc = new Scanner(System.in);

    public static void mainMenu()
    {
        System.out.println(RED + "\n\t\t\tSBI BANK ATM SYSTEM" + RESET);
        System.out.println(BLUE + "\t\t\t----------------------" + RESET);
    }

    public static long inputUserNumber() {
    try {
        System.out.print(CYAN + "\n\tEnter your User Number: " + RESET);
        return sc.nextLong();
    } catch (InputMismatchException e) {
        System.out.println(RED + "\n\tInvalid input! Please enter numbers only." + RESET);
        sc.nextLine(); 
        return -1;
    }
}


    public static int inputUserPin() {
    try {
        System.out.print(CYAN + "\tEnter your PIN: " + RESET);
        return sc.nextInt();
    } catch (InputMismatchException e) {
        System.out.println(RED + "\n\tInvalid PIN! Please enter digits only." + RESET);
        sc.nextLine();
        return -1;
    }
}


    public static void checkingData() throws InterruptedException {
        clearScreen();
        mainMenu();
        System.out.print(YELLOW + "\nChecking credentials " + RESET);
        for (int i = 0; i < 5; i++) {
            System.out.print(".");
            Thread.sleep(500);
        }
        System.out.println();
    }

    public static void timer(int seconds) throws InterruptedException {
        clearScreen();
        mainMenu();
        while (seconds >= 0) {
            System.out.print(CYAN + "\rNext transaction available in: " + seconds + " seconds" + RESET);
            Thread.sleep(1000);
            seconds--;
        }
        System.out.println();
    }

    public static void clearScreen() {
        System.out.print(CLEAR_SCREEN);
        System.out.flush();
    }


}
