package mother.GUI;

import mother.exceptions.InvalidNIDException;
import mother.model.*;
import mother.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

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

class GUIAdminLoginWindow extends JFrame {
    public GUIAdminLoginWindow() {
        setTitle("Admin Login");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel("Username:"));
        JTextField usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        JPasswordField passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            if (usernameField.getText().equals("admin") &&
                    new String(passwordField.getPassword()).equals("admin123")) {
                new GUIAdminPanel().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials");
            }
        });

        panel.add(new JLabel());
        panel.add(loginButton);
        add(panel);
    }
}

class GUIUserLoginWindow extends JFrame {
    public GUIUserLoginWindow() {
        setTitle("User Login");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel("Username:"));
        JTextField usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("NID (10 digits):"));
        JTextField nidField = new JTextField();
        panel.add(nidField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            try {
                if (nidField.getText().length() != 10 || !nidField.getText().matches("\\d+")) {
                    throw new InvalidNIDException("NID must be 10 digits");
                }
                new GUIUserMenu(usernameField.getText(), nidField.getText()).setVisible(true);
                dispose();
            } catch (InvalidNIDException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        panel.add(new JLabel());
        panel.add(loginButton);
        add(panel);
    }
}

class GUIAdminPanel extends JFrame {
    private JTable casesTable;
    private JComboBox<String> statusComboBox;
    private JButton refreshButton;
    private JButton updateButton;
    private JButton logoutButton;
    private JButton statsButton;

    public GUIAdminPanel() {
        setTitle("Admin Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Title label
        JLabel titleLabel = new JLabel("Case Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Table to display cases
        casesTable = new JTable();
        refreshCaseTable();

        JScrollPane scrollPane = new JScrollPane(casesTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Control panel for actions
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // Status update components
        JPanel statusPanel = new JPanel();
        statusPanel.add(new JLabel("Update Status:"));
        statusComboBox = new JComboBox<>(new String[]{"Pending", "Investigating", "Resolved"});
        statusPanel.add(statusComboBox);

        updateButton = new JButton("Update Selected");
        updateButton.addActionListener(this::updateCaseStatus);

        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshCaseTable());

        statsButton = new JButton("View Statistics");
        statsButton.addActionListener(this::showStatistics);

        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(this::logoutAdmin);

        controlPanel.add(statusPanel);
        controlPanel.add(updateButton);
        controlPanel.add(refreshButton);
        controlPanel.add(statsButton);
        controlPanel.add(logoutButton);

        mainPanel.add(controlPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void updateCaseStatus(ActionEvent e) {
        int selectedRow = casesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a case to update.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String caseId = casesTable.getValueAt(selectedRow, 0).toString();
        String newStatus = (String) statusComboBox.getSelectedItem();

        try {
            ArrayList<Case> cases = FileHandler.loadCases();
            for (Case c : cases) {
                if (c.getCaseId().equals(caseId)) {
                    c.setStatus(newStatus);
                    break;
                }
            }
            FileHandler.saveCases(cases);
            JOptionPane.showMessageDialog(this, "Case status updated successfully!");
            refreshCaseTable();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error updating case status: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshCaseTable() {
        ArrayList<Case> cases = FileHandler.loadCases();
        String[] columnNames = {"Case ID", "Reporter NID", "Victim", "Platform", "Status"};
        Object[][] data = new Object[cases.size()][5];
        for (int i = 0; i < cases.size(); i++) {
            Case c = cases.get(i);
            data[i][0] = c.getCaseId();
            data[i][1] = c.getReporterNid();
            data[i][2] = c.getVictimName();
            data[i][3] = c.getPlatform();
            data[i][4] = c.getStatus();
        }
        casesTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }

    private void showStatistics(ActionEvent e) {
        try {
            ArrayList<Case> cases = FileHandler.loadCases();
            long pending = cases.stream().filter(c -> c.getStatus().equals("Pending")).count();
            long investigating = cases.stream().filter(c -> c.getStatus().equals("Investigating")).count();
            long resolved = cases.stream().filter(c -> c.getStatus().equals("Resolved")).count();

            JDialog statsDialog = new JDialog(this, "Case Statistics", true);
            statsDialog.setSize(400, 300);
            statsDialog.setLayout(new BorderLayout());

            JPanel statsPanel = new JPanel(new GridLayout(4, 1));
            statsPanel.add(new JLabel("Total Cases: " + cases.size(), JLabel.CENTER));
            statsPanel.add(new JLabel("Pending: " + pending, JLabel.CENTER));
            statsPanel.add(new JLabel("Investigating: " + investigating, JLabel.CENTER));
            statsPanel.add(new JLabel("Resolved: " + resolved, JLabel.CENTER));

            statsDialog.add(statsPanel, BorderLayout.CENTER);

            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(ev -> statsDialog.dispose());
            statsDialog.add(closeButton, BorderLayout.SOUTH);

            statsDialog.setLocationRelativeTo(this);
            statsDialog.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading statistics: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logoutAdmin(ActionEvent e) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?", "Confirm Logout",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new GUILoginWindow().setVisible(true);
        }
    }
}

class GUICrimeRateWindow extends JFrame {
    public GUICrimeRateWindow() {
        setTitle("Crime Rates");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        ArrayList<CrimeZone> crimeData = FileHandler.loadCrimeData();
        DefaultListModel<String> listModel = new DefaultListModel<>();

        for (CrimeZone zone : crimeData) {
            listModel.addElement(zone.toString());
        }

        JList<String> crimeList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(crimeList);
        add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        add(closeButton, BorderLayout.SOUTH);
    }
}

class GUILoginWindow extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public GUILoginWindow() {
        setTitle("Crime Reporting System - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.equals("admin") && password.equals("admin123")) {
                new GUIAdminPanel().setVisible(true);
                dispose();
            } else {
                try {
                    // Reuse existing validation from console version
                    if (username.isEmpty() || password.isEmpty()) {
                        throw new InvalidNIDException("Fields cannot be empty");
                    }
                    new GUIUserMenu(username, password).setVisible(true);
                    dispose();
                } catch (InvalidNIDException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(new JLabel());
        panel.add(loginButton);
        add(panel);
    }
}

class GUIUserMenu extends JFrame {
    public GUIUserMenu(String username, String nid) {
        setTitle("User Dashboard - " + username);
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Reuse existing FileHandler to load cases
        ArrayList<Case> cases = FileHandler.loadCases();
        DefaultListModel<String> caseListModel = new DefaultListModel<>();

        for (Case c : cases) {
            if (c.getReporterNid().equals(nid)) {
                caseListModel.addElement(c.getCaseId() + " - " + c.getStatus());
            }
        }

        JList<String> caseList = new JList<>(caseListModel);
        panel.add(new JScrollPane(caseList), BorderLayout.CENTER);

        JButton newCaseButton = new JButton("File New Case");
        newCaseButton.addActionListener(e -> {
            new GUIFileCaseWindow(username, nid).setVisible(true);
            dispose();
        });

        panel.add(newCaseButton, BorderLayout.SOUTH);

        add(panel);
    }
}

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

        // Input fields panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Victim's Name:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(victimNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Victim's Age:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(victimAgeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Platform:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(platformComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Incident Details:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        inputPanel.add(new JScrollPane(incidentDetailsArea), gbc);
        gbc.weightx = 0.0;

        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(new JLabel("Evidence (optional):"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        inputPanel.add(new JScrollPane(evidenceArea), gbc);
        gbc.weightx = 0.0;

        panel.add(inputPanel, BorderLayout.CENTER);

        JButton submitButton = new JButton("Submit Case");
        submitButton.addActionListener(e -> submitCase(username, nid));

        panel.add(submitButton, BorderLayout.SOUTH);

        add(panel);
    }

    private void submitCase(String username, String nid) {
        try {
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
