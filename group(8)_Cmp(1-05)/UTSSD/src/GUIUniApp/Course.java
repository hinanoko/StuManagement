package GUIUniApp;

public class Course {
    private String name;

    private String ID;

    private double score;

    public Course() {
    }

    public Course(String name, String ID, double score) {
        this.name = name;
        this.ID = ID;
        this.score = score;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return "Course{" +
                "name='" + name + '\'' +
                ", ID='" + ID + '\'' +
                ", score=" + score +
                '}';
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
