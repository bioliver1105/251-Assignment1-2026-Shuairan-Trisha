package nz.ac.massey.cs251;

import javax.swing.*;
import java.awt.*;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.io.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Main extends JFrame{
    private static final int Frame_WIDTH = 800;
    private static final int Frame_HEIGHT = 600;

    private JTextArea textArea;
    private JMenuItem select, copy, paste, cut;
    private JLabel infoLabel;

    public Main() {
        super("Text Editor");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(Frame_WIDTH, Frame_HEIGHT);
        this.setResizable(true);

        textArea = new JTextArea();
        this.add(new JScrollPane(textArea));

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

        newItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Discard current text and start a new document?",
                    "New", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                textArea.setText("");
            }
        });

        openItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files (*.txt)", "txt"));

            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    textArea.setText(content.toString());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error opening file: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        saveItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files (*.txt)", "txt"));

            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".txt")) {
                    file = new File(file.getAbsolutePath() + ".txt");
                }
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write(textArea.getText());
                    JOptionPane.showMessageDialog(this, "File saved successfully.",
                            "Save", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        exitItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to exit?",
                    "Exit", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        select.addActionListener(e -> textArea.selectAll()); 
        copy.addActionListener(e -> textArea.copy());
        paste.addActionListener(e -> textArea.paste());
        cut.addActionListener(e -> textArea.cut());

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

        infoLabel = new JLabel("Time & Date");
        viewMenu.add(infoLabel);
        infoLabel.setText("Time" + time);

        ImageIcon searchIcon = new ImageIcon("search.png");
        find.setIcon(searchIcon);
        find.addActionListener(e -> searchText());

        this.setVisible(true);
    }

        private void searchText() {
            String query = JOptionPane.showInputDialog(this, "Find:", "Search", JOptionPane.PLAIN_MESSAGE);
            if (query == null || query.isEmpty()) return;

            highlightAllMatches(query);
        }

        private void highlightAllMatches(String query) {
            Highlighter highlighter = textArea.getHighlighter();
            highlighter.removeAllHighlights();

            String content = textArea.getText();
            Highlighter.HighlightPainter painter =
                    new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);

            int index = 0;
            int matchCount = 0;
            try {
                while ((index = content.indexOf(query, index)) != -1) {
                    highlighter.addHighlight(index, index + query.length(), painter);
                    index += query.length();
                    matchCount++;
                }
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }

            if (matchCount == 0) {
                JOptionPane.showMessageDialog(this, "No matches found for \"" + query + "\"");
            }
        }



    public static void main(String[] args){ new Main(); }
}