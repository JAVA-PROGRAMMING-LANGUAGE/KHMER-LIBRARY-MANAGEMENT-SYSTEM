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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import lms.DbConnection;
import lms.MyDialog;
import lms.pojo.BookPojo;

/**
 * FXML Controller class
 *
 * @author SRUN VANNARA
 */
public class AddBookController implements Initializable {

    @FXML
    private TextField txtSearch;
    @FXML
    private ComboBox<String> cboCategory;
    @FXML
    private Button btnSave;
    @FXML
    private TableView<BookPojo> tblBook;
    @FXML
    private TableColumn<BookPojo, String> cId;
    @FXML
    private TableColumn<BookPojo, String> cTitle;
    @FXML
    private TableColumn<BookPojo, String> cSubTitle;
    @FXML
    private TableColumn<BookPojo, String> cCategory;
    @FXML
    private TableColumn<BookPojo, String> cAuthor;
    @FXML
    private TableColumn<BookPojo, String> cPrintYear;
    @FXML
    private TableColumn<BookPojo, String> cBookNum;
    @FXML
    private TableColumn<BookPojo, String> cOther;

    private Connection conn = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtTitle;
    @FXML
    private TextField txtSubTitle;
    @FXML
    private TextField txtAuthor;
    @FXML
    private TextField txtPrintYear;
    @FXML
    private TextField txtNumBook;
    @FXML
    private TextField txtOther;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnSave.setVisible(false);
        conn = DbConnection.connect();
        initTable();
        unfocusId();
        getSelectedRowData();
        loadCategory();
        loadBook();
    }    
    
    private void initTable() {
        cId.setCellValueFactory(new PropertyValueFactory("id"));
        cTitle.setCellValueFactory(new PropertyValueFactory("title"));
        cSubTitle.setCellValueFactory(new PropertyValueFactory("subTitle"));
        cCategory.setCellValueFactory(new PropertyValueFactory("category"));
        cAuthor.setCellValueFactory(new PropertyValueFactory("author"));
        cPrintYear.setCellValueFactory(new PropertyValueFactory("printYear"));
        cBookNum.setCellValueFactory(new PropertyValueFactory("bookNum"));
        cOther.setCellValueFactory(new PropertyValueFactory("other"));
    }

    private void loadCategory() {
        String sql = "SELECT category FROM tb_category";
        ObservableList<String> category = FXCollections.observableArrayList();
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                category.add(rs.getString(1));
            }
            cboCategory.getItems().setAll(category);
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

    private void loadBook() {
        String sql = "SELECT * FROM tb_book LIMIT 50";
        ObservableList<BookPojo> book = FXCollections.observableArrayList();
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                book.add(new BookPojo(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)));
            }
            tblBook.getItems().setAll(book);
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
        return txtId.getText().trim().equals("") || txtId.isFocused() || txtTitle.getText().trim().equals("")
                || cboCategory.getSelectionModel().isEmpty() || !parseInt(txtNumBook.getText().trim());
    }


    @FXML
    private void clickSave(MouseEvent event) {
        if (!isEmptyField()) {
            if (btnSave.getText().equals("រក្សាទុក")) {
                save();
            } else {
                showUpdateDialog("កែប្រែទិន្នន័យសៀវភៅ!", "តើអ្នកពិតជាចង់កែប្រែមែនទេ?");
            }
        } else {
            new MyDialog().showInfoDialog("មិនបានជោគជ័យ!", "កូដ/ISBN ចំណងជើង ប្រភេទ និងចំនួនសៀវភៅ(ចំនួនគត់)មិនអាចទទេ។");
        }
    }

    private void save() {
        try {
            String sql = "INSERT INTO tb_book(b_id, title, sub_title, category, author, print_year, num_book, other) values(?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, txtId.getText().toUpperCase().trim());
            pst.setString(2, txtTitle.getText().trim().toUpperCase());
            pst.setString(3, txtSubTitle.getText().trim());
            pst.setString(4, cboCategory.getSelectionModel().getSelectedItem());
            pst.setString(5, txtAuthor.getText().trim());
            pst.setString(6, txtPrintYear.getText().trim());
            pst.setInt(7, Integer.parseInt(txtNumBook.getText().trim()));
            pst.setString(8, txtOther.getText().trim());
            pst.executeUpdate();
            clearInput();
            loadBook();
        } catch (SQLException ex) {
            new MyDialog().showInfoDialog("មានបញ្ហា!", ex.getMessage());
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
            String sql = "UPDATE tb_book SET title=? , sub_title=? , category=? , author=? , print_year=? , num_book=? , other=? WHERE b_id=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, txtTitle.getText().trim().toUpperCase());
            pst.setString(2, txtSubTitle.getText().trim());
            pst.setString(3, cboCategory.getSelectionModel().getSelectedItem());
            pst.setString(4, txtAuthor.getText().trim());
            pst.setString(5, txtPrintYear.getText().trim());
            pst.setInt(6, Integer.parseInt(txtNumBook.getText().trim()));
            pst.setString(7, txtOther.getText().trim());
            pst.setString(8, txtId.getText().toUpperCase().trim());
            pst.executeUpdate();
            clearInput();
            loadBook();
        } catch (SQLException ex) {
            //Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
            new MyDialog().showInfoDialog("មានបញ្ហា!", ex.getMessage());
        } finally {
            try {
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    @FXML
    private void clickNew(MouseEvent event) {
        clearInput();
    }

    @FXML
    private void clickDelete(MouseEvent event) {
        delete();
    }

    private void clearInput() {
        txtId.setText("");
        txtTitle.setText("");
        txtSubTitle.setText("");
        cboCategory.getSelectionModel().clearSelection();
        txtAuthor.setText("");
        txtPrintYear.setText("");
        txtNumBook.setText("");
        txtOther.setText("");
        tblBook.getSelectionModel().clearSelection();
        btnSave.setVisible(false);
        btnSave.setText("រក្សាទុក");
        
    }

    @FXML
    private void idTextChange(KeyEvent event) {
        txtTitle.setText("");
        txtSubTitle.setText("");
        cboCategory.getSelectionModel().clearSelection();
        txtAuthor.setText("");
        txtPrintYear.setText("");
        txtNumBook.setText("");
        txtOther.setText("");
        btnSave.setText("រក្សាទុក");
        btnSave.setVisible(false);
        tblBook.getSelectionModel().clearSelection();
    }

    private void unfocusId() {
        txtId.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                if (!txtId.getText().trim().isEmpty()) {
                    btnSave.setVisible(true);
                }
                String sql = "SELECT * FROM tb_book WHERE b_id=?";
                ObservableList<BookPojo> book = FXCollections.observableArrayList();
                try {
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, txtId.getText().toUpperCase().trim());
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        txtTitle.setText(rs.getString(2));
                        txtSubTitle.setText(rs.getString(3));
                        cboCategory.getSelectionModel().select(rs.getString(4));
                        txtAuthor.setText(rs.getString(5));
                        txtPrintYear.setText(rs.getString(6));
                        txtNumBook.setText(rs.getString(7));
                        txtOther.setText(rs.getString(8));
                        btnSave.setText("កែប្រែ");
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
        });
    }

    private void getSelectedRowData() {
        tblBook.setOnMouseClicked(e -> {
            if (tblBook.getSelectionModel().getSelectedItem() != null) {
                BookPojo book = tblBook.getItems().get(tblBook.getSelectionModel().getSelectedIndex());
                txtId.setText(book.getId());
                txtTitle.setText(book.getTitle());
                txtSubTitle.setText(book.getSubTitle());
                cboCategory.getSelectionModel().select(book.getCategory());
                txtAuthor.setText(book.getAuthor());
                txtPrintYear.setText(book.getPrintYear());
                txtNumBook.setText(book.getBookNum());
                txtOther.setText(book.getOther());
                btnSave.setText("កែប្រែ");
                btnSave.setVisible(true);
            }
        });
    }
    private void showDeleteDialog(String head, String body) {
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

    private void delete() {
        int row = tblBook.getSelectionModel().getSelectedIndex();
        if (row < 0) {
            new MyDialog().showInfoDialog("ការលុបមិនបានជោគជ័យ!", "សូមជ្រើសរើសសៀវភៅដែលចង់លុបពីក្នុងតារាងរួចចុចប៊ូតុងលុប។");
        } else {
            String id = tblBook.getSelectionModel().getSelectedItem().getId();
            Button close = new Button("ទេ");
            Button ok = new Button("បាទ");
            close.setStyle("-fx-cursor:hand ; -fx-font-color:red ; -fx-border-color:white; -fx-background-color:white");
            ok.setStyle("-fx-cursor:hand; -fx-font-color:red ; -fx-border-color:white; -fx-background-color:white ; -fx-text-fill:red");
            JFXDialogLayout content = new JFXDialogLayout();
            JFXDialog dialog = new JFXDialog(MainController.stackPane, content, JFXDialog.DialogTransition.CENTER, true);
            content.setHeading(new Text("លុបសៀវភៅ!"));
            content.setBody(new Text("តើអ្នកពិតជាចង់លុបសៀវភៅនេះមែនទេ?"));
            content.setStyle("-fx-font-size: 15; -fx-font-family: 'Kh System'");
            content.setActions(close, ok);
            close.setOnAction(e -> {
                dialog.close();
            });
            ok.setOnAction(e -> {
                try {
                    String sql = "DELETE FROM tb_book WHERE b_id=?";
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, id);
                    pst.executeUpdate();
                    clearInput();
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
                dialog.close();
            });
            dialog.show();

        }
    }

    @FXML
    private void clickClearSearch(MouseEvent event) {
        clearInput();
        loadBook();
        txtSearch.setText("");
    }

    @FXML
    private void clickSearch(MouseEvent event) {
        clearInput();
        if (!txtSearch.getText().isEmpty()) {
            ObservableList<BookPojo> bookList = FXCollections.observableArrayList();
            String sql = "SELECT * FROM tb_book WHERE b_id LIKE ?"
                    + "UNION SELECT * FROM tb_book WHERE title LIKE ? ORDER BY title  LIMIT 100";
            try {
                pst = conn.prepareStatement(sql);
                pst.setString(1, txtSearch.getText().trim());
                pst.setString(2, txtSearch.getText().toUpperCase().trim() + "%");
                rs = pst.executeQuery();
                while (rs.next()) {
                    bookList.add(new BookPojo(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)));
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
        }
    }
}
