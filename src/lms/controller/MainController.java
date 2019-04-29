/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lms.controller;

import animatefx.animation.BounceIn;
import animatefx.animation.JackInTheBox;
import animatefx.animation.Pulse;
import animatefx.animation.RollIn;
import animatefx.animation.RubberBand;
import animatefx.animation.Shake;
import animatefx.animation.Swing;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lms.DbConnection;
import lms.MyDialog;

/**
 * FXML Controller class
 *
 * @author SRUN VANNARA
 */
public class MainController implements Initializable {

    @FXML
    private VBox vbStatistic;
    @FXML
    private VBox vbAddBook;
    @FXML
    private VBox vbIssueBook;
    @FXML
    private VBox vbRegisterMember;
    @FXML
    private VBox vbRetrunBook;
    @FXML
    private VBox vbMaterial;
    @FXML
    private Button btnRegisterMember;
    @FXML
    private Button btnIssueBook;
    @FXML
    private Button btnReturnBook;
    @FXML
    private Button btnAddBook;
    @FXML
    private Button btnMaterial;
    @FXML
    private Button btnStatistic;
    @FXML
    private Button btnAbout;
    @FXML
    private VBox vbAbout;
    @FXML
    private StackPane mainPane;
    public static StackPane stackPane;

    private Connection conn = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    @FXML
    private Button btnHelp;
    @FXML
    private VBox vbHelp;


    //private AnchorPane mainPane;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.stackPane = mainPane;
        conn = DbConnection.connect();
        loadForm();
        clickMenu();

    }

    @FXML
    private void clickExit(MouseEvent event) {
        Platform.exit();
    }

    private void loadForm() {
        try {
            AnchorPane registerMember = FXMLLoader.load(getClass().getResource("/lms/view/Register.fxml"));
            vbRegisterMember.getChildren().add(registerMember);
            
            AnchorPane issueBook = FXMLLoader.load(getClass().getResource("/lms/view/IssueBook.fxml"));
            vbIssueBook.getChildren().add(issueBook);
            
            AnchorPane returnBook = FXMLLoader.load(getClass().getResource("/lms/view/ReturnBook.fxml"));
            vbRetrunBook.getChildren().add(returnBook);
            
            AnchorPane addBook = FXMLLoader.load(getClass().getResource("/lms/view/AddBook.fxml"));
            vbAddBook.getChildren().add(addBook);
            
            AnchorPane addMaterial = FXMLLoader.load(getClass().getResource("/lms/view/AddMaterial.fxml"));
            vbMaterial.getChildren().add(addMaterial);

            AnchorPane viewStatistic = FXMLLoader.load(getClass().getResource("/lms/view/ViewSatistic.fxml"));
            vbStatistic.getChildren().add(viewStatistic);

            AnchorPane about = FXMLLoader.load(getClass().getResource("/lms/view/about.fxml"));
            vbAbout.getChildren().add(about);


        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void hidePane() {
        vbRegisterMember.setVisible(false);
        vbIssueBook.setVisible(false);
        vbRetrunBook.setVisible(false);
        vbAddBook.setVisible(false);
        vbMaterial.setVisible(false);
        vbStatistic.setVisible(false);
        vbAbout.setVisible(false);
        vbHelp.setVisible(false);
    }

    private void clickMenu() {
        
        btnRegisterMember.setOnAction((e) -> {
            hidePane();
            vbRegisterMember.setVisible(true);
            new RollIn(vbRegisterMember).play();
        });
        
        btnIssueBook.setOnAction((e) -> {
            hidePane();
            vbIssueBook.setVisible(true);
            new JackInTheBox(vbIssueBook).play();
        });
        
        btnReturnBook.setOnAction((e) -> {
            hidePane();
            vbRetrunBook.setVisible(true);
            new BounceIn(vbRetrunBook).play();
        });
        
        btnAddBook.setOnAction((e)->{
            hidePane();
            vbAddBook.setVisible(true);
            new Pulse(vbAddBook).play();
        });
        
        btnMaterial.setOnAction((e)->{
            hidePane();
            vbMaterial.setVisible(true);
            new Shake(vbMaterial).play();
        });
        
        btnStatistic.setOnAction((e)->{
            hidePane();
            vbStatistic.setVisible(true);
            new RubberBand(vbStatistic).play();
        });
        btnAbout.setOnAction((e) -> {
            hidePane();
            vbAbout.setVisible(true);
            new Swing(vbAbout).play();
        });
        btnHelp.setOnAction((e) -> {
            try {
                File file = new File("help.html");
                Desktop.getDesktop().open(file);
            } catch (Exception ex) {
                new MyDialog().showInfoDialog("មានបញ្ហា!", ex.getMessage());
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    @FXML
    private void clickMinimize(MouseEvent event) {
        Stage stage = (Stage) stackPane.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void clickAccount(MouseEvent event) {
        Button close = new Button("បោះបង់");
        Button ok = new Button("ប្ដូរ");
        close.setStyle("-fx-cursor:hand ; -fx-border-color:white; -fx-background-color:white");
        ok.setStyle("-fx-cursor:hand; -fx-border-color:white; -fx-background-color:white ; -fx-text-fill:red");
        JFXDialogLayout content = new JFXDialogLayout();
        JFXDialog dialog = new JFXDialog(MainController.stackPane, content, JFXDialog.DialogTransition.TOP, true);

        content.setHeading(new Text("ប្ដូរពាក្យសម្ងាត់!"));
        PasswordField txtOldPass = new PasswordField();
        txtOldPass.setPromptText("សូមវាយពាក្យសម្ងាត់បច្ចុប្បន្ន");
        PasswordField txtNewPass = new PasswordField();
        txtNewPass.setPromptText("សូមវាយពាក្យសម្ងាត់ថ្មី");
        
        VBox vb = new VBox();
        vb.getChildren().setAll(txtOldPass, txtNewPass);
        
        content.setBody(vb);
        content.setStyle("-fx-font-size: 15; -fx-font-family: 'Kh System'");
        content.setActions(close, ok);
        close.setOnAction(e -> {
            dialog.close();
        });
        ok.setOnAction(e -> {
            String sql = "SELECT * FROM tb_pwd WHERE pwd=?";
            try {
                pst = conn.prepareStatement(sql);
                pst.setString(1, txtOldPass.getText());
                rs = pst.executeQuery();

                if (!rs.next()) {
                    new MyDialog().showInfoDialog("ប្ដូរពាក្យសម្ងាត់!", "ពាក្យសម្ងាត់បច្ចុប្បន្នមិនត្រឹមត្រូវ។");
                    return;
                } else if (txtNewPass.getText().toCharArray().length < 4) {
                    new MyDialog().showInfoDialog("ប្ដូរពាក្យសម្ងាត់!", "ពាក្យសម្ងាត់ថ្មីត្រូវមានចាប់ពី៤តួឡើងទៅ។");
                    return;
                } else {
                    updatePwd(txtNewPass.getText());
                }

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

            dialog.close();
        });
        dialog.show();
    }

    private void updatePwd(String pwd) {
        try {
            String sql = "UPDATE tb_pwd SET pwd=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, pwd);
            pst.executeUpdate();
            new MyDialog().showInfoDialog("ប្ដូរពាក្យសម្ងាត់!", "ពាក្យសម្ងាត់ត្រូវបានប្ដូរ។\nនៅពេលបើកកម្មវិធីលើកក្រោយត្រូវវាយពាក្យសម្ងាត់ថ្មីនេះ។");
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
    private void clickChangePwd(MouseEvent event) {
        Button close = new Button("បោះបង់");
        Button ok = new Button("ប្ដូរ");
        close.setStyle("-fx-cursor:hand ; -fx-border-color:white; -fx-background-color:white");
        ok.setStyle("-fx-cursor:hand; -fx-border-color:white; -fx-background-color:white ; -fx-text-fill:red");
        JFXDialogLayout content = new JFXDialogLayout();
        JFXDialog dialog = new JFXDialog(MainController.stackPane, content, JFXDialog.DialogTransition.TOP, true);

        content.setHeading(new Text("ប្ដូរពាក្យសម្ងាត់!"));
        PasswordField txtOldPass = new PasswordField();
        txtOldPass.setPromptText("សូមវាយពាក្យសម្ងាត់បច្ចុប្បន្ន");
        PasswordField txtNewPass = new PasswordField();
        txtNewPass.setPromptText("សូមវាយពាក្យសម្ងាត់ថ្មី");

        VBox vb = new VBox();
        vb.getChildren().setAll(txtOldPass, txtNewPass);

        content.setBody(vb);
        content.setStyle("-fx-font-size: 15; -fx-font-family: 'Kh System'");
        content.setActions(close, ok);
        close.setOnAction(e -> {
            dialog.close();
        });
        ok.setOnAction(e -> {
            String sql = "SELECT * FROM tb_pwd WHERE pwd=?";
            try {
                pst = conn.prepareStatement(sql);
                pst.setString(1, txtOldPass.getText());
                rs = pst.executeQuery();

                if (!rs.next()) {
                    new MyDialog().showInfoDialog("ប្ដូរពាក្យសម្ងាត់!", "ពាក្យសម្ងាត់បច្ចុប្បន្នមិនត្រឹមត្រូវ។");
                    return;
                } else if (txtNewPass.getText().toCharArray().length < 4) {
                    new MyDialog().showInfoDialog("ប្ដូរពាក្យសម្ងាត់!", "ពាក្យសម្ងាត់ថ្មីត្រូវមានចាប់ពី៤តួឡើងទៅ។");
                    return;
                } else {
                    updatePwd(txtNewPass.getText());
                }

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

            dialog.close();
        });
        dialog.show();
    }

    @FXML
    private void clickQuit(MouseEvent event) {
        Platform.exit();
    }

}
