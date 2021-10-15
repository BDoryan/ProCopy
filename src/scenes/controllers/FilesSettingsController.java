package scenes.controllers;

import com.jfoenix.controls.*;
import doryanbessiere.procopy.fr.ProCopy;
import doryanbessiere.procopy.fr.ProCopyListener;
import doryanbessiere.procopy.fr.configuration.ProCopyConfiguration;
import doryanbessiere.procopy.fr.configuration.ProCopyType;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import scenes.LoaderManager;
import scenes.Main;

import java.io.File;

public class FilesSettingsController {

    private static ProCopyConfiguration configuration = new ProCopyConfiguration();

    @FXML
    private JFXCheckBox ignore_error;

    @FXML
    private JFXCheckBox overwrite;

    @FXML
    private JFXCheckBox copy_emptyfile;

    @FXML
    private JFXCheckBox copy_emptydirectory;

    @FXML
    private JFXCheckBox copy_hiddenfile;

    @FXML
    private JFXCheckBox copy_hiddendirectory;

    @FXML
    private JFXCheckBox copy_subdirectories;

    @FXML
    private JFXCheckBox clear_destination_directory;

    @FXML
    private JFXCheckBox use_multithreading;

    @FXML
    private JFXTextField thread_count;

    @FXML
    private JFXButton finish_button;

    @FXML
    private JFXTextField extensions_ignore;

    public static ProCopyConfiguration getConfiguration() {
        return configuration;
    }

    public void initialize(){
        ignore_error.selectedProperty().addListener((observable, oldValue, newValue) -> {
            configuration.setIgnoreError(ignore_error.isSelected());
        });
        overwrite.selectedProperty().addListener((observable, oldValue, newValue) -> {
            configuration.setOverwrite(overwrite.isSelected());
        });

        copy_emptydirectory.selectedProperty().addListener((observable, oldValue, newValue) -> {
            configuration.setBlacklists(ProCopyType.EMPTY_DIRECTORY, !copy_emptydirectory.isSelected());
        });
        copy_emptyfile.selectedProperty().addListener((observable, oldValue, newValue) -> {
            configuration.setBlacklists(ProCopyType.EMPTY_FILE, !copy_emptyfile.isSelected());
        });

        copy_hiddenfile.selectedProperty().addListener((observable, oldValue, newValue) -> {
            configuration.setBlacklists(ProCopyType.EMPTY_FILE, !copy_hiddenfile.isSelected());
        });
        copy_hiddendirectory.selectedProperty().addListener((observable, oldValue, newValue) -> {
            configuration.setBlacklists(ProCopyType.EMPTY_DIRECTORY, !copy_hiddendirectory.isSelected());
        });

        copy_subdirectories.selectedProperty().addListener((observable, oldValue, newValue) -> {
            configuration.setBlacklists(ProCopyType.SUB_DIRECTORIES, !copy_subdirectories.isSelected());
        });

        clear_destination_directory.selectedProperty().addListener((observable, oldValue, newValue) -> {
            configuration.setClearDestinationDirectory(clear_destination_directory.isSelected());
        });

        use_multithreading.selectedProperty().addListener((observable, oldValue, newValue) -> {
            configuration.setUseMultiThreading(use_multithreading.isSelected());
        });

        thread_count.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    thread_count.setText(newValue.replaceAll("[^\\d]", ""));
                    configuration.setMaxThread(Integer.valueOf(thread_count.getText()));
                }
            }
        });

        extensions_ignore.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                System.out.println(extensions_ignore.getText());
                if(extensions_ignore.getText() != null && !extensions_ignore.getText().isEmpty()){
                    configuration.setIgnoreExtensions(extensions_ignore.getText().split(","));
                } else {
                    configuration.setIgnoreExtensions(null);
                }
            }
        });

        finish_button.setOnAction(event -> {
            LoaderManager.MAIN_CONTAINER.setCenter(LoaderManager.FILES_CONTAINER);
        });
    }
}
