/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import data.*;
import io.Database;
import java.awt.CardLayout;
import java.awt.Color;

/**
 *
 */
public class MainUI extends javax.swing.JFrame {

	private OpenHashTable table;
	
	private boolean isEditing;

	/**
	 * Creates new form NewJFrame
	 */
	public MainUI() {
		isEditing = false;
		table = new OpenHashTable(2);
		Database.instance().load(table);

		initComponents();
		
		setEditableEmployeeInfoPanel(false);
		
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
		
		updateDisplayTable();
	}

	public void updateDisplayTable() {
		ListModel<EmployeeInfo> listModel = employeeList.getModel();

		//Check to see if the list is an instance of DefaultListModel, for type safety
		if (listModel instanceof DefaultListModel<?>) {
			DefaultListModel<EmployeeInfo> entries = (DefaultListModel<EmployeeInfo>) listModel;

			// Clear the list
			entries.removeAllElements();

			String search = searchTextField.getText().toLowerCase();
			//doSearch indicate if there are non-space characters
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

						// Tests if each parameter found a match in this EmployeeInfo object
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

	private void saveTable() {
		Database.instance().save(table);
	}

    private void addBlankEmployee(){
    		isEditing = false;
            setEditableEmployeeInfoPanel(true);
            clearEmployeeInfo();
    }
    
    private void editEmployee(int empnum){
    		isEditing = true;
    		setEditableEmployeeInfoPanel(true);
    		displayEmployeeInfo(table.searchEmployee(empnum));
    }
    
    private void displayEmployeeInfo(EmployeeInfo employee){
    	empInfoFirstName.setText(employee.getFirstName());
    	empInfoLastName.setText(employee.getLastName());
    	empInfoEmpnum.setText(Integer.toString(employee.getEmployeeNumber()));
    	empInfoComboBoxGender.setSelectedItem(employee.getGender());
    	empInfoDeductionRate.setText(Double.toString(employee.getDeductionsRate()));
    	empInfoComboBoxLocation.setSelectedItem(employee.getLocation());
    	fullTimeIncomeTextField.setText(Double.toString(employee.getDeductionsRate()));
    	if(employee instanceof FullTimeEmployee){
    		fullTimeRadioButton.setSelected(true);
    		FullTimeEmployee fullEmployee = (FullTimeEmployee)employee;
    		fullTimeSalaryTextField.setText(Double.toString(fullEmployee.getYearlySalary()));
    		fullTimeIncomeTextField.setText(Double.toString(fullEmployee.calcAnnualIncome()));
    		selectWagePanel();
    	} else {
    		if (employee instanceof PartTimeEmployee){
    			partTimeRadioButton.setSelected(true);
        		PartTimeEmployee partEmployee = (PartTimeEmployee)employee;
        		partTimeHourlyWageTextField.setText(Double.toString(partEmployee.getHourlyWage()));
                partTimeHoursWorkedTextField.setText(Double.toString(partEmployee.getHoursPerWeek()));
                partTimeWeeksWorkedTextField.setText(Double.toString(partEmployee.getWeeksPerYear()));
                partTimeIncomeTextField.setText(Double.toString(partEmployee.calcAnnualIncome()));
                selectWagePanel();
    		}
    	}
    }
    
    private void clearEmployeeInfo(){
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
    
    //Set whether or not everything in EmployeeInfoPanel is enabled for editing
    private void setEditableEmployeeInfoPanel(boolean editable){
    	
    	//Make all text fields editable all other component enabled if editable is true
    	//Otherwise set all text field as not editable and disable all other components
    	empInfoEmpnum.setEditable(editable);
    	empInfoFirstName.setEditable(editable);
    	empInfoLastName.setEditable(editable);
    	empInfoComboBoxGender.setEnabled(editable);
    	empInfoComboBoxLocation.setEnabled(editable);
    	empInfoDeductionRate.setEditable(editable);
    	partTimeRadioButton.setEnabled(editable);
    	fullTimeRadioButton.setEnabled(editable);
    	
    	//Set all valid text field in partTimeWagePanel as editable depends on the boolean
    	//editable. Annual income is not a valid text field for this as it is never editable
        partTimeHourlyWageTextField.setEditable(editable);
        partTimeHoursWorkedTextField.setEditable(editable);
        partTimeWeeksWorkedTextField.setEditable(editable);
        
        //Set editable status for all valid text field in fullTimeWagePanel
        fullTimeSalaryTextField.setEditable(editable);
        
        //The employeeList and the buttons for adding, removing and editing employees
        //are locked when editing the info panel to prevent losing any changes
        employeeList.setEnabled(!editable);
        addButton.setEnabled(!editable);
        removeButton.setEnabled(!editable);
        editButton.setEnabled(!editable);
        
        //Enable the doneButton and clearButton when editing
        doneButton.setEnabled(editable);
        clearButton.setEnabled(editable);
    }
    
    public void selectWagePanel(){
            if(fullTimeRadioButton.isSelected()){
                ((CardLayout)wagePanel.getLayout()).show(wagePanel,"fullTimeWageCard");
            } else {
                if(partTimeRadioButton.isSelected()){
                    ((CardLayout)wagePanel.getLayout()).show(wagePanel,"partTimeWageCard");
                }
            }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fullPartTimeButtonGroup = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
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
        jLabel1 = new javax.swing.JLabel();
        empInfoFirstName = new javax.swing.JTextField();
        lastNameEmpInfoLabel = new javax.swing.JLabel();
        empInfoLastName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        empInfoDeductionRate = new javax.swing.JTextField();
        portraitPanel = new ImagePanel(IconType.USA);
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
        clearButton = new IconButton(IconType.CLEAR);
        jMenuBar2 = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        menuItemSave = new javax.swing.JMenuItem();
        menuEdit = new javax.swing.JMenu();
        menuHelp = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setViewportView(employeeList);

        labelEmployeeNumber.setText("<html>Employee<br>&nbsp;Number</p></html>");

        labelLastName.setText("Last Name");

        labelFirstName.setText("First Name");

        searchTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchTextFieldActionPerformed(evt);
            }
        });

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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(listHeadingPanelLayout.createSequentialGroup()
                .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 67, Short.MAX_VALUE))
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

        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        removeButton.setText("iconButton1");
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        editButton.setText("iconButton1");
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });

        saveButton.setText("iconButton1");
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

        doneButton.setText("iconButton1");
        doneButton.setEnabled(false);
        doneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doneButtonActionPerformed(evt);
            }
        });

        employeeInfoPanel.setBackground(new java.awt.Color(204, 204, 204));
        employeeInfoPanel.setEnabled(false);

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel1.setText("First Name");

        lastNameEmpInfoLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lastNameEmpInfoLabel.setText("Last Name");

        empInfoLastName.setSize(new java.awt.Dimension(10, 26));

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel2.setText("Deduction Rate");

        portraitPanel.setBackground(new java.awt.Color(204, 204, 204));
        portraitPanel.setPreferredSize(new java.awt.Dimension(160, 200));

        javax.swing.GroupLayout portraitPanelLayout = new javax.swing.GroupLayout(portraitPanel);
        portraitPanel.setLayout(portraitPanelLayout);
        portraitPanelLayout.setHorizontalGroup(
            portraitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 193, Short.MAX_VALUE)
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

        genderLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        genderLabel1.setText("Location");

        empInfoComboBoxLocation.setModel(new javax.swing.DefaultComboBoxModel<>(new Location[] { Location.MISSISSAUGA, Location.OTTAWA, Location.CHICAGO }));
        empInfoComboBoxLocation.setSelectedIndex(-1);
        empInfoComboBoxLocation.setSelectedItem(null);

        wagePanel.setLayout(new java.awt.CardLayout());

        fullTimeWagePanel.setPreferredSize(new java.awt.Dimension(492, 70));

        fullTimeAnnualSalaryLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        fullTimeAnnualSalaryLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fullTimeAnnualSalaryLabel.setText("<html>Annual<br>Salary</p></html>");

        fullTimeIncomeLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        fullTimeIncomeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fullTimeIncomeLabel.setText("<html>Annual<br>Income</p></html>");

        fullTimeIncomeTextField.setEditable(false);

        javax.swing.GroupLayout fullTimeWagePanelLayout = new javax.swing.GroupLayout(fullTimeWagePanel);
        fullTimeWagePanel.setLayout(fullTimeWagePanelLayout);
        fullTimeWagePanelLayout.setHorizontalGroup(
            fullTimeWagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fullTimeWagePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(fullTimeWagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fullTimeAnnualSalaryLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fullTimeSalaryTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 209, Short.MAX_VALUE)
                .addGroup(fullTimeWagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fullTimeIncomeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fullTimeIncomeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        partTimeHourlyWageLabel.setText("<html>Hourly<br>&nbsp;Wage</p></html>");

        partTimeHoursWorkedLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        partTimeHoursWorkedLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        partTimeHoursWorkedLabel.setText("<html>&nbsp;&nbsp;Hours<br>per Week</p></html>");

        partTimeWeeksWorkedLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        partTimeWeeksWorkedLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        partTimeWeeksWorkedLabel.setText("<html>&nbsp;&nbsp;Weeks<br>per Year</p></html>");

        partTimeWageLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        partTimeWageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        partTimeWageLabel.setText("<html>Annual<br>Income</p></html>");

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                .addGroup(partTimeWagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(partTimeHoursWorkedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(partTimeWagePanelLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(partTimeHoursWorkedLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                .addGroup(partTimeWagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(partTimeWeeksWorkedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, partTimeWagePanelLayout.createSequentialGroup()
                        .addComponent(partTimeWeeksWorkedLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
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
                        .addComponent(portraitPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(employeeInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(employeeInfoPanelLayout.createSequentialGroup()
                                .addGroup(employeeInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(employeeInfoPanelLayout.createSequentialGroup()
                                        .addGroup(employeeInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel1)
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
                                .addGroup(employeeInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(employeeInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(empInfoDeductionRate)
                                        .addGroup(employeeInfoPanelLayout.createSequentialGroup()
                                            .addComponent(fullTimeRadioButton)
                                            .addGap(18, 49, Short.MAX_VALUE)
                                            .addComponent(partTimeRadioButton)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE))
                                        .addComponent(jLabel2))
                                    .addComponent(empnumLabel)
                                    .addComponent(empInfoEmpnum, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                            .addComponent(jLabel1)
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
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(empInfoDeductionRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(portraitPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(employeeInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(partTimeRadioButton)
                    .addComponent(fullTimeRadioButton))
                .addGap(18, 18, 18)
                .addComponent(wagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                .addContainerGap())
        );

        clearButton.setText("iconButton1");
        clearButton.setEnabled(false);
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        menuFile.setText("File");

        menuItemSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        menuItemSave.setText("save");
        menuItemSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemSaveActionPerformed(evt);
            }
        });
        menuFile.add(menuItemSave);

        jMenuBar2.add(menuFile);

        menuEdit.setText("Edit");
        jMenuBar2.add(menuEdit);

        menuHelp.setText("Help");
        jMenuBar2.add(menuHelp);

        setJMenuBar(jMenuBar2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(listHeadingPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)))
                        .addGap(15, 15, 15))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void searchTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchTextFieldActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        addBlankEmployee();
    }//GEN-LAST:event_addButtonActionPerformed

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
    	EmployeeInfo removedEmployee = employeeList.getSelectedValue();
    	if(removedEmployee != null){
    		table.removeEmployee(removedEmployee.getEmployeeNumber());
        	updateDisplayTable();
        	clearEmployeeInfo();
    	}
    }//GEN-LAST:event_removeButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        if(employeeList.getSelectedValue()!=null){
        	editEmployee(employeeList.getSelectedValue().getEmployeeNumber());
        }
    }//GEN-LAST:event_editButtonActionPerformed

    private void doneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doneButtonActionPerformed
    	//Code for creating an or replacing an employee
    	try{
    		EmployeeInfo newEmployee;
	    	if(fullTimeRadioButton.isSelected()){
	    		newEmployee = new FullTimeEmployee(Integer.valueOf(empInfoEmpnum.getText()),
	    				empInfoFirstName.getText(),empInfoLastName.getText(),(Gender)empInfoComboBoxGender.getSelectedItem(),
	    				(Location)empInfoComboBoxLocation.getSelectedItem(),Double.valueOf(empInfoDeductionRate.getText()),
	    				Double.valueOf(fullTimeSalaryTextField.getText()));
	    		if(isEditing){
	    			table.removeEmployee(employeeList.getSelectedValue().getEmployeeNumber());
	    		}
	    		table.addEmployee(newEmployee);
	    	} else {
	    		if (partTimeRadioButton.isSelected()){
	    			newEmployee = new PartTimeEmployee(Integer.valueOf(empInfoEmpnum.getText()),
	        				empInfoFirstName.getText(),empInfoLastName.getText(),(Gender)empInfoComboBoxGender.getSelectedItem(),
	        				(Location)empInfoComboBoxLocation.getSelectedItem(),Double.valueOf(empInfoDeductionRate.getText()),
	        				Double.valueOf(partTimeHourlyWageTextField.getText()),Double.valueOf(partTimeHoursWorkedTextField.getText()),
	        				Double.valueOf(partTimeWeeksWorkedTextField.getText()));
		    		if(isEditing){
		    			table.removeEmployee(employeeList.getSelectedValue().getEmployeeNumber());
		    		}
	        		table.addEmployee(newEmployee);
	    		}
	    	} 
	    	clearEmployeeInfo();
            updateDisplayTable();
            setEditableEmployeeInfoPanel(false);
    	}catch(NumberFormatException e){
	    	//Create error message
	    }
    }//GEN-LAST:event_doneButtonActionPerformed

    private void fullTimeRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fullTimeRadioButtonActionPerformed
        //TODO catch the event
        selectWagePanel();
    }//GEN-LAST:event_fullTimeRadioButtonActionPerformed

    private void partTimeRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_partTimeRadioButtonActionPerformed
        //TODO catch the event
        selectWagePanel();
    }//GEN-LAST:event_partTimeRadioButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        saveTable();
    }//GEN-LAST:event_saveButtonActionPerformed

    private void menuItemSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemSaveActionPerformed
        saveTable();
    }//GEN-LAST:event_menuItemSaveActionPerformed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clearButtonActionPerformed

    /**
     * @param args the command line arguments
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
    private javax.swing.JLabel empnumLabel;
    private javax.swing.ButtonGroup fullPartTimeButtonGroup;
    private javax.swing.JLabel fullTimeAnnualSalaryLabel;
    private javax.swing.JLabel fullTimeIncomeLabel;
    private javax.swing.JTextField fullTimeIncomeTextField;
    private javax.swing.JRadioButton fullTimeRadioButton;
    private javax.swing.JTextField fullTimeSalaryTextField;
    private javax.swing.JPanel fullTimeWagePanel;
    private javax.swing.JLabel genderLabel;
    private javax.swing.JLabel genderLabel1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelEmployeeNumber;
    private javax.swing.JLabel labelFirstName;
    private javax.swing.JLabel labelLastName;
    private javax.swing.JLabel lastNameEmpInfoLabel;
    private javax.swing.JPanel listHeadingPanel;
    private javax.swing.JMenu menuEdit;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenu menuHelp;
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
