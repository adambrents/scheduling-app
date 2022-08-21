package model;

import javafx.collections.ObservableList;

public class Report {

    private String reportName;
    private int reportId;

    /**
     * @param reportName
     * @param reportId
     */
    public Report(String reportName, int reportId) {
        this.reportName = reportName;
        this.reportId = reportId;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }
}
