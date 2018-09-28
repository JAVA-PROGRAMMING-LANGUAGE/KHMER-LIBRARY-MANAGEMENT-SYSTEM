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
import lms.MyDialog;
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
    @FXML
    private MaterialIconView btnSearch1;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cboGender.getItems().addAll("ប្រុស", "ស្រី");
        //Connect to database
        conn = DbConnection.connect();
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
    private void showUpdateDialog(String head, String body) {
        Button close = new Button("ទេ");
        Button ok = new Button("បាទ");
        close.setStyle("-fx-cursor:hand ; -fx-font-color:red ; -fx-border-color:white; -fx-background-color:white");
        ok.setStyle("-fx-cursor:hand; -fx-font-color:red ; -fx-border-color:white; -fx-background-color:white ; -fx-text-fill:red");
        JFXDialogLayout content = new JFXDialogLayout();
        JFXDialog dialog = new JFXDialog(MainController.stackPane, content, JFXDialog.DialogTransition.CENTER, true);
        content.setHeading(new Text(head));
        content.setBody(new Text(body));
        content.setStyle("-fx-font-size: 15; -fx-font-family: 'Kh System'");
        content.setActions(close, ok);
        close.setOnAction(e -> {
            dialog.close();
        });
        ok.setOnAction(e -> {
            update();
            dialog.close();
        });
        dialog.show();
    }
    /**
     * Load all members.
     */
    private void loadMember() {
        ObservableList<MemberPojo> memberList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM tb_member ORDER BY id  DESC LIMIT 200";
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                memberList.add(new MemberPojo(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10)));
            }
            tblMember.getItems().setAll(memberList);
            clearInput();
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
                btnSave.setText("កែប្រែ");
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
            if (btnSave.getText().equals("រក្សាទុក")) {
                save();
            } else {
                showUpdateDialog("កែប្រែទិន្នន័យអ្នកខ្ចី!", "តើអ្នកពិតជាចង់កែប្រែមែនទេ?");
            }
        } else {
            new MyDialog().showInfoDialog("ការរក្សាទុកមិនបានជោគជ័យ!", "សូមបំពេញចន្លោះទាំងអស់ដើម្បីអាចរក្សាទុកបាន។");
        }
    }

    @FXML
    private void clickNew(MouseEvent event) {
        clearInput();
    }
    
    private void clearInput() {
        txtId.setText("");
        txtName.setText("");
        txtLatin.setText("");
        cboGender.getSelectionModel().clearSelection();
        txtBirth.setText("");
        txtVillage.setText("");
        txtCommune.setText("");
        txtDistrict.setText("");
        txtProvince.setText("");
        txtPhone.setText("");
        btnSave.setText("រក្សាទុក");
        tblMember.getSelectionModel().clearSelection();

    }

    @FXML
    private void clickDelete(MouseEvent event) {
        showDeleteDialog("លុបឈ្មោះអ្នកខ្ចី!", "តើអ្នកពិតជាចង់លុបមែនទេ?");
    }

    private void showDeleteDialog(String head, String body) {
        int row = tblMember.getSelectionModel().getSelectedIndex();
        if (row < 0) {
            new MyDialog().showInfoDialog("ការលុបមិនបានជោគជ័យ!", "សូមជ្រើសរើសអ្នកខ្ចីដែលចង់លុបពីក្នុងតារាងរួចចុចប៊ូតុងលុប។");
        } else {
            Button close = new Button("ទេ");
            Button ok = new Button("បាទ");
            close.setStyle("-fx-cursor:hand ; -fx-font-color:red ; -fx-border-color:white; -fx-background-color:white");
            ok.setStyle("-fx-cursor:hand; -fx-font-color:red ; -fx-border-color:white; -fx-background-color:white ; -fx-text-fill:red");
            JFXDialogLayout content = new JFXDialogLayout();
            JFXDialog dialog = new JFXDialog(MainController.stackPane, content, JFXDialog.DialogTransition.CENTER, true);
            content.setHeading(new Text(head));
            content.setBody(new Text(body));
            content.setStyle("-fx-font-size: 15; -fx-font-family: 'Kh System'");
            content.setActions(close, ok);
            close.setOnAction(e -> {
                dialog.close();
            });
            ok.setOnAction(e -> {
                delete();
                dialog.close();
            });
            dialog.show();

        }
    }
    private void delete() {
        try {
            String sql = "DELETE FROM tb_member WHERE id=?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(txtId.getText()));
            pst.executeUpdate();
            clearInput();
            loadMember();
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
    private void update() {
        try {
            String sql = "UPDATE tb_member SET name=?, latin=?, gender=?, bd=?, village=?, commune=?, district=?, province=?, phone=? WHERE id=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, txtName.getText().trim());
            pst.setString(2, txtLatin.getText().trim().toUpperCase());
            pst.setString(3, cboGender.getSelectionModel().getSelectedItem());
            pst.setString(4, txtBirth.getText().trim());
            pst.setString(5, txtVillage.getText().trim());
            pst.setString(6, txtCommune.getText().trim());
            pst.setString(7, txtDistrict.getText().trim());
            pst.setString(8, txtProvince.getText().trim());
            pst.setString(9, txtPhone.getText().trim());
            pst.setInt(10, Integer.parseInt(txtId.getText()));
            pst.executeUpdate();
            loadMember();
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

    private void save() {
        try {
            String sql = "INSERT INTO tb_member(name, latin, gender, bd, village, commune, district, province, phone) values(?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, txtName.getText().trim());
            pst.setString(2, txtLatin.getText().trim());
            pst.setString(3, cboGender.getSelectionModel().getSelectedItem());
            pst.setString(4, txtBirth.getText().trim());
            pst.setString(5, txtVillage.getText().trim());
            pst.setString(6, txtCommune.getText().trim());
            pst.setString(7, txtDistrict.getText().trim());
            pst.setString(8, txtProvince.getText().trim());
            pst.setString(9, txtPhone.getText().trim());
            pst.executeUpdate();
            loadMember();
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
    private void clickClearSearch(MouseEvent event) {
        txtSearch.setText("");
        loadMember();
    }

    /**
     *
     * Convert String to integer
     */
    private Boolean parseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    @FXML
    private void clickSearch(MouseEvent event) {
        clearInput();
        if (!txtSearch.getText().isEmpty()) {
            ObservableList<MemberPojo> memberList = FXCollections.observableArrayList();
            String sql = "SELECT * FROM tb_member WHERE id=?"
                    + "UNION SELECT * FROM tb_member WHERE name LIKE ?"
                    + "UNION SELECT * FROM tb_member WHERE latin LIKE ?";
            try {
                pst = conn.prepareStatement(sql);
                if (parseInt(txtSearch.getText().trim())) {
                    pst.setInt(1, Integer.parseInt(txtSearch.getText().trim()));
                } else {
                    pst.setInt(1, 0);
                }
                pst.setString(2, txtSearch.getText().trim() + "%");
                pst.setString(3, txtSearch.getText().trim().toUpperCase() + "%");
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
    }

}
