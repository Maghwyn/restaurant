package com.codingfactory.restaurant.controllers;

import com.codingfactory.restaurant.interfaces.FactoryInterface;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Class TimerController is a controller class with a reference to the layout FactoryController.
 * It is responsible for displaying the timer for the application.
 * It implements the Initializable and FactoryInterface interfaces.
 * FactoryInterface is used to set the layout parent controller.
 * Initializable is used to initialize the components of the dialog.
 */
public class TimerController implements Initializable, FactoryInterface {
    @FXML
    private Label timer;

    /**
     * The layout FactoryController that manage all others controllers.
     */
    private FactoryController factoryController;

    /**
     * The thread that will be used to track the timer.
     */
    private Thread thread;

    /**
     * Define the properties necessary to decrement the timer.
     * Minutes define the total amount of minutes the timer will last for.
     * Threshold define a value that will trigger some change in the factoryController once reached.
     * Timeout is the end of the timer.
     */
    int minutes = 25;
    int threshold = 15;
    int timeout = 0;
    int totalSeconds = minutes * 60;
    int totalSecondsThreshold = threshold * 60;

    /**
     * A boolean flag to define if the timer thread is active or not.
     */
    boolean isActive = false;

    /**
     * The methods create a thread that will be stored on the private thread property of the Class.
     * It also define an event when the threshold is reached, the layout flag that define if
     * the admin can still take order will be set to false.
     * Each second, we decrease the timer by 1 second and set a label to match the format of the
     * timer visually.
     */
    public void startTimer() {
        if (!isActive) {
            thread = new Thread(() -> {
                while (totalSeconds >= timeout) {
                    try {
                        if(factoryController != null && factoryController.canTakeOrder && totalSeconds < totalSecondsThreshold) {
                            factoryController.canTakeOrder = false;
                        }

                        Platform.runLater(() -> {
                            timer.setText(String.format("%d:%02d", totalSeconds / 60, totalSeconds % 60));
                        });
                        Thread.sleep(1000);
                        totalSeconds--;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();
            isActive = true;
        }
    }

    /**
     * Interrupt the timer gracefully.
     */
    public void interruptTimer() {
        if(thread != null && isActive) {
            thread.interrupt();
            isActive = false;
        }
    }

    @Override
    /**
     * Method initialize, initializes the timer component on load.
     * @param url URL to initialize the components.
     * @param resourceBundle ResourceBundle to initialize the components.
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startTimer();
    }


    @Override
    /**
     * Set the factoryController to the timer class, so we can have access to its public functions.
     * @param controller: the parent of the Class TimerController.
     */
    public void setFactoryController(FactoryController controller) {
        this.factoryController = controller;
    }
}
