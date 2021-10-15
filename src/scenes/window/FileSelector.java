package scenes.window;

import javafx.scene.Scene;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public class FileSelector {

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

    public ArrayList<File> getIgnoreFiles() {
        return ignoreFiles;
    }

    public void show(Stage primaryStage) {
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage);

        load();

        Scene scene = new Scene(tree, 500, 500);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

    public void load(){
        FileItem sourceItem = new FileItem(source);
        TreeItem<FileItem> sourceTree = new TreeItem<>(sourceItem);
        sourceTree.setExpanded(true);

        tree = new TreeView<>(sourceTree);
        tree.setStyle("-fx-font-family: 'Consolas';");
        tree.getStylesheets().add("scenes/assets/css/checkbox.css");

        loadDirectory(sourceTree, source);
        tree.setCellFactory(new Callback<TreeView<FileItem>, TreeCell<FileItem>>() {
            @Override
            public TreeCell<FileItem> call(TreeView<FileItem> param) {
                CheckBoxTreeCell<FileItem> node = new CheckBoxTreeCell<FileItem>();
                node.getStyleClass().add("jfx-check-box");
                return node;
            }
        });
    }

    private void loadDirectory(TreeItem<FileItem> tree, File source){
        if(source.listFiles() == null || source.listFiles().length < 0)return;
        for(File file : Objects.requireNonNull(source.listFiles(pathname -> {
            return !ignoreFiles.contains(pathname);
        }))){
            FileItem fileItem = new FileItem(file);
            TreeItem<FileItem> fileTree = new TreeItem<>(fileItem);
            fileTree.setGraphic(fileItem.getIcon());
            if(file.isDirectory()){
                loadDirectory(fileTree, file);
                tree.getChildren().add(fileTree);
            } else {
                tree.getChildren().add(fileTree);
            }
        }
        tree.getChildren().sort(Comparator.comparing(t -> t.getValue().getFile().isDirectory() ? 0 : 1));
    }
}
