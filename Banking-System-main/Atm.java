import java.util.Scanner;

import bank.*;



public class Atm implements colors,constants 
{
        static Scanner sc = new Scanner(System.in);

        public static void main(String[] args) throws Exception {

            while (true) 
            {
            Screens.clearScreen();
            Screens.mainMenu();

            long inputUser = Screens.inputUserNumber();
            if( inputUser == -1)
            {
                continue;
            }
            int userIndex = FileHandle.findUserIndex(inputUser);

            int inputPin = FileHandle.inputUserPin();

            if(inputPin == -1 )
            {
                continue;
            }


            if( inputUser == ADMIN_USERNAME && inputPin == ADMIN_PASSWORD )
            {
                FileHandle.adminLogin();
            }






            if (userIndex != -1 &&  FileHandle.check(userIndex, inputPin) ) {
                Screens.checkingData();
                FileHandle.handleTransaction(userIndex);
            } else if (inputUser == 0 && inputPin == 0) {
                System.out.println(RED + "\n\n\t\t\tExiting Program..." + RESET);
                Thread.sleep(3000);
                break;
            } else {
                System.out.println(RED + "\n\n\t\tInvalid User Number or Password" + RESET);
                Thread.sleep(3000);
            }
            Screens.timer(7);
        }
            
        }

        
}
