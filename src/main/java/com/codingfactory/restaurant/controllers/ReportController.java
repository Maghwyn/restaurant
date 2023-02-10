package com.codingfactory.restaurant.controllers;

import com.codingfactory.restaurant.MongoConnection;
import com.codingfactory.restaurant.PDFGenerator;
import com.codingfactory.restaurant.interfaces.FactoryInterface;
import com.codingfactory.restaurant.models.Report;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Class ReportController is a controller class with a reference to the layout FactoryController.
 * It is responsible for displaying the report page defined in the DrawerController (the pdf generator).
 * It implements the Initializable interface.
 * Initializable is used to initialize the components of the dialog.
 */
public class ReportController implements Initializable {
    @FXML
    private Button generate;

    @FXML
    private Label resultMessage;

    /**
     * @return The mongodb collection revenues.
     */
    private MongoCollection revenues() {
        return MongoConnection.getDatabase().getCollection("revenues");
    }


    @Override
    /**
     * Method initialize, initializes the report page component on load.
     * It sets the event to the button so that it call the function that will trigger the generation of the PDF.
     * @param url URL to initialize the components.
     * @param resourceBundle ResourceBundle to initialize the components.
     */
    public void initialize(URL location, ResourceBundle resources) {
        generate.setOnMouseClicked(this::generatePDF);
    }

    /**
     * Method generatePDF will build the PDF and wait for a return boolean value.
     * This boolean value will be used to display a message to let the user know of a fail or a succeed.
     * Remove this message after 3 seconds.
     * @param e MouseEvent that triggered the method.
     */
    private void generatePDF(MouseEvent e) {
        List<Report> reports = getLastThreeReports();
        PDFGenerator generator = new PDFGenerator();
        Boolean res = generator.build(reports);

        if(res.equals(Boolean.TRUE)) {
            resultMessage.setText("Votre rapport a été généré !");
        } else resultMessage.setText("Une erreur est survenue pendant la génération du rapport.");
        resultMessage.setVisible(true);

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(3000);
                resultMessage.setVisible(false);
            } catch (InterruptedException err) {
                err.printStackTrace();
            }
        });

        thread.start();
    }

    /**
     * Method getLastThreeReports will retrieve all the reports from the mongodb revenues collection.
     * And finally sort them out based on their date of creation.
     * @return The 3 latest report model.
     */
    private List<Report> getLastThreeReports() {
        ArrayList<Report> arrayReport = new ArrayList();

        // In real situation, the limit and the filter would be defined before the iterator.
        MongoCursor<Document> cursor = revenues().find().iterator();
        try {
            while (cursor.hasNext()) {
                Document revenue = cursor.next();
                ObjectId id = revenue.getObjectId("_id");
                Integer capital = revenue.getInteger("capital");
                Integer expenditure = revenue.getInteger("expenditure");
                Boolean isOngoing = revenue.getBoolean("isOngoing");
                Date createdAt = revenue.getDate("createdAt");
                arrayReport.add(new Report(id, capital, expenditure, createdAt, isOngoing));
            }
        } finally {
            cursor.close();
        }

        return arrayReport.stream()
                .sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))
                .limit(3).toList();
    }
}
