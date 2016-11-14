package hashTable;

import java.util.*;

/***************

NAME :									          Yijie Wang
STUDENT NUMBER:							            718084

ICS4UO-A, November 4, 2016

THIS FILE IS PART OF THE PROGRAM: the binary tree assignment

*****************/
/*
 * This is the MyHashTable class, it stores data inside a
 * hash table using buckets and hashing function
 * This table uses open hashing and closed addressing
 */

public class MyHashTable {
	// buckets is an array of ArrayList<EmployeeInfo>.  Each item in an ArrayList is an EmployeeInfo object.
	private ArrayList<EmployeeInfo>[] buckets;

	// CONSTRUCTOR
	public MyHashTable(int bucketNumber) {
		// Construct the hash table (open hashing/closed addressing) as an array of bucketNumber ArrayLists.
		// Instantiate an array to have an ArrayList as each element of the array.
		buckets = new ArrayList[bucketNumber];

		// For each element in the array, instantiate its ArrayList.
		for (int i = 0; i < bucketNumber; i++) {
			buckets[i] = new ArrayList<EmployeeInfo>();  // Instantiate the ArrayList for bucket i.
		}
	}

	public int calcBucket(int keyValue) {
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

	//Add the employee to the hash table
	public boolean addEmployee(EmployeeInfo theEmployee) {
		boolean successful;
		successful = buckets[calcBucket(theEmployee.getEmployeeNumber())].add(theEmployee);
		return successful;
	}
	
	//Search and return the EmployeeInfo object based on the employee number entered
	public EmployeeInfo searchEmployee(int employeeNum){
		for (EmployeeInfo i : buckets[calcBucket(employeeNum)]){
			if (i.getEmployeeNumber() == employeeNum){
				return i;
			}
		}	
		return null;
	}

	//Search and remove the employeeInfo object using employee number, then return the removed object
	public EmployeeInfo removeEmployee(int employeeNum) {
		EmployeeInfo result;
		result = searchEmployee(employeeNum);
		if(result != null){
			buckets[calcBucket(employeeNum)].remove(result);
		}
		return result;
	}
	
	//Display the content stored in this hash table
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
} // end class MyHashTable
