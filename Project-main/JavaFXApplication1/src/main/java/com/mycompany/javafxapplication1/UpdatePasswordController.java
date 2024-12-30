package com.mycompany.javafxapplication1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class UpdatePasswordController {

    @FXML
    private PasswordField oldPasswordField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmPasswordField;

    private String username; // Username of the logged-in user

    public void initialize(String username) {
        this.username = username;
    }

    @FXML
    private void handleUpdatePassword(ActionEvent event) {
        try {
            DB db = new DB();
            String oldPassword = oldPasswordField.getText();
            String newPassword = newPasswordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            // Validate fields
            if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                showError("All fields are required.");
                return;
            }

            // Validate old password
            if (!db.validateUser(username, oldPassword)) {
                showError("Old password is incorrect.");
                return;
            }

            // Validate new passwords match
            if (!newPassword.equals(confirmPassword)) {
                showError("New passwords do not match.");
                return;
            }

            // Update password
            if (db.updatePassword(username, newPassword)) {
                showSuccess("Password updated successfully.");
                closeWindow();
            } else {
                showError("Failed to update the password. Please try again.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("An error occurred while updating the password.");
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) oldPasswordField.getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
