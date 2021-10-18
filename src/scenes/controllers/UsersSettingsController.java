package scenes.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import doryanbessiere.procopy.fr.configuration.ProCopyConfiguration;
import doryanbessiere.procopy.fr.configuration.ProCopyType;
import doryanbessiere.procopy.fr.configuration.user.Directories;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import scenes.LoaderManager;
import scenes.Main;
import scenes.window.FileSelector;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class UsersSettingsController {

    private static ProCopyConfiguration configuration = new ProCopyConfiguration();

    public static ProCopyConfiguration getConfiguration() {
        return configuration;
    }

    @FXML
    private JFXButton finish_button;

    @FXML
    private JFXButton select_files;

    @FXML
    private VBox box;

    @FXML
    private JFXCheckBox copy_saved_games;

    private HashMap<Directories, JFXCheckBox> checkboxs = new HashMap<>();

    public void initialize(){
        configuration.setSelectAll(false);
        for(Directories directory : Directories.values()){
            File fileDirectory = new File(UsersController.userFile, directory.getName());
            JFXCheckBox checkbox = new JFXCheckBox("Copier le dossier '"+directory.getFr()+"' du répertoire utilisateur.");
            checkbox.setTextFill(Color.WHITE);
            checkbox.setStyle("-fx-font-size: 14px; -fx-font-family: 'Consolas';");
            checkbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue){
                    configuration.addSelect(fileDirectory);
                } else {
                    configuration.removeSelect(fileDirectory);
                }
            });
            checkbox.setSelected(true);
            checkboxs.put(directory, checkbox);
            configuration.addSelect(fileDirectory);
            box.getChildren().add(checkbox);
        }

        select_files.setOnAction(event -> {
            FileSelector fileSelector = new FileSelector("Sélectionner les fichiers à importer", UsersController.userFile){
                @Override
                public void finish() {
                    super.finish();
                    configuration.setSelects(getSelects());
                }
            };
            fileSelector.setSelects(configuration.getSelects());
            fileSelector.show(Main.stage);
        });

        finish_button.setOnAction(event -> {
            LoaderManager.MAIN_CONTAINER.setCenter(LoaderManager.USERS_CONTAINER);
        });
    }
}
