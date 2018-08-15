/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lms.controller;

import animatefx.animation.FadeIn;
import animatefx.animation.FadeInUp;
import animatefx.animation.JackInTheBox;
import animatefx.animation.RollIn;
import animatefx.animation.RotateIn;
import animatefx.animation.Shake;
import animatefx.animation.SlideInDown;
import animatefx.animation.ZoomIn;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

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

    //private AnchorPane mainPane;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadForm();
        clickMenu();

    }

    @FXML
    private void clickExit(MouseEvent event) {
        Platform.exit();
    }

    private void loadForm() {
        try {
            AnchorPane register = FXMLLoader.load(getClass().getResource("/lms/view/Register.fxml"));
            vbRegisterMember.getChildren().add(register);

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
            new Shake(vbIssueBook).play();
        });
        
        btnReturnBook.setOnAction((e) -> {
            hidePane();
            vbRetrunBook.setVisible(true);
            new FadeInUp(vbRetrunBook).play();
        });
        
        btnAddBook.setOnAction((e)->{
            hidePane();
            vbAddBook.setVisible(true);
            new ZoomIn(vbAddBook).play();
        });
        
        btnMaterial.setOnAction((e)->{
            hidePane();
            vbMaterial.setVisible(true);
            new RotateIn(vbMaterial).play();
        });
        
        btnStatistic.setOnAction((e)->{
            hidePane();
            vbStatistic.setVisible(true);
            new SlideInDown(vbStatistic).play();//
        });
    }

}
