/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.CardLayout;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import data.EmployeeInfo;
import data.FullTimeEmployee;
import data.Gender;
import data.Location;
import data.OpenHashTable;
import data.PartTimeEmployee;
import io.Database;

/**
 * The main user interface class for creating an employee database
 */
public class MainUI extends javax.swing.JFrame {

	// Serial code for JFrame
	private static final long serialVersionUID = -5250494138480577419L;
	
	//Web site for the help manual
	private static final String HELP_URL = "https://docs.google.com/document/d/1CnoW9rv26RZfZ2koTzjqFZrtr3Hw4pRkOjv-A8SjeX8/edit?usp=sharing";

	//String constants
	private static final Object SAVE_MESSAGE = "Save all changes before closing the program?";
	private static final String SAVE_TITLE = "Unsaved changes!";

	private OpenHashTable table;
	/** The employee that's currently being edited */
	private EmployeeInfo editingEmployee;

	private boolean hasUnsavedChanges = false;
	
	//Control switches for the UI.
	//If the employee information panel is editable or not
	private boolean infoPanelEditable;
	/**
	 * @return if the user is currently editing an employee.
	 */
	private boolean isEditing() {
		return editingEmployee != null;
	}

	private static int BUCKET_NUMBER = 2;

	/**
	 * Constructor for creating the MainUI
	 */
	public MainUI() {
		table = new OpenHashTable(BUCKET_NUMBER);
		// Load the hash table from the save file using the Database class
		Database.instance().load(table);

		// Initialise the GUI components, using Netbean generated code
		initComponents();

		editingEmployee = null;
		infoPanelEditable = false;
		setEditableEmployeeInfoPanel(infoPanelEditable);

		// Add DocumentListener for searchTextField to allow instantaneous searches
		searchTextField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				action();
			}

			public void removeUpdate(DocumentEvent e) {
				action();
			}

			public void insertUpdate(DocumentEvent e) {
				action();
			}

			private void action() {
				updateDisplayTable();
			}
		});

		// Update the displayed table
		updateDisplayTable();
	}

	// Update the display table based on the text in the search bar
	private void updateDisplayTable() {
		ListModel<EmployeeInfo> listModel = employeeList.getModel();

		// Check to see if the list is an instance of DefaultListModel, for type safety
		if (listModel instanceof DefaultListModel<?>) {
			DefaultListModel<EmployeeInfo> entries = (DefaultListModel<EmployeeInfo>) listModel;

			// Clears the list
			entries.removeAllElements();

			String search = searchTextField.getText().toLowerCase();
			// doSearch indicates if there are non-space characters
			boolean doSearch = !search.replace(" ", "").equals("");

			// If there's a search, filter things out
			if (doSearch) {
				// The parameters to search for (Each word that the user has typed in)
				String[] params = search.split(" ");

				// For every EmployeeInfo in the hash table
				for (ArrayList<EmployeeInfo> bucket : table.getBuckets()) {
					for (EmployeeInfo e : bucket) {

						// Keep track if each parameter matched a field
						boolean[] addResult = new boolean[params.length];
						for (int i = 0; i < addResult.length; i++) {
							addResult[i] = false;
						}

						// Test each parameter for a match
						for (int i = 0; i < params.length; i++) {

							String param = params[i];

							// Test first name
							if (e.getFirstName().toLowerCase().contains(param)) {
								addResult[i] = true;
							}

							// Test last name
							if (e.getLastName().toLowerCase().contains(param)) {
								addResult[i] = true;
							}

							// Test ID number
							if (("" + e.getEmployeeNumber()).contains(param)) {
								addResult[i] = true;
							}
						}

						// Tests if each parameter found a match in this EmployeeInfo object, in case of multiple words
						boolean trulyAdd = true;
						for (int j = 0; j < addResult.length; j++) {
							if (addResult[j] == false)
								trulyAdd = false;
						}

						// If the above code found it to be true, add the element
						if (trulyAdd) {
							entries.addElement(e);
						}
					}
				}
			} else {// nothing is being searched, add everything
				// For every EmployeeInfo in the hash table
				for (ArrayList<EmployeeInfo> bucket : table.getBuckets()) {
					for (EmployeeInfo e : bucket) {
						// Add it
						entries.addElement(e);
					}
				}
			}
		} else {
			System.err.println("Display table's ListModel isn't an instance of DefaultListModel");
		}
	}

	/**
	 * Save the table using the Database class
	 */
	private void saveTable() {
		Database.instance().save(table);
		hasUnsavedChanges = false;
	}

	/**
	 * Switch the interface to adding mode
	 */
	private void addBlankEmployee() {
		editingEmployee = null;
		infoPanelEditable = true;
		setEditableEmployeeInfoPanel(infoPanelEditable);
		employeeList.clearSelection();
		clearEmployeeInfo();
	}

	/**
	 * Switch the interface to editing mode, only called when an employee is selected
	 * 
	 * @param employee
	 *            the selected employee
	 */
	private void editEmployee(EmployeeInfo employee) {
		editingEmployee = employee;
		infoPanelEditable = true;
		setEditableEmployeeInfoPanel(infoPanelEditable);
		displayEmployeeInfo(employee);
	}

	/**
	 * Display the selected employee on the information section on the right
	 * 
	 * @param employee
	 *            the employee to display
	 */
	private void displayEmployeeInfo(EmployeeInfo employee) {
		// Set the value of all text field and combo box to that of the employee
		empInfoFirstName.setText(employee.getFirstName());
		empInfoLastName.setText(employee.getLastName());
		empInfoEmpnum.setText(Integer.toString(employee.getEmployeeNumber()));
		empInfoComboBoxGender.setSelectedItem(employee.getGender());
		// Display the double as a percentage rounded to at most 2 decimal digits
		// This is added to prevent a java internal error related to binary representation of decimals
		empInfoDeductionRate.setText(Double.toString((Math.round(employee.getDeductionsRate() * 10000.0) / 100.0)));
		empInfoComboBoxLocation.setSelectedItem(employee.getLocation());

		//All text field for money will be formatted to two decimal places
		NumberFormat moneyFormatter = new DecimalFormat("#0.00");
		// Check if the employee is part time or full time
		if (employee instanceof FullTimeEmployee) {
			// For full time employee, set and display the full time panel
			fullTimeRadioButton.setSelected(true);
			selectWagePanel();
			FullTimeEmployee fullEmployee = (FullTimeEmployee) employee;
			// Display the double values, all of them are rounded to at most 2 decimal places
			fullTimeSalaryTextField.setText(moneyFormatter.format((Math.round(fullEmployee.getYearlySalary() * 100.0) / 100.0)));
			fullTimeIncomeTextField.setText(moneyFormatter.format(Math.round(fullEmployee.calcAnnualIncome() * 100.0) / 100.0));

			// Remove all temporary values stored on partTimeWagePanel
			partTimeHourlyWageTextField.setText("");
			partTimeHoursWorkedTextField.setText("");
			partTimeWeeksWorkedTextField.setText("");
			partTimeIncomeTextField.setText("");
		} else {
			if (employee instanceof PartTimeEmployee) {
				// For part time employee, set and display the part time panel
				partTimeRadioButton.setSelected(true);
				selectWagePanel();
				PartTimeEmployee partEmployee = (PartTimeEmployee) employee;
				// Display the double values, all of them are rounded to at most 2 decimal places
				partTimeHourlyWageTextField.setText(moneyFormatter.format((Math.round(partEmployee.getHourlyWage() * 100.0) / 100.0)));
				partTimeHoursWorkedTextField.setText(Double.toString((Math.round(partEmployee.getHoursPerWeek() * 100.0) / 100.0)));
				partTimeWeeksWorkedTextField.setText(Double.toString((Math.round(partEmployee.getWeeksPerYear() * 100.0) / 100.0)));
				partTimeIncomeTextField.setText(moneyFormatter.format(Math.round(partEmployee.calcAnnualIncome() * 100.0) / 100.0));

				// Remove all temporary values stored on fullTimeWagePanel
				fullTimeSalaryTextField.setText("");
				fullTimeIncomeTextField.setText("");
			}
		}
	}

	/**
	 * Removes all instance values stored in the GUI components
	 */
	private void clearEmployeeInfo() {
		empInfoFirstName.setText("");
		empInfoLastName.setText("");
		empInfoEmpnum.setText("");
		empInfoDeductionRate.setText("");
		empInfoComboBoxGender.setSelectedIndex(-1);
		empInfoComboBoxLocation.setSelectedIndex(-1);
		fullTimeRadioButton.setSelected(false);
		fullTimeIncomeTextField.setText("");
		fullTimeSalaryTextField.setText("");
		fullTimeIncomeTextField.setText("");
		partTimeRadioButton.setSelected(false);
		partTimeHourlyWageTextField.setText("");
		partTimeHoursWorkedTextField.setText("");
		partTimeWeeksWorkedTextField.setText("");
		partTimeIncomeTextField.setText("");
	}

	/**
	 * Set whether or not everything in EmployeeInfoPanel is enabled for editing
	 * 
	 * @param editable
	 */
	private void setEditableEmployeeInfoPanel(boolean editable) {

		// Make all text fields editable all other component enabled if editable is true
		// Otherwise set all text field as not editable and disable all other components
		empInfoEmpnum.setEditable(editable);
		empInfoFirstName.setEditable(editable);
		empInfoLastName.setEditable(editable);
		empInfoComboBoxGender.setEnabled(editable);
		empInfoComboBoxLocation.setEnabled(editable);
		empInfoDeductionRate.setEditable(editable);
		partTimeRadioButton.setEnabled(editable);
		fullTimeRadioButton.setEnabled(editable);

		// Set all valid text field in partTimeWagePanel as editable depends on the boolean
		// editable. Annual income is not a valid text field for this as it is never editable
		partTimeHourlyWageTextField.setEditable(editable);
		partTimeHoursWorkedTextField.setEditable(editable);
		partTimeWeeksWorkedTextField.setEditable(editable);

		// Set editable status for all valid text field in fullTimeWagePanel
		fullTimeSalaryTextField.setEditable(editable);

		// The employeeList and the buttons for adding, removing and editing employees
		// are locked when editing the info panel to prevent losing any changes
		employeeList.setEnabled(!editable);
		addButton.setEnabled(!editable);
		removeButton.setEnabled(!editable);
		editButton.setEnabled(!editable);
		
		//Disable the menu edit options when editing
		menuItemAdd.setEnabled(!editable);
		menuItemRemove.setEnabled(!editable);
		menuItemEdit.setEnabled(!editable);

		// Enable the doneButton and clearButton when editing
		doneButton.setEnabled(editable);
		clearButton.setEnabled(editable);
	}

	/**
	 * Select the wagePanel to display based on whether full time or part time button is selected.
	 */
	public void selectWagePanel() {
		if (fullTimeRadioButton.isSelected()) {
			((CardLayout) wagePanel.getLayout()).show(wagePanel, "fullTimeWageCard");
		} else {
			if (partTimeRadioButton.isSelected()) {
				((CardLayout) wagePanel.getLayout()).show(wagePanel, "partTimeWageCard");
			}
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fullPartTimeButtonGroup = new javax.swing.ButtonGroup();
        employeeListScrollPane = new javax.swing.JScrollPane();
        employeeList = new gui.EmployeeList();
        employeeList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(employeeList.getSelectedValue() != null){
                    displayEmployeeInfo(employeeList.getSelectedValue());
                }
            }
        });
        listHeadingPanel = new javax.swing.JPanel();
        labelEmployeeNumber = new javax.swing.JLabel();
        labelLastName = new javax.swing.JLabel();
        labelFirstName = new javax.swing.JLabel();
        searchTextField = new IconTextField("Search", IconType.SEARCH);
        buttonPanel = new javax.swing.JPanel();
        addButton = new IconButton(IconType.ADD);
        removeButton = new IconButton(IconType.REMOVE);
        editButton = new IconButton(IconType.EDIT);
        saveButton = new IconButton(IconType.SAVE);
        doneButton = new IconButton(IconType.DONE);
        employeeInfoPanel = new javax.swing.JPanel();
        firstNameLabel = new javax.swing.JLabel();
        empInfoFirstName = new javax.swing.JTextField();
        lastNameEmpInfoLabel = new javax.swing.JLabel();
        empInfoLastName = new javax.swing.JTextField();
        deductionRateLabel = new javax.swing.JLabel();
        empInfoDeductionRate = new javax.swing.JTextField();
        portraitPanel = new ImagePanel(IconType.UNKNOWN);
        empnumLabel = new javax.swing.JLabel();
        fullTimeRadioButton = new javax.swing.JRadioButton();
        partTimeRadioButton = new javax.swing.JRadioButton();
        genderLabel = new javax.swing.JLabel();
        empInfoComboBoxGender = new javax.swing.JComboBox<>();
        genderLabel1 = new javax.swing.JLabel();
        empInfoComboBoxLocation = new javax.swing.JComboBox<>();
        wagePanel = new javax.swing.JPanel();
        fullTimeWagePanel = new javax.swing.JPanel();
        fullTimeAnnualSalaryLabel = new javax.swing.JLabel();
        fullTimeIncomeLabel = new javax.swing.JLabel();
        fullTimeIncomeTextField = new javax.swing.JTextField();
        fullTimeSalaryTextField = new javax.swing.JTextField();
        partTimeWagePanel = new javax.swing.JPanel();
        partTimeHourlyWageLabel = new javax.swing.JLabel();
        partTimeHoursWorkedLabel = new javax.swing.JLabel();
        partTimeHourlyWageTextField = new javax.swing.JTextField();
        partTimeHoursWorkedTextField = new javax.swing.JTextField();
        partTimeWeeksWorkedLabel = new javax.swing.JLabel();
        partTimeWeeksWorkedTextField = new javax.swing.JTextField();
        partTimeWageLabel = new javax.swing.JLabel();
        partTimeIncomeTextField = new javax.swing.JTextField();
        empInfoEmpnum = new javax.swing.JTextField();
        deductionRatePercentSignLabel = new javax.swing.JLabel();
        clearButton = new IconButton(IconType.CLEAR);
        menuBar = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        menuItemSave = new javax.swing.JMenuItem();
        menuEdit = new javax.swing.JMenu();
        menuItemAdd = new javax.swing.JMenuItem();
        menuItemRemove = new javax.swing.JMenuItem();
        menuItemEdit = new javax.swing.JMenuItem();
        menuHelp = new javax.swing.JMenu();
        menuItemManual = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(900, 450));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                windowsClosingHandler(evt);
            }
        });

        employeeListScrollPane.setViewportView(employeeList);

        labelEmployeeNumber.setText("<html>Employee<br>&nbsp;Number</p></html>");

        labelLastName.setText("Last Name");

        labelFirstName.setText("First Name");

        javax.swing.GroupLayout listHeadingPanelLayout = new javax.swing.GroupLayout(listHeadingPanel);
        listHeadingPanel.setLayout(listHeadingPanelLayout);
        listHeadingPanelLayout.setHorizontalGroup(
            listHeadingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(listHeadingPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelFirstName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelLastName)
                .addGap(12, 12, 12)
                .addComponent(labelEmployeeNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(63, Short.MAX_VALUE))
            .addGroup(listHeadingPanelLayout.createSequentialGroup()
                .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        listHeadingPanelLayout.setVerticalGroup(
            listHeadingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(listHeadingPanelLayout.createSequentialGroup()
                .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(listHeadingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelEmployeeNumber, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, listHeadingPanelLayout.createSequentialGroup()
                        .addGroup(listHeadingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelLastName)
                            .addComponent(labelFirstName))
                        .addContainerGap())))
        );

        addButton.setToolTipText("Add a new employee");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        removeButton.setToolTipText("Remove the employee");
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        editButton.setToolTipText("Edit the employee");
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });

        saveButton.setToolTipText("Save the employee table");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout buttonPanelLayout = new javax.swing.GroupLayout(buttonPanel);
        buttonPanel.setLayout(buttonPanelLayout);
        buttonPanelLayout.setHorizontalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(removeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(editButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        buttonPanelLayout.setVerticalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonPanelLayout.createSequentialGroup()
                .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(removeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 24, Short.MAX_VALUE))
        );

        doneButton.setToolTipText("Confirm changes");
        doneButton.setEnabled(false);
        doneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doneButtonActionPerformed(evt);
            }
        });

        employeeInfoPanel.setBackground(new java.awt.Color(204, 204, 204));
        employeeInfoPanel.setEnabled(false);

        firstNameLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        firstNameLabel.setText("First Name");

        lastNameEmpInfoLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lastNameEmpInfoLabel.setText("Last Name");

        empInfoLastName.setSize(new java.awt.Dimension(10, 26));

        deductionRateLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        deductionRateLabel.setText("Deduction Rate");

        portraitPanel.setBackground(new java.awt.Color(204, 204, 204));
        portraitPanel.setPreferredSize(new java.awt.Dimension(160, 200));

        javax.swing.GroupLayout portraitPanelLayout = new javax.swing.GroupLayout(portraitPanel);
        portraitPanel.setLayout(portraitPanelLayout);
        portraitPanelLayout.setHorizontalGroup(
            portraitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 160, Short.MAX_VALUE)
        );
        portraitPanelLayout.setVerticalGroup(
            portraitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        empnumLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        empnumLabel.setText("Employee Number");

        fullPartTimeButtonGroup.add(fullTimeRadioButton);
        fullTimeRadioButton.setSelected(true);
        fullTimeRadioButton.setText("Full Time");
        fullTimeRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fullTimeRadioButtonActionPerformed(evt);
            }
        });

        fullPartTimeButtonGroup.add(partTimeRadioButton);
        partTimeRadioButton.setText("Part Time");
        partTimeRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                partTimeRadioButtonActionPerformed(evt);
            }
        });

        genderLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        genderLabel.setText("Gender\n");

        empInfoComboBoxGender.setModel((new javax.swing.DefaultComboBoxModel<>(new Gender[] { Gender.MALE, Gender.FEMALE, Gender.OTHER })));
        empInfoComboBoxGender.setSelectedIndex(-1);
        empInfoComboBoxGender.setSelectedItem(null);
        empInfoComboBoxGender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                empInfoComboBoxGenderActionPerformed(evt);
            }
        });

        genderLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        genderLabel1.setText("Location");

        empInfoComboBoxLocation.setModel(new javax.swing.DefaultComboBoxModel<>(new Location[] { Location.MISSISSAUGA, Location.OTTAWA, Location.CHICAGO }));
        empInfoComboBoxLocation.setSelectedIndex(-1);
        empInfoComboBoxLocation.setSelectedItem(null);

        wagePanel.setLayout(new java.awt.CardLayout());

        fullTimeWagePanel.setPreferredSize(new java.awt.Dimension(492, 70));

        fullTimeAnnualSalaryLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        fullTimeAnnualSalaryLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fullTimeAnnualSalaryLabel.setText("<html>Annual<br>Salary ($)</p></html>");

        fullTimeIncomeLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        fullTimeIncomeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fullTimeIncomeLabel.setText("<html>Annual<br>Income ($)</p></html>");

        fullTimeIncomeTextField.setEditable(false);

        javax.swing.GroupLayout fullTimeWagePanelLayout = new javax.swing.GroupLayout(fullTimeWagePanel);
        fullTimeWagePanel.setLayout(fullTimeWagePanelLayout);
        fullTimeWagePanelLayout.setHorizontalGroup(
            fullTimeWagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fullTimeWagePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(fullTimeWagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fullTimeSalaryTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fullTimeAnnualSalaryLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 209, Short.MAX_VALUE)
                .addGroup(fullTimeWagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fullTimeIncomeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fullTimeIncomeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        fullTimeWagePanelLayout.setVerticalGroup(
            fullTimeWagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, fullTimeWagePanelLayout.createSequentialGroup()
                .addGroup(fullTimeWagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fullTimeAnnualSalaryLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fullTimeIncomeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(fullTimeWagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fullTimeIncomeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fullTimeSalaryTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        wagePanel.add(fullTimeWagePanel, "fullTimeWageCard");

        partTimeWagePanel.setPreferredSize(new java.awt.Dimension(492, 70));

        partTimeHourlyWageLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        partTimeHourlyWageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        partTimeHourlyWageLabel.setText("<html>Hourly<br>Wage ($)</p></html>");

        partTimeHoursWorkedLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        partTimeHoursWorkedLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        partTimeHoursWorkedLabel.setText("<html>&nbsp;&nbsp;Hours<br>per Week</p></html>");

        partTimeWeeksWorkedLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        partTimeWeeksWorkedLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        partTimeWeeksWorkedLabel.setText("<html>&nbsp;&nbsp;Weeks<br>per Year</p></html>");

        partTimeWageLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        partTimeWageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        partTimeWageLabel.setText("<html>&nbsp;&nbsp;Annual<br>Income ($)</p></html>");

        partTimeIncomeTextField.setEditable(false);

        javax.swing.GroupLayout partTimeWagePanelLayout = new javax.swing.GroupLayout(partTimeWagePanel);
        partTimeWagePanel.setLayout(partTimeWagePanelLayout);
        partTimeWagePanelLayout.setHorizontalGroup(
            partTimeWagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(partTimeWagePanelLayout.createSequentialGroup()
                .addGroup(partTimeWagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(partTimeWagePanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(partTimeHourlyWageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(partTimeWagePanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(partTimeHourlyWageTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                .addGroup(partTimeWagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(partTimeHoursWorkedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(partTimeWagePanelLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(partTimeHoursWorkedLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                .addGroup(partTimeWagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(partTimeWeeksWorkedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, partTimeWagePanelLayout.createSequentialGroup()
                        .addComponent(partTimeWeeksWorkedLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                .addGroup(partTimeWagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(partTimeIncomeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, partTimeWagePanelLayout.createSequentialGroup()
                        .addComponent(partTimeWageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16)))
                .addContainerGap())
        );
        partTimeWagePanelLayout.setVerticalGroup(
            partTimeWagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, partTimeWagePanelLayout.createSequentialGroup()
                .addGroup(partTimeWagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(partTimeHourlyWageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(partTimeHoursWorkedLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(partTimeWeeksWorkedLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(partTimeWageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(partTimeWagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(partTimeHourlyWageTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(partTimeHoursWorkedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(partTimeWeeksWorkedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(partTimeIncomeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        wagePanel.add(partTimeWagePanel, "partTimeWageCard");

        deductionRatePercentSignLabel.setText("%");

        javax.swing.GroupLayout employeeInfoPanelLayout = new javax.swing.GroupLayout(employeeInfoPanel);
        employeeInfoPanel.setLayout(employeeInfoPanelLayout);
        employeeInfoPanelLayout.setHorizontalGroup(
            employeeInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(employeeInfoPanelLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(employeeInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(employeeInfoPanelLayout.createSequentialGroup()
                        .addComponent(wagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 571, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(employeeInfoPanelLayout.createSequentialGroup()
                        .addComponent(portraitPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(employeeInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(employeeInfoPanelLayout.createSequentialGroup()
                                .addGroup(employeeInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(employeeInfoPanelLayout.createSequentialGroup()
                                        .addGroup(employeeInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(firstNameLabel)
                                            .addComponent(empInfoComboBoxGender, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(empInfoFirstName))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                    .addGroup(employeeInfoPanelLayout.createSequentialGroup()
                                        .addComponent(genderLabel)
                                        .addGap(98, 98, 98)))
                                .addGroup(employeeInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(empInfoLastName)
                                    .addGroup(employeeInfoPanelLayout.createSequentialGroup()
                                        .addGroup(employeeInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(genderLabel1)
                                            .addComponent(lastNameEmpInfoLabel)
                                            .addComponent(empInfoComboBoxLocation, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addGroup(employeeInfoPanelLayout.createSequentialGroup()
                                .addComponent(fullTimeRadioButton)
                                .addGap(49, 49, 49)
                                .addComponent(partTimeRadioButton)
                                .addGap(60, 169, Short.MAX_VALUE))
                            .addGroup(employeeInfoPanelLayout.createSequentialGroup()
                                .addGroup(employeeInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(empnumLabel)
                                    .addComponent(empInfoEmpnum, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(employeeInfoPanelLayout.createSequentialGroup()
                                        .addComponent(empInfoDeductionRate, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(deductionRatePercentSignLabel))
                                    .addComponent(deductionRateLabel))
                                .addGap(0, 0, Short.MAX_VALUE))))))
        );
        employeeInfoPanelLayout.setVerticalGroup(
            employeeInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(employeeInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(empnumLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(employeeInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(employeeInfoPanelLayout.createSequentialGroup()
                        .addComponent(empInfoEmpnum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(employeeInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(firstNameLabel)
                            .addComponent(lastNameEmpInfoLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(employeeInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(empInfoFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(empInfoLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)
                        .addGroup(employeeInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(genderLabel)
                            .addComponent(genderLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(employeeInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(empInfoComboBoxGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(empInfoComboBoxLocation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deductionRateLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(employeeInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(empInfoDeductionRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(deductionRatePercentSignLabel)))
                    .addComponent(portraitPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(employeeInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(partTimeRadioButton)
                    .addComponent(fullTimeRadioButton))
                .addGap(18, 18, 18)
                .addComponent(wagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                .addContainerGap())
        );

        clearButton.setToolTipText("Cancel Changes");
        clearButton.setEnabled(false);
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        menuFile.setText("File");

        menuItemSave.setText("save");
        menuItemSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemSaveActionPerformed(evt);
            }
        });
        menuFile.add(menuItemSave);

        menuBar.add(menuFile);

        menuEdit.setText("Edit");

        menuItemAdd.setText("Add new employee");
        menuItemAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemAddActionPerformed(evt);
            }
        });
        menuEdit.add(menuItemAdd);

        menuItemRemove.setText("Remove employee");
        menuItemRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemRemoveActionPerformed(evt);
            }
        });
        menuEdit.add(menuItemRemove);

        menuItemEdit.setText("Edit employee");
        menuItemEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemEditActionPerformed(evt);
            }
        });
        menuEdit.add(menuItemEdit);

        menuBar.add(menuEdit);

        menuHelp.setText("Help");

        menuItemManual.setText("Open help document");
        menuItemManual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemManualActionPerformed(evt);
            }
        });
        menuHelp.add(menuItemManual);

        menuBar.add(menuHelp);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(listHeadingPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(employeeListScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(24, 24, 24)
                                .addComponent(employeeInfoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(clearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(doneButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(employeeInfoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(doneButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(clearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(listHeadingPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(employeeListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)))
                        .addGap(15, 15, 15))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void menuItemAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemAddActionPerformed
        //Same as pressing the add employee button
        addButtonActionPerformed(evt);
	}//GEN-LAST:event_menuItemAddActionPerformed

	private void menuItemRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemRemoveActionPerformed
		//Pass the method call to the remove button
		removeButtonActionPerformed(evt);
	}//GEN-LAST:event_menuItemRemoveActionPerformed

	private void menuItemEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemEditActionPerformed
		//Pass the method call to the edit button
		editButtonActionPerformed(evt);
	}//GEN-LAST:event_menuItemEditActionPerformed

	private void menuItemManualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemManualActionPerformed
    	//Open the web site that contains the manual
    	try{
    		URI link = new URI(HELP_URL);
        	Desktop.getDesktop().browse(link);
    	} catch (URISyntaxException | IOException e){
    		e.printStackTrace();
    	}
	}//GEN-LAST:event_menuItemManualActionPerformed

	private void windowsClosingHandler(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowsClosingHandler
		//Prompt the user to save before closing the program if there are any unsaved changes
		if(hasUnsavedChanges){
			int saveOption = JOptionPane.showOptionDialog(this, SAVE_MESSAGE, SAVE_TITLE, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
			//If yes is selected, save the table then close
			if(saveOption == JOptionPane.YES_OPTION){
				saveTable();
				//Exit, the parameter 0 means no error in execution
				System.exit(0);
			} else{
				//If no is selected, close without saving
				if(saveOption == JOptionPane.NO_OPTION){
					System.exit(0);
				}
			}
		} else{
			//Exit the program if there are no unsaved changes
			System.exit(0);
		}
		//Do nothing if cancel is neither yes or no is selected
	}//GEN-LAST:event_windowsClosingHandler

	private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_addButtonActionPerformed
		addBlankEmployee();
	}// GEN-LAST:event_addButtonActionPerformed

	private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_removeButtonActionPerformed
		EmployeeInfo removedEmployee = employeeList.getSelectedValue();
		if (removedEmployee != null) {
			table.removeEmployee(removedEmployee.getEmployeeNumber());
			updateDisplayTable();
			clearEmployeeInfo();
			hasUnsavedChanges = true;
		}
	}// GEN-LAST:event_removeButtonActionPerformed

	private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_editButtonActionPerformed
		if (employeeList.getSelectedValue() != null) {
			editEmployee(employeeList.getSelectedValue());
		}
	}// GEN-LAST:event_editButtonActionPerformed

	/**
	 * Code for creating or replacing an employee
	 * 
	 * @param evt
	 */
	private void doneButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_doneButtonActionPerformed
		String errorMessage = new String("Cannot create the employee for the following reasons:\n");
		boolean isValidEmployee = true;
		int newEmployeeNumber = 0;
		double newEmployeeDeduction = 0;
		double newFullTimeSalary = 0;
		double newPartTimeWage = 0;
		double newPartTimeHoursWorked = 0;
		double newPartTimeWeeksWorked = 0;

		// Checking for errors in employee number
		try {
			newEmployeeNumber = Integer.valueOf(empInfoEmpnum.getText());
			// Only positive employee number is accepted
			if (newEmployeeNumber <= 0) {
				errorMessage += "\nEmployee number must be positive!";
				isValidEmployee = false;
			} else {
				EmployeeInfo toTest = table.searchEmployee(newEmployeeNumber);

				// Check to see if employee number is occupied by another employee when adding an employee
				if (toTest != null && toTest != editingEmployee) {
					errorMessage += "\nAn employee with the given employee number already exists!";
					isValidEmployee = false;
				}
			}
		} catch (NumberFormatException e) {
			isValidEmployee = false;
			errorMessage += "\nEmployee number is not a positive integer!";
		}

		if (empInfoComboBoxLocation.getSelectedItem() == null)
		{
			errorMessage += "\nMust choose a work location!";
			isValidEmployee = false;
		}
		
		if (empInfoComboBoxGender.getSelectedItem() == null)
		{
			errorMessage += "\nMust choose a gender!";
			isValidEmployee = false;
		}
		
		// Checking for errors in deduction rate
		try {
			newEmployeeDeduction = Double.valueOf(empInfoDeductionRate.getText());
			// Deduction rate cannot be less than 0 or greater than 100
			if (newEmployeeDeduction < 0 || newEmployeeDeduction > 100) {
				errorMessage += "\nDeductions rate must be between 0 and 100!";
				isValidEmployee = false;
			}
		} catch (NumberFormatException e) {
			isValidEmployee = false;
			errorMessage += "\nDeductions rate is not a valid numerical value!";
		}

		// Check for inputs in the wage panel
		// Check salary for full time employee
		if (fullTimeRadioButton.isSelected()) {
			try {
				newFullTimeSalary = Double.valueOf(fullTimeSalaryTextField.getText());
				if (newFullTimeSalary < 0) {
					isValidEmployee = false;
					errorMessage += "\nAnnual salary cannot be less than 0!";
				}
			} catch (NumberFormatException e) {
				isValidEmployee = false;
				errorMessage += "\nAnnual salary is not a valid numerical value!";
			}
		} else {
			// Check wage inputs for part time employee
			if (partTimeRadioButton.isSelected()) {

				// Check for weekly wage
				try {
					newPartTimeWage = Double.valueOf(partTimeHourlyWageTextField.getText());
					if (newPartTimeWage < 0) {
						errorMessage += "\nHourly wage cannot be less than 0!";
						isValidEmployee = false;
					}
				} catch (NumberFormatException e) {
					isValidEmployee = false;
					errorMessage += "\nHourly wage is not a valid numerical value!";
				}

				// Check for hours worked
				try {
					newPartTimeHoursWorked = Double.valueOf(partTimeHoursWorkedTextField.getText());
					if (newPartTimeHoursWorked < 0) {
						errorMessage += "\nHours per week cannot be less than 0!";
						isValidEmployee = false;
					}
					// Prevent employee being overworked
					if (newPartTimeHoursWorked > PartTimeEmployee.MAX_HOURS_PER_WEEK) {
						errorMessage += "\nHours per week invalid! Only " + PartTimeEmployee.MAX_HOURS_PER_WEEK + " hours in a week!";
						isValidEmployee = false;
					}
				} catch (NumberFormatException e) {
					isValidEmployee = false;
					errorMessage += "\nHours per week is not a valid numerical value!";
				}

				// Check for weeks worked
				try {
					newPartTimeWeeksWorked = Double.valueOf(partTimeWeeksWorkedTextField.getText());
					if (newPartTimeWeeksWorked < 0) {
						errorMessage += "\nWeeks per year cannot be less than 0!";
						isValidEmployee = false;
					}
					if (newPartTimeWeeksWorked > PartTimeEmployee.MAX_WEEKS_PER_YEAR) {
						errorMessage += "\nWeeks per year cannot be greater than " + PartTimeEmployee.MAX_WEEKS_PER_YEAR + " (or 365/7 to be exact)!";
						isValidEmployee = false;
					}
				} catch (NumberFormatException e) {
					isValidEmployee = false;
					errorMessage += "\nWeeks per year is not a valid numerical value!";
				}

			} else {
				// Somehow neither button is selected, should never happen
				errorMessage += "\nINTERNAL ERROR 100: Radio button not selected, please contact us";
			}
		}

		// If all inputs are valid
		if (isValidEmployee) {

			// If the user is editing an entry, replace the employee being edited with the updated employee
			if (isEditing()) {
				if (fullTimeRadioButton.isSelected()) {
					table.replaceEmployee(editingEmployee.getEmployeeNumber(), new FullTimeEmployee(newEmployeeNumber, empInfoFirstName.getText(), empInfoLastName.getText(), (Gender) empInfoComboBoxGender.getSelectedItem(), (Location) empInfoComboBoxLocation.getSelectedItem(), newEmployeeDeduction / 100, newFullTimeSalary));
				} else if (partTimeRadioButton.isSelected()) {
					table.replaceEmployee(editingEmployee.getEmployeeNumber(), new PartTimeEmployee(newEmployeeNumber, empInfoFirstName.getText(), empInfoLastName.getText(), (Gender) empInfoComboBoxGender.getSelectedItem(), (Location) empInfoComboBoxLocation.getSelectedItem(), newEmployeeDeduction / 100, newPartTimeWage, newPartTimeHoursWorked, newPartTimeWeeksWorked));
				}
				// Else create a new entry
			} else {
				if (fullTimeRadioButton.isSelected()) {
					table.addEmployee(new FullTimeEmployee(newEmployeeNumber, empInfoFirstName.getText(), empInfoLastName.getText(), (Gender) empInfoComboBoxGender.getSelectedItem(), (Location) empInfoComboBoxLocation.getSelectedItem(), newEmployeeDeduction / 100, newFullTimeSalary));
				} else if (partTimeRadioButton.isSelected()) {
					table.addEmployee(new PartTimeEmployee(newEmployeeNumber, empInfoFirstName.getText(), empInfoLastName.getText(), (Gender) empInfoComboBoxGender.getSelectedItem(), (Location) empInfoComboBoxLocation.getSelectedItem(), newEmployeeDeduction / 100, newPartTimeWage, newPartTimeHoursWorked, newPartTimeWeeksWorked));
				}
			}

			clearEmployeeInfo();
			updateDisplayTable();
			infoPanelEditable = false;
			setEditableEmployeeInfoPanel(infoPanelEditable);
			//There are unsaved changes
			hasUnsavedChanges = true;
			editingEmployee = null;
		} else {
			JOptionPane.showMessageDialog(this, errorMessage, "Cannot create employee", JOptionPane.ERROR_MESSAGE);
		}
	}// GEN-LAST:event_doneButtonActionPerformed

	private void fullTimeRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_fullTimeRadioButtonActionPerformed
		selectWagePanel();
	}// GEN-LAST:event_fullTimeRadioButtonActionPerformed

	private void partTimeRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_partTimeRadioButtonActionPerformed
		selectWagePanel();
	}// GEN-LAST:event_partTimeRadioButtonActionPerformed

	private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveButtonActionPerformed
		saveTable();
	}// GEN-LAST:event_saveButtonActionPerformed

	private void menuItemSaveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuItemSaveActionPerformed
		saveTable();
	}// GEN-LAST:event_menuItemSaveActionPerformed

	private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_clearButtonActionPerformed
		// Cancel the edit and discard it
		infoPanelEditable = false;
		setEditableEmployeeInfoPanel(infoPanelEditable);
		// Reset the employee info panel to display the selected employee
		clearEmployeeInfo();
		if (editingEmployee != null) {
			//select and display the employee that was previously selected, in case the search bar was used
			employeeList.setSelectedValue(editingEmployee, true);
			displayEmployeeInfo(editingEmployee);
		}
	}// GEN-LAST:event_clearButtonActionPerformed

	// Change the profile picture based on gender
	private void empInfoComboBoxGenderActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_empInfoComboBoxGenderActionPerformed
		// The index for the different genders, change them here if new gender is discovered
		final int MALE_INDEX = 0;
		final int FEMALE_INDEX = 1;
		final int OTHER_INDEX = 2;
		switch (empInfoComboBoxGender.getSelectedIndex()) {
		case MALE_INDEX:
			((ImagePanel) portraitPanel).setImage(IconType.MALE);
			break;
		case FEMALE_INDEX:
			((ImagePanel) portraitPanel).setImage(IconType.FEMALE);
			break;
		case OTHER_INDEX:
			((ImagePanel) portraitPanel).setImage(IconType.OTHER);
			break;
		default:
			((ImagePanel) portraitPanel).setImage(IconType.UNKNOWN);
			break;
		}
	}// GEN-LAST:event_empInfoComboBoxGenderActionPerformed

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the default look and feel */
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new MainUI().setVisible(true);
			}
		});
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gui.IconButton addButton;
    private javax.swing.JPanel buttonPanel;
    private gui.IconButton clearButton;
    private javax.swing.JLabel deductionRateLabel;
    private javax.swing.JLabel deductionRatePercentSignLabel;
    private gui.IconButton doneButton;
    private gui.IconButton editButton;
    private javax.swing.JComboBox<Gender> empInfoComboBoxGender;
    private javax.swing.JComboBox<Location> empInfoComboBoxLocation;
    private javax.swing.JTextField empInfoDeductionRate;
    private javax.swing.JTextField empInfoEmpnum;
    private javax.swing.JTextField empInfoFirstName;
    private javax.swing.JTextField empInfoLastName;
    private javax.swing.JPanel employeeInfoPanel;
    private gui.EmployeeList employeeList;
    private javax.swing.JScrollPane employeeListScrollPane;
    private javax.swing.JLabel empnumLabel;
    private javax.swing.JLabel firstNameLabel;
    private javax.swing.ButtonGroup fullPartTimeButtonGroup;
    private javax.swing.JLabel fullTimeAnnualSalaryLabel;
    private javax.swing.JLabel fullTimeIncomeLabel;
    private javax.swing.JTextField fullTimeIncomeTextField;
    private javax.swing.JRadioButton fullTimeRadioButton;
    private javax.swing.JTextField fullTimeSalaryTextField;
    private javax.swing.JPanel fullTimeWagePanel;
    private javax.swing.JLabel genderLabel;
    private javax.swing.JLabel genderLabel1;
    private javax.swing.JLabel labelEmployeeNumber;
    private javax.swing.JLabel labelFirstName;
    private javax.swing.JLabel labelLastName;
    private javax.swing.JLabel lastNameEmpInfoLabel;
    private javax.swing.JPanel listHeadingPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu menuEdit;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenu menuHelp;
    private javax.swing.JMenuItem menuItemAdd;
    private javax.swing.JMenuItem menuItemEdit;
    private javax.swing.JMenuItem menuItemManual;
    private javax.swing.JMenuItem menuItemRemove;
    private javax.swing.JMenuItem menuItemSave;
    private javax.swing.JLabel partTimeHourlyWageLabel;
    private javax.swing.JTextField partTimeHourlyWageTextField;
    private javax.swing.JLabel partTimeHoursWorkedLabel;
    private javax.swing.JTextField partTimeHoursWorkedTextField;
    private javax.swing.JTextField partTimeIncomeTextField;
    private javax.swing.JRadioButton partTimeRadioButton;
    private javax.swing.JLabel partTimeWageLabel;
    private javax.swing.JPanel partTimeWagePanel;
    private javax.swing.JLabel partTimeWeeksWorkedLabel;
    private javax.swing.JTextField partTimeWeeksWorkedTextField;
    private javax.swing.JPanel portraitPanel;
    private gui.IconButton removeButton;
    private gui.IconButton saveButton;
    private gui.IconTextField searchTextField;
    private javax.swing.JPanel wagePanel;
    // End of variables declaration//GEN-END:variables
}
