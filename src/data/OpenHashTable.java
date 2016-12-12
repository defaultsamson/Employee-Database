package data;

import java.util.ArrayList;

import data.EmployeeInfo;

/**
 *  The hash table data structure used to store all the employee information
 */
public class OpenHashTable {
	// buckets is an array of ArrayList<EmployeeInfo>.  Each item in an ArrayList is an EmployeeInfo object.
	private ArrayList<EmployeeInfo>[] buckets;
	
	/**
	 * Create a hash table
	 * @param bucketNumber	
	 * 			The number of buckets
	 */
	@SuppressWarnings("unchecked")
	public OpenHashTable(int bucketNumber) {
		// Construct the hash table (open hashing/closed addressing) as an array of bucketNumber ArrayLists.
		// Instantiate an array to have an ArrayList as each element of the array.
		buckets = new ArrayList[bucketNumber];

		// For each element in the array, instantiate its ArrayList.
		for (int i = 0; i < bucketNumber; i++) {
			buckets[i] = new ArrayList<EmployeeInfo>();  // Instantiate the ArrayList for bucket i.
		}
	}

	/**
	 * Find the bucket that an employee belongs to
	 * @param keyValue	The employee number of the employee
	 * @return			The bucket the employee is in
	 */
	private int calcBucket(int keyValue) {
		// Returns the bucket number as the integer keyValue modulo the number of buckets for the hash table.
		int reminder = keyValue % buckets.length;
		//If the reminder is positive, which it generally will be, return it
		if (reminder >= 0){
			return reminder;
		} else{
			//If the user enters a negative keyValue, return the positive reminder
			return buckets.length + reminder;
		}
	}

	/**
	 * Add an employee to the hash table
	 * @param theEmployee	The new employee to be added
	 * @return				Whether or not the employee is successfully added
	 */
	public boolean addEmployee(EmployeeInfo theEmployee) {
		boolean successful;
		successful = buckets[calcBucket(theEmployee.getEmployeeNumber())].add(theEmployee);
		return successful;
	}
	
	/**
	 * Search the table to find a specific employee
	 * @param employeeNum	The employee number of the employee
	 * @return				The employee with the specified employee number
	 */
	public EmployeeInfo searchEmployee(int employeeNum){
		for (EmployeeInfo i : buckets[calcBucket(employeeNum)]){
			if (i.getEmployeeNumber() == employeeNum){
				return i;
			}
		}	
		return null;
	}

	/**
	 * Search and remove an employee
	 * @param employeeNum	The employee number of the employee that will be removed
	 * @return				The employee that is removed
	 */
	public EmployeeInfo removeEmployee(int employeeNum) {
		EmployeeInfo result;
		result = searchEmployee(employeeNum);
		if(result != null){
			buckets[calcBucket(employeeNum)].remove(result);
		}
		return result;
	}
	
	/**
	 * Display the content of hash table onto the console
	 */
	public void displayContents() {
		//Go through all buckets
		for(int i = 0; i < buckets.length; i ++){
			System.out.println("Bucket " + i);
			//Display all objects in every bucket
			for (EmployeeInfo j : buckets[i]){
				System.out.println("    " + j);
			}
		}
	}
	
	/**
	 * Getter method for the buckets
	 * @return	The buckets of the hash table
	 */
	public ArrayList<EmployeeInfo>[] getBuckets(){
		return buckets;
	}
}
