package data;

/**
 *	Stores the information of an employee
 */
public class EmployeeInfo {
	
	//Private variables to store the employee information
	private int empNumber;
	private String firstName;
	private String lastName;
	private Gender sex;
	private Location workLocation;
	protected double deductionsRate;

	/**
	 * Creates an EmployeeInfo object, called by the subclass FullTimeEmployee and PartTimeEmployee
	 * @param num				The employee number of the employee, must be different from all other existing employees
	 * @param firstName			The first name of the employee
	 * @param lastName			The last name of the employee
	 * @param gender			The gender of the employee, must be a value from the Gender enumeration
	 * @param workLocation		The work place of the employee, must be a value from the wokrLocation enumeration
	 * @param deductionsRate	The deductions rate expressed as a decimal, must be between 0 and 1
	 */
	public EmployeeInfo(int num, String firstName, String lastName, Gender gender, Location workLocation, double deductionsRate) {
		empNumber = num;
		this.firstName = firstName;
		this.lastName = lastName;
		sex = gender;
		this.workLocation = workLocation;
		this.deductionsRate = deductionsRate;
	}

	/**
	 * Calculate the annual income of the employee
	 * @return	The annual income of the employee
	 */
	public double calcAnnualIncome() {
		//The parent class returns 0, the subclasses have their own calculation methods
		return 0D;
	}

	/*
	 * Getter and setter methods for the private variables
	 */
	
	/**
	 * @param newDeductionsRate must be between 0 and 1
	 */
	public void setDeductionsRate(double newDeductionsRate) {
		deductionsRate = newDeductionsRate;
	}

	public double getDeductionsRate() {
		return deductionsRate;
	}

	/**
	 * @param newEmployeeNumber must be different from all other existing employee numbers
	 */
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
