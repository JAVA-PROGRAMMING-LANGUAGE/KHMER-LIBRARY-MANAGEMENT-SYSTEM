/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lms.controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
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
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lms.DbConnection;

/**
 * FXML Controller class
 *
 * @author SRUN VANNARA
 */
public class LoginController implements Initializable {

    @FXML
    private AnchorPane paneLogin;
    private Connection conn = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    @FXML
    private ImageView imgLoading;
    @FXML
    private FontAwesomeIconView imgBook;
    @FXML
    private TextField txtPwd;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conn = DbConnection.connect();

    }

    @FXML
    private void clickLogin(MouseEvent event) {
        String sql = "SELECT * FROM tb_pwd WHERE pwd=?";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, txtPwd.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                imgBook.setVisible(false);
                imgLoading.setVisible(true);
                txtPwd.setText("");
                txtPwd.setPromptText("");
                new Thread(r).start();
                System.out.println("right");
            } else {
                txtPwd.setPromptText("ពាក្យសម្ងាត់មិនត្រឹមត្រូវ!");
                txtPwd.setText("");
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

    }
    Runnable r = () -> {
        loadMainForm();
    };

    private void loadMainForm() {
        Stage stage = (Stage) paneLogin.getScene().getWindow();
        try {
            Parent mainForm = FXMLLoader.load(getClass().getResource("/lms/view/Main.fxml"));
            Scene scene = new Scene(mainForm);
            Platform.runLater(() -> {

                stage.setScene(scene);
                Screen screen = Screen.getPrimary();
                Rectangle2D bounds = screen.getVisualBounds();

                stage.setX(bounds.getMinX());
                stage.setY(bounds.getMinY());

                stage.setMaxWidth(bounds.getWidth());
                stage.setMinWidth(bounds.getWidth());

                stage.setMaxHeight(bounds.getHeight());
                stage.setMinHeight(bounds.getHeight());

                stage.show();
            });

        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void clickExit(MouseEvent event) {
        System.exit(0);
    }


}
