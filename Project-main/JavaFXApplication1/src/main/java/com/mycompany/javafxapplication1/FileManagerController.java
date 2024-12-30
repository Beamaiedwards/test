/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.javafxapplication1;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileManagerController {

    @FXML
    private TextField filePathField;

    @FXML
    private TextArea fileContentArea;

    private File currentFile;

    // Browse for a file
    @FXML
    private void browseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a File");
        currentFile = fileChooser.showOpenDialog(new Stage());

        if (currentFile != null) {
            filePathField.setText(currentFile.getAbsolutePath());
        }
    }

    // Create a new file
    @FXML
    private void createFile() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Create a New File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File newFile = fileChooser.showSaveDialog(new Stage());

            if (newFile != null) {
                if (newFile.createNewFile()) {
                    filePathField.setText(newFile.getAbsolutePath());
                    fileContentArea.setText("New file created! Add content here...");
                    currentFile = newFile;
                } else {
                    fileContentArea.setText("Failed to create file.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            fileContentArea.setText("Error creating file: " + e.getMessage());
        }
    }

    // Open an existing file
    @FXML
    private void openFile() {
        if (filePathField.getText().isEmpty()) {
            fileContentArea.setText("No file path provided.");
            return;
        }

        try {
            currentFile = new File(filePathField.getText());
            if (currentFile.exists()) {
                String content = Files.readString(Path.of(currentFile.getPath()));
                fileContentArea.setText(content);
            } else {
                fileContentArea.setText("File not found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            fileContentArea.setText("Error reading file: " + e.getMessage());
        }
    }

    // Save changes to the file
    @FXML
    private void saveFile() {
        if (currentFile == null) {
            fileContentArea.setText("No file is currently open.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile))) {
            writer.write(fileContentArea.getText());
            fileContentArea.setText("File saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            fileContentArea.setText("Error saving file: " + e.getMessage());
        }
    }

    // Delete a file
    @FXML
    private void deleteFile() {
        if (currentFile == null || !currentFile.exists()) {
            fileContentArea.setText("No file selected or file does not exist.");
            return;
        }

        if (currentFile.delete()) {
            filePathField.clear();
            fileContentArea.setText("File deleted successfully!");
            currentFile = null;
        } else {
            fileContentArea.setText("Failed to delete the file.");
        }
    }

    // Distribute file into chunks
    @FXML
    private void distributeChunks() {
        if (currentFile == null || !currentFile.exists()) {
            fileContentArea.setText("No file selected or file does not exist.");
            return;
        }

        try {
            // Read file content
            String content = Files.readString(Path.of(currentFile.getPath()));
            int chunkSize = 1024; // Size of each chunk in bytes
            int totalChunks = (int) Math.ceil((double) content.length() / chunkSize);

            // Create chunks
            for (int i = 0; i < totalChunks; i++) {
                int start = i * chunkSize;
                int end = Math.min(start + chunkSize, content.length());
                String chunk = content.substring(start, end);

                // Write each chunk to a new file
                File chunkFile = new File(currentFile.getParent(), currentFile.getName() + ".chunk" + (i + 1));
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(chunkFile))) {
                    writer.write(chunk);
                }
            }

            fileContentArea.setText("File distributed into " + totalChunks + " chunks.");
        } catch (IOException e) {
            e.printStackTrace();
            fileContentArea.setText("Error distributing file: " + e.getMessage());
        }
    }
}