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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import lms.DbConnection;
import lms.MyDialog;
import lms.pojo.MaterialPojo;

/**
 * FXML Controller class
 *
 * @author SRUN VANNARA
 */
public class AddMaterialController implements Initializable {

    @FXML
    private TextField txtName;
    @FXML
    private TextField txtPrice;
    @FXML
    private TextField txtNum;
    @FXML
    private TextField txtDonate;
    @FXML
    private TextField txtOther;
    @FXML
    private Button btnSave;
    @FXML
    private TableView<MaterialPojo> tblMaterial;
    @FXML
    private TableColumn<MaterialPojo, Integer> cId;
    @FXML
    private TableColumn<MaterialPojo, String> cName;
    @FXML
    private TableColumn<MaterialPojo, String> cPrice;
    @FXML
    private TableColumn<MaterialPojo, String> cNum;
    @FXML
    private TableColumn<MaterialPojo, String> cDonate;
    @FXML
    private TableColumn<MaterialPojo, String> cOther;
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
        loadMaterial();
        getSelectedRowData();
    }    

    private void initTable() {
        cName.setCellValueFactory(new PropertyValueFactory("name"));
        cPrice.setCellValueFactory(new PropertyValueFactory("price"));
        cNum.setCellValueFactory(new PropertyValueFactory("num"));
        cDonate.setCellValueFactory(new PropertyValueFactory("donate"));
        cOther.setCellValueFactory(new PropertyValueFactory("other"));
        cId.setCellValueFactory(new PropertyValueFactory("id"));
    }

    private void loadMaterial() {
        String sql = "SELECT * FROM tb_material ORDER BY name  LIMIT 50";
        ObservableList<MaterialPojo> materialList = FXCollections.observableArrayList();
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                materialList.add(new MaterialPojo(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
            }
            tblMaterial.getItems().setAll(materialList);
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

    private Boolean parseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
    private Boolean isEmptyField() {
        return txtName.getText().trim().equals("") || !parseInt(txtNum.getText().trim());
    }
    @FXML
    private void clickSave(MouseEvent event) {
        if (!isEmptyField()) {
            if (btnSave.getText().equals("រក្សាទុក")) {
                save();
            } else {
                update();
            }
        } else {
            new MyDialog().showInfoDialog("ការរក្សាទុកមិនបានជោគជ័យ!", "សូមបំពេញឈ្មោះសម្ភារ និងចំនួន(ចំនួនគត់)។");
        }

    }

    private void save() {
            try {
                String sql = "INSERT INTO tb_material(name,price,num,donate,other) values(?,?,?,?,?)";
                pst = conn.prepareStatement(sql);
                pst.setString(1, txtName.getText().trim());
                pst.setString(2, txtPrice.getText().trim());
                pst.setString(3, txtNum.getText());
                pst.setString(4, txtDonate.getText().trim());
                pst.setString(5, txtOther.getText().trim());
                pst.executeUpdate();
                clearInput();
                loadMaterial();
            } catch (SQLException ex) {
                new MyDialog().showInfoDialog("ការរក្សាទុកមិនបានជោគជ័យ!", ex.getMessage());
            } finally {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
    }

    private void update() {
        Button close = new Button("ទេ");
        Button ok = new Button("បាទ");
        close.setStyle("-fx-cursor:hand ; -fx-font-color:red ; -fx-border-color:white; -fx-background-color:white");
        ok.setStyle("-fx-cursor:hand; -fx-font-color:red ; -fx-border-color:white; -fx-background-color:white ; -fx-text-fill:red");
        JFXDialogLayout content = new JFXDialogLayout();
        JFXDialog dialog = new JFXDialog(MainController.stackPane, content, JFXDialog.DialogTransition.CENTER, true);
        content.setHeading(new Text("កែប្រែសម្ភារ!"));
        content.setBody(new Text("តើអ្នកពិតជាចង់កែប្រែសម្ភារនេះមែនទេ?"));
        content.setStyle("-fx-font-size: 15; -fx-font-family: 'Kh System'");
        content.setActions(close, ok);
        close.setOnAction(e -> {
            dialog.close();
        });
        ok.setOnAction(e -> {
            try {
                String sql = "UPDATE tb_material SET name=?, price=?, num=?, donate=?, other=? WHERE id=?";
                int id = tblMaterial.getSelectionModel().getSelectedItem().getId();
                pst = conn.prepareStatement(sql);
                pst.setString(1, txtName.getText().trim());
                pst.setString(2, txtPrice.getText().trim());
                pst.setString(3, txtNum.getText());
                pst.setString(4, txtDonate.getText().trim());
                pst.setString(5, txtOther.getText().trim());
                pst.setInt(6, id);
                pst.executeUpdate();
                clearInput();
                loadMaterial();
            } catch (SQLException ex) {
                Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            dialog.close();
        });
        dialog.show();
    }

    
    private void getSelectedRowData() {
        tblMaterial.setOnMouseClicked(e -> {
            if (tblMaterial.getSelectionModel().getSelectedItem() != null) {
                MaterialPojo material = tblMaterial.getItems().get(tblMaterial.getSelectionModel().getSelectedIndex());
                txtName.setText(material.getName());
                txtPrice.setText(material.getPrice());
                txtNum.setText(material.getNum());
                txtDonate.setText(material.getDonate());
                txtOther.setText(material.getOther());
                btnSave.setText("កែប្រែ");
                btnSave.setVisible(true);
            }
        });
    }
    @FXML
    private void clickNew(MouseEvent event) {
        clearInput();
    }

    private void clearInput() {
        txtName.setText("");
        txtPrice.setText("");
        txtNum.setText("");
        txtDonate.setText("");
        txtOther.setText("");
        btnSave.setText("រក្សាទុក");
        tblMaterial.getSelectionModel().clearSelection();
    }

    @FXML
    private void clickDelete(MouseEvent event) {
        int row = tblMaterial.getSelectionModel().getSelectedIndex();
        if (row < 0) {
            new MyDialog().showInfoDialog("ការលុបមិនបានជោគជ័យ!", "សូមជ្រើសរើសសម្ភារអ្នកដែលចង់លុបពីក្នុងតារាងរួចចុចប៊ូតុងលុប។");
        } else {
            int id = tblMaterial.getSelectionModel().getSelectedItem().getId();
            Button close = new Button("ទេ");
            Button ok = new Button("បាទ");
            close.setStyle("-fx-cursor:hand ; -fx-font-color:red ; -fx-border-color:white; -fx-background-color:white");
            ok.setStyle("-fx-cursor:hand; -fx-font-color:red ; -fx-border-color:white; -fx-background-color:white ; -fx-text-fill:red");
            JFXDialogLayout content = new JFXDialogLayout();
            JFXDialog dialog = new JFXDialog(MainController.stackPane, content, JFXDialog.DialogTransition.CENTER, true);
            content.setHeading(new Text("លុបសម្ភារ!"));
            content.setBody(new Text("តើអ្នកពិតជាចង់លុបសម្ភារនេះមែនទេ?"));
            content.setStyle("-fx-font-size: 15; -fx-font-family: 'Kh System'");
            content.setActions(close, ok);
            close.setOnAction(e -> {
                dialog.close();
            });
            ok.setOnAction(e -> {
                try {
                    String sql = "DELETE FROM tb_material WHERE id=?";
                    pst = conn.prepareStatement(sql);
                    pst.setInt(1, id);
                    pst.executeUpdate();
                    clearInput();
                    loadMaterial();
                } catch (SQLException ex) {
                    Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        pst.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                dialog.close();
            });
            dialog.show();

        }
    }

}
