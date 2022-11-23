package ImageIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ImageIconExample {
    private JFrame mainFrame;
    private JLabel headerLabel;
    private JLabel statusLabel;
    private JPanel controlPanel;

    public ImageIconExample(){
        prepareGUI();
    }
    public static void main(String[] args){
        ImageIconExample  swingControlDemo = new ImageIconExample();
        swingControlDemo.showImageIconDemo();
    }
    private void prepareGUI(){
        mainFrame = new JFrame("Java Swing ImageIcon 示例");
        mainFrame.setSize(500,500);
//        mainFrame.setLayout(new GridLayout(3, 1));
        mainFrame.setLayout(new FlowLayout());

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }
        });
        headerLabel = new JLabel("", JLabel.CENTER);
        statusLabel = new JLabel("",JLabel.CENTER);
        statusLabel.setSize(350,100);

        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        mainFrame.add(headerLabel);
        mainFrame.add(controlPanel);
        mainFrame.add(statusLabel);
        mainFrame.setVisible(true);
    }
    // Returns an ImageIcon, or null if the path was invalid.
    private static ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = ImageIconExample.class.getResource(path);

        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    private void showImageIconDemo(){
        headerLabel.setText("Control in action: ImageIcon");
        ImageIcon icon = createImageIcon("../images/tux.png","Java");
//        ImageIcon icon = createImageIcon("../images/cloud.jpeg","Java");

        JLabel commentlabel = new JLabel("zhangyuwei", icon, JLabel.CENTER);
        controlPanel.add(commentlabel);
        mainFrame.setVisible(true);
    }
}
