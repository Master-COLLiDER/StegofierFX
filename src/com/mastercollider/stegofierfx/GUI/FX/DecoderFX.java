package GUI.FX;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import java.io.IOException;

public class DecoderFX extends Application {


    private double xOffset,yOffset;

    @Override
    public void start(Stage primaryStage) {

    }

    public void display(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../Layouts/DecoderLayout.fxml"));

            stage.setScene(new Scene(root));

            //we gonna drag the frame
            root.setOnMousePressed(event -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });

            root.setOnMouseDragged(event -> {
                if (event.getButton().equals(MouseButton.PRIMARY))
                {
                    stage.setX(event.getScreenX() - xOffset);
                    stage.setY(event.getScreenY() - yOffset);
                }
            });
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
