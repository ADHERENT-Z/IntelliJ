package textFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Scanner;

public class TextFileTest {
    public static void main(String[] args) throws IOException{
//        Employee[] staff = new Employee[3];
//
//        staff[0] = new Employee("Carl Cracker", 75000, 1987, 12, 15);
//        staff[1] = new Employee("Harry Hacker", 50000, 1989, 10, 1);
//        staff[2] = new Employee("Tony Tester", 40000, 1990, 3, 15);
//
//        // save all employee records to the file employee.dat
//        try (PrintWriter out = new PrintWriter("employee.dat", String.valueOf(StandardCharsets.UTF_8))) {
//            writeData(staff, out);
//        }

        // retrieve all records into a new array
        Scanner in = new Scanner(new FileInputStream("employee.dat"), "UTF-8");
        Employee[] newStaff = readData(in);
        for (Employee e : newStaff) {
            System.out.println(e);
        }
    }

    /**
     * Reads an array of employees from a scanner
     * @param in the scanner
     * @return the array of employees
     */
    private static Employee[] readData(Scanner in){
        /**
         *  通过 Scanner 类的 nextxx() 与 nextLine() 方法获取输入的字符串，
         *  使用 hasNext 与 hasNextLine 判断是否还有输入的数据
         *
         *  nextxx() 与 nextLine() 的区别
         *  nextxx():只取数字部分
         *      对输入有效字符前遇到的空白，nextxx()方法自动将其去掉
         *      只有输入有效数字后才能将其后面输入的空白作为分隔符或者结束符
         *      next()方法不能读取带有空格的字符串
         *  nextLine():
         *      可以获取空白
         *      以 Enter 作为结束符，也就是 nextLine() 方法返回的是输入回车键以前的所有字符
         */
        int n = in.nextInt();
        in.nextLine();
        Employee[] employees = new Employee[n];
        for (int i = 0; i < n; i++){
            employees[i] = readEmployee(in);
        }
        return employees;
    }

    /**
     * Reads employee data from a buffered reader
     * @param in the scanner
     */
    private static Employee readEmployee(Scanner in) {
        String line = in.nextLine();
        String[] tokens = line.split("\\|");
        String name = tokens[0];
        double salary = Double.parseDouble(tokens[1]);
        LocalDate hireDate = LocalDate.parse(tokens[2]);
        int year = hireDate.getYear();
        int month = hireDate.getMonthValue();
        int day = hireDate.getDayOfMonth();
        return new Employee(name, salary, year, month, day);
    }

    /**
     * Writes all employees in an array to a print writer
     * @param employees an array of employees
     * @param out a print writer
     */
    private static void writeData(Employee[] employees, PrintWriter out)
            throws IOException{
        // write number of employees
        out.println(employees.length);
        for (Employee e : employees) {
            writeEmployee(out, e);
        }
    }

    /**
     * Writes employee data to a print writer
     * @param out the print writer
     */
    private static void writeEmployee(PrintWriter out, Employee e) {
        out.println(e.getName() + "|" + e.getSalary() + "|" + e.getHireDay());
    }
}
