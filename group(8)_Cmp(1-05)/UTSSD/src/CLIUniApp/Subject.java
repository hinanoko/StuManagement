package CLIUniApp;


import java.io.Serializable;
import java.util.Random;

public class Subject implements Serializable {
    private String courseID;


    public Subject() {
    }



    public Subject(String courseID) {

        this.courseID = courseID;

    }

    // Getter and setter methods for courseName, courseID, and grade


    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }



    @Override
    public String toString() {
        return
                "Course ID: " + courseID + "\n";

    }

    public static String generateCourseID(){

        Random r = new Random();

        int randomNum = r.nextInt(998) + 1;

        // Convert a random number into a string and fill in zeros before it
        String formattedNumber = String.format("%03d", randomNum);

        return formattedNumber;
    }


    public static int generateStudentmark(){
        Random r = new Random();
        int mark = r.nextInt(74) + 25;
        return mark;
    }

    public static String getGradeLevel(double marks)
    {
        if(marks >= 85){
            return "HD";
        } else if (marks >= 75 && marks < 85) {
            return "D";
        } else if (marks >= 65 && marks < 75) {
            return "C";
        } else if (marks >= 50 && marks < 65) {
            return "P";
        } else if (marks < 50) {
            return "Z";
        }
        return "error";
    }
}

