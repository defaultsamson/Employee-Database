package data;

/**
 *	A subclass of EmployeeInfo for part time employees
 */
public class PartTimeEmployee extends EmployeeInfo {

	public static final int MAX_HOURS_PER_WEEK = 168;
	public static final int MAX_WEEKS_PER_YEAR = 365 / 7;
	
	private double hourlyWage;
	private double hoursPerWeek;
	private double weeksPerYear;
	
	//Constructor
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

	//Setter and getter for hourlyWage
	/**
	 * @param newHourlyWage	must be greater or equal to 0
	 */
	public void setHourlyWage(double newHourlyWage) {
		hourlyWage = newHourlyWage;
	}

	public double getHourlyWage() {
		return hourlyWage;
	}

	//Setter and getter for hoursPerWeek
	/**
	 * @param newHoursPerWeek	must be greater or equal to 0 and less than MAX_HOUR_PER_WEEK
	 */
	public void setHoursPerWeek(double newHoursPerWeek) {
		hoursPerWeek = newHoursPerWeek;
	}

	/**
	 * @return	The number of hours per week worked by the employee, 0 means the employee is not currently working
	 */
	public double getHoursPerWeek() {
		return hoursPerWeek;
	}

	/**
	 * @param newWeeksPerYear	must be greater or equal to 0 and less than MAX_WEEKS_PER_YEAR
	 */
	public void setWeeksPerYear(double newWeeksPerYear) {
		weeksPerYear = newWeeksPerYear;
	}

	/**
	 * @return	The number of weeks worked per year, 0 means the employee is not currently working
	 */
	public double getWeeksPerYear() {
		return weeksPerYear;
	}
}
