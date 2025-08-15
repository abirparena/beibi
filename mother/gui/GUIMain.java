package mother.gui;

import mother.model.*;
import mother.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUIMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainMenuWindow().setVisible(true);
        });
    }
}

class MainMenuWindow extends JFrame {
    public MainMenuWindow() {
        setTitle("Crime Reporting System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title Label
        JLabel titleLabel = new JLabel("Main Menu", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel);

        // Admin Button
        JButton adminButton = new JButton("Admin Access");
        adminButton.addActionListener(e -> {
            new GUIAdminLoginWindow().setVisible(true);
            dispose();
        });

        // File Case Button
        JButton fileCaseButton = new JButton("File New Case");
        fileCaseButton.addActionListener(e -> {
            new GUIUserLoginWindow().setVisible(true);
            dispose();
        });

        // Crime Rate Button
        JButton crimeRateButton = new JButton("View Crime Rates");
        crimeRateButton.addActionListener(e -> {
            new GUICrimeRateWindow().setVisible(true);
            dispose();
        });

        panel.add(adminButton);
        panel.add(fileCaseButton);
        panel.add(crimeRateButton);

        add(panel);
    }
}