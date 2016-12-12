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
	
	/**
	 * Creates a part time employee
	 * @param num				The employee number of the employee, must be different from all other existing employees
	 * @param firstName			The first name of the employee
	 * @param lastName			The last name of the employee
	 * @param gender			The gender of the employee, must be a value from the Gender enumeration
	 * @param workLocation		The work place of the employee, must be a value from the wokrLocation enumeration
	 * @param deductionsRate	The deductions rate expressed as a decimal, must be between 0 and 1
	 * @param hourlyWage		The wage per hour, must be positive
	 * @param hoursPerWeek		The hours worked per week, must be between 0 and MAX_HOUR_PER_WEEK
	 * @param weeksPerYear		The weeks worked per year, must be between 0 and MAX_WEEKS_PER_YEAR
	 */
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
