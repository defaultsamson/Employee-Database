package hashTable;

public class PartTimeEmployee extends EmployeeInfo{
	private double hourlyWage;
	private double hoursPerWeek;
	private double weeksPerYear;
	
	public PartTimeEmployee(int empNo, String fName, String lName, int sex, int loc, double dRate, 
			double wage, double hourPWeek, double weekPYear){
		super(empNo,fName,lName,sex,loc,dRate);
		hourlyWage = wage;
		hoursPerWeek = hourPWeek;
		weeksPerYear = weekPYear;
	}
	
	@Override
	public double calcAnnualIncome(){
		return hourlyWage * hoursPerWeek * weeksPerYear * (1 - deductionRate);
	}
}
