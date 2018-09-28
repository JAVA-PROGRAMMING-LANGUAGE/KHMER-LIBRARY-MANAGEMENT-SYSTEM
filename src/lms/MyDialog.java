/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lms;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import lms.controller.MainController;

/**
 *
 * @author SRUN VANNARA
 */
public class MyDialog {

    public void showInfoDialog(String head, String body) {
        Button close = new Button("បិទ");
        close.setStyle("-fx-cursor:hand;  -fx-font-color:red ; -fx-border-color:white; -fx-background-color:white ");
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

}
