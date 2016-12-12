package data;

/**
 *	A subclass of EmployeeInfo for full time employees
 */
public class FullTimeEmployee extends EmployeeInfo {

	private double yearlySalary;

	public FullTimeEmployee(int num, String firstName, String lastName, Gender gender, Location workLocation, double deductionsRate, double yearlySalary) {
		super(num, firstName, lastName, gender, workLocation, deductionsRate);

		this.yearlySalary = yearlySalary;
	}

	@Override
	public double calcAnnualIncome() {
		return yearlySalary * (1D - deductionsRate);
	}

	//Getter and setter for yearly salary
	public void setYearlySalary(double newSalary) {
		yearlySalary = newSalary;
	}

	public double getYearlySalary() {
		return yearlySalary;
	}
}
