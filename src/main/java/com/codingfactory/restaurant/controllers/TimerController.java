package com.codingfactory.restaurant.controllers;

import com.codingfactory.restaurant.interfaces.FactoryInterface;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;


public class TimerController implements Initializable, FactoryInterface {
    @FXML
    private Label timer;

    private FactoryController factoryController;

    private Thread thread;

    int minutes = 25;
    int threshold = 15;
    int timeout = 0;
    int totalSeconds = minutes * 60;
    int totalSecondsThreshold = threshold * 60;

    boolean isActive = false;

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

    public void interruptTimer() {
        if(thread != null && isActive) {
            thread.interrupt();
            isActive = false;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startTimer();
    }

    @Override
    public void setFactoryController(FactoryController controller) {
        this.factoryController = controller;
    }
}
