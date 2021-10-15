package scenes;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import scenes.controllers.*;

import java.io.IOException;

public class LoaderManager {

    public static BorderPane MAIN_CONTAINER;
    public static BorderPane FILES_CONTAINER;
    public static BorderPane USERS_CONTAINER;

    public static VBox FILES_SETTINGS_CONTAINER;
    public static VBox USERS_SETTINGS_CONTAINER;

    public static void initialize() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(LoaderManager.class.getClassLoader().getResource("scenes/main.fxml"));
        loader.setController(new MainController());
        MAIN_CONTAINER = (BorderPane) loader.load();

        loader = new FXMLLoader();
        loader.setLocation(LoaderManager.class.getClassLoader().getResource("scenes/files.fxml"));
        loader.setController(new FilesController());
        FILES_CONTAINER = (BorderPane) loader.load();

        loader = new FXMLLoader();
        loader.setLocation(LoaderManager.class.getClassLoader().getResource("scenes/files.fxml"));
        loader.setController(new FilesController());
        FILES_CONTAINER = (BorderPane) loader.load();

        loader = new FXMLLoader();
        loader.setLocation(LoaderManager.class.getClassLoader().getResource("scenes/users.fxml"));
        loader.setController(new UsersController());
        USERS_CONTAINER = (BorderPane) loader.load();

        loader = new FXMLLoader();
        loader.setLocation(LoaderManager.class.getClassLoader().getResource("scenes/filesSettings.fxml"));
        loader.setController(new FilesSettingsController());
        FILES_SETTINGS_CONTAINER = (VBox) loader.load();

        loader = new FXMLLoader();
        loader.setLocation(LoaderManager.class.getClassLoader().getResource("scenes/usersSettings.fxml"));
        loader.setController(new UsersSettingsController());
        USERS_SETTINGS_CONTAINER = (VBox) loader.load();
    }
}
