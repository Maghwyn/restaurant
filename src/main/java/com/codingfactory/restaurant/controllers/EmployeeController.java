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

/**
 * Enum EmployeeTarget define the tabulation navigation on the TableView.
 * Excel like.
 */
enum EmployeeTarget {
    CURRENT,
    PREVIOUS
}


/**
 * Class EmployeeController is a controller that manage the TableView of the employee.
 * Its goal is to display, switch between the tabs and trigger multiple modal form and alerts.
 * It implements the Initializable and FactoryInterface interfaces.
 * FactoryInterface is used to set the layout parent controller.
 * Initializable is used to initialize the components of the dialog.
 */
public class EmployeeController implements Initializable, FactoryInterface {
    @FXML
    private Button newEmployeeButton;

    @FXML
    private Tab currentEmployeeTab;

    @FXML
    private Tab previousEmployeeTab;

    @FXML
    /**
     * Define the current working employee of the company.
     */
    private TableView currentEmployeeTableView;

    @FXML
    /**
     * Define the previous working employee of the company.
     * They are still in the database.
     */
    private TableView previousEmployeeTableView;

    /**
     * The layout FactoryController that manage all others controllers.
     */
    private FactoryController factoryController;

    /**
     * Property used to store the EmployeeDetails when opening a modal.
     */
    private Employee currentEmployeeDetails = null;

    /**
     * Define the filters.
     * @deprecated previousFilter (it should be reworked)
     */
    private EmployeeTarget previousFilter = EmployeeTarget.CURRENT;
    private EmployeeTarget filterBy = EmployeeTarget.CURRENT;

    public List attend = new ArrayList();
    public ObservableList<Employee> employeesList;

    /**
     * @return The mongodb collection employees.
     */
    private MongoCollection employees() {
        return MongoConnection.getDatabase().getCollection("employees");
    }

    @Override
    /**
     * Set the factoryController to the router class, so we can have access to its public functions.
     * @param controller The parent of the Class DrawerController.
     */
    public void setFactoryController(FactoryController controller) {
        this.factoryController = controller;
    }

    @Override
    /**
     * Method initialize, initializes the employee TableView on load.
     * Set the table custom property to identify which one was clicked on a generic function.
     * Set the buttons events to switch tabs and open the modal.
     * Define a row factory that will automatically set an event on the row when its created.
     * Initialize the TableView data.
     * @param url URL to initialize the components.
     * @param resourceBundle ResourceBundle to initialize the components.
     */
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

    /**
     * Method switchTab will set the filters and switch between the new and previous employee.
     * @param e Base Class Event.
     */
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

    /**
     * Open the formNewEmployee and happen it later to the modal child.
     * This modal will show a form that will let you create an employee.
     * @param e MouseEvent that triggered the method.
     */
    private void openNewEmployeeModal(MouseEvent e) {
        this.factoryController.openModal("views/formNewEmployee.fxml", this);
    }

    /**
     * Open the openEditEmployee and happen it later to the modal child.
     * This modal will show a form that will let you edit or fire an employee.
     * @param e MouseEvent that triggered the method.
     */
    private void openEditEmployee(MouseEvent e) {
        TableRow row = (TableRow) e.getSource();
        currentEmployeeDetails = (Employee) row.getItem();

        this.factoryController.openModal("views/formEditEmployee.fxml", this);
    }

    /**
     * Open the openDeleteModal and happen it later to the modal child.
     * This modal will show a warning alert that will let you definitively delete a previous employee.
     * @param e MouseEvent that triggered the method.
     */
    private void openDeleteModal(MouseEvent e) {
        TableRow row = (TableRow) e.getSource();
        currentEmployeeDetails = (Employee) row.getItem();

        this.factoryController.openModal("views/alertDeleteEmployee.fxml", this);
    }

    /**
     * Effectively close the modal by calling the FactoryController forceCloseModal method.
     */
    public void closeModal() {
        this.factoryController.forceCloseModal();
    }

    /**
     * Method createNewEmployee will create a newly employee.
     * We set the ObjectId manually for reactivity purpose.
     * @param employee Employee model
     */
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

    /**
     * Method editEmployee is generic and will accept and edit information or an edit that would fire the employee.
     * This effectively refresh the TableView in both case.
     * @param employee Employee model
     */
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

    /**
     * Method deleteEmployee will remove the employee from the observableList, refresh the TableView.
     * Then remove the employee from the database.
     */
    public void deleteEmployee() {
        employeesList.remove(currentEmployeeDetails);
        previousEmployeeTableView.refresh();

        employees().deleteOne(new Document("_id", currentEmployeeDetails.getId()));
    }

    /**
     * Method createTableRows setting the items based on the current filter.
     * Can either be the new employee or the previous employee.
     */
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

    /**
     * Method getFilteredEmployee set the current employeesList depending on the status of the filter.
     * Each time this method is called, the previous data array are reset.
     * Can either be the new employee or the previous employee.
     */
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

    /**
     * @return Current Employee that will be used in the modal;
     */
    public Employee getCurrentEmployeeDetails() {
        return currentEmployeeDetails;
    }
}
