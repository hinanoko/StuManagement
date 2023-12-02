//32555, Fundamental of Software Development, Group 8, Mingqian_Wang, Junting_Ye, Weijie_He
//Any Question please contact by email

package CLIUniApp;



import java.io.*;
import java.util.Scanner;

public class Login {
    public Login() {
        String reset = "\u001B[0m";  // reset the colour
        String red = "\u001B[31m";    // red
        String green = "\u001B[32m";  // green
        String yellow = "\u001B[33m"; // yellow
        String blue = "\u001B[34m";  // blue

        String fileName = "src/CLIUniApp/students.data";
        Scanner sc = new Scanner(System.in);
        boolean flag1 = true;
        do {
            try {
                System.out.print(blue + "Student System (l/r/x) :" + reset);    //choice of login, register or exist

                String num = sc.next();

                switch (num) {
                    case "r":
                        System.out.println(green + "Student Sign Up" + reset);   //enter the register code
                        Student s1 = new Student();
                        register(s1, fileName);
                        break;
                    case "x":
                        flag1 = false;                    //exist back to Unisystem platform
                        break;
                    case "l":
                        Enrol ee = new Enrol();             //enter the login interface
                        break;
                    default:
                        System.out.println("wrong choice");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        } while (flag1);
    }


    public static void register(Student s1, String fileName){
        String reset = "\u001B[0m";  // reset the colour
        String red = "\u001B[31m";    // red
        String yellow = "\u001B[33m"; // yellow
        Scanner sc = new Scanner(System.in);

        boolean flag = false;
        boolean flag1 = false;
        do {
            System.out.print("Email: ");
            String email = sc.next();
            System.out.print("Password: ");
            String password = sc.next();
            flag = s1.isValidEmail(email);                     //check the email and password whether conform to format
            flag1 = s1.isValidPassword(password);

            flag = flag && flag1;           //And option, one false all false
            if(flag == false ){
                    System.out.println(red + "Incorrect email or password format" + reset);
            }
            else{
                System.out.println(yellow + "email and password formats acceptable" + reset);
                boolean flag2 = doesEmailExist(fileName, email);    //determine whether the email has exist
                String name = extractNameFromEmail(email);
                if(flag2 == false){
                    System.out.println(red + "Student " + name + " already exists" + reset);
                    break;
                }
                else{
                    System.out.print("Name: ");       //type the name
                    String nul = sc.nextLine();         //eat the space
                    String name1 = sc.nextLine();
                    s1.setName(name1);
                    s1.setEmail(email);
                    s1.setPassword(password);
                    String ID = Student.generateStudentID(fileName);           //generate the unique student ID
                    s1.setID(ID);

                    Database.saveStudentData(s1, fileName);       //write IO into the file

                    System.out.println(yellow + "Enrolling Student " + s1.getName() + reset);
                }
            }
        } while (flag == false);


    }


    public static boolean isStudentIDUnique(String studentID, String fileName) {   //use to judge whether the ID is unique
        try (Scanner fileScanner = new Scanner(new File(fileName))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] studentData = line.split(",");
                String studentIDFromFile = studentData[3]; // The position of student ID in the data

                // Check if the generated ID matches any IDs in the file
                if (studentIDFromFile.equals(studentID)) {
                    return false; // ID is not unique
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return true; // ID is unique
    }

    public static boolean doesEmailExist(String filePath, String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length > 1 && fields[1].equals(email)) {
                    return false; // If the email exists in the database, return false
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true; // If the email does not exist in the database, return true
    }

    public static String extractNameFromEmail(String email) {  //used to display the name on the email
        // Find the location of the '@' symbol
        int atIndex = email.indexOf('@');

        // If the '@' symbol is found
        if (atIndex != -1) {
            // Extract the part before the '@' symbol as the name
            String name = email.substring(0, atIndex);

            // You can remove '.' from the name if necessary
            name = name.replace(".", "");

            return name;
        } else {
            // If there is no '@' symbol, return an empty string or other error indication
            return "";
        }
    }

}
