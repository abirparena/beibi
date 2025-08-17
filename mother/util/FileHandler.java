package mother.util;

import mother.model.CrimeZone;
import mother.model.Case;
import java.io.*;
import java.util.ArrayList;

/**
 * Handles file operations (Binary I/O)
 * Demonstrates METHOD OVERLOADING
 */
public class FileHandler {
    private static final String CASES_FILE = "cases.dat";
    private static final String CRIME_DATA_FILE = "crime_data.dat";

    // Method Overloading (saveData with different parameters)
    public static void saveData(Object data) throws IOException {
        saveData(data, CASES_FILE);
    }

    public static void saveData(Object data, String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filename))) {
            oos.writeObject(data);
        }
    }

    public static Object loadData(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filename))) {
            return ois.readObject();
        }
    }

    public static ArrayList<Case> loadCases() {
        try {
            Object data = loadData(CASES_FILE);
            if (data instanceof ArrayList) {
                return (ArrayList<Case>) data;
            }
        } catch (Exception e) {
            // Return empty list if file doesn't exist
        }
        return new ArrayList<>();
    }

    public static void saveCases(ArrayList<Case> cases) throws IOException {
        saveData(cases, CASES_FILE);
    }

    public static ArrayList<CrimeZone> loadCrimeData() {
        try {
            Object data = loadData(CRIME_DATA_FILE);
            if (data instanceof ArrayList) {
                return (ArrayList<CrimeZone>) data;
            }
        } catch (Exception e) {
            // Initialize default data
            ArrayList<CrimeZone> defaultZones = new ArrayList<>();
            defaultZones.add(new CrimeZone("Mohammadpur", 75));
            defaultZones.add(new CrimeZone("Gulshan", 27));
            defaultZones.add(new CrimeZone("Dhanmondi", 34));
            defaultZones.add(new CrimeZone("Shonir Akhra", 67));
            return defaultZones;
        }
        return new ArrayList<>();
    }

    public static void saveCrimeData(ArrayList<CrimeZone> zones) throws IOException {
        saveData(zones, CRIME_DATA_FILE);
    }

}