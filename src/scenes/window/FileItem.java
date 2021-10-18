package scenes.window;

import com.jfoenix.controls.JFXCheckBox;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class FileItem extends JFXCheckBox {

    private JFXCheckBox checkBox;
    private File file;
    private ImageView icon;

    public FileItem(File file) {
        super(file.getName());
        setMaxSize(16,16);
        this.file = file;

        String path = "scenes/assets/images/icons/"+ (file.isDirectory() ? "directory" : "file")+".png";
        icon = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream(path)));
        icon.setFitHeight(16);
        icon.setFitWidth(16);
        if(file.isHidden()){
            icon.setOpacity(0.3);
        }
    }

    private boolean byParent  =false;

    public void setByParent(boolean byParent) {
        this.byParent = byParent;
    }

    private boolean byParent(){
        return byParent;
    }

    public JFXCheckBox getCheckBox() {
        return checkBox;
    }

    public File getFile() {
        return file;
    }

    public ImageView getIcon() {
        return icon;
    }

    @Override
    public String toString() {
        return file.getName();
    }
}
