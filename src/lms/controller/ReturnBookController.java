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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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
    private TableColumn<MemberPojo, String> cLate;

    @FXML
    private TableColumn<BookPojo, String> cBid;
    @FXML
    private TableColumn<BookPojo, String> cTitle;
    @FXML
    private TableColumn<BookPojo, String> cIssueDate;
    @FXML
    private TableColumn<BookPojo, String> cReturn;
    @FXML
    private Button btnReturn;
    @FXML
    private TableView<MemberPojo> tblMember;
    @FXML
    private TableView<BookPojo> tblBook;

    private Connection conn = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conn = DbConnection.connect();
        initTable();
        loadMember();
        getSelectedRowData();
    }    

    private void initTable() {
        cMid.setCellValueFactory(new PropertyValueFactory("id"));
        cName.setCellValueFactory(new PropertyValueFactory("name"));
        cLatin.setCellValueFactory(new PropertyValueFactory("latin"));
        cGender.setCellValueFactory(new PropertyValueFactory("gender"));
        cPhone.setCellValueFactory(new PropertyValueFactory("phone"));
        cLate.setCellValueFactory(new PropertyValueFactory("lateReturn"));

        cBid.setCellValueFactory(new PropertyValueFactory("id"));
        cTitle.setCellValueFactory(new PropertyValueFactory("title"));
        cIssueDate.setCellValueFactory(new PropertyValueFactory("issueDate"));
        cReturn.setCellValueFactory(new PropertyValueFactory("returnDate"));
    }
    @FXML
    private void clickReturn(MouseEvent event) {
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
        loadMember();
    }
    private void loadMember() {
        ObservableList<MemberPojo> memberList = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT id,name,latin,gender,phone,(CURRENT_DATE-tb_issue.issue_date ) as late FROM tb_member INNER JOIN tb_issue ON tb_member.id=tb_issue.m_id ORDER BY late DESC LIMIT 200";
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                memberList.add(new MemberPojo(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
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
        String sql = "SELECT DISTINCT id,name,latin,gender,phone,(CURRENT_DATE-tb_issue.issue_date ) as late FROM tb_member INNER JOIN tb_issue ON id=m_id WHERE name LIKE ? \n"
                + "UNION SELECT DISTINCT id,name,latin,gender,phone,(CURRENT_DATE-tb_issue.issue_date ) as late FROM tb_member INNER JOIN tb_issue ON id=m_id WHERE latin LIKE ? LIMIT 200";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, txtSearchMember.getText().trim() + "%");
            pst.setString(2, txtSearchMember.getText().trim().toUpperCase() + "%");
            rs = pst.executeQuery();
            while (rs.next()) {
                memberList.add(new MemberPojo(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
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
        String sql = "SELECT b.b_id, b.title, to_char(i.issue_date,'dd-MM-yyyy'),to_char(i.issue_date+14,'dd-MM-yyyy') FROM tb_book as b INNER JOIN tb_issue as i ON b.b_id=i.b_id WHERE i.m_id=?";
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

}
