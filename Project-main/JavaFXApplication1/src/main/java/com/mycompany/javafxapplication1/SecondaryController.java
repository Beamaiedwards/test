package com.mycompany.javafxapplication1;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;



public class SecondaryController {
    
    @FXML
    private TextField userTextField;
    
    @FXML
    private TableView dataTableView;

    @FXML
    private Button secondaryButton;
    
    @FXML
    private Button refreshBtn;
    
    @FXML
    private TextField customTextField;
    
    @FXML
    private void RefreshBtnHandler(ActionEvent event){
        Stage primaryStage = (Stage) customTextField.getScene().getWindow();
        customTextField.setText((String)primaryStage.getUserData());
    }
        
    @FXML
    private void switchToPrimary(){
        Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) secondaryButton.getScene().getWindow();
        try {
            
        
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("primary.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 640, 480);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Login");
            secondaryStage.show();
            primaryStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialise(String[] credentials) {
        userTextField.setText(credentials[0]);
        DB myObj = new DB();
        ObservableList<User> data;
        try {
            data = myObj.getDataFromTable();
            TableColumn user = new TableColumn("User");
        user.setCellValueFactory(
        new PropertyValueFactory<>("user"));

        TableColumn pass = new TableColumn("Pass");
        pass.setCellValueFactory(
            new PropertyValueFactory<>("pass"));
        dataTableView.setItems(data);
        dataTableView.getColumns().addAll(user, pass);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SecondaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private Button terminalButton;  // Button to open the terminal
    
    @FXML
private void openTerminal() {
        try {
            // Load the terminal.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("terminal.fxml"));
            Parent root = loader.load();

            // Create a new stage for the terminal
            Stage terminalStage = new Stage();
            terminalStage.setTitle("Terminal");
            terminalStage.setScene(new Scene(root, 600, 400)); // Adjust dimensions as needed

            // Show the terminal window
            terminalStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private Button fileManagerButton;

    @FXML
    private void openFileManager() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FileManager.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("File Manager");
            stage.setScene(new Scene(root, 640, 480)); // Adjust dimensions if needed
            stage.show();
        } catch (IOException e) {
            // Log the error for debugging
            
        }
    }
    
    @FXML
    private void deleteAccountHandler() {
        try {
            DB db = new DB();
            String username = userTextField.getText(); // Assume userTextField contains the username

            // Confirm the deletion
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Deletion");
            confirmAlert.setHeaderText("Are you sure you want to delete this account?");
            confirmAlert.setContentText("This action cannot be undone.");

            confirmAlert.showAndWait().ifPresent(new Consumer<ButtonType>() {
                @Override
                public void accept(ButtonType response) {
                    if (response == ButtonType.OK) {
                        try {
                            if (db.deleteUser(username)) {
                                dialogue("Account deleted successfully.");
                                switchToPrimary();
                            } else {
                                dialogue("Failed to delete the account. User may not exist.");
                            }
                        } catch (ClassNotFoundException e) {
                            dialogue("An error occurred while deleting the account.");
                        }
                    }
                }
            });
        } catch (Exception e) {
            dialogue("An error occurred while processing your request.");
        }
    }
    
    private void dialogue(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void openUpdatePasswordScreen(ActionEvent event) {
        try {
            // Load the Update Password screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdatePassword.fxml"));
            Parent root = loader.load();

            // Pass the username to the UpdatePasswordController
            UpdatePasswordController controller = loader.getController();
            controller.initialize(userTextField.getText()); // Ensure userTextField contains the username

            // Open the new window
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 400, 300));
            stage.setTitle("Update Password");
            stage.show();
        } catch (IOException e) {
        }
    }



}
