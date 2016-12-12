package data;

public class PartTimeEmployee extends EmployeeInfo {

	private double hourlyWage;
	private double hoursPerWeek;
	private double weeksPerYear;

	public PartTimeEmployee(int num, String firstName, String lastName, Gender gender, Location workLocation, double deductionsRate, double hourlyWage, double hoursPerWeek, double weeksPerYear) {
		super(num, firstName, lastName, gender, workLocation, deductionsRate);

		this.hourlyWage = hourlyWage;
		this.hoursPerWeek = hoursPerWeek;
		this.weeksPerYear = weeksPerYear;
	}

	@Override
	public double calcAnnualIncome() {
		return hourlyWage * hoursPerWeek * weeksPerYear * (1D - deductionsRate);
	}

	public void setHourlyWage(double newHourlyWage) {
		hourlyWage = newHourlyWage;
	}

	public double getHourlyWage() {
		return hourlyWage;
	}

	public void setHoursPerWeek(double newHoursPerWeek) {
		hoursPerWeek = newHoursPerWeek;
	}

	public double getHoursPerWeek() {
		return hoursPerWeek;
	}

	public void setWeeksPerYear(double newWeeksPerYear) {
		weeksPerYear = newWeeksPerYear;
	}

	public double getWeeksPerYear() {
		return weeksPerYear;
	}
}
