/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lms.controller;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import lms.DbConnection;
import lms.pojo.BookPojo;
import lms.pojo.MemberPojo;

/**
 * FXML Controller class
 *
 * @author SRUN VANNARA
 */
public class ReturnBookController implements Initializable {

    @FXML
    private TextField txtSearchMember;
    @FXML
    private TableColumn<MemberPojo, String> cMid;
    @FXML
    private TableColumn<MemberPojo, String> cName;
    @FXML
    private TableColumn<MemberPojo, String> cLatin;
    @FXML
    private TableColumn<MemberPojo, String> cGender;
    @FXML
    private TableColumn<MemberPojo, String> cPhone;

    @FXML
    private TableColumn<BookPojo, String> cBid;
    @FXML
    private TableColumn<BookPojo, String> cTitle;
    @FXML
    private TableColumn<BookPojo, String> cSub;
    @FXML
    private TableColumn<BookPojo, String> cNumDay;

    @FXML
    private Button btnReturn;
    @FXML
    private TableView<MemberPojo> tblMember;
    @FXML
    private TableView<BookPojo> tblBook;

    private Connection conn = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    @FXML
    private ComboBox<String> cboCatBorrow;



    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conn = DbConnection.connect();
        cboCatBorrow.getItems().addAll("មិនទាន់លើស14ថ្ងៃ", "លើស14ថ្ងៃ");
        cboCatBorrow.getSelectionModel().selectFirst();
        initTable();
        loadMember2();
        getSelectedRowData();
        typeChange();
    }    

    private void initTable() {
        cMid.setCellValueFactory(new PropertyValueFactory("id"));
        cName.setCellValueFactory(new PropertyValueFactory("name"));
        cLatin.setCellValueFactory(new PropertyValueFactory("latin"));
        cGender.setCellValueFactory(new PropertyValueFactory("gender"));
        cPhone.setCellValueFactory(new PropertyValueFactory("phone"));

        cBid.setCellValueFactory(new PropertyValueFactory("id"));
        cTitle.setCellValueFactory(new PropertyValueFactory("title"));
        cSub.setCellValueFactory(new PropertyValueFactory("subTitle"));
        cNumDay.setCellValueFactory(new PropertyValueFactory("numDay"));
    }
    @FXML
    private void clickReturn(MouseEvent event) {
        Button close = new Button("ទេ");
        Button ok = new Button("បាទ");
        close.setStyle("-fx-cursor:hand ; -fx-font-color:red ; -fx-border-color:white; -fx-background-color:white");
        ok.setStyle("-fx-cursor:hand; -fx-font-color:red ; -fx-border-color:white; -fx-background-color:white ; -fx-text-fill:red");
        JFXDialogLayout content = new JFXDialogLayout();
        JFXDialog dialog = new JFXDialog(MainController.stackPane, content, JFXDialog.DialogTransition.CENTER, true);
        content.setHeading(new Text("សងសៀវភៅ!"));
        content.setBody(new Text("តើអ្នកពិតជាចង់សងសៀវភៅនេះមែនទេ?"));
        content.setStyle("-fx-font-size: 15; -fx-font-family: 'Kh System'");
        content.setActions(close, ok);
        close.setOnAction(e -> {
            dialog.close();
        });
        ok.setOnAction(e -> {
            returnBook();
            dialog.close();
        });
        dialog.show();

    }

    private void returnBook() throws NumberFormatException {
        try {
            String sql = "DELETE FROM tb_issue WHERE m_id=? AND b_id=?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(tblMember.getSelectionModel().getSelectedItem().getId()));
            pst.setString(2, tblBook.getSelectionModel().getSelectedItem().getId());
            pst.executeUpdate();
            loadBook();
        } catch (SQLException ex) {
            Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void clickClear(MouseEvent event) {
        txtSearchMember.setText("");
        tblBook.getItems().clear();
        btnReturn.setVisible(false);
        int choise = cboCatBorrow.getSelectionModel().getSelectedIndex();
        if (choise == 0) {
            loadMember2();
        } else {
            loadMember();
        }
    }

    /**
     * Load who have borrowed book more than 14 days.
     */
    private void loadMember() {
        ObservableList<MemberPojo> memberList = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT id,name,latin,gender,phone,issue_date FROM tb_member JOIN tb_issue ON id=m_id WHERE (CURRENT_DATE-issue_date )>14 ORDER BY issue_date ";
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                memberList.add(new MemberPojo(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
            }
            tblMember.getItems().setAll(memberList);
        } catch (SQLException ex) {
            Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Load who have borrowed book less than 14 days.
     */
    private void loadMember2() {
        ObservableList<MemberPojo> memberList = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT id,name,latin,gender,phone,issue_date FROM tb_member JOIN tb_issue ON id=m_id WHERE (CURRENT_DATE-issue_date )<14 ORDER BY issue_date ";
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                memberList.add(new MemberPojo(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
            }
            tblMember.getItems().setAll(memberList);
        } catch (SQLException ex) {
            Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
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
    private void clickSearch(MouseEvent event) {
        tblBook.getItems().clear();
        if (txtSearchMember.getText().trim().isEmpty()) {
            return;
        }
        ObservableList<MemberPojo> memberList = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT id,name,latin,gender,phone FROM tb_member  JOIN tb_issue ON tb_member.id=tb_issue.m_id WHERE name LIKE ?"
                + "UNION SELECT DISTINCT id,name,latin,gender,phone FROM tb_member  JOIN tb_issue ON tb_member.id=tb_issue.m_id WHERE latin LIKE ?";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, txtSearchMember.getText().trim() + "%");
            pst.setString(2, txtSearchMember.getText().trim().toUpperCase() + "%");
            rs = pst.executeQuery();
            while (rs.next()) {
                memberList.add(new MemberPojo(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
            }
            tblMember.getItems().setAll(memberList);
        } catch (SQLException ex) {
            Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void getSelectedRowData() {
        tblMember.setOnMouseClicked(e -> {
            int row = tblMember.getSelectionModel().getSelectedIndex();
            btnReturn.setVisible(false);
            if (row < 0) {
                return;
            }
            loadBook();
        });

        tblBook.setOnMouseClicked(e -> {
            int row = tblBook.getSelectionModel().getSelectedIndex();
            if (row >= 0) {
                btnReturn.setVisible(true);
            }
        });
    }

    /**
     *
     * Load issued book when clicked on member table row
     */
    private void loadBook() throws NumberFormatException {
        String sql = "SELECT b.b_id, b.title, b.sub_title,(CURRENT_DATE-i.issue_date) FROM tb_book as b INNER JOIN tb_issue as i ON b.b_id=i.b_id WHERE i.m_id=?";
        ObservableList<BookPojo> bookList = FXCollections.observableArrayList();
        try {
            pst = conn.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(tblMember.getSelectionModel().getSelectedItem().getId()));
            rs = pst.executeQuery();
            while (rs.next()) {
                bookList.add(new BookPojo(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));
            }
            tblBook.getItems().setAll(bookList);
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
        cboCatBorrow.setOnAction(e -> {
            int choise = cboCatBorrow.getSelectionModel().getSelectedIndex();
            if (choise == 0) {
                tblBook.getItems().clear();
                btnReturn.setVisible(false);
                loadMember2();
            } else {
                tblBook.getItems().clear();
                loadMember();
                btnReturn.setVisible(false);
            }
        });
    }

}
