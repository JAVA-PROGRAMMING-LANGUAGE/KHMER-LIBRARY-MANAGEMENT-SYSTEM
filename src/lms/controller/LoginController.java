/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lms.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author SRUN VANNARA
 */
public class LoginController implements Initializable {

    @FXML
    private AnchorPane paneLogin;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void clickLogin(MouseEvent event) {
        loadMainForm();
    }

    private void loadMainForm() {
        Stage stage = (Stage) paneLogin.getScene().getWindow();
        try {
            Parent mainForm = FXMLLoader.load(getClass().getResource("/lms/view/Main.fxml"));
            Scene scene = new Scene(mainForm);
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
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}