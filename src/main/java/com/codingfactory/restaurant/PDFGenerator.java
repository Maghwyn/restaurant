package com.codingfactory.restaurant;

import com.codingfactory.restaurant.models.Report;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class PDFGenerator provides a method to generate a PDF based on .ftl template.
 * The data is filled into a template .ftl, parsed then converted into a W3C document xml, and finally rendered as a PDF.
 */
public class PDFGenerator {

    /**
     * Method loadTemplate load the .ftl template and returns a manipulable instance of it.
     * @return Template | null
     */
    private Template loadTemplate() {
        try {
            Configuration configuration = new Configuration(new Version(2, 3, 32));
            File templateFile = new File(PDFGenerator.class.getResource("template/template.ftl").toURI());
            configuration.setDirectoryForTemplateLoading(templateFile.getParentFile());
            Template template = configuration.getTemplate(templateFile.getName());
            return template;
        } catch(IOException | URISyntaxException err) {
            err.printStackTrace();
            return null;
        }
    }

    /**
     * Method fillTemplateData read the .ftl template and inject the necessary data where defined.
     * It then parse the html string to a Document, and convert it to an xml Document.
     * @return Document | null
     */
    private Document fillTemplateData(Template template, String dateNow, String timeNow, List<Report> reports) {
        try (Writer writer = new StringWriter()) {
            Map<String, Object> data = new HashMap<>();
            data.put("date", dateNow);
            data.put("time", timeNow);

            // Good old for would be better there.
            AtomicInteger idx = new AtomicInteger();
            reports.stream()
                    .forEachOrdered(report -> {
                        Integer index = idx.getAndIncrement();
                        data.put("report" + index + "_title", report.getCreatedAt());
                        data.put("report" + index + "_cap", report.getCapital());
                        data.put("report" + index + "_exp", report.getExpenditure());
                    });

            // If the values aren't set, it crashes.
            Integer index = idx.getAndIncrement();
            for(int i = index; i < 3; i++) {
                data.put("report" + i + "_title", "N/A");
                data.put("report" + i + "_cap", "N/A");
                data.put("report" + i + "_exp", "N/A");
            }

            template.process(data, writer);
            Document document = Jsoup.parse(writer.toString(), "UTF-8");
            document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
            return document;
        } catch(TemplateException | IOException err) {
            err.printStackTrace();
            return null;
        }
    }

    /**
     * Method buildPDF read the xml Document, render the PDF and output it in a Desktop Dir.
     * @return boolean The result, if it failed or succeeded.
     */
    private Boolean buildPDF(Document document, String pathOutput) {
        try (OutputStream outputStream = new FileOutputStream(pathOutput)) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withUri(pathOutput);
            builder.toStream(outputStream);
            builder.withW3cDocument(new W3CDom().fromJsoup(document), "/");
            builder.run();
            return true;
        } catch(IOException err) {
            err.printStackTrace();
            return false;
        }
    }

    /**
     * Method build is the core generator of the PDFGenerator Class.
     * It generates the output file path, load the pdf Document. Inject and parse.
     * Retrieve the xml Document and build the xml Document into a redable PDF.
     * @param reports The 3 latest report model.
     * @return boolean
     */
    public Boolean build(List<Report> reports) {
        PDFGeneratorFilePath file = new PDFGeneratorFilePath();
        file.generatePath();

        Template template = loadTemplate();
        Document document = fillTemplateData(template, file.getDateNow(), file.getTimeNow(), reports);
        if(document == null) return false;
        Boolean res = buildPDF(document, file.getPathOutput());
        return res;
    }

    /**
     * Private Class PDFGeneratorFilePath has the sole purpose
     * of generating the output pathname and filename.
     */
    private static class PDFGeneratorFilePath {
        private String dateNow;
        private String timeNow;
        private String pathOutput;

        public void generatePath() {
            LocalDate dateNow = LocalDate.now();
            LocalTime timeNow = LocalTime.now();
            DateTimeFormatter pattern = DateTimeFormatter.ofPattern("HH-mm");
            String timeFormat = pattern.format(timeNow);

            String userHome = System.getProperty("user.home");
            String docOutput = "/Desktop/el-ristorante-reports";
            String fileName = "report_" + dateNow + "_" + timeFormat + ".pdf";

            String pathDocOutput = userHome + docOutput;
            try {
                Files.createDirectories(Paths.get(pathDocOutput));
            } catch(IOException err) {
                err.printStackTrace();
            }

            String pathOutput = pathDocOutput + "/" + fileName;

            this.dateNow = dateNow.toString();
            this.timeNow = timeFormat;
            this.pathOutput = pathOutput;
        }

        public String getDateNow() {
            return dateNow;
        }

        public String getTimeNow() {
            return timeNow;
        }

        public String getPathOutput() {
            return pathOutput;
        }
    }
}