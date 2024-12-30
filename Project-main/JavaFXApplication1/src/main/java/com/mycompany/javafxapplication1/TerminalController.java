/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.javafxapplication1;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class TerminalController {

    @FXML
    private TextArea terminalOutput;  // TextArea to show terminal output

    @FXML
    private TextField commandInput;   // TextField for entering commands

    // Method for the terminal button
    @FXML
    private void openTerminal(ActionEvent event) {
        terminalOutput.appendText("Terminal is now open.\n");
    }

    // Handle the command execution when the user presses the button
    @FXML
    private void handleRunCommand() {
        String command = commandInput.getText().trim();
        if (!command.isEmpty()) {
            runCommand(command);
        }
        commandInput.clear();  // Clear the input field after running the command
    }

    // This method runs the command using ProcessBuilder
    private void runCommand(String command) {
        StringBuilder output = new StringBuilder();
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.redirectErrorStream(true);  // Merge standard and error output

        try {
            // Split the command into an array for ProcessBuilder
            String[] commandArgs = command.split(" ");
            processBuilder.command(commandArgs);

            // Start the process and get the output
            Process process = processBuilder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            // Display the output in the terminal output area
            terminalOutput.appendText(output.toString());

        } catch (IOException e) {
            terminalOutput.appendText("Error executing command: " + e.getMessage() + "\n");
        }
    }

    // Optional: Allow the user to press Enter to execute the command
    @FXML
    private void handleEnterKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleRunCommand();
        }
    }
    
    
}
