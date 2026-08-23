package nz.ac.massey.cs251;

import javax.swing.*;
import java.awt.*;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.text.*;
import java.io.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends JFrame{
    private static final int Frame_WIDTH = 800;
    private static final int Frame_HEIGHT = 500;

    private JTextPane textArea;
    private JMenuItem select, copy, paste, cut;
    private JLabel infoLabel;

    public Main() {
        super("Text Editor");
        ConfigLoader config = new ConfigLoader("config.yml");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(Frame_WIDTH, Frame_HEIGHT);
        this.setResizable(true);
        this.setLocationRelativeTo(null);

        textArea = new JTextPane();
        this.add(new JScrollPane(textArea));

        textArea.setFont(new Font(config.fontFamily, Font.PLAIN, config.fontSize));
        textArea.setBackground(config.textBackground);
        textArea.setForeground(config.textForeground);
        textArea.setCaretColor(config.textForeground);

        JMenuBar MenuBar = new JMenuBar();
        this.setJMenuBar(MenuBar);

        MenuBar.setBackground(config.menuBackground);
        MenuBar.setOpaque(true);

        JMenu fileMenu = new JMenu("File");
        JMenu searchMenu = new JMenu("Search");
        JMenu viewMenu = new JMenu("View");
        JMenu editMenu = new JMenu("Edit");
        JMenu helpMenu = new JMenu("Help");

        fileMenu.setBackground(config.menuBackground);
        fileMenu.setOpaque(true);
        searchMenu.setBackground(config.menuBackground);
        searchMenu.setOpaque(true);
        viewMenu.setBackground(config.menuBackground);
        viewMenu.setOpaque(true);
        editMenu.setBackground(config.menuBackground);
        editMenu.setOpaque(true);
        helpMenu.setBackground(config.menuBackground);
        helpMenu.setOpaque(true);
        fileMenu.setIcon(scaledIcon("file1.png"));
        searchMenu.setIcon(scaledIcon("search1.png"));
        viewMenu.setIcon(scaledIcon("view1.png"));
        editMenu.setIcon(scaledIcon("edit1.png"));
        helpMenu.setIcon(scaledIcon("help1.png"));



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
        JMenuItem aboutMenu = new JMenuItem("About");
        aboutMenu.setBackground(config.menuBackground);
        aboutMenu.setOpaque(true);
        aboutMenu.setIcon(scaledIcon("about1.png"));
        JMenuItem guideItem = new JMenuItem("How to Use");



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
            fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files (*.txt, *.rtf)", "txt", "rtf"));

            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String fileName = file.getName().toLowerCase();

                try {
                    if (fileName.endsWith(".rtf")) {
                        javax.swing.text.rtf.RTFEditorKit rtfKit = new javax.swing.text.rtf.RTFEditorKit();
                        javax.swing.text.Document rtfDoc = rtfKit.createDefaultDocument();
                        try (FileInputStream fis = new FileInputStream(file)) {
                            rtfKit.read(fis, rtfDoc, 0);
                        }
                        textArea.setText(rtfDoc.getText(0, rtfDoc.getLength()));
                    } else {
                        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                            StringBuilder content = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                content.append(line).append("\n");
                            }
                            textArea.setText(content.toString());
                        }
                    }
                    applySyntaxHighlighting(textArea, profileForFile(file.getName()));
                }
                        catch(Exception ex){
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
        aboutMenu.addActionListener(e -> {
            ImageIcon rawIcon = new ImageIcon("about-icon.png");
            Image scaledImage = rawIcon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            ImageIcon aboutIcon = new ImageIcon(scaledImage);
            JOptionPane.showMessageDialog(this,
                    "Text Editor\n\n" +
                            "Developed by: \n" +
                            "Trisha Chand (ID: 25016426)\n" +
                            "Shuairan Bi (ID: 24021445)\n\n" +
                            "159.251 Software Design and Construction\n" +
                            "Massey University, 2026",
                    "About",
                    JOptionPane.PLAIN_MESSAGE,
                    aboutIcon);
        });

        guideItem.addActionListener(e -> {
            ImageIcon rawIcon = new ImageIcon("help-icon.png");
            Image scaledImage = rawIcon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            ImageIcon guideIcon = new ImageIcon(scaledImage);
            JOptionPane.showMessageDialog(this,
                    "Quick Guide\n\n" +
                            "File > New/Open/Save/Print/Export/Exit - manage your document\n" +
                            "Search > Find - search for a word in the text\n" +
                            "Edit > Select/Copy/Paste/Cut - standard text editing\n" +
                            "View > Date & Time - see the current date and time\n\n" +
                            "Happy editing!",
                    "Help",
                    JOptionPane.PLAIN_MESSAGE,
                    guideIcon);
        });

        exportItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files (*.pdf)", "pdf"));

            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".pdf")) {
                    file = new File(file.getAbsolutePath() + ".pdf");
                }

                try (PDDocument document = new PDDocument()) {
                    PDPage page = new PDPage();
                    document.addPage(page);

                    PDPageContentStream contentStream = new PDPageContentStream(document, page);
                    contentStream.beginText();
                    contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                    contentStream.newLineAtOffset(25, 750);

                    String[] lines = textArea.getText().split("\n");
                    for (String line : lines) {
                        contentStream.showText(line);
                        contentStream.newLineAtOffset(0, -15);
                    }

                    contentStream.endText();
                    contentStream.close();
                    document.save(file);

                    JOptionPane.showMessageDialog(this, "Exported to PDF successfully.",
                            "Export PDF", JOptionPane.INFORMATION_MESSAGE);


                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error exporting PDF: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        printItem.addActionListener(e -> {
            try {
                java.awt.print.PrinterJob job = java.awt.print.PrinterJob.getPrinterJob();
                job.setPrintable(new java.awt.print.Printable() {
                    @Override
                    public int print(java.awt.Graphics graphics, java.awt.print.PageFormat pageFormat, int pageIndex) throws java.awt.print.PrinterException {
                        if (pageIndex > 0) {
                            return java.awt.print.Printable.NO_SUCH_PAGE;
                        }
                        java.awt.Graphics2D g2d = (java.awt.Graphics2D) graphics;
                        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                        textArea.printAll(g2d);
                        return java.awt.print.Printable.PAGE_EXISTS;
                    }
                });

                boolean doPrint = job.printDialog();
                if (doPrint) {
                    job.print();
                }
            } catch (java.awt.print.PrinterException ex) {
                JOptionPane.showMessageDialog(this, "Error printing: " + ex.getMessage(),
                        "Print Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(printItem);
        fileMenu.add(exportItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        searchMenu.add(find);

        editMenu.add(select);
        editMenu.add(copy);
        editMenu.add(paste);
        editMenu.add(cut);

        helpMenu.add(guideItem);


        MenuBar.add(fileMenu);
        MenuBar.add(viewMenu);
        MenuBar.add(helpMenu);
        MenuBar.add(searchMenu);
        MenuBar.add(editMenu);
        MenuBar.add(aboutMenu);


        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedTime = time.format(formatter);

        infoLabel = new JLabel("Time & Date");
        viewMenu.add(infoLabel);
        infoLabel.setText("Date & Time: " + formattedTime);


        ImageIcon searchIcon = new ImageIcon("search.png");
        find.setIcon(searchIcon);
        find.addActionListener(e -> searchText());

        this.setVisible(true);
    }

    private ImageIcon scaledIcon(String filename) {
        ImageIcon rawIcon = new ImageIcon(filename);
        Image scaled = rawIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
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

    private static final Set<String> JAVA_KEYWORDS = Set.of(
            "abstract","assert","boolean","break","byte","case","catch","char","class","const",
            "continue","default","do","double","else","enum","extends","final","finally","float",
            "for","goto","if","implements","import","instanceof","int","interface","long","native",
            "new","package","private","protected","public","return","short","static","strictfp",
            "super","switch","synchronized","this","throw","throws","transient","try","void",
            "volatile","while","true","false","null"
    );

    private static final Set<String> CPP_KEYWORDS = Set.of(
            "alignas","alignof","and","asm","auto","bool","break","case","catch","char","class",
            "const","constexpr","continue","default","delete","do","double","else","enum","explicit",
            "export","extern","false","float","for","friend","goto","if","inline","int","long",
            "mutable","namespace","new","noexcept","nullptr","operator","private","protected","public",
            "return","short","signed","sizeof","static","struct","switch","template","this","throw",
            "true","try","typedef","typename","union","unsigned","using","virtual","void","volatile","while"
    );

    private static final Set<String> PYTHON_KEYWORDS = Set.of(
            "False","None","True","and","as","assert","async","await","break","class","continue",
            "def","del","elif","else","except","finally","for","from","global","if","import","in",
            "is","lambda","nonlocal","not","or","pass","raise","return","try","while","with","yield","self"
    );

    private static final Set<String> JS_KEYWORDS = Set.of(
            "break","case","catch","class","const","continue","debugger","default","delete","do",
            "else","export","extends","finally","for","function","if","import","in","instanceof",
            "let","new","of","return","static","super","switch","this","throw","try","typeof","var",
            "void","while","with","yield","async","await","true","false","null","undefined"
    );

    private static final Pattern WORD_PATTERN = Pattern.compile("\\b[a-zA-Z_][a-zA-Z0-9_]*\\b");
    private static final Pattern STRING_PATTERN = Pattern.compile("\"([^\"\\\\]|\\\\.)*\"|'([^'\\\\]|\\\\.)*'");
    private static final Pattern SLASH_COMMENT = Pattern.compile("//[^\\n]*");
    private static final Pattern HASH_COMMENT = Pattern.compile("#[^\\n]*");

    private record LanguageProfile(Set<String> keywords, Pattern commentPattern) {}

    static LanguageProfile profileForFile(String filename) {
        String lower = filename.toLowerCase();
        if (lower.endsWith(".java")) return new LanguageProfile(JAVA_KEYWORDS, SLASH_COMMENT);
        if (lower.endsWith(".js"))   return new LanguageProfile(JS_KEYWORDS, SLASH_COMMENT);
        if (lower.endsWith(".py"))  return new LanguageProfile(PYTHON_KEYWORDS, HASH_COMMENT);
        if (lower.endsWith(".cpp") || lower.endsWith(".h") || lower.endsWith(".hpp"))
            return new LanguageProfile(CPP_KEYWORDS, SLASH_COMMENT);
        return null; // unrecognized extension — leave file uncolored
    }

    static void applySyntaxHighlighting(JTextPane pane, LanguageProfile profile) {
        StyledDocument doc = pane.getStyledDocument();

        SimpleAttributeSet defaultAttrs = new SimpleAttributeSet();
        StyleConstants.setForeground(defaultAttrs, Color.BLACK);
        doc.setCharacterAttributes(0, doc.getLength(), defaultAttrs, true);

        if (profile == null) return; // unsupported file type — leave plain black text

        String text;
        try {
            text = doc.getText(0, doc.getLength());
        } catch (BadLocationException ex) {
            ex.printStackTrace();
            return;
        }

        SimpleAttributeSet keywordStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(keywordStyle, new Color(239, 106, 34));
        StyleConstants.setBold(keywordStyle, true);

        SimpleAttributeSet stringStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(stringStyle, new Color(34, 228, 34));

        SimpleAttributeSet commentStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(commentStyle, new Color(128, 128, 128));
        StyleConstants.setItalic(commentStyle, true);

        Matcher wordMatcher = WORD_PATTERN.matcher(text);
        while (wordMatcher.find()) {
            if (profile.keywords().contains(wordMatcher.group())) {
                doc.setCharacterAttributes(wordMatcher.start(), wordMatcher.end() - wordMatcher.start(), keywordStyle, true);
            }
        }
        Matcher stringMatcher = STRING_PATTERN.matcher(text);
        while (stringMatcher.find()) {
            doc.setCharacterAttributes(stringMatcher.start(), stringMatcher.end() - stringMatcher.start(), stringStyle, true);
        }
        Matcher commentMatcher = profile.commentPattern().matcher(text);
        while (commentMatcher.find()) {
            doc.setCharacterAttributes(commentMatcher.start(), commentMatcher.end() - commentMatcher.start(), commentStyle, true);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new StartScreen());
    }}