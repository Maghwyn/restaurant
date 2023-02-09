package com.codingfactory.restaurant.controllers;

import com.codingfactory.restaurant.MongoConnection;
import com.codingfactory.restaurant.interfaces.ControllerInterface;
import com.codingfactory.restaurant.interfaces.FactoryInterface;
import com.codingfactory.restaurant.models.Employee;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import org.bson.Document;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

enum EmployeeTarget {
    CURRENT,
    PREVIOUS
}

public class EmployeeController implements Initializable, FactoryInterface {
    @FXML
    private Button newEmployeeButton;

    @FXML
    private Tab currentEmployeeTab;

    @FXML
    private Tab previousEmployeeTab;

    @FXML
    private TableView currentEmployeeTableView;

    @FXML
    private TableView previousEmployeeTableView;

    private FactoryController factoryController;

    private EmployeeTarget previousFilter = EmployeeTarget.CURRENT;
    private EmployeeTarget filterBy = EmployeeTarget.CURRENT;

    public List attend = new ArrayList();
    public ObservableList<Employee> employeesList;

    private MongoCollection employees() {
        return MongoConnection.getDatabase().getCollection("employees");
    }

    @Override
    public void setFactoryController(FactoryController controller) {
        this.factoryController = controller;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentEmployeeTab.getProperties().put("target", EmployeeTarget.CURRENT);
        previousEmployeeTab.getProperties().put("target", EmployeeTarget.PREVIOUS);

        newEmployeeButton.setOnMouseClicked(this::openNewEmployeeModal);
        currentEmployeeTab.setOnSelectionChanged(this::switchTab);
        previousEmployeeTab.setOnSelectionChanged(this::switchTab);

        getFilteredEmployee();
        createTableRows();
    }

    private void switchTab(Event e) {
        Tab tab = (Tab) e.getSource();
        EmployeeTarget target = (EmployeeTarget) tab.getProperties().get("target");

        if(target != previousFilter) {
            previousFilter = target;
            filterBy = target;
        }
    }

    public void openNewEmployeeModal(MouseEvent e) {
        this.factoryController.openModal("views/formNewEmployee.fxml", this);
    }

    public void closeNewEmployeeModal() {
        this.factoryController.forceCloseModal();
    }

    public void createNewEmployee(Employee employee) {
        if(filterBy == EmployeeTarget.CURRENT) {
            employeesList.add(employee);
            createTableRows();
        }
    }

    public void createTableRows() {
        if(employeesList.size() > 0) {
            if(filterBy == EmployeeTarget.CURRENT) {
                currentEmployeeTableView.setItems(employeesList);
                return;
            }

            if(filterBy == EmployeeTarget.PREVIOUS) {
                previousEmployeeTableView.setItems(employeesList);
            }
        }
    }

    public ObservableList getFilteredEmployee() {
        MongoCursor<Document> cursor = employees().find(new Document("status", filterBy.toString())).iterator();
        try {
            while (cursor.hasNext()) {
                Document employee = cursor.next();
                String name = employee.getString("name");
                String job = employee.getString("job");
                String status = employee.getString("status");
                attend.add(new Employee(name, job, status));
            }
            employeesList = FXCollections.observableArrayList(attend);
            return employeesList;
        } finally {
            cursor.close();
        }
    }
}
