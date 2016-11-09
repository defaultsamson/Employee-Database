package data.employee;

public class EmployeeInfo {
	private int empNumber;
	private String firstName;
	private String lastName;
	private Gender sex;
	private Location workLocation;
	protected double deductionsRate;

	public EmployeeInfo(int num, String firstName, String lastName, Gender gender, Location workLocation, double deductionsRate) {
		empNumber = num;
		this.firstName = firstName;
		this.lastName = lastName;
		sex = gender;
		this.workLocation = workLocation;
		this.deductionsRate = deductionsRate;
	}

	public Location getLocation() {
		return workLocation;
	}

	public double getDeductionsRate() {
		return deductionsRate;
	}

	public Gender getGender() {
		return sex;
	}

	public String getLastName() {
		return lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public int getEmployeeNumber() {
		return empNumber;
	}

	public double calcAnnualIncome() {
		return 0D;
	}
}
