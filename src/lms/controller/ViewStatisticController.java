/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lms.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lms.DbConnection;
import lms.MyDialog;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * FXML Controller class
 *
 * @author SRUN VANNARA
 */
public class ViewStatisticController implements Initializable {

    @FXML
    private ComboBox<String> cboType;

    private Connection conn = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    @FXML
    private VBox paneStatistic;
    @FXML
    private Button paneExport2;
    @FXML
    private Button paneExport1;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cboType.getItems().addAll("ចំនួនអ្នកចុះឈ្មោះតាមភេទ", "ចំនួនសៀវភៅតាមប្រភេទ", "ចំនួនសៀវភៅដែលកំពុងខ្ចី");
        conn = DbConnection.connect();
        cboType.getSelectionModel().selectFirst();
        loadMember();
        typeChange();
        paneStatistic.setPrefHeight(300);
        paneStatistic.setStyle("-fx-background-color:#b8b09b;-fx-background-radius:10");
        paneExport1.setStyle("-fx-background-color:#e3c9c9;-fx-border-color:none; -fx-border-radius:10");
        paneExport2.setStyle("-fx-background-color:#a8e6cf;-fx-border-color:none ; -fx-border-radius:10");
    }

    private void loadMember() {
        String sql = "SELECT gender, COUNT(id) FROM tb_member GROUP BY gender";
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                pieChartData.add(new PieChart.Data(rs.getString(1) + "សរុប " + rs.getInt(2) + "នាក់", rs.getInt(2)));
            }
            PieChart chart = new PieChart(pieChartData);
            chart.setLegendSide(Side.BOTTOM);
            chart.setLabelsVisible(false);
            paneStatistic.getChildren().setAll(chart);
        } catch (SQLException ex) {
            Logger.getLogger(AddBookController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void typeChange() {
        cboType.setOnAction(e -> {
            String choise = cboType.getSelectionModel().getSelectedItem();
            if (choise.equals("ចំនួនអ្នកចុះឈ្មោះតាមភេទ")) {
                paneStatistic.setPrefHeight(290);
                paneStatistic.setStyle("-fx-background-color:#ff6f69;-fx-background-radius:10");
                paneExport1.setStyle("-fx-background-color:#ffeead;-fx-border-color:none; -fx-border-radius:10");
                paneExport2.setStyle("-fx-background-color: #96ceb4;-fx-border-color:none; -fx-border-radius:10");
                loadMember();
            } else if (choise.equals("ចំនួនសៀវភៅតាមប្រភេទ")) {
                paneStatistic.setPrefHeight(Double.MIN_VALUE);
                paneStatistic.setStyle("-fx-background-color:#88d8b0;-fx-background-radius:10");
                paneExport1.setStyle("-fx-background-color:#f6cd61;-fx-border-color:none; -fx-border-radius:10");
                paneExport2.setStyle("-fx-background-color:#e7d3d3;-fx-border-color:none; -fx-border-radius:10");
                loadBookByCategory();
            } else {
                paneStatistic.setPrefHeight(300);
                paneStatistic.setStyle("-fx-background-color:#84c1ff; -fx-background-radius:10");
                paneExport1.setStyle("-fx-background-color:#83d0c9;-fx-border-color:none; -fx-border-radius:10");
                paneExport2.setStyle("-fx-background-color: #feb2a8;-fx-border-color:none; -fx-border-radius:10");
                loadIssuedBook();
            }
        });
    }

    private void loadBookByCategory() {
        paneStatistic.getChildren().clear();
        String sql = "SELECT category, SUM(num_book) FROM tb_book GROUP BY category";
        int n = 0;
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                paneStatistic.getChildren().add(new Label(rs.getString(1) + " មាន" + rs.getInt(2) + "ក្បាល"));
                n = n + rs.getInt(2);
            }
            paneStatistic.getChildren().add(new Label("សរុប " + n + "ក្បាល"));
        } catch (SQLException ex) {
            Logger.getLogger(AddBookController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void loadIssuedBook() {
        String sql = "SELECT COUNT(*), (SELECT COUNT(b_id) FROM tb_issue WHERE CURRENT_DATE-issue_date<14) FROM tb_issue ";
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            int n = 0;
            while (rs.next()) {
                n = rs.getInt(1) - rs.getInt(2);
                pieChartData.add(new PieChart.Data("សៀវភៅដែលខ្ចីលើស14ថ្ងៃមានចំនួន " + n + "ក្បាល", n));
                pieChartData.add(new PieChart.Data("សៀវភៅដែលខ្ចីមិនលើស14ថ្ងៃមានចំនួន " + rs.getInt(2) + "ក្បាល", rs.getInt(2)));
            }
            PieChart chart = new PieChart(pieChartData);
            chart.setLegendSide(Side.BOTTOM);
            chart.setLabelsVisible(false);
            paneStatistic.getChildren().setAll(chart);
        } catch (SQLException ex) {
            Logger.getLogger(AddBookController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void clickExportMember(MouseEvent event) throws IOException {

        FileChooser fileChooser = new FileChooser();
        File file = null;
        fileChooser.setTitle("ជ្រើសរើសទីតាំង និងបំពេញឈ្មោះឯកសារ");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel File", "*.xls")
        );

        Stage stage = (Stage) MainController.stackPane.getScene().getWindow();
        file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            loadAllMember(file);
            System.out.println(file);
        }

    }

    private void loadAllMember(File file) throws IOException {
        String sql = "SELECT * FROM tb_member ORDER BY name ";
        HSSFWorkbook workBook = new HSSFWorkbook();
        HSSFSheet sheet = workBook.createSheet("អ្នកចុះឈ្មោះទាំងអស់");

        int n = 0;
        HSSFRow row = sheet.createRow(n);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("លេខសម្គាល់");
        cell = row.createCell(1);
        cell.setCellValue("ឈ្មោះ");
        cell = row.createCell(2);
        cell.setCellValue("ជាអក្សរឡាតាំង");
        cell = row.createCell(3);
        cell.setCellValue("ភេទ");
        cell = row.createCell(4);
        cell.setCellValue("ថ្ងៃខែឆ្នាំកំណើត");
        cell = row.createCell(5);
        cell.setCellValue("ភូមិ");
        cell = row.createCell(6);
        cell.setCellValue("ឃុំ");
        cell = row.createCell(7);
        cell.setCellValue("ស្រុក");
        cell = row.createCell(8);
        cell.setCellValue("ខេត្ត");
        cell = row.createCell(9);
        cell.setCellValue("លេខទូរស័ព្ទ");
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                n = n + 1;
                row = sheet.createRow(n);
                cell = row.createCell(0);
                cell.setCellValue(rs.getInt(1));
                cell = row.createCell(1);
                cell.setCellValue(rs.getString(2));
                cell = row.createCell(2);
                cell.setCellValue(rs.getString(3));
                cell = row.createCell(3);
                cell.setCellValue(rs.getString(4));
                cell = row.createCell(4);
                cell.setCellValue(rs.getString(5));
                cell = row.createCell(5);
                cell.setCellValue(rs.getString(6));
                cell = row.createCell(6);
                cell.setCellValue(rs.getString(7));
                cell = row.createCell(7);
                cell.setCellValue(rs.getString(8));
                cell = row.createCell(8);
                cell.setCellValue(rs.getString(9));
                cell = row.createCell(9);
                cell.setCellValue(rs.getString(10));
            }
            workBook.write(new FileOutputStream(file));
            new MyDialog().showInfoDialog("ការនាំចេញបានជោគជ័យ!", "សូមចូលទៅបើកនៅទីតាំង " + file.toString());

        } catch (SQLException ex) {
            Logger.getLogger(AddBookController.class.getName()).log(Level.SEVERE, null, ex);
            new MyDialog().showInfoDialog("មានបញ្ហា!", ex.getMessage());
        } finally {
            try {
                rs.close();
                pst.close();
                workBook.close();
            } catch (SQLException ex) {
                Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void clickExportBook(MouseEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        File file = null;
        fileChooser.setTitle("ជ្រើសរើសទីតាំង និងបំពេញឈ្មោះឯកសារ");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel File", "*.xls")
        );

        Stage stage = (Stage) MainController.stackPane.getScene().getWindow();
        file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            loadAllBook(file);
            System.out.println(file);
        }
    }

    private void loadAllBook(File file) throws IOException {
        String sql = "SELECT * FROM tb_book ORDER BY title ";
        HSSFWorkbook workBook = new HSSFWorkbook();
        HSSFSheet sheet = workBook.createSheet("សៀវភៅទាំងអស់");

        int n = 0;
        HSSFRow row = sheet.createRow(n);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("កូដ/ISBN");
        cell = row.createCell(1);
        cell.setCellValue("ចំណងជើង");
        cell = row.createCell(2);
        cell.setCellValue("ចំណងជើងរង");
        cell = row.createCell(3);
        cell.setCellValue("ប្រភេទ");
        cell = row.createCell(4);
        cell.setCellValue("អ្នកនិពន្ធ");
        cell = row.createCell(5);
        cell.setCellValue("ឆ្នាំបោះពុម្ព");
        cell = row.createCell(6);
        cell.setCellValue("ចំនួន");
        cell = row.createCell(7);
        cell.setCellValue("ផ្សេងៗ");
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                n = n + 1;
                row = sheet.createRow(n);
                cell = row.createCell(0);
                cell.setCellValue(rs.getString(1));
                cell = row.createCell(1);
                cell.setCellValue(rs.getString(2));
                cell = row.createCell(2);
                cell.setCellValue(rs.getString(3));
                cell = row.createCell(3);
                cell.setCellValue(rs.getString(4));
                cell = row.createCell(4);
                cell.setCellValue(rs.getString(5));
                cell = row.createCell(5);
                cell.setCellValue(rs.getString(6));
                cell = row.createCell(6);
                cell.setCellValue(rs.getString(7));
                cell = row.createCell(7);
                cell.setCellValue(rs.getString(8));
            }
            workBook.write(new FileOutputStream(file));
            new MyDialog().showInfoDialog("ការនាំចេញបានជោគជ័យ!", "សូមចូលទៅបើកនៅទីតាំង " + file.toString());

        } catch (SQLException ex) {
            Logger.getLogger(AddBookController.class.getName()).log(Level.SEVERE, null, ex);
            new MyDialog().showInfoDialog("មានបញ្ហា!", ex.getMessage());
        } finally {
            try {
                rs.close();
                pst.close();
                workBook.close();
            } catch (SQLException ex) {
                Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
