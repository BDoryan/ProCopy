package scenes.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import doryanbessiere.procopy.fr.configuration.ProCopyConfiguration;
import doryanbessiere.procopy.fr.configuration.ProCopyType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import scenes.LoaderManager;

public class UsersSettingsController {

    private static ProCopyConfiguration configuration = new ProCopyConfiguration();

    public static ProCopyConfiguration getConfiguration() {
        return configuration;
    }

    @FXML
    private JFXButton finish_button;

    public void initialize(){
        finish_button.setOnAction(event -> {
            LoaderManager.MAIN_CONTAINER.setCenter(LoaderManager.FILES_CONTAINER);
        });
    }
}
