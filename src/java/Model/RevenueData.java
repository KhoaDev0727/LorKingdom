package Model;

/**
 *
 * @author Truong Van Khang - CE181852
 */
public class RevenueData {

    private String MonthYear;
    private double Revenue;

    public RevenueData(String MonthYear, double Revenue) {
        this.MonthYear = MonthYear;
        this.Revenue = Revenue;
    }

    public RevenueData() {
    }

    public String getMonthYear() {
        return MonthYear;
    }

    public void setMonthYear(String MonthYear) {
        this.MonthYear = MonthYear;
    }

    public double getRevenue() {
        return Revenue;
    }

    public void setRevenue(double Revenue) {
        this.Revenue = Revenue;
    }
}
