//32555, Fundamental of Software Development, Group 8, Mingqian_Wang, Junting_Ye, Weijie_He
//Any Question please contact by email

package CLIUniApp;


import java.util.Scanner;

public class UniSystem {
    public UniSystem() {
        String reset = "\u001B[0m";  // reset the colour
        String red = "\u001B[31m";    // red
        String green = "\u001B[32m";  // green
        String yellow = "\u001B[33m"; // yellow
        String blue = "\u001B[34m";  // blue

        String fileName = "src/CLIUniApp/students.data";    //Relative path, can be modified to absolute path as needed
        Database.checkAndCreateFile(fileName);            //check the file whether exist

        while (true) {
            System.out.print(blue + "University System: (A)dmin, (S)tudent, or X :" + reset); //the choice of Admin or Student
            Scanner sc = new Scanner(System.in);
            String num = sc.next();
            if (num.equals("S")) {
                new Login();
            } else if (num.equals("A")) {
                new Admin();
            } else if (num.equals("X")) {
                System.out.println(yellow + "Thank you" + reset);
                System.exit(0);
            } else {
                System.out.println(red + "wrong command" + reset);
            }
        }
    }


}
