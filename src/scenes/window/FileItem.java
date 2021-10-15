package scenes.window;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class FileItem {

    private File file;
    private ImageView icon;
    private boolean selected = false;

    public FileItem(File file) {
        this.file = file;

        String path = "scenes/assets/images/icons/"+ (file.isDirectory() ? "directory" : "file")+".png";
        icon = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream(path)));
        icon.setFitHeight(16);
        icon.setFitWidth(16);
        if(file.isHidden()){
            icon.setOpacity(0.3);
        }
    }

    public File getFile() {
        return file;
    }

    public ImageView getIcon() {
        return icon;
    }

    public boolean isSelected() {
        return selected;
    }

    @Override
    public String toString() {
        return file.getName();
    }
}
