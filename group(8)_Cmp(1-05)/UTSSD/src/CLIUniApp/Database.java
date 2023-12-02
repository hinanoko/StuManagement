package CLIUniApp;


import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Database {

    public static boolean checkAndCreateFile(String fileName) {
        File file = new File(fileName);

        if (file.exists()) {
            // file already exist
            return true;
        } else {
            // File does not exist, create file
            try {
                if (file.createNewFile()) {
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public static void saveStudentData(Student s1, String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {
            // Write student information into a file using a comma separated format
            writer.println(s1.getName() + "," + s1.getEmail() + "," + s1.getPassword() + "," + s1.getID());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Set<String> getCourseIDsByStudentID(String fileName, String studentID) {
        Set<String> courseIDs = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] elements = line.split(",");
                if (elements.length > 3 && elements[3].equals(studentID)) {
                    // Line containing the specified student ID
                    for (int i = 4; i < elements.length; i++) {
                        String courseData = elements[i].trim();
                        if (courseData.matches("\\d+\\(\\d+\\)")) { // Matching Course ID (Grade)
                            String courseID = courseData.split("\\(")[0];
                            courseIDs.add(courseID);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return courseIDs;
    }

    public static void clearFileData(String filePath) throws IOException {
        File file = new File(filePath);

        // delete original file
        if (file.exists()) {
            if (file.delete()) {
            } else {
                throw new IOException("fail to delete original file");
            }
        }

        // Create a new empty file
        if (file.createNewFile()) {
        } else {
            throw new IOException("fail to create new file");
        }
    }


    public static void deleteCourseForStudent(String studentID, String courseIDToDelete, String fileName) {
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
            int lineNumberToDelete = -1;
            for (int i = 0; i < lines.size(); i++) {
                String[] studentData = lines.get(i).split(",");
                if (studentData.length > 3 && studentData[3].equals(studentID)) {
                    lineNumberToDelete = i;
                    break;
                }
            }

            if (lineNumberToDelete != -1) {
                // Obtain students' original course information (if any)
                String studentLine = lines.get(lineNumberToDelete);
                String[] studentData = studentLine.split(",");
                StringBuilder updatedStudentData = new StringBuilder(studentLine);

                // Traverse students' course information
                for (int i = 4; i < studentData.length; i++) {
                    String courseInfo = studentData[i];
                    String courseID = Enrol.extractCourseID(courseInfo);

                    // If the course ID matches the ID to be deleted, delete the course information from the student data
                    if (courseID.equals(courseIDToDelete)) {
                        int courseIndex = updatedStudentData.indexOf(courseInfo);
                        int commaIndex = updatedStudentData.lastIndexOf(",", courseIndex);
                        if (commaIndex != -1) {
                            updatedStudentData.delete(commaIndex, courseIndex + courseInfo.length());
                        }
                    }
                }

                // Update student data rows
                lines.set(lineNumberToDelete, updatedStudentData.toString());

                // Write back file
                PrintWriter writer = new PrintWriter(new FileWriter(fileName));
                for (String updatedLine : lines) {
                    writer.println(updatedLine);
                }
                writer.close();

            } else {
                System.out.println("The specified student ID was not found:" + studentID);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
