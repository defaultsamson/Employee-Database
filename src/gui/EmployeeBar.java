package gui;

import javax.swing.JButton;
import javax.swing.JPanel;
import data.EmployeeInfo;
import data.PartTimeEmployee;
import data.FullTimeEmployee;


public class EmployeeBar extends JPanel{
	
	private EmployeeInfo employee;
	private boolean isFullTime;
	
	public EmployeeBar (FullTimeEmployee emp){
            employee = emp;
	}
}
