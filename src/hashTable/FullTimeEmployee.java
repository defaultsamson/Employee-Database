package hashTable;

public class FullTimeEmployee extends EmployeeInfo{
	
	private double yearlySalary;
	
	public FullTimeEmployee(int empNo, String fName, String lName, int sex, int loc, double dRate, double salary){
		super(empNo,fName,lName,sex,loc,dRate);
		yearlySalary = salary;
	}
	
	@Override
	public double calcAnnualIncome(){
		return yearlySalary * (1 - deductionRate);
	}
}
