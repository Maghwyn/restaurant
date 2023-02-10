package com.codingfactory.restaurant.controllers;

import com.codingfactory.restaurant.interfaces.ControllerInterface;
import com.codingfactory.restaurant.models.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class FormControllerNewEmployee implements Initializable, ControllerInterface<EmployeeController> {
    @FXML
    private TextField employeeName;

    @FXML
    private ComboBox employeeJob;

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

    private void createNewEmployee(MouseEvent e) {
        String name = employeeName.getText();
        String job = (String) employeeJob.getValue();

        if(name.isEmpty() || job.isEmpty()) {
            errorMessage.setText("Tous les champs sont requis");
            errorMessage.setVisible(true);
            return;
        }

        Employee employee = new Employee(null, name, job, 0, "CURRENT");
        employeeController.createNewEmployee(employee);
        employeeController.closeModal();
    }
}
