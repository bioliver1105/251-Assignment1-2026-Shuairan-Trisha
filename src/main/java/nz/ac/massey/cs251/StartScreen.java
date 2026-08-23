package nz.ac.massey.cs251;

import javax.swing.*;
import java.awt.*;

// Displays a start screen with a Start button that opens the main editor window
public class StartScreen extends JFrame {

    public StartScreen() {
        super("Text Editor");
        setSize(800,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JLabel background = new JLabel(new ImageIcon("start4.png"));
        background.setLayout(null);

        JButton startButton = new JButton("Start");
        startButton.setBounds(350,340,100,40);
        startButton.setBackground(new Color(70, 217, 192));
        startButton.setForeground(new Color(20,30,50));
        startButton.setFocusPainted(false);
        startButton.addActionListener(e -> {
            this.dispose();
            SwingUtilities.invokeLater(Main::new);
        });

        background.add(startButton);
        this.add(background);
        this.setVisible(true);
    }
}