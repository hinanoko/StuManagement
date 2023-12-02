package CLIUniApp;


import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Admin {

    public Admin(){
        String fileName = "src/CLIUniApp/students.data";

        boolean flag = true;

        do {
            String reset = "\u001B[0m";  // reset the colour
            String red = "\u001B[31m";    // red
            String green = "\u001B[32m";  // green
            String yellow = "\u001B[33m"; // yellow
            String blue = "\u001B[34m";  // blue

            System.out.print(blue + "Admin System(c/g/p/r/s/x): " + reset);
            Scanner sc = new Scanner(System.in);
            String num = sc.next();


            switch (num) {
                case "c":
                    System.out.println(yellow + "Clearing students database" + reset);
                    System.out.print(red + "Are you sure you want to clear the database  (Y)ES/(N)O: " + reset);
                    String del = sc.next();
                    if (del.equals("Y")) {
                        try {
                            Database.clearFileData(fileName);
                            System.out.println(yellow + "Students data cleared" + reset);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {

                    }

                    break;
                case "g":
                    System.out.println(yellow + "Grade Grouping" + reset);
                    groupStudent(fileName);

                    break;
                case "p":
                    System.out.println(yellow + "PASS/FAIL Participation" + reset);
                    List<Student> studentInfoList1 = processStudentData(fileName);
                    List<Student> studentInfoListFail = new ArrayList<>();
                    List<Student> studentInfoListPass = new ArrayList<>();
                    for (Student student : studentInfoList1) {
                        if(Subject.getGradeLevel(Double.parseDouble(student.getAveragelevel())).equals("Z")){
                            studentInfoListFail.add(student);
                        }
                        else{
                            studentInfoListPass.add(student);
                        }
                    }

                    System.out.println("Pass --> [");
                    for (Student sp : studentInfoListPass) {
                        System.out.print(sp.getName() + " :: " + sp.getID() + " --> Grade:  "
                        + Subject.getGradeLevel(Double.parseDouble(sp.getAveragelevel())) + " - MARK: " + sp.getAveragelevel() + "," + '\n');
                    }
                    System.out.print("]" + '\n');

                    System.out.println("Fail --> [");
                    for (Student sf : studentInfoListFail) {
                        System.out.print(sf.getName() + " :: " + sf.getID() + " --> Grade:  "
                        + Subject.getGradeLevel(Double.parseDouble(sf.getAveragelevel())) + " - MARK: " + sf.getAveragelevel() + "," + '\n');
                    }
                    System.out.print("]" + '\n');


                    break;
                case "r":

                    System.out.print("Remove by ID: ");
                    String studentIDToDelete = sc.next();
                    try {
                        boolean success = deleteStudentData(fileName, studentIDToDelete);
                        if (success) {
                            System.out.println(yellow + "Removing Student " + studentIDToDelete + " Account" + reset);
                        } else {
                            System.out.println(red + "Student " + studentIDToDelete + " does not exist" + reset);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
                case "s":
                    System.out.println(yellow + "Student List" + reset);

                    try {
                        parseStudentData(fileName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
                case "x":

                    flag = false;

                    break;
                default:

                    System.out.println(red + "Wrong choice" + reset);

                    break;
            }

        }while(flag);
    }

    public void parseStudentData(String filePath) throws IOException {

        File file = new File(filePath);

        if (file.length() == 0) {
            System.out.println("< Nothing to Display >");
            return; // If the file is empty, return it directly without continuing with the following code
        }

        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;


        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 3) {
                String name = parts[0].trim();
                String email = parts[1].trim();
                String studentID = parts[3].trim();

                // Output student information
                System.out.println(name + " :: " + studentID + " --> Email: " + email);
            }
        }


        reader.close();
    }

    public void groupStudent(String fileName){
        String reset = "\u001B[0m";  // reset the colour
        String red = "\u001B[31m";    // red
        String green = "\u001B[32m";  // green
        String yellow = "\u001B[33m"; // yellow
        String blue = "\u001B[34m";  // blue
        File file = new File(fileName);

        if (file.length() == 0) {
            System.out.println("< Nothing to Display >");
            return; // If the file is empty, return it directly without continuing with the following code
        }

        List<Student> studentInfoList = processStudentData(fileName);
        List<Student> studentInfoListZ = new ArrayList<>();
        List<Student> studentInfoListD = new ArrayList<>();
        List<Student> studentInfoListHD = new ArrayList<>();
        List<Student> studentInfoListC = new ArrayList<>();
        List<Student> studentInfoListP = new ArrayList<>();
        for (Student student : studentInfoList) {

            if(Subject.getGradeLevel(Double.parseDouble(student.getAveragelevel())).equals("Z")){
                studentInfoListZ.add(student);
            }
            else if(Subject.getGradeLevel(Double.parseDouble(student.getAveragelevel())).equals("D")){
                studentInfoListD.add(student);
            }
            else if(Subject.getGradeLevel(Double.parseDouble(student.getAveragelevel())).equals("HD")){
                studentInfoListHD.add(student);
            }
            else if(Subject.getGradeLevel(Double.parseDouble(student.getAveragelevel())).equals("C")){
                studentInfoListC.add(student);
            }
            else if(Subject.getGradeLevel(Double.parseDouble(student.getAveragelevel())).equals("P")){
                studentInfoListP.add(student);
            }
        }

        System.out.print("Z --> [");
        for (Student sz : studentInfoListZ) {
            System.out.print(sz.getName() + " :: " + sz.getID() + " --> Grade:  "
                    + Subject.getGradeLevel(Double.parseDouble(sz.getAveragelevel())) + " - MARK: " + sz.getAveragelevel() + "," + '\n');
        }
        System.out.print("]" + '\n');

        System.out.println("D --> [");
        for (Student sd : studentInfoListD) {
            System.out.print(sd.getName() + " :: " + sd.getID() + " --> Grade:  "
                    + Subject.getGradeLevel(Double.parseDouble(sd.getAveragelevel())) + " - MARK: " + sd.getAveragelevel() + "," + '\n');
        }
        System.out.print("]" + '\n');

        System.out.println("HD --> [");
        for (Student shd : studentInfoListHD) {
            System.out.print(shd.getName() + " :: " + shd.getID() + " --> Grade:  "
                    + Subject.getGradeLevel(Double.parseDouble(shd.getAveragelevel())) + " - MARK: " + shd.getAveragelevel() + "," + '\n');
        }
        System.out.print("]" + '\n');

        System.out.println("C --> [");
        for (Student scc : studentInfoListC) {
            System.out.print(scc.getName() + " :: " + scc.getID() + " --> Grade:  "
                    + Subject.getGradeLevel(Double.parseDouble(scc.getAveragelevel())) + " - MARK: " + scc.getAveragelevel() + "," + '\n');
        }
        System.out.print("]" + '\n');

        System.out.println("P --> [");
        for (Student sp : studentInfoListP) {
            System.out.print(sp.getName() + " :: " + sp.getID() + " --> Grade:  "
                    + Subject.getGradeLevel(Double.parseDouble(sp.getAveragelevel())) + " - MARK: " + sp.getAveragelevel() + "," + '\n');
        }
        System.out.print("]" + '\n');

    }


    public static boolean deleteStudentData(String filePath, String studentId) throws IOException{
        File file = new File(filePath);
        List<String> lines = new ArrayList<>();
        boolean deleted = false;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] parts = currentLine.split(",");
                if (parts.length >= 4 && parts[3].equals(studentId)) {
                    // Skip the line with the matching student ID
                    deleted = true;
                    continue;
                }
                lines.add(currentLine);
            }

            reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            for (String line : lines) {
                writer.write(line + System.getProperty("line.separator"));
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return deleted;
    }





    public List<Student> processStudentData(String filename) {
        List<Student> studentInfoList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            DecimalFormat decimalFormat = new DecimalFormat("#.##"); // Keep two decimal places

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) {
                    String name = data[0];
                    String id = data[3];
                    double averageGrade = calculateAverageGrade(data);
                    String level = Subject.getGradeLevel(averageGrade);
                    String strValue = decimalFormat.format(averageGrade);

                    // Create a student information object and add it to the collection
                    Student studentInfo = new Student(name, null, null, id, null, strValue);
                    studentInfoList.add(studentInfo);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return studentInfoList;
    }

    private double calculateAverageGrade(String[] data) {
        double sum = 0;
        int count = 0;
        for (int i = 3; i < data.length; i++) {
            String[] courseInfo = data[i].split("\\(");
            if (courseInfo.length == 2) {
                try {
                    int grade = Integer.parseInt(courseInfo[1].replace(")", ""));
                    sum += grade;
                    count++;
                } catch (NumberFormatException e) {
                    // Handle invalid grade format
                }
            }
        }

        if (count > 0) {
            return sum / count;
        } else {
            return 0.0; // Return 0 if no valid grades found
        }
    }

}
