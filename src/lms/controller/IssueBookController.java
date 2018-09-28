/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lms.controller;

import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lms.DbConnection;
import lms.MyDialog;
import lms.pojo.BookPojo;
import lms.pojo.MemberPojo;

/**
 * FXML Controller class
 *
 * @author SRUN VANNARA
 */
public class IssueBookController implements Initializable {

    @FXML
    private TextField txtSearchMember;
    @FXML
    private TableView<MemberPojo> tblMember;
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
    private TextField txtSearchBook;
    @FXML
    private TableView<BookPojo> tblBook;
    @FXML
    private TableColumn<BookPojo, String> cBid;
    @FXML
    private TableColumn<BookPojo, String> cTitle;
    @FXML
    private TableColumn<BookPojo, String> cSubtitle;
    @FXML
    private TableColumn<BookPojo, String> cCategory;
    @FXML
    private TableColumn<BookPojo, String> cAuthor;
    @FXML
    private Label lblInfo;
    @FXML
    private Button btnIssue;

    private Connection conn = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    @FXML
    private MaterialIconView iconInfo;
    @FXML
    private Label lblDate;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conn = DbConnection.connect();
        initTable();
        getSelectedRowData();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateTime now = LocalDateTime.now();
        lblDate.setText("កាលបរិច្ឆេទខ្ចីៈ " + dtf.format(now) + "\n" + "កាលបរិច្ឆេទត្រូវសងៈ " + dtf.format(now.plusDays(14)));
    }    

    private void initTable() {
        cMid.setCellValueFactory(new PropertyValueFactory("id"));
        cName.setCellValueFactory(new PropertyValueFactory("name"));
        cLatin.setCellValueFactory(new PropertyValueFactory("latin"));
        cGender.setCellValueFactory(new PropertyValueFactory("gender"));
        cPhone.setCellValueFactory(new PropertyValueFactory("phone"));

        cBid.setCellValueFactory(new PropertyValueFactory("id"));
        cTitle.setCellValueFactory(new PropertyValueFactory("title"));
        cSubtitle.setCellValueFactory(new PropertyValueFactory("subTitle"));
        cCategory.setCellValueFactory(new PropertyValueFactory("category"));
        cAuthor.setCellValueFactory(new PropertyValueFactory("author"));
    }

    @FXML
    private void clickSearchMember(MouseEvent event) {
        lblInfo.setVisible(false);
        iconInfo.setVisible(false);
        tblBook.getItems().clear();
        btnIssue.setVisible(false);
        lblDate.setVisible(false);
        tblMember.getItems().clear();
        if (!txtSearchMember.getText().isEmpty()) {
            ObservableList<MemberPojo> memList = FXCollections.observableArrayList();
            String sql = "SELECT * FROM tb_member WHERE name LIKE ?"
                    + "UNION SELECT * FROM tb_member WHERE latin LIKE ? ORDER BY latin  LIMIT 200";
            try {
                pst = conn.prepareStatement(sql);
                pst.setString(1, txtSearchMember.getText().trim() + "%");
                pst.setString(2, txtSearchMember.getText().toUpperCase().trim() + "%");
                rs = pst.executeQuery();
                while (rs.next()) {
                    memList.add(new MemberPojo(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
                }
                tblMember.getItems().setAll(memList);
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
    }

    @FXML
    private void clickSearchBook(MouseEvent event) {
        tblBook.getItems().clear();
        lblDate.setVisible(false);
        btnIssue.setVisible(false);
        if (txtSearchBook.getText().trim().isEmpty()) {
            return;
        }
        int row = tblMember.getSelectionModel().getSelectedIndex();
        if (row >= 0) {
            ObservableList<BookPojo> bookList = FXCollections.observableArrayList();
            String sql = "SELECT * FROM tb_book WHERE b_id LIKE ?"
                    + "UNION SELECT * FROM tb_book WHERE title LIKE ? ORDER BY title  LIMIT 200";
            try {
                pst = conn.prepareStatement(sql);
                pst.setString(1, txtSearchBook.getText().toUpperCase().trim() + "%");
                pst.setString(2, txtSearchBook.getText().toUpperCase().trim() + "%");
                rs = pst.executeQuery();
                while (rs.next()) {
                    bookList.add(new BookPojo(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
                }
                tblBook.getItems().setAll(bookList);
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
        } else {
            new MyDialog().showInfoDialog("មិនអាចស្វែងរកសៀវភៅបានឡើយ!", "សូមជ្រើសរើសអ្នកដែលចង់ឱ្យខ្ចីសៀវភៅជាមុនសិន។");
        }
    }

    private void getSelectedRowData() {
        tblMember.setOnMouseClicked(e -> {
            if (tblMember.getSelectionModel().getSelectedItem() != null) {
                lblInfo.setVisible(true);
                iconInfo.setVisible(true);
                tblBook.getSelectionModel().clearSelection();
                lblDate.setVisible(false);
                loadNumBorrow();
            }
        });
        
        tblBook.setOnMouseClicked(e -> {
            int row = tblBook.getSelectionModel().getSelectedIndex();
            if (row >= 0) {
                btnIssue.setVisible(true);
                lblDate.setVisible(true);
            }

        });
    }

    private void loadNumBorrow() throws NumberFormatException {
        String sql = "SELECT COUNT(*) FROM tb_issue WHERE m_id=?";
        btnIssue.setVisible(false);
        try {
            pst = conn.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(tblMember.getSelectionModel().getSelectedItem().getId()));
            rs = pst.executeQuery();
            int n = 0;
            while (rs.next()) {
                n = rs.getInt(1);
            }
            if (n >= 2) {
                iconInfo.setStyle("-fx-fill:red");
                lblInfo.setStyle("-fx-text-fill:red");
                lblInfo.setText("បានខ្ចីសៀវភៅចំនួន" + n + "ក្បាលហើយ មិនអាចឱ្យខ្ចីទៀតទេ។");
                txtSearchBook.setEditable(false);
                txtSearchBook.setText("");
                tblBook.setDisable(true);
                
            } else {
                iconInfo.setStyle("-fx-fill:#4c787e");
                lblInfo.setStyle("-fx-text-fill:#4c787e");
                lblInfo.setText("បានខ្ចីសៀវភៅចំនួន" + n + "ក្បាល។");
                txtSearchBook.setEditable(true);
                tblBook.setDisable(false);
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
    private void clickIssue(MouseEvent event) {
        int mId = Integer.parseInt(tblMember.getSelectionModel().getSelectedItem().getId());
        String bId = tblBook.getSelectionModel().getSelectedItem().getId();
        try {
            String sql = "INSERT INTO tb_issue(m_id, b_id, issue_date) values(?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, mId);
            pst.setString(2, bId);
            pst.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            pst.executeUpdate();
            tblMember.getSelectionModel().clearSelection();
            tblBook.getItems().clear();
            lblDate.setVisible(false);
            btnIssue.setVisible(false);
            lblDate.setVisible(false);
            new MyDialog().showInfoDialog("ការឱ្យខ្ចីបានជោគជ័យ!", "សូមជ្រើសរើសអ្នកខ្ចីដើម្បីឱ្យខ្ចីសៀវភៅថ្មីទៀត។");
        } catch (SQLException ex) {
            new MyDialog().showInfoDialog("ការឱ្យខ្ចីមិនបានជោគជ័យ!", "សៀវភៅនេះបានខ្ចីរួចហើយ មិនអាចឱ្យខ្ចីម្ដងទៀតទេ។");
        } finally {
            try {
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
