/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lms.controller;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
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
import lms.pojo.MemberPojo;

/**
 * FXML Controller class
 *
 * @author SRUN VANNARA
 */
public class RegisterController implements Initializable {

    @FXML
    private ComboBox<String> cboGender;
    @FXML
    private TextField txtProvince;
    @FXML
    private TextField txtPhone;
    @FXML
    private TextField txtSearch;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtLatin;
    @FXML
    private TextField txtBirth;
    @FXML
    private TextField txtVillage;
    @FXML
    private TextField txtCommune;
    @FXML
    private TextField txtDistrict;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnNew;
    @FXML
    private Button btnDelete;
    @FXML
    private TableView<MemberPojo> tblMember;
    @FXML
    private MaterialIconView btnSearch;

    private Connection conn = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    @FXML
    private TableColumn<MemberPojo, String> cId;
    @FXML
    private TableColumn<MemberPojo, String> cName;
    @FXML
    private TableColumn<MemberPojo, String> cLatin;
    @FXML
    private TableColumn<MemberPojo, String> cGender;
    @FXML
    private TableColumn<MemberPojo, String> cPhone;
    @FXML
    private TableColumn<MemberPojo, String> cBirth;
    @FXML
    private TableColumn<MemberPojo, String> cVillage;
    @FXML
    private TableColumn<MemberPojo, String> cCommune;
    @FXML
    private TableColumn<MemberPojo, String> cDistrict;
    @FXML
    private TableColumn<MemberPojo, String> cProvince;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cboGender.getItems().addAll("ប្រុស", "ស្រី");
        DbConnection dbConnection = new DbConnection();
        conn = dbConnection.connect();
        initTable();
        loadMember();
        getSelectedRowData();
    }
    /**
     *
     * Check if blank field exists
     */
    private Boolean isEmptyField() {
        if (txtName.getText().trim().equals("") || txtLatin.getText().trim().equals("")
                || cboGender.getSelectionModel().isEmpty()
                || txtBirth.getText().trim().equals("") || txtVillage.getText().trim().equals("")
                || txtCommune.getText().trim().equals("") || txtDistrict.getText().trim().equals("")
                || txtProvince.getText().trim().equals("") || txtPhone.getText().trim().equals("")) {
            return true;
        } else {
            return false;
        }
    }

    private void showInfoDialog(String head, String body) {
        Button close = new Button("បិទ");
        close.setStyle("-fx-cursor:hand; -fx-font-size: 15px; -fx-font-family: 'Kh System' ; -fx-font-color:red ; -fx-border-color:white; -fx-background-color:white ; -fx-text-fill:red");
        JFXDialogLayout content = new JFXDialogLayout();
        JFXDialog dialog = new JFXDialog(MainController.stackPane, content, JFXDialog.DialogTransition.CENTER, true);
        content.setHeading(new Text(head));
        content.setBody(new Text(body));
        content.setStyle("-fx-font-size: 15; -fx-font-family: 'Kh System'");
        content.setActions(close);
        close.setOnAction(e -> {
            dialog.close();
        });
        dialog.show();
    }

    /**
     * Load all members.
     */
    private void loadMember() {
        ObservableList<MemberPojo> memberList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM tb_member";
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                memberList.add(new MemberPojo(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10)));
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
            if (tblMember.getSelectionModel().getSelectedItem() != null) {
                MemberPojo memberDto = tblMember.getItems().get(tblMember.getSelectionModel().getSelectedIndex());
                txtId.setText(memberDto.getId());
                txtName.setText(memberDto.getName());
                txtLatin.setText(memberDto.getLatin());
                cboGender.getSelectionModel().select(memberDto.getGender());
                txtBirth.setText(memberDto.getBd());
                txtVillage.setText(memberDto.getVillage());
                txtCommune.setText(memberDto.getCommune());
                txtDistrict.setText(memberDto.getDistrict());
                txtProvince.setText(memberDto.getProvince());
                txtPhone.setText(memberDto.getPhone());
            }
        });
    }
    
    private void initTable() {
        cId.setCellValueFactory(new PropertyValueFactory("id"));
        cName.setCellValueFactory(new PropertyValueFactory("name"));
        cLatin.setCellValueFactory(new PropertyValueFactory("latin"));
        cGender.setCellValueFactory(new PropertyValueFactory("gender"));
        cBirth.setCellValueFactory(new PropertyValueFactory("bd"));
        cVillage.setCellValueFactory(new PropertyValueFactory("village"));
        cCommune.setCellValueFactory(new PropertyValueFactory("commune"));
        cDistrict.setCellValueFactory(new PropertyValueFactory("district"));
        cProvince.setCellValueFactory(new PropertyValueFactory("province"));
        cPhone.setCellValueFactory(new PropertyValueFactory("phone"));
    }

    @FXML
    private void clickSave(MouseEvent event) {
        if (!isEmptyField()) {

        } else {
            showInfoDialog("ការរក្សាទុកមិនបានជោគជ័យ", "សូមបំពេញរាល់ចន្លោះទាំងអស់ដើម្បីអាចរក្សាទុកបាន។");
        }
    }

    @FXML
    private void clickNew(MouseEvent event) {
    }

    @FXML
    private void clickDelete(MouseEvent event) {
    }
}
