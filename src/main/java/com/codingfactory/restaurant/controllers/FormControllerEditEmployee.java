package com.codingfactory.restaurant.controllers;


import com.codingfactory.restaurant.interfaces.ControllerInterface;
import com.codingfactory.restaurant.models.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class FormControllerEditEmployee implements Initializable, ControllerInterface<EmployeeController> {
    @FXML
    private TextField employeeName;

    @FXML
    private ComboBox employeeJob;

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

        ObservableList<String> jobsList = FXCollections.observableArrayList();
        jobsList.add("plongeur");
        jobsList.add("chef");
        jobsList.add("sous-chef");
        jobsList.add("serveur");
        jobsList.add("bar/tabac");
        jobsList.add("technicien");
        jobsList.add("comptable");

        employeeJob.setItems(jobsList);
    }

    /**
     * Method to set values on fields from an employee
     */
    private void fillFormData() {
        Employee editEmployee = employeeController.getCurrentEmployeeDetails();
        employeeName.setText(editEmployee.getName());
        employeeJob.setValue(editEmployee.getJob());
        employeeWorkedHours.setText(editEmployee.getWorkedHours().toString());
    }

    /**
     * Method to toggle if we want to fire an employee
     * @param e {@link MouseEvent}
     */
    private void toggleFireButton(MouseEvent e) {
        if(warningCheckbox.isSelected()) {
            fireEmployeeButton.setDisable(false);
        } else fireEmployeeButton.setDisable(true);
    }

    /**
     * Method to fire an employee and put it in the Previous employee state
     * @param e {@link MouseEvent}
     */
    private void fireEmployee(MouseEvent e) {
        if(fireEmployeeButton.isDisable()) return;
        employeeController.getCurrentEmployeeDetails().setStatus("PREVIOUS");
        editEmployee(e);
    }

    /**
     * Method to update the employee we want to edit and check if fields are not empty
     * @param e {@link MouseEvent}
     */
    private void editEmployee(MouseEvent e) {
        String name = employeeName.getText();
        String job = (String) employeeJob.getValue();
        String strWorkedHours = employeeWorkedHours.getText();

        if(name.isEmpty() || job.isEmpty() || strWorkedHours.isEmpty()) {
            errorMessage.setText("Tous les champs sont requis");
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
