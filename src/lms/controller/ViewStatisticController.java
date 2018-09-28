/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lms.controller;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import lms.DbConnection;

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
    private Region paneExport;

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
            chart.setLegendSide(Side.LEFT);
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
                paneStatistic.setStyle("-fx-background-color:#b8b09b;-fx-background-radius:10");
                paneExport.setStyle("-fx-background-color:#b8b09b;-fx-background-radius:10");
                loadMember();
            } else if (choise.equals("ចំនួនសៀវភៅតាមប្រភេទ")) {
                paneStatistic.setStyle("-fx-background-color:#4e6376;-fx-background-radius:10");
                paneExport.setStyle("-fx-background-color:#4e6376;-fx-background-radius:10");
                loadBookByCategory();
            } else {
                paneStatistic.setStyle("-fx-background-color:#84888b;-fx-background-radius:10");
                paneExport.setStyle("-fx-background-color:#84888b;-fx-background-radius:10");
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
                pieChartData.add(new PieChart.Data("ចំនួនសៀវភៅដែលបានខ្ចីលើស14ថ្ងៃមាន " + n + "ក្បាល", n));
                pieChartData.add(new PieChart.Data("ចំនួនសៀវភៅដែលបានខ្ចីមិនលើស14ថ្ងៃមាន " + rs.getInt(2) + "ក្បាល", rs.getInt(2)));
            }
            PieChart chart = new PieChart(pieChartData);
            chart.setLegendSide(Side.RIGHT);
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
    private void clickExportMember(MouseEvent event) {
        String sql = "SELECT * FROM tb_member ";
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {

            }
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
    private void clickExportBook(MouseEvent event) {
    }

}
