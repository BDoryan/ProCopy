package scenes.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import doryanbessiere.procopy.fr.ProCopy;
import doryanbessiere.procopy.fr.ProCopyListener;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import scenes.LoaderManager;
import scenes.Main;

import java.io.File;

public class FilesController {

    @FXML
    private Text source_path_text;

    @FXML
    private JFXButton source_button;

    @FXML
    private Text destination_path_text;

    @FXML
    private JFXButton destination_button;

    @FXML
    private JFXButton logs_button;

    @FXML
    private JFXButton configuration_button;

    @FXML
    private JFXButton start_button;

    @FXML
    private JFXButton stop_button;

    @FXML
    private JFXProgressBar progressbar;

    @FXML
    private Text text_progressbar;

    private File source;
    private File destination;
    private ProCopy proCopy;

    @FXML
    public void initialize(){
        start_button.setDisable(true);
        stop_button.setDisable(true);
        destination_button.setOnAction(event -> {
            destination = openDirectoryChooser("Sélectionner le répertoire de destination");
            if(destination == null){
                destination_path_text.setText("Chemin d'accès");
            } else {
                destination_path_text.setText(destination.getPath());
            }
            start_button.setDisable(source == null || destination == null);
        });
        source_button.setOnAction(event -> {
            source = openDirectoryChooser("Sélectionner le répertoire source");
            if(source == null){
                source_path_text.setText("Chemin d'accès");
            } else {
                source_path_text.setText(source.getPath());
            }

            start_button.setDisable(source == null || destination == null);
        });
        start_button.setOnAction(event -> {
            if(proCopy != null)return;

            start_button.setDisable(true);
            stop_button.setDisable(false);

            proCopy = new ProCopy(source, destination);
            proCopy.setConfiguration(FilesSettingsController.getConfiguration());
            proCopy.getListeners().add(new ProCopyListener() {
                @Override
                public void start() {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            text_progressbar.setText("Le processus est entrain de calculer la taille...");
                        }
                    });
                }

                @Override
                public void finish() {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    start_button.setDisable(false);
                                    stop_button.setDisable(true);

                                    progressbar.setProgress(1);
                                    text_progressbar.setText("Le processus est terminé, temps d'éxécution : "+getElapsed(proCopy.getElapsed()));
                                    proCopy = null;
                                }
                            });
                        }
                    });
                }

                @Override
                public void progress(long current, long total) {
                    double percentage = (100 * current) / total;

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            progressbar.setProgress((float) percentage / 100);
                            text_progressbar.setText("Le processus est en cours de progression : "+((int) percentage)+"% ("+current+"/"+total+")");
                        }
                    });
                }
            });
            proCopy.start();
        });

        stop_button.setOnAction(event -> {
            if(proCopy != null){
                proCopy.stop();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        start_button.setDisable(false);
                        stop_button.setDisable(true);
                        text_progressbar.setText("Le processus à été annulé.");
                        proCopy = null;
                    }
                });
            }
        });

        configuration_button.setOnAction(event -> {
            LoaderManager.MAIN_CONTAINER.setCenter(LoaderManager.SETTINGS_CONTAINER);
        });
    }

    private File openDirectoryChooser(String title){
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle(title);
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        return chooser.showDialog(Main.stage);
    }
    public static String getElapsed(final long ms) {
        long millis = ms % 1000;
        long x = ms / 1000;
        long seconds = x % 60;
        x /= 60;
        long minutes = x % 60;
        x /= 60;
        long hours = x % 24;

        return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, millis);
    }
}
