public class Patient {
    private String name;
    private String disease;
    private int age;

    Patient(String name, String disease, int age) {
        this.name = name;
        this.disease = disease;
        this.age = age;
    }

    public String toString() {
        return "Name: " + name + ", Disease: " + disease + ", Age: " + age;
    }
}