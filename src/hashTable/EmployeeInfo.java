package hashTable;
/***************

NAME :									          Yijie Wang
STUDENT NUMBER:							            718084

ICS4UO-A, November 4, 2016

THIS FILE IS PART OF THE PROGRAM: the hash table assignment

*****************/

//public class EmployeeInfo
public class EmployeeInfo {
    // Attributes
    private int empNumber;
    private String firstName;
    private String lastName;
    private int sex;
    private int location;
    protected double deductionRate;
    
    private final String[] sexValue = {"Male","Female","Other"};
    private final String[] locationValue = {"Mississauga","Ottawa","Chicago"};
    
    // Constructor to assign default dummy values to attributes.
    public EmployeeInfo () {
		empNumber = 777777;
		firstName = "Bugs";
		lastName = "Bunny";
    }
    
    // Constructor with additional features like sex, location and deductionRate
    public EmployeeInfo(int empNo, String fName, String lName, int sex, int loc, double dRate){
		empNumber = empNo;
		firstName = fName;
		lastName = lName;
		this.sex = sex;
		location = loc;
		deductionRate = dRate;
    }

    // Constructor to assign passed values to attributes for the HashTable
    public EmployeeInfo (int empNo, String fName, String lName) {
		empNumber = empNo;
		firstName = fName;
		lastName = lName;
    }

    // Getter method for employeeNumber attribute.
    public int getEmployeeNumber() {
		return (empNumber);
    }

    // Setter method for employeeNumber attributes.
    public void setEmployeeNumber (int empNo) {
    	if (empNo < 0) {
    		return;
    	} else {
    		empNumber = empNo;
    	}
    }
    
    public String getGender(){
    	return sexValue[sex];
    }
    
    public String getLocation(){
    	return locationValue[location];
    }
    
    // Method for calculating annual income, it returns 0 in this superclass
    public double calcAnnualIncome(){
    	return 0;
    }
    
    // Display the name of the employee
    public String toString(){
    	return firstName + " " + lastName;
    }
}
