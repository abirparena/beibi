package mother.gui;

import mother.model.*;
import mother.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

class GUIFileCaseWindow extends JFrame {
    // Declare all UI components as fields
    private JTextField victimNameField;
    private JTextField victimAgeField;
    private JComboBox<String> platformComboBox;
    private JTextArea incidentDetailsArea;
    private JTextArea evidenceArea;

    public GUIFileCaseWindow(String username, String nid) {
        setTitle("File New Case");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Initialize all components
        victimNameField = new JTextField(20);
        victimAgeField = new JTextField(5);

        String[] platforms = {"Facebook", "Twitter", "Instagram", "WhatsApp", "Other"};
        platformComboBox = new JComboBox<>(platforms);

        incidentDetailsArea = new JTextArea(8, 40);
        incidentDetailsArea.setLineWrap(true);
        incidentDetailsArea.setWrapStyleWord(true);

        evidenceArea = new JTextArea(8, 40);
        evidenceArea.setLineWrap(true);
        evidenceArea.setWrapStyleWord(true);

        // Form layout
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        // Victim info
        JPanel victimPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        victimPanel.add(new JLabel("Victim Name:"));
        victimPanel.add(victimNameField);
        victimPanel.add(new JLabel("Age:"));
        victimPanel.add(victimAgeField);

        // Platform selection
        JPanel platformPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        platformPanel.add(new JLabel("Platform:"));
        platformPanel.add(platformComboBox);

        // Incident details
        JPanel incidentPanel = new JPanel(new BorderLayout());
        incidentPanel.add(new JLabel("Incident Details:"), BorderLayout.NORTH);
        incidentPanel.add(new JScrollPane(incidentDetailsArea), BorderLayout.CENTER);

        // Evidence
        JPanel evidencePanel = new JPanel(new BorderLayout());
        evidencePanel.add(new JLabel("Evidence:"), BorderLayout.NORTH);
        evidencePanel.add(new JScrollPane(evidenceArea), BorderLayout.CENTER);

        // Submit button
        JButton submitButton = new JButton("Submit Case");
        submitButton.addActionListener(e -> submitCase(username, nid));

        // Add all components to main panel
        formPanel.add(victimPanel);
        formPanel.add(platformPanel);
        formPanel.add(incidentPanel);
        formPanel.add(evidencePanel);

        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(submitButton, BorderLayout.SOUTH);

        add(panel);  // Now 'panel' is actually used
    }

    private void submitCase(String username, String nid) {
        try {
            // Validate inputs
            if (victimNameField.getText().trim().isEmpty() ||
                    victimAgeField.getText().trim().isEmpty() ||
                    incidentDetailsArea.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Please fill all required fields!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Reuse existing Case class
            Case newCase = new Case(
                    username,
                    nid,
                    victimNameField.getText().trim(),
                    victimAgeField.getText().trim(),
                    (String) platformComboBox.getSelectedItem(),
                    incidentDetailsArea.getText().trim(),
                    evidenceArea.getText().trim()
            );

            // Reuse FileHandler from original code
            ArrayList<Case> cases = FileHandler.loadCases();
            cases.add(newCase);
            FileHandler.saveCases(cases);

            JOptionPane.showMessageDialog(this,
                    "Case submitted successfully!\nCase ID: " + newCase.getCaseId(),
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            dispose();
            new GUIUserMenu(username, nid).setVisible(true);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error saving case: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}