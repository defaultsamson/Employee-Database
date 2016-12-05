package data;

import java.util.ArrayList;

public class OpenHashTable {
	private int numItems;
	private ArrayList<ArrayList<EmployeeInfo>> buckets;

	public OpenHashTable(int columns) {
		numItems = 0;

		// Creates an acceptable value for the number of columns
		int colCount = Math.max(1, columns);

		// Creates a set number of columns
		buckets = new ArrayList<ArrayList<EmployeeInfo>>(colCount);

		// Creates dynamically resizable rows
		for (int i = 0; i < colCount; i++) {
			buckets.add(new ArrayList<EmployeeInfo>());
		}
	}

	public boolean addEmployee(EmployeeInfo e) {
		try {
			int index = calcBucket(e.getEmployeeNumber());
			buckets.get(index).add(e);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public int calcBucket(int keyValue) {
		// Finds the column to put the EmployeeInfo into based on its mod of
		// the number of columns
		return keyValue % buckets.size();
	}

	public void displayContents() {
		for (int i = 0; i < buckets.size(); i++) { // Goes through columns
			for (int j = 0; j < buckets.get(i).size(); j++) { // Goes through
																// rows
				EmployeeInfo e = buckets.get(i).get(j);
				System.out.println("(c:" + i + " r:" + j + ") #:" + e.getEmployeeNumber() + " name:" + e.getFirstName());
			}
		}
	}

	/**
	 * Finds the position of an employee in a bucket from the employee's ID.
	 * 
	 * @param id
	 *            the employee's ID
	 * @return
	 */
	private int findInTable(int id) {
		// Gets the bucket the employee is in
		int buck = calcBucket(id);
		ArrayList<EmployeeInfo> bucket = buckets.get(buck);

		// Finds their position in that bucket
		for (int i = 0; i < bucket.size(); i++) {
			if (bucket.get(i).getEmployeeNumber() == id)
				return i;
		}

		// -1 if not found iin bucket for some reason |:I
		return -1;
	}

	public ArrayList<ArrayList<EmployeeInfo>> getBuckets() {
		return buckets;
	}

	public int getNumItems() {
		return numItems;
	}

	/**
	 * Removes an employee's info.
	 * 
	 * @param id
	 *            the employee's number
	 * @return the employee info being removed
	 */
	public EmployeeInfo removeEmployee(int id) {
		for (ArrayList<EmployeeInfo> row : buckets) { // Goes through columns
			for (EmployeeInfo e : row) { // Goes through rows
				if (e.getEmployeeNumber() == id) {
					row.remove(e);
					return e;
				}
			}
		}

		return null;
	}

	/**
	 * Finds an employee's info from their id.
	 * 
	 * @param id
	 * @return
	 */
	public EmployeeInfo searchEmployee(int id) {
		for (ArrayList<EmployeeInfo> row : buckets) { // Goes through columns
			for (EmployeeInfo e : row) { // Goes through rows
				if (e.getEmployeeNumber() == id)
					return e;
			}
		}

		return null;
	}
}