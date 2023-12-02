package CLIUniApp;




import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Student implements Serializable {

    private String name;
    private String email;
    private String password;
    private String ID;

    private String averagelevel;

    private ArrayList<Subject> subjects;

    public Student() {
    }

    public Student(String name, String email, String password, String ID, ArrayList<Subject> subjects, String averagelevel) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.ID = ID;
        this.subjects = subjects;
        this.averagelevel = averagelevel;

    }

    public String getAveragelevel() {
        return averagelevel;
    }

    public void setAveragelevel(String averagelevel) {
        this.averagelevel = averagelevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {

            this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public ArrayList<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(ArrayList<Subject> subjects) {
        this.subjects = subjects;
    }

    public void addSubject(Subject subject) {
        if (subjects == null) {
            subjects = new ArrayList<>();
        }
        subjects.add(subject);
    }

    // Remove a course from a student's elective course list
    public void removeSubject(Subject subject) {
        subjects.remove(subject);
    }

    public static String generateStudentID(String fileName) {
        String studentID;
        boolean isUnique;

        do {
            // Generate random numbers between 1 and 999999
            int randomNumber = new Random().nextInt(999998) + 1;

            // Format random numbers into a 6-digit string
            studentID = String.format("%06d", randomNumber);

            // Check if the generated ID already exists in the file
            isUnique = Login.isStudentIDUnique(studentID, fileName);

        } while (!isUnique);

        return studentID;
    }

    public boolean isValidEmail(String email) {
        // Define regular expression patterns for email
        String emailPattern = "^[a-zA-Z]+\\.[a-zA-Z]+@university\\.com$";

        // Compiling Regular Expressions
        Pattern pattern = Pattern.compile(emailPattern);

        // Using regular expression patterns to match input emails
        Matcher matcher = pattern.matcher(email);

        // Return matching results
        return matcher.matches();
    }

    public boolean isValidPassword(String password) {
        // Define regular expression patterns for passwords
        String passwordPattern = "^[A-Z][a-zA-Z]{4,}[0-9]{3,}$";

        // Compiling Regular Expressions
        Pattern pattern = Pattern.compile(passwordPattern);

        // Using regular expression patterns to match input passwords
        Matcher matcher = pattern.matcher(password);

        // Return matching results
        return matcher.matches();
    }




    }




