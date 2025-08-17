package mother.model;

import java.io.Serializable;


public class CrimeZone implements Serializable {
    private String zoneName;
    private int crimeCount;

    public CrimeZone(String zoneName, int crimeCount) {
        this.zoneName = zoneName;
        this.crimeCount = crimeCount;
    }

    // Getters
    public String getZoneName(){
        return zoneName; }
    public int getCrimeCount(){
        return crimeCount; }

    // Setters
    public void setCrimeCount(int crimeCount) {
        this.crimeCount = crimeCount;
    }
}