package scenes.controllers;

import com.jfoenix.controls.JFXButton;
import doryanbessiere.procopy.fr.commons.OsCheck;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import scenes.LoaderManager;

import java.io.IOException;

public class MainController {

    @FXML
    private BorderPane container;

    @FXML
    private JFXButton sidebar_files;

    @FXML
    private JFXButton sidebar_users;

    @FXML
    public void initialize(){
        sidebar_files.setOnAction(event -> {
            container.setCenter(LoaderManager.FILES_CONTAINER);
        });
        if(OsCheck.getOperatingSystemType() == OsCheck.OSType.Windows){
            sidebar_users.setOnAction(event -> {
                container.setCenter(LoaderManager.USERS_CONTAINER);
            });
        } else {
            sidebar_users.setDisable(true);
        }
    }
}
