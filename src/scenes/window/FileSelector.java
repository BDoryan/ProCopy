package scenes.window;

import com.jfoenix.controls.JFXButton;
import com.sun.scenario.effect.impl.state.HVSeparableKernel;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class FileSelector {

    private ArrayList<String> selects = new ArrayList<>();
    private TreeView<FileItem> tree;

    private String title;
    private File source;

    private ArrayList<File> ignoreFiles = new ArrayList<>();

    public FileSelector(String title, File source) {
        this.title = title;
        this.source = source;
    }

    public FileSelector(String title, File source,File... ignoreFiles) {
        this(title, source);
        this.ignoreFiles.addAll(Arrays.asList(ignoreFiles));
    }

    public void setSelects(ArrayList<String> selects) {
        this.selects.clear();
        this.selects.addAll(selects);
    }

    public void finish(){
        stage.close();
    }

    private ArrayList<File> getIgnoreFiles() {
        return ignoreFiles;
    }
    private Stage stage;

    public void show(Stage primaryStage) {
        stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage);

        Label message = new Label("Chargement du répertoire : "+source.getPath());
        message.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 18px;");

        VBox loading = new VBox(new ImageView(new Image("scenes/assets/images/loading.gif")));
        loading.getChildren().add(message);
        loading.setAlignment(Pos.CENTER);

        Scene scene = new Scene(loading, 500, 500);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                load();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        JFXButton finish = new JFXButton("Terminé");
                        finish.setStyle("-fx-background-color: #3260b3; -fx-font-size: 20px; -fx-font-family: 'Consolas';");
                        finish.setTextFill(Color.WHITE);

                        AnchorPane anchorPane = new AnchorPane(tree, finish);

                        AnchorPane.setTopAnchor(tree, 0d);
                        AnchorPane.setLeftAnchor(tree, 0d);
                        AnchorPane.setRightAnchor(tree, 0d);
                        AnchorPane.setBottomAnchor(tree, 40d);

                        HBox center = new HBox();
                        center.setMinHeight(40d);
                        center.setMaxHeight(40d);
                        center.setAlignment(Pos.CENTER);

                        AnchorPane.setRightAnchor(finish, 0d);
                        AnchorPane.setLeftAnchor(finish, 0d);
                        AnchorPane.setBottomAnchor(finish, 0d);
                        finish.setOnAction(event -> {
                            finish();
                        });

                        scene.setRoot(anchorPane);
                    }
                });
            }
        }).start();
    }

    public void load(){
        FileItem sourceItem = new FileItem(source);
        TreeItem<FileItem> sourceTree = new TreeItem<>(sourceItem);
        sourceTree.setExpanded(true);

        tree = new TreeView<>(sourceTree);
        tree.setStyle("-fx-focus-color: transparent; -fx-font-family: 'Consolas';");

        loadDirectory(sourceTree, source);
    }

    private void loadDirectory(TreeItem<FileItem> tree, File source){
        if(source.listFiles() == null || source.listFiles().length < 0) return;
        for(File file : Objects.requireNonNull(source.listFiles(pathname -> {
            return !ignoreFiles.contains(pathname);
        }))){
            FileItem fileItem = new FileItem(file);
            TreeItem<FileItem> fileTree = new TreeItem<>(fileItem);
            fileTree.setGraphic(fileItem.getIcon());

            boolean selected = selects.contains(file.getPath());

            if(selected || tree.getValue().isSelected())
                fileItem.setSelected(true);

            fileItem.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if(newValue){
                        addSelect(fileItem.getFile());
                        fileTree.setExpanded(true);
                        System.out.println("Add : "+fileItem.getFile());
                    } else {
                        removeSelect(fileItem.getFile());
                        System.out.println("Remove : "+fileItem.getFile());
                    }

                    for(TreeItem<FileItem> item : fileTree.getChildren()){
                        FileItem value = item.getValue();
                        value.setSelected(newValue);
                    }
                }
            });
            fileTree.expandedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    BooleanProperty bb = (BooleanProperty) observable;
                    TreeItem t = (TreeItem) bb.getBean();
                    // Do whatever with t
                    if(newValue){
                        fileTree.getChildren().clear();
                        loadDirectory(fileTree, file);
                    }
                }
            });

            if(selected && file.isDirectory()){
                loadDirectory(fileTree, file);
            } else if (!selected && file.isDirectory()){
                if(file.listFiles() != null && file.listFiles().length > 0){
                    FileItem fakeItem = new FileItem(new File("~"));
                    TreeItem<FileItem> fakeTree = new TreeItem<>(fakeItem);
                    fileTree.getChildren().add(fakeTree);
                }
            }
            tree.getChildren().add(fileTree);
        }
        tree.getChildren().sort(Comparator.comparing(t -> t.getValue().getFile().isDirectory() ? 0 : 1));
    }

    public void addSelect(File base) {
        if(base.isDirectory() && base.listFiles() != null && base.listFiles().length > 0){
            for(File file : base.listFiles()){
                if(file.isDirectory()){
                    addSelect(file);
                } else {
                    selects.add(file.getPath());
                }
            }
        }
        selects.add(base.getPath());
    }

    public void removeSelect(File base){
        if(base.isDirectory()){
            for(File file : base.listFiles()){
                if(file.isDirectory()){
                    removeSelect(file);
                } else {
                    selects.remove(file.getPath());
                }
            }
        }
        selects.remove(base.getPath());
    }

    public ArrayList<String> getSelects() {
        return selects;
    }

    public ArrayList<File> getFileSelects() {
        ArrayList<File> files = new ArrayList<>();
        selects.forEach(file -> {
            files.add(new File(file));
        });
        return files;
    }
}
