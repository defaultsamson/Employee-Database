import data.employee.FullTimeEmployee;
import data.employee.Gender;
import data.employee.Location;
import data.employee.PartTimeEmployee;
import io.save.Database;

public class Main {
	public static void main(String[] args) {
		Database.instance().table.add(new PartTimeEmployee(53, "Karlos", "Ketter", Gender.MALE, Location.CHICAGO, 0.15, 11.5, 30, 25));
		Database.instance().table.add(new FullTimeEmployee(1, "Cameron", "Scott", Gender.FEMALE, Location.MISSISSAUGA, 0.13, 55300));
		Database.instance().save();
	}
}
