package com.codingfactory.restaurant.controllers;


import com.codingfactory.restaurant.interfaces.ControllerInterface;
import com.codingfactory.restaurant.models.Employee;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class FormControllerEditEmployee implements Initializable, ControllerInterface<EmployeeController> {
    @FXML
    private TextField employeeName;

    @FXML
    private TextField employeeJob;

    @FXML
    private TextField employeeWorkedHours;

    @FXML
    private Button editEmployeeButton;

    @FXML
    private Text errorMessage;

    @FXML
    private CheckBox warningCheckbox;

    @FXML
    private Button fireEmployeeButton;

    private EmployeeController employeeController;

    @Override
    public void setParentController(EmployeeController controller) {
        this.employeeController = controller;
        fillFormData();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        editEmployeeButton.setOnMouseClicked(this::editEmployee);
        warningCheckbox.setOnMouseClicked(this::toggleFireButton);
        fireEmployeeButton.setOnMouseClicked(this::fireEmployee);
    }

    private void fillFormData() {
        Employee editEmployee = employeeController.getCurrentEmployeeDetails();
        employeeName.setText(editEmployee.getName());
        employeeJob.setText(editEmployee.getJob());
        employeeWorkedHours.setText(editEmployee.getWorkedHours().toString());
    }

    private void toggleFireButton(MouseEvent e) {
        if(warningCheckbox.isSelected()) {
            fireEmployeeButton.setDisable(false);
        } else fireEmployeeButton.setDisable(true);
    }

    private void fireEmployee(MouseEvent e) {
        if(fireEmployeeButton.isDisable()) return;
        employeeController.getCurrentEmployeeDetails().setStatus("PREVIOUS");
        editEmployee(e);
    }

    private void editEmployee(MouseEvent e) {
        String name = employeeName.getText();
        String job = employeeJob.getText();
        String strWorkedHours = employeeWorkedHours.getText();

        if(name.isEmpty() || job.isEmpty() || strWorkedHours.isEmpty()) {
            errorMessage.setText("All fields are required");
            errorMessage.setVisible(true);
            return;
        }

        Integer workedHours = Integer.valueOf(strWorkedHours);
        Employee current = employeeController.getCurrentEmployeeDetails();
        Employee employee = new Employee(current.getId(), name, job, workedHours, current.getStatus());
        employeeController.editEmployee(employee);
        employeeController.closeModal();
    }
}