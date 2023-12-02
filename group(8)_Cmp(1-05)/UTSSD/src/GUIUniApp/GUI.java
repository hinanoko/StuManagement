package GUIUniApp;


import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.scene.text.Text;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import javafx.geometry.Pos;

public class GUI extends Application {
    private Stage primaryStage; // main window
    private Stage secondaryStage; // New Window

    private Stage courseWindow; // Window for displaying selected courses

    private Stage courseSelectionWindow; // Window for displaying course selection

    private Stage courseDeleteWindow;

    private Stage changePasswordWindow;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("GUIUniApp Login");

        // Create login layout
        VBox loginLayout = new VBox(10);
        loginLayout.setPadding(new Insets(10));

        // Create text boxes and labels
        TextField useremailField = new TextField();
        useremailField.setPromptText("Student Email");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        Label messageLabel = new Label();

        // Create login button
        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            String userEmail = useremailField.getText();
            String password = passwordField.getText();
            if (!login(userEmail, password).equals("false")) {
                messageLabel.setText("Login successful, welcome " + login(userEmail, password));
                String username = GotstudentID(userEmail, password);
                openSecondaryWindow(username);
            } else {
                messageLabel.setText("Login fail, the email or password maybe incorrect");
                //showAlert("LoginFail", "The email for login or password maybe wrong");
                openErrorWindow1();
            }
        });

        // Add control to login layout
        loginLayout.getChildren().addAll(useremailField, passwordField, loginButton, messageLabel);
        loginLayout.setAlignment(Pos.CENTER);

        // Create a scene and set it for the main stage
        Scene scene = new Scene(loginLayout, 500, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openSecondaryWindow(String username) {
        // Create a layout for a new window
        VBox secondaryLayout = new VBox(10);
        secondaryLayout.setPadding(new Insets(10));
        Scene secondaryScene = new Scene(secondaryLayout, 400, 300);
        secondaryStage = new Stage();
        secondaryStage.setScene(secondaryScene);
        secondaryStage.setTitle("Welcome to GUIUniApp");

        // Create a control in a new window
        Button enrollButton = new Button("Enrol subject");
        Button dropButton = new Button("Delete subject");
        Button displayCoursesButton = new Button("Show subject");
        Button changePasswordButton = new Button("Change password");
        Button exitButton = new Button("Exit");

        // Add event handler
        enrollButton.setOnAction(e -> {
            // Implement course selection function
            openCourseSelectionWindow(username);
        });

        dropButton.setOnAction(e -> {
            // Implement course deletion function
            deleteSelectedCourses(username);
        });

        displayCoursesButton.setOnAction(e -> {
            // Show courses
            displaySelectedCourses(username);
        });

        changePasswordButton.setOnAction(e -> {
            // Change password
            changePassword(username);
        });

        exitButton.setOnAction(e -> {
            secondaryStage.close();
        });

        // Add controls to a new window layout
        secondaryLayout.getChildren().addAll(enrollButton, dropButton, displayCoursesButton, changePasswordButton, exitButton);
        secondaryLayout.setAlignment(Pos.CENTER);

        secondaryStage.show();
    }

    private String login(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader("src/CLIUniApp/students.data"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] studentInfo = line.split(",");
                if (studentInfo.length >= 4) { // Assuming the third item is the student password and the fourth item is the student ID
                    String storedUsername = studentInfo[0];
                    String storedPassword = studentInfo[2];
                    String studentID = studentInfo[3];
                    String studentEmail = studentInfo[1];

                    if (username.equals(studentEmail) && password.equals(storedPassword)) {
                        return storedUsername; // Login succeeded
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "false"; // Login failed
    }

    private String GotstudentID(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader("src/CLIUniApp/students.data"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] studentInfo = line.split(",");
                if (studentInfo.length >= 4) { // Assuming the third item is the student password and the fourth item is the student ID
                    String storedUsername = studentInfo[0];
                    String storedPassword = studentInfo[2];
                    String studentID = studentInfo[3];
                    String studentEmail = studentInfo[1];

                    if (username.equals(studentEmail) && password.equals(storedPassword)) {
                        return studentID; // Login succeeded
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "false"; // Login failed
    }

    private void displaySelectedCourses(String username) {
        // Create a layout for displaying selected courses
        VBox coursesLayout = new VBox(10);
        coursesLayout.setPadding(new Insets(10));
        Scene courseScene = new Scene(coursesLayout, 400, 300);
        courseWindow = new Stage();
        courseWindow.setScene(courseScene);
        courseWindow.setTitle("SubjectSelected");

        Text titleLabel = new Text("This is the course showing page, show subjectID");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        coursesLayout.getChildren().add(titleLabel);

        // Read course information that students have selected
        List<String> selectedCourses = getSelectedCourses(username);

        // If there are no selected courses
        if (selectedCourses.isEmpty()) {
            Text noCoursesText = new Text("You didn't select any subject");
            coursesLayout.getChildren().add(noCoursesText);
        } else {
            // Create a text label to display the selected courses
            for (String course : selectedCourses) {
                Label courseLabel = new Label(course);
                coursesLayout.getChildren().add(courseLabel);
            }
        }

        courseWindow.show();
    }


    private void openCourseSelectionWindow(String studentID) {
        // Create a layout for displaying course selections
        VBox courseSelectionLayout = new VBox(10);
        courseSelectionLayout.setPadding(new Insets(10));
        Scene courseSelectionScene = new Scene(courseSelectionLayout, 500, 300);
        courseSelectionWindow = new Stage();
        courseSelectionWindow.setScene(courseSelectionScene);
        courseSelectionWindow.setTitle("SelectSubjecy");

        // Add a text label at the top of the window
        Text titleLabel = new Text("This is the course selection page, showing subjectID");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        courseSelectionLayout.getChildren().add(titleLabel);

        // Read all courses
        List<String> selectedCourses = getSelectedCourses(studentID);
       // List<Course> allCourses = getAllCourses();
        List<String> randomCourseIDs = generateRandomCourseIDs(selectedCourses);

        // Create a checkbox group to store course selections
        if (selectedCourses.size() >= 4) {
            //showAlert("Enrol Error", "You can't exceed four subjects");
            openErrorWindow2();
            courseSelectionWindow.close();
            return;
        }

        ToggleGroup courseGroup = new ToggleGroup();
        // Create a button for selecting courses
        for (String courseID : randomCourseIDs) {
            RadioButton courseRadioButton = new RadioButton(courseID);
            courseRadioButton.setToggleGroup(courseGroup);
            courseSelectionLayout.getChildren().add(courseRadioButton);
        }

        // Create Selection Button
        Button selectButton = new Button("select");
        selectButton.setOnAction(e -> {
            String selectedCourseID = getSelectedCourseID(courseGroup.getSelectedToggle());
            if (selectedCourseID != null) {
                String courseInfo = "," + selectedCourseID + "(" + generateRandomScore() + ")";
                updateStudentCourses(studentID, courseInfo);
                courseSelectionWindow.close();
            }
        });

        // Add controls to course selection layout
        courseSelectionLayout.getChildren().addAll(selectButton);
        courseSelectionLayout.setAlignment(Pos.CENTER);

        courseSelectionWindow.show();
    }

    private void openErrorWindow1() {
        Stage errorStage = new Stage();
        errorStage.setTitle("Error");

        Label errorLabel = new Label("The email for login or password maybe wrong");
        errorLabel.setStyle("-fx-text-fill: #ff0000;"); // Set the font color to red
        Button closeButton = new Button("OK");
        closeButton.setOnAction(e -> errorStage.close());

        VBox.setMargin(closeButton, new Insets(20, 20, 20, 200));

        VBox errorLayout = new VBox(10);
        errorLayout.getChildren().addAll(errorLabel, closeButton);
        Scene errorScene = new Scene(errorLayout, 300, 100);

        errorStage.setScene(errorScene);
        errorStage.show();
    }

    private void openErrorWindow2() {
        Stage errorStage = new Stage();
        errorStage.setTitle("Error");

        Label errorLabel = new Label("You can't exceed four subjects");
        errorLabel.setStyle("-fx-text-fill: red;"); // Set the font color to red
        Button closeButton = new Button("OK");
        closeButton.setOnAction(e -> errorStage.close());

        VBox.setMargin(closeButton, new Insets(20, 20, 20, 200));

        VBox errorLayout = new VBox(10);
        errorLayout.getChildren().addAll(errorLabel, closeButton);
        Scene errorScene = new Scene(errorLayout, 300, 100);

        errorStage.setScene(errorScene);
        errorStage.show();
    }


    private List<String> getSelectedCourses(String studentID) {
        List<String> selectedCourses = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/CLIUniApp/students.data"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] studentInfo = line.split(",");
                if (studentInfo.length >= 4 && studentInfo[3].equals(studentID)) {
                    for (int i = 4; i < studentInfo.length; i++) {
                        selectedCourses.add(studentInfo[i].split("\\(")[0]); // Obtain course ID from course information
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return selectedCourses;
    }


    private int generateRandomScore() {
        Random random = new Random();
        int mark = random.nextInt(75) + 25;
        return mark; // Generate random scores from 0 to 100
    }

    // Update students' selected courses
    private void updateStudentCourses(String studentID, String courseInfo) {
        String fileName = "src/CLIUniApp/students.data";
        String newCourse = courseInfo;
        int lineToModify = findStudentLineByStudentID(fileName, studentID); // Index of the third row (starting from 0)

        try {
            // read file
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            StringBuilder fileContents = new StringBuilder();
            int lineIndex = 0;

            while ((line = br.readLine()) != null) {
                if (lineIndex == lineToModify) {
                    // Add a new course ID and grade to the course list in the third row
                    line += newCourse;
                }
                fileContents.append(line).append(System.lineSeparator());
                lineIndex++;
            }
            br.close();

            // Write updated data back to file
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
            bw.write(fileContents.toString());
            bw.close();

            System.out.println("Data has updated and saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static int findStudentLineByStudentID(String fileName, String targetStudentID) {
        try {
            // Open file to read data
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {
                // Split each row of data
                String[] parts = line.split(",");
                if (parts.length > 3 && parts[3].equals(targetStudentID)) {
                    // Student ID matching
                    br.close();
                    return lineNumber;
                }
                lineNumber++;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1; // No matching student ID found, returning -1 indicates not found
    }

    public void deleteSelectedCourses(String username){
        VBox courseDeleteLayout = new VBox(10);
        courseDeleteLayout.setPadding(new Insets(10));
        Scene courseDeleteScene = new Scene(courseDeleteLayout, 500, 300);
        courseDeleteWindow = new Stage();
        courseDeleteWindow.setScene(courseDeleteScene);
        courseDeleteWindow.setTitle("DeleteSubject");

        List<String> selectedCourses = getSelectedCourses(username);

        // Show courses selected by students
        for (String course : selectedCourses) {
            Text courseText = new Text(course);
            Button deleteButton = new Button("Delete"); // Add Delete Button
            deleteButton.setOnAction(e -> {
                // Add logic for deleting courses here
                String courseToDelete = courseText.getText(); // Obtain the course to be deleted
                // Calling the method of deleting courses
                deleteStudentCourse(username, courseToDelete);

                // Update the interface and delete the corresponding courses from the interface
                courseDeleteLayout.getChildren().remove(courseText);
                courseDeleteWindow.close();
            });

            courseDeleteLayout.getChildren().addAll(courseText, deleteButton);
        }

        courseDeleteWindow.show();
    }

    private void deleteStudentCourse(String username, String courseIDToDelete) {
        try {
            // Read the file and save each line of data
            ArrayList<String> lines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader("src/CLIUniApp/students.data"));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();

            // Find the row where the specified student ID is located
            int lineNumberToDelete = -1;
            for (int i = 0; i < lines.size(); i++) {
                String[] studentData = lines.get(i).split(",");
                if (studentData.length > 3 && studentData[3].equals(username)) {
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
                    String courseID = extractCourseID(courseInfo);

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
                PrintWriter writer = new PrintWriter(new FileWriter("src/CLIUniApp/students.data"));
                for (String updatedLine : lines) {
                    writer.println(updatedLine);
                }
                writer.close();

            } else {
                System.out.println("The specified student ID was not found:" + username);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changePassword(String userId){
        VBox changePasswordLayout = new VBox(10);
        changePasswordLayout.setPadding(new Insets(10));
        Scene changePasswordScene = new Scene(changePasswordLayout, 500, 300);
        changePasswordWindow = new Stage();
        changePasswordWindow.setScene(changePasswordScene);
        changePasswordWindow.setTitle("ModifyPassword");

        Label instructionLabel = new Label("Please input the new password");
        PasswordField newPasswordField = new PasswordField();
        Button changePasswordButton = new Button("Confirm to change");

        changePasswordButton.setOnAction(e -> {
            String newPass = newPasswordField.getText();
            if (isValidPassword(newPass)) {
                // Password format verification passed, call the method to modify the password
                changeUserPassword(userId, newPass);
                changePasswordWindow.close();
            } else {
                showAlert("Password format error",
                        "Password must conform to requirements:\n- first letter is capital" +
                                "\n- at least five letters\n- followed by three or more numbers");
            }
        });

        changePasswordLayout.getChildren().addAll(instructionLabel, newPasswordField, changePasswordButton);

        changePasswordWindow.show();
    }

    private void changeUserPassword(String userId, String newPassword) {
        // Implement the logic of modifying passwords here, updating passwords in student data files

        String fileName = "src/CLIUniApp/students.data";
        List<String> fileContents = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 3 && parts[3].equals(userId)) {
                    // Student ID matching, update password field
                    parts[2] = newPassword; // The third item is the password field
                }
                fileContents.add(String.join(",", parts));
            }
            br.close();

            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
            for (String content : fileContents) {
                bw.write(content);
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isValidPassword(String password) {
        // Password format validation rules
        String regex = "^[A-Z][a-zA-Z]{4,}[0-9]{3,}$";
        return Pattern.matches(regex, password);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private List<String> generateRandomCourseIDs(List<String> selectedCourses) {
        List<String> randomCourseIDs = new ArrayList<>();
        Random random = new Random();

        while (randomCourseIDs.size() < 4) {
            int randomID = random.nextInt(999) + 1; // Generate random IDs from 1 to 999
            String courseID = String.format("%03d", randomID); // Format to three digits, for example: 001
            if (!selectedCourses.contains(courseID) && !randomCourseIDs.contains(courseID)) {
                randomCourseIDs.add(courseID);
            }
        }

        return randomCourseIDs;
    }

    private String getSelectedCourseID(Toggle selectedToggle) {
        RadioButton radioButton = (RadioButton) selectedToggle;
        if (radioButton != null) {
            return radioButton.getText(); // Return the selected random course ID
        }
        return null;
    }


    public static String extractCourseID(String courseInfo) {
        // Assuming the format of course information is: course ID (grade)
        int openingParenthesisIndex = courseInfo.indexOf('(');
        if (openingParenthesisIndex != -1) {
            return courseInfo.substring(0, openingParenthesisIndex);
        }
        return courseInfo; // If there are no grades, return the entire course information
    }


}
