package view;

public class ScreenHelper {

    //TODO make all calls to screens use variables
    public String addApptScrn = "/view/AddAppointmentsScreen.fxml";
    public String addCustScrn = "/view/AddCustomerScreen.fxml";
    public String allApptScrn = "/view/AllAppointmentsScreen.fxml";
    public static String loginScreen = "/view/LoginScreen.fxml";
    public String mainScreen = "/view/MainScreen.fxml";
    public String modApptScrn = "/view/ModifyAppointmentsScreen.fxml";
    public String modCustScrn = "/view/ModifyCustomersScreen.fxml";
    public String reportsScrn = "/view/ReportsScreen.fxml";

    public String getAddApptScrn() {
        return addApptScrn;
    }

    public String getAddCustScrn() {
        return addCustScrn;
    }

    public String getAllApptScrn() {
        return allApptScrn;
    }

    public static String getLoginScreen() {
        return loginScreen;
    }

    public String getMainScreen() {
        return mainScreen;
    }

    public String getModApptScrn() {
        return modApptScrn;
    }

    public String getModCustScrn() {
        return modCustScrn;
    }

    public String getReportsScrn() {
        return reportsScrn;
    }
}
