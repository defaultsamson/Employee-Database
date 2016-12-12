package data;

/**
 *	A subclass of EmployeeInfo for full time employees
 */
public class FullTimeEmployee extends EmployeeInfo {

	//The employee's annual salary before deduction
	private double yearlySalary;

	/**
	 * Creates a full time employee
	 * @param num				The employee number of the employee, must be different from all other existing employees
	 * @param firstName			The first name of the employee
	 * @param lastName			The last name of the employee
	 * @param gender			The gender of the employee, must be a value from the Gender enumeration
	 * @param workLocation		The work place of the employee, must be a value from the wokrLocation enumeration
	 * @param deductionsRate	The deductions rate expressed as a decimal, must be between 0 and 1
	 * @param yearlySalary		The annual salary of the employee before deduction
	 */
	public FullTimeEmployee(int num, String firstName, String lastName, Gender gender, Location workLocation, double deductionsRate, double yearlySalary) {
		super(num, firstName, lastName, gender, workLocation, deductionsRate);

		this.yearlySalary = yearlySalary;
	}

	@Override
	public double calcAnnualIncome() {
		return yearlySalary * (1D - deductionsRate);
	}

	//Getter and setter for yearly salary
	/**
	 * @param newSalary	the annual salary before deduction
	 */
	public void setYearlySalary(double newSalary) {
		yearlySalary = newSalary;
	}

	/**
	 * @return	The annual salary after deduction
	 */
	public double getYearlySalary() {
		return yearlySalary;
	}
}
