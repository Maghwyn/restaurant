package com.codingfactory.restaurant.controllers;

import com.codingfactory.restaurant.MongoConnection;
import com.codingfactory.restaurant.interfaces.FactoryInterface;
import com.codingfactory.restaurant.models.Employee;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.UpdateResult;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import org.bson.Document;
import org.bson.types.ObjectId;

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

    private Employee currentEmployeeDetails = null;

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

        currentEmployeeTableView.setRowFactory(tv -> {
            TableRow row = new TableRow();
            row.setOnMouseClicked(this::openEditEmployee);
            return row;
        });

        previousEmployeeTableView.setRowFactory(tv -> {
            TableRow row = new TableRow();
            row.setOnMouseClicked(this::openDeleteModal);
            return row;
        });

        getFilteredEmployee();
        createTableRows();
    }

    private void switchTab(Event e) {
        Tab tab = (Tab) e.getSource();
        EmployeeTarget target = (EmployeeTarget) tab.getProperties().get("target");

        if(target != previousFilter) {
            previousFilter = target;
            filterBy = target;
            getFilteredEmployee();
            createTableRows();
        }
    }

    private void openNewEmployeeModal(MouseEvent e) {
        this.factoryController.openModal("views/formNewEmployee.fxml", this);
    }

    private void openEditEmployee(MouseEvent e) {
        TableRow row = (TableRow) e.getSource();
        currentEmployeeDetails = (Employee) row.getItem();

        this.factoryController.openModal("views/formEditEmployee.fxml", this);
    }

    private void openDeleteModal(MouseEvent e) {
        TableRow row = (TableRow) e.getSource();
        currentEmployeeDetails = (Employee) row.getItem();

        this.factoryController.openModal("views/alertDeleteEmployee.fxml", this);
    }

    public void closeModal() {
        this.factoryController.forceCloseModal();
    }

    public void createNewEmployee(Employee employee) {
        ObjectId id = new ObjectId();

        Document newEmployee = new Document("name", employee.getName())
                .append("job", employee.getJob())
                .append("workedHours", employee.getWorkedHours())
                .append("status", employee.getStatus());
        newEmployee.put("_id", id);
        employees().insertOne(newEmployee);
        employee.setId(id);

        if(filterBy == EmployeeTarget.CURRENT) {
            employeesList.add(employee);
            currentEmployeeTableView.refresh();
        }
    }

    public void editEmployee(Employee employee) {
        if(employee.getStatus() == "PREVIOUS") {
            employeesList.remove(currentEmployeeDetails);
        } else {
            int index = currentEmployeeTableView.getSelectionModel().selectedIndexProperty().get();
            employeesList.set(index, employee);
        }
        currentEmployeeTableView.refresh();

        Document editEmployee = new Document("name", employee.getName())
                .append("job", employee.getJob())
                .append("workedHours", employee.getWorkedHours())
                .append("status", employee.getStatus());
        employees().updateOne(new Document("_id", employee.getId()), new Document("$set", editEmployee));
    }

    public void deleteEmployee() {
        employeesList.remove(currentEmployeeDetails);
        previousEmployeeTableView.refresh();

        employees().deleteOne(new Document("_id", currentEmployeeDetails.getId()));
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
        if(employeesList != null) {
            attend = new ArrayList();
            employeesList.removeAll();
        }

        MongoCursor<Document> cursor = employees().find(new Document("status", filterBy.toString())).iterator();
        try {
            while (cursor.hasNext()) {
                Document employee = cursor.next();
                ObjectId id = employee.getObjectId("_id");
                String name = employee.getString("name");
                String job = employee.getString("job");
                String status = employee.getString("status");
                Integer workedHours = employee.getInteger("workedHours");
                attend.add(new Employee(id, name, job, workedHours, status));
            }
            employeesList = FXCollections.observableArrayList(attend);
            return employeesList;
        } finally {
            cursor.close();
        }
    }

    public Employee getCurrentEmployeeDetails() {
        return currentEmployeeDetails;
    }
}
