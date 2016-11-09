package data.employee;

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

	public double getYearlySalary() {
		return yearlySalary;
	}
}
