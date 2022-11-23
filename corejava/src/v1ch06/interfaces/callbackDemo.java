package v1ch06.interfaces;
import javax.swing.*;

public class callbackDemo {


    public static void main(String[] args) {


        TimePrinter timePrinter = new TimePrinter();

        // construct a timer that calls the listener
        // once every second
        Timer timer = new Timer(1000, timePrinter);
        timer.start();

        // keep program running until the user selects "OK"
        JOptionPane.showMessageDialog(null, "Quit program?");
        System.exit(0);

    }
}
