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

public class ReportController implements Initializable, FactoryInterface {
    @FXML
    private Button generate;

    @FXML
    private Label resultMessage;

    private MongoCollection revenues() {
        return MongoConnection.getDatabase().getCollection("revenues");
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        generate.setOnMouseClicked(this::generatePDF);
    }

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

    @Override
    public void setFactoryController(FactoryController controller) {}
}
