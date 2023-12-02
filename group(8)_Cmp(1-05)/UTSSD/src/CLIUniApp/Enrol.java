//32555, Fundamental of Software Development, Group 8, Mingqian_Wang, Junting_Ye, Weijie_He
//Any Question please contact by email
package CLIUniApp;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Enrol {
    public Enrol() {
        String reset = "\u001B[0m";  // reset the colour
        String red = "\u001B[31m";    // red
        String green = "\u001B[32m";  // green
        String yellow = "\u001B[33m"; // yellow
        String blue = "\u001B[34m";  // blue

        String studentFileName = "src/CLIUniApp/students.data"; // Relative path, can be modified to absolute path as needed

        System.out.println(green + "Student Sign In" + reset);
        Scanner loginScanner = new Scanner(System.in);
        boolean newflag1 = false;
        do {
        System.out.print("Email: ");
        String enteredID = loginScanner.next();
        System.out.print("Password: ");
        String enteredPassword = loginScanner.next();
        //Check if the email and password formats are correct
        boolean newflag = ((new Student().isValidEmail(enteredID)) && (new Student().isValidPassword(enteredPassword)));
        newflag1 = newflag;
            if (newflag == true) {

                System.out.println(yellow + "email and password formats acceptable" + reset);
                //Check if the user exists
                boolean loginSuccessful = validateLogin(enteredID, enteredPassword, studentFileName);

                if (loginSuccessful) {
                    //Save the student's information in a Student Class
                    Student loggedInStudent = loadStudentFromDataFile(enteredID, studentFileName);
                    boolean backtologin = true;

                    if (loggedInStudent != null) {

                        do {
                            System.out.print(blue + "Student Course Menu (c/e/r/s/x) :" + reset);  //show the choice
                            String num = loginScanner.next();
                            switch (num) {
                                case "e":
                                    //Check the quantity of courses that students have registered for
                                    int len = getLineLengthByStudentID(studentFileName, loggedInStudent.getID());
                                    if (len < 8) {
                                        select(loggedInStudent, studentFileName, len); //Random registration method
                                    } else {
                                        //Four subjects have been filled
                                        System.out.println(red + "Students are allowed to enrol in 4 subjects only" + reset);
                                    }
                                    break;
                                case "r":
                                    //First, extract the ID of the course that the student has already registered for
                                    Set<String> selectedCourseIDs = getSelectedCourseIDs(loggedInStudent.getID(), studentFileName);

                                    System.out.print("Remove Subject by ID: ");
                                    String deID = loginScanner.next();

                                    //If the course you want to remove exists in the current student's selected course list
                                    if (isCourseSelected(deID, selectedCourseIDs)) {
                                        System.out.println(yellow + "Droping Subject-" + deID + reset);
                                        //Remove course methods
                                        Database.deleteCourseForStudent(loggedInStudent.getID(), deID, studentFileName);
                                        //Calculate how many courses the student currently has left
                                        int len1 = getLineLengthByStudentID(studentFileName, loggedInStudent.getID());
                                        System.out.println(yellow + "You are now enrolled in " + (len1 - 4) + " out of 4 subjects" + reset);
                                    } else {
                                        System.out.println(red + "subject " + deID + " didn't exist" + reset);
                                    }

                                    break;
                                case "s":
                                    //Display the code of the currently selected course
                                    Set<String> courseIDs = Database.getCourseIDsByStudentID(studentFileName, loggedInStudent.getID());
                                    int numberOfUnits = courseIDs.size();
                                    System.out.println(yellow + "showing " + numberOfUnits + " subjects" + reset);
                                    for (String courseID : courseIDs) {
                                        System.out.println("[ Subject::" + courseID + " -- mark = " +
                                                getGradeByStudentIDAndCourseID(studentFileName, loggedInStudent.getID(), courseID) + " -- grade =  " +
                                                Subject.getGradeLevel(getGradeByStudentIDAndCourseID(studentFileName, loggedInStudent.getID(), courseID)) + " ]");
                                    }
                                    break;
                                case "c":
                                    boolean judgeflag = true;
                                    do {
                                        System.out.println(yellow + "Updating Password" + reset);
                                        System.out.print("New Password: ");
                                        String pass = loginScanner.next();
                                        //System.out.print('\n');
                                        boolean judgeflag1 = true;
                                        do {
                                            System.out.print("Confirm Password: ");
                                            String newpass = loginScanner.next();
                                            boolean same = pass.equals(newpass);
                                            if (same == true) {
                                                judgeflag1 = false;
                                                boolean passflag = loggedInStudent.isValidPassword(pass);
                                                if (passflag == true) {
                                                    loggedInStudent.setPassword(pass);
                                                    changeStudentPassword(studentFileName, loggedInStudent.getID(), pass);
                                                    judgeflag = false;
                                                } else {
                                                    System.out.println("wrong password format");
                                                }
                                            } else {
                                                System.out.println(red + "Password does not match - try again" + reset);
                                            }
                                        } while (judgeflag1 == true);
                                    } while (judgeflag == true);
                                    break;
                                case "x":
                                    backtologin = false;
                                    break;
                                default:
                                    System.out.println("Invalid choice. Please choose a valid option.");
                                    break;
                            }
                        } while (backtologin == true);
                    }

                } else {
                    System.out.println(red + "Student does not exist" + reset);
                }
            } else {
                System.out.println(red + "Incorrect email or password format" + reset);
            }
        }while(newflag1 == false);
    }

    public void select(Student s1, String studentFileName, int len) {

        String reset = "\u001B[0m";
        String red = "\u001B[31m";
        String green = "\u001B[32m";
        String yellow = "\u001B[33m";
        String blue = "\u001B[34m";

        int lineNumberToDisplay = findLineByStudentID(studentFileName, s1.getID());

        Set<String> courseIDs = Database.getCourseIDsByStudentID(studentFileName, s1.getID());

        Random r = new Random();

        String detnumber = "000";

        boolean flag5 = true;
        do {

            String formattedNumber = Subject.generateCourseID();

            if(courseIDs.contains(formattedNumber))
            {

            }else{
                detnumber = formattedNumber;
                flag5 = false;
            }
        }while (flag5);


        addCourseToStudent(s1.getID(), detnumber, Subject.generateStudentmark(), studentFileName);

        System.out.println(yellow + "Enrolling in Subject-" + detnumber + reset);

        System.out.println(yellow + "You are now enrolled in " + (len - 3) + " out of 4 subjects" + reset);

        }

    public static boolean validateLogin(String enteredID, String enteredPassword, String fileName) {
        try (Scanner fileScanner = new Scanner(new File(fileName))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] studentData = line.split(",");
                String studentIDFromFile = studentData[1]; // The position of student ID in the data
                String studentPasswordFromFile = studentData[2]; // The position of the password in the data

                // Check if the entered ID and password match
                if (studentIDFromFile.equals(enteredID) && studentPasswordFromFile.equals(enteredPassword)) {
                    return true; // login successful
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return false; // login fail
    }

    public static Student loadStudentFromDataFile(String studentID, String studentFileName) {
        ArrayList<Student> students = new ArrayList<>();

        // Load data from student files
        try (Scanner studentScanner = new Scanner(new File(studentFileName))) {
            while (studentScanner.hasNextLine()) {
                String line = studentScanner.nextLine();
                String[] studentData = line.split(",");
                String idFromFile = studentData[1]; // The position of student ID in the data

                if (idFromFile.equals(studentID)) {
                    String name = studentData[0];
                    String email = studentData[1];
                    String password = studentData[2];
                    String studentnewID = studentData[3];
                    ArrayList<Subject> subjects = new ArrayList<>();

                    // If the student file contains course information, add it to the student object
                    if (studentData.length > 4) {

                        for (int i = 4; i < studentData.length; i = i + 1) {
                            String courseId = extractCourseCode(studentData[i]);

                            //int courseGrade = extractCourseGrade(studentData[i]); // 暂时将成绩设置为0

                            Subject subject = new Subject(courseId);
                            subjects.add(subject);
                        }
                    }

                    Student student = new Student(name, email, password, studentnewID, subjects, null);
                    students.add(student);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (!students.isEmpty()) {
            return students.get(0);
        }

        return null;
    }

    public static int findLineByStudentID(String filePath, String studentID) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                // Assuming the student ID is in the fourth field of each row (separated by commas)
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[3].equals(studentID)) {
                    return lineNumber;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // If the student ID is not found, return -1
        return -1;
    }


    public static String extractCourseCode(String data) {
        // Use regular expressions to match the course number, which is the numerical part
        Pattern pattern = Pattern.compile("(\\d+)");
        Matcher matcher = pattern.matcher(data);

        if (matcher.find()) {
            // Use group (1) to obtain the matched content, which is the course number
            return matcher.group(1);
        } else {
            // If no match is found, return an empty string or other default value
            return "";
        }
    }

    public static int extractCourseGrade(String data) {
        // Use regular expressions to match the numerical parts within parentheses, i.e. grades
        Pattern pattern = Pattern.compile("\\((\\d+)\\)");
        Matcher matcher = pattern.matcher(data);

        if (matcher.find()) {
            // Use Integer. parseInt() to convert the matched score string to an integer and return
            return Integer.parseInt(matcher.group(1));
        } else {
            // If no match is found, consider returning a default value or throwing an exception
            throw new IllegalArgumentException("didn't find the grade");
        }
    }

    public static void addCourseToStudent(String studentID, String courseToAdd, int courseGrade, String fileName) {
        try {
            // Read the file and save each line of data
            ArrayList<String> lines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();

            // Find the row where the specified student ID is located
            int lineNumberToInsertAfter = -1;
            for (int i = 0; i < lines.size(); i++) {
                String[] studentData = lines.get(i).split(",");
                if (studentData.length > 3 && studentData[3].equals(studentID)) {
                    lineNumberToInsertAfter = i;
                    break;
                }
            }

            if (lineNumberToInsertAfter != -1) {
                // Build course information to be added
                String newCourseInfo = "," + courseToAdd + "(" + courseGrade + ")";
                // Obtain students' original course information (if any)
                String studentLine = lines.get(lineNumberToInsertAfter);
                StringBuilder updatedStudentData = new StringBuilder(studentLine);

                // Add new course information after existing course information
                updatedStudentData.append(newCourseInfo);

                // Update student data rows
                lines.set(lineNumberToInsertAfter, updatedStudentData.toString());

                // Write back file
                PrintWriter writer = new PrintWriter(new FileWriter(fileName));
                for (String updatedLine : lines) {
                    writer.println(updatedLine);
                }
                writer.close();

            } else {
                System.out.println("specified student ID not found: " + studentID);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static boolean isCourseSelected(String courseID, Set<String> selectedCourseIDs) {
        return selectedCourseIDs.contains(courseID);
    }

    public static int getLineLengthByStudentID(String fileName, String studentID) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] elements = line.split(",");
                if (elements.length > 3 && elements[3].equals(studentID)) {
                    // Line containing the specified student ID
                    return elements.length;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1; // No rows were found for the specified student ID
    }


    // Extract course ID
    public static String extractCourseID(String courseInfo) {
        // Assuming the format of course information is: course ID (grade)
        int openingParenthesisIndex = courseInfo.indexOf('(');
        if (openingParenthesisIndex != -1) {
            return courseInfo.substring(0, openingParenthesisIndex);
        }
        return courseInfo; // If there are no grades, return the entire course information
    }

    public static Set<String> getSelectedCourseIDs(String studentID, String fileName) {
        Set<String> selectedCourseIDs = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] studentData = line.split(",");
                if (studentData.length > 3 && studentData[3].equals(studentID)) {
                    // The student information contains the specified student ID
                    for (int i = 4; i < studentData.length; i++) {
                        String courseInfo = studentData[i];
                        String courseID = extractCourseID(courseInfo);
                        selectedCourseIDs.add(courseID);
                    }
                    break; // Do not continue traversing after finding the specified student
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return selectedCourseIDs;
    }

    public static boolean changeStudentPassword(String fileName, String studentID, String newPassword) {
        try {
            // Read the file and save each line of data
            ArrayList<String> lines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            boolean studentFound = false;

            while ((line = reader.readLine()) != null) {
                String[] studentData = line.split(",");
                if (studentData.length > 3 && studentData[3].equals(studentID)) {
                    studentFound = true;
                    // 更新学生密码
                    studentData[2] = newPassword;
                    line = String.join(",", studentData);
                }
                lines.add(line);
            }
            reader.close();

            if (studentFound) {
                // Write back file
                PrintWriter writer = new PrintWriter(new FileWriter(fileName));
                for (String updatedLine : lines) {
                    writer.println(updatedLine);
                }
                writer.close();
                return true; // The return password has been successfully changed
            } else {
                return false; // Student not found or change failed
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Exception occurred, change failed
        }
    }

    public static int getGradeByStudentIDAndCourseID(String fileName, String studentID, String courseID) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] elements = line.split(",");
                if (elements.length > 3 && elements[3].equals(studentID)) {
                    // Line containing the specified student ID
                    for (int i = 4; i < elements.length; i++) {
                        String courseData = elements[i].trim();
                        if (courseData.matches("\\d+\\(\\d+\\)")) { // Matching Course ID (Grade)
                            String[] parts = courseData.split("\\(");
                            String currentCourseID = parts[0];
                            if (currentCourseID.equals(courseID)) {
                                String gradeStr = parts[1].replace(")", "");
                                return Integer.parseInt(gradeStr);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // If no matching students or courses are found
        return -1;
    }


}
