package data;

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

	public double calcAnnualIncome() {
		return 0D;
	}

	public void setDeductionsRate(double newDeductionsRate) {
		deductionsRate = newDeductionsRate;
	}

	public double getDeductionsRate() {
		return deductionsRate;
	}

	public void setEmployeeNumber(int newEmployeeNumber) {
		empNumber = newEmployeeNumber;
	}

	public int getEmployeeNumber() {
		return empNumber;
	}

	public void setFirstName(String newName) {
		firstName = newName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setLastName(String newName) {
		lastName = newName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setGender(Gender newGender) {
		sex = newGender;
	}

	public Gender getGender() {
		return sex;
	}

	public void setLocation(Location newLoc) {
		workLocation = newLoc;
	}

	public Location getLocation() {
		return workLocation;
	}
}
