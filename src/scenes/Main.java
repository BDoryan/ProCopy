package scenes;

import doryanbessiere.procopy.fr.ProCopy;
import doryanbessiere.procopy.fr.configuration.ProCopyConfiguration;
import doryanbessiere.procopy.fr.configuration.ProCopyMode;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import scenes.controllers.MainController;
import scenes.window.FileSelector;

import java.io.File;

public class Main extends Application {

    public static void test() {
        ProCopy proCopy = new ProCopy(new File("C:\\Users\\Utilisateur\\Documents\\ProCopy\\source"), new File("C:\\Users\\Utilisateur\\Documents\\ProCopy\\destination"));
        proCopy.setConfiguration(new ProCopyConfiguration(
                ProCopyMode.COPY,
                true,
                true,
                false,
                false,
                true,
                true,
                true,
                false,
                true,
                2000)
        );
        proCopy.start();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    public static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        LoaderManager.initialize();

        primaryStage.setTitle("ProCopy - 0.0.1");
        primaryStage.setScene(new Scene(LoaderManager.MAIN_CONTAINER));
        primaryStage.setMinHeight(720);
        primaryStage.setMinWidth(1080);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });

        primaryStage.show();
        stage = primaryStage;

        LoaderManager.MAIN_CONTAINER.getStylesheets().add("/scenes/assets/css/jfxcombobox.css");

        //new FileSelector("test", new File(System.getProperty("user.home")+"/Documents")).show(primaryStage);
    }
}
