package mother.gui;

import mother.model.CrimeZone;
import mother.util.FileHandler;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class GUICrimeRateWindow extends JFrame {
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