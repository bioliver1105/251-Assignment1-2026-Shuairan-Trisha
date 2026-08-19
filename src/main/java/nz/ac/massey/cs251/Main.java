package nz.ac.massey.cs251;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main extends JFrame{
    private static final int Frame_WIDTH = 800;
    private static final int Frame_HEIGHT = 600;

    private JTextArea textArea;
    private JMenuItem selectText, copy, paste, cut;
    private JLabel infoLabel;

    public Main(){
        super("Text Editor");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(Frame_WIDTH, Frame_HEIGHT);
        this.setResizable(true);

        JMenuBar MenuBar = new JMenuBar();
        this.setJMenuBar(MenuBar);

        JMenu fileMenu = new JMenu("File");
        JMenu viewMenu = new JMenu("View");
        JMenu helpMenu = new JMenu("Help");

        JMenuItem newItem = new JMenuItem("New");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem printItem = new JMenuItem("Print");
        JMenuItem exportItem = new JMenuItem("Export as PDF");
        JMenuItem exitItem = new JMenuItem("Exit");

        JMenuItem dateTimeItem = new JMenuItem("Date & Time");

        JMenuItem aboutItem = new JMenuItem("About");

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(printItem);
        fileMenu.add(exportItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        viewMenu.add(dateTimeItem);

        helpMenu.add(aboutItem);

        MenuBar.add(fileMenu);
        MenuBar.add(viewMenu);
        MenuBar.add(helpMenu);




        LocalDateTime time = LocalDateTime.now();

        infoLabel= new JLabel("Time & Date");
        MenuBar.add(infoLabel);
        infoLabel.setText("Time" + time);

        this.setVisible(true);
    }

    public static void main(String[] args){ new Main(); }
}