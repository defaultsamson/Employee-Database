package gui;

import javax.swing.JPanel;

import data.EmployeeInfo;
import data.FullTimeEmployee;

public class EmployeeBar extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8690389488462080521L;

	private EmployeeInfo employee;
	private boolean isFullTime;

	public EmployeeBar(FullTimeEmployee emp) {
		employee = emp;
	}
}
