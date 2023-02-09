package com.codingfactory.restaurant.controllers;

import com.codingfactory.restaurant.interfaces.ControllerInterface;
import com.codingfactory.restaurant.models.Employee;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeControllerForm implements Initializable, ControllerInterface<EmployeeController> {
    @FXML
    private TextField employeeName;

    @FXML
    private TextField employeeJob;

    @FXML
    private Button createEmployeeButton;

    @FXML
    private Text errorMessage;

    private EmployeeController employeeController;

    @Override
    public void setParentController(EmployeeController controller) {
        this.employeeController = controller;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createEmployeeButton.setOnMouseClicked(this::createNewEmployee);
    }

    private void createNewEmployee(MouseEvent e) {
        String name = employeeName.getText();
        String job = employeeJob.getText();

        if(name.isEmpty() || job.isEmpty()) {
            errorMessage.setText("All fields are required");
            return;
        }

        Employee employee = new Employee(name, job, "CURRENT");
        employeeController.createNewEmployee(employee);
        employeeController.closeNewEmployeeModal();
    }
}
