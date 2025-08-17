package mother.model;

import java.time.LocalDateTime;
import java.io.Serializable;

/
public class Case implements Serializable {
    private String caseId;
    private String reporterName;
    private String reporterNid;
    private String victimName;
    private String victimAge;
    private String contactinfo;
    private String platform;
    private String incidentDetails;
    private String evidence;
    private String status; // Pending, Investigating, Resolved
    private LocalDateTime submissionTime;

    public Case(String reporterName, String reporterNid, String victimName,
                String victimAge, String contactinfo, String platform, String incidentDetails,
                String evidence) {
        this.caseId = "CASE-" + System.currentTimeMillis();
        this.reporterName = reporterName;
        this.reporterNid = reporterNid;
        this.victimName = victimName;
        this.victimAge = victimAge;
        this.contactinfo = contactinfo;
        this.platform = platform;
        this.incidentDetails = incidentDetails;
        this.evidence = evidence;
        this.status = "Pending";
        this.submissionTime = LocalDateTime.now();
    }

    // Getter methods (Encapsulation)
    public String getCaseId()
    { return caseId; }
    public String getReporterName()
    { return reporterName; }
    public String getReporterNid()
    { return reporterNid; }
    public String getVictimName()
    { return victimName; }
    public String getVictimAge()
    { return victimAge; }
    public String getContactinfo()
    {return contactinfo;}
    public String getPlatform()
    { return platform; }
    public String getStatus()
    { return status; }
    public LocalDateTime getSubmissionTime()
    { return submissionTime; }
    public String getEvidence()
    {return evidence;}
    public String getIncidentDetails()
    {return incidentDetails;}

    // Setter for status
    public void setStatus(String status) { this.status = status; }
}