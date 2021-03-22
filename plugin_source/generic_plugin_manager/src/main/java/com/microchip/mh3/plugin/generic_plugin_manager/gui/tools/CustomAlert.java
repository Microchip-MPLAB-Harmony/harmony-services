/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microchip.mh3.plugin.generic_plugin_manager.gui.tools;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;


public class CustomAlert {
    
    private static CustomAlert objAlert = null;
    
    public static CustomAlert getInstance(){
        if(objAlert==null){
            objAlert = new CustomAlert();
        }
        return objAlert;
    }
    
    public void showError(String szMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        getAlert(alert, szMessage, "Error alert").showAndWait();
    }
    
    public void showInformation(String szMessage){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        getAlert(alert, szMessage, "Information alert").showAndWait();
    }
    
    public void showWarning(String szMessage) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        getAlert(alert, szMessage, "Warning alert").showAndWait();
    }
    
    public Optional<ButtonType> showConfirmation(String szMessage){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        // option != null.
        Optional<ButtonType> option = getAlert(alert, szMessage, "Confirmation Alert").showAndWait();
        return option;
    }
    
    private Alert getAlert(Alert alert, String szMessage, String szTitle){
        alert.setHeaderText(null);
        alert.setTitle(szTitle);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setContentText(szMessage);
        return alert;
    }
}
