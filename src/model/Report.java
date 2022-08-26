package model;

import javafx.collections.ObservableList;

public class Report {

    private String reportName;
    private int reportId;

    /**
     * constructor for Report
     * @param reportName
     * @param reportId
     */
    public Report(String reportName, int reportId) {
        this.reportName = reportName;
        this.reportId = reportId;
    }

    /**
     * getter/setter for Report
     * @return
     */
    public String getReportName() {
        return reportName;
    }

    /**
     * getter/setter for Report
     * @param reportName
     */
    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    /**
     * getter/setter for Report
     * @return
     */
    public int getReportId() {
        return reportId;
    }

    /**
     * getter/setter for Report
     * @param reportId
     */
    public void setReportId(int reportId) {
        this.reportId = reportId;
    }
}
