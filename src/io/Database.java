package io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import data.EmployeeInfo;
import data.FullTimeEmployee;
import data.Gender;
import data.Location;
import data.OpenHashTable;
import data.PartTimeEmployee;

public class Database {
	private static Database instance;

	public static Database instance() {
		if (instance == null) {
			instance = new Database(new File("db.json"));
		}
		return instance;
	}

	// All employees
	private static final String KEY_ID = "id"; // int, employee ID
	private static final String KEY_FIRSTNAME = "fst"; // String, first name
	private static final String KEY_LASTNAME = "lst"; // String, last name
	private static final String KEY_GENDER = "gen"; // Gender, the gender
	private static final String KEY_LOCATION = "loc"; // Location, the gender
	private static final String KEY_DEDUCTIONS = "ded"; // double, the deductions rate

	// Full Time
	private static final String KEY_YEARLY_SALARY = "sal"; // double, the yearly salary

	// Part Time
	private static final String KEY_HOURLY_SALARY = "dph"; // double, the hourly wage
	private static final String KEY_WEEKLY_HOURS = "hpw"; // double, the hours worked per week
	private static final String KEY_WEEKS_PER_YEAR = "wpy"; // double, weeks worked per year

	private File file;

	/**
	 * The database that can be saved to and loaded from.
	 * 
	 * @param file
	 *            the file to write to/read from
	 * @param table
	 *            the OpenHashTable
	 */
	public Database(File file) {
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		this.file = file;
	}

	/**
	 * Loads the file into the hash table.
	 * 
	 * @return if the file loaded properly or not.
	 */
	public boolean load(OpenHashTable table) {
		try {
			// Loads lines from the file
			List<String> lines = Files.readAllLines(file.toPath());

			// Parses loaded lines into JSON objects, then to EmployeeInfo
			for (String s : lines) {
				try {
					JSONObject jObj = (JSONObject) JSONValue.parse(s);

					int id = Integer.parseInt(jObj.get(KEY_ID).toString());
					String first = jObj.get(KEY_FIRSTNAME).toString();
					String last = jObj.get(KEY_LASTNAME).toString();
					Gender gender = Gender.valueOf(jObj.get(KEY_GENDER).toString());
					Location location = Location.valueOf(jObj.get(KEY_LOCATION).toString());
					double deductions = Double.parseDouble(jObj.get(KEY_DEDUCTIONS).toString());

					// Tells whether it's a full time employee or not based on whether the entry has a yearly salary
					boolean isFullTime = jObj.get(KEY_YEARLY_SALARY) != null;

					if (isFullTime) {
						double yearlySalary = Double.parseDouble(jObj.get(KEY_YEARLY_SALARY).toString());
						table.addEmployee(new FullTimeEmployee(id, first, last, gender, location, deductions, yearlySalary));
					} else {
						double hourlySalary = Double.parseDouble(jObj.get(KEY_HOURLY_SALARY).toString());
						double weeklyHours = Double.parseDouble(jObj.get(KEY_WEEKLY_HOURS).toString());
						double weeksPerYear = Double.parseDouble(jObj.get(KEY_WEEKS_PER_YEAR).toString());
						table.addEmployee(new PartTimeEmployee(id, first, last, gender, location, deductions, hourlySalary, weeklyHours, weeksPerYear));
					}

				} catch (Exception e) {
					// Failed to load entry
					e.printStackTrace();
				}
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public File getFile() {
		return file;
	}

	/**
	 * Saves the hash table to the file specified upon creation of this Database.
	 * 
	 * @return if the file saved properly or not.
	 */
	@SuppressWarnings("unchecked")
	public boolean save(OpenHashTable table) {
		List<String> lines = new ArrayList<String>();
		for (ArrayList<EmployeeInfo> bucket : table.getBuckets()) {
			for (EmployeeInfo e : bucket) {
				JSONObject obj = new JSONObject();

				obj.put(KEY_ID, e.getEmployeeNumber());
				obj.put(KEY_FIRSTNAME, e.getFirstName());
				obj.put(KEY_LASTNAME, e.getLastName());
				obj.put(KEY_GENDER, e.getGender().toString());
				obj.put(KEY_LOCATION, e.getLocation().toString());
				obj.put(KEY_DEDUCTIONS, e.getDeductionsRate());

				if (e instanceof FullTimeEmployee) {
					FullTimeEmployee f = (FullTimeEmployee) e;
					obj.put(KEY_YEARLY_SALARY, f.getYearlySalary());
				} else if (e instanceof PartTimeEmployee) {
					PartTimeEmployee p = (PartTimeEmployee) e;
					obj.put(KEY_HOURLY_SALARY, p.getHourlyWage());
					obj.put(KEY_WEEKLY_HOURS, p.getHoursPerWeek());
					obj.put(KEY_WEEKS_PER_YEAR, p.getWeeksPerYear());
				}

				lines.add(obj.toJSONString());
			}
		}

		try {
			FileUtil.writeLines(file, lines);
			return true;
		} catch (IOException e) {
			e.printStackTrace();

			return false;
		}
	}
}
