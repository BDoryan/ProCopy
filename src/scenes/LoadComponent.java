package scenes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;

public class LoadComponent {

    private FXMLLoader loader;
    private Object controller;
    private Node node;

    public LoadComponent(FXMLLoader loader, Object controller) throws IOException {
        this.loader = loader;
        this.controller = controller;
        this.loader.setController(controller);
        this.node = loader.load();
    }

    public Node getNode() {
        return node;
    }

    public FXMLLoader getLoader() {
        return loader;
    }

    public Object getController() {
        return controller;
    }
}
