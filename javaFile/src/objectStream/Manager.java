package objectStream;

public class Manager extends Employee {

    private Employee secretary;

    /**
     * Constructs a Manager without a secretary
     * @param name the employee's name
     * @param salary the salary
     * @param year the hire year
     * @param month the hire month
     * @param day the hire day
     */
    public Manager(String name, double salary, int year, int month, int day) {
        super(name, salary, year, month, day);
        this.secretary = null;
    }

    public void setSecretary(Employee secretary) {
        this.secretary = secretary;
    }

    public String toString() {
        return super.toString() + "[secretary=" + secretary + "]";
    }
}
