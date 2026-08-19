package nz.ac.massey.cs251;

import javax.swing.*;
import java.awt.*;
import javax.swing.ImageIcon;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main extends JFrame{
    private static final int Frame_WIDTH = 800;
    private static final int Frame_HEIGHT = 600;

    private JTextArea textArea;
    private JMenuItem select, copy, paste, cut;
    private JLabel infoLabel;

    public Main(){
        super("Text Editor");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(Frame_WIDTH, Frame_HEIGHT);
        this.setResizable(true);

        JMenuBar MenuBar = new JMenuBar();
        this.setJMenuBar(MenuBar);

        JMenu fileMenu = new JMenu("File");
        JMenu searchMenu = new JMenu("Search");
        JMenu viewMenu = new JMenu("View");
        JMenu editMenu = new JMenu("Edit");
        JMenu helpMenu = new JMenu("Help");


        JMenuItem newItem = new JMenuItem("New");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem printItem = new JMenuItem("Print");
        JMenuItem exportItem = new JMenuItem("Export as PDF");
        JMenuItem exitItem = new JMenuItem("Exit");
        JMenuItem find = new JMenuItem(" ");
        JMenuItem select = new JMenuItem("Select");
        JMenuItem copy = new JMenuItem("Copy");
        JMenuItem paste = new JMenuItem("Paste");
        JMenuItem cut = new JMenuItem("Cut");

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

        searchMenu.add(find);

        editMenu.add(select);
        editMenu.add(copy);
        editMenu.add(paste);
        editMenu.add(cut);

        MenuBar.add(fileMenu);
        MenuBar.add(viewMenu);
        MenuBar.add(helpMenu);
        MenuBar.add(searchMenu);
        MenuBar.add(editMenu);




        LocalDateTime time = LocalDateTime.now();

        infoLabel= new JLabel("Time & Date");
        viewMenu.add(infoLabel);
        infoLabel.setText("Time" + time);

        ImageIcon searchIcon = new ImageIcon("search.png");
        find.setIcon(searchIcon);

        this.setVisible(true);
    }

    public static void main(String[] args){ new Main(); }
}