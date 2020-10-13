package com.mastercollider.stegofierfx.GUI.Controllers;


import com.jfoenix.controls.JFXCheckBox;
import com.mastercollider.stegofierfx.GUI.FX.EncoderFX;
import com.mastercollider.stegofierfx.GUI.FX.RSAKeyGeneratorFX;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class DecoderFxController implements Initializable {


    @FXML public Label labelSelectedCoverImage;
    @FXML public Button btnBrowseCoverImage;
    @FXML public Label labelInvalidCoverImage;
    @FXML public TextArea textAreaOutput;
    @FXML public VBox groupDecryption;
    @FXML public HBox groupPassword;
    @FXML public PasswordField passwordField;
    @FXML public HBox groupPrivateKey;
    @FXML public Label labelSelectedPrivteKey;
    @FXML public Button btnBrowsePrivateKey;
    @FXML public Label labelDecryptionError;
    @FXML public ImageView imageViewCoverImage;
    @FXML public GridPane groupImageDetails;
    @FXML public Label labelEncryptionType;
    @FXML public Label labelColorChannel;
    @FXML public Button btnStartDecoding;
    @FXML public Label labelDecodingError;
    @FXML public HBox groupOutputFile;
    @FXML public Label labelSelectedOutput;
    @FXML public Button btnBrowseOutput;
    @FXML public JFXCheckBox jfxCheckboxOutput;


    @FXML private Button btnMainMinimize;
    @FXML private Button btnMainExit;
    @FXML private Button btnSideBarEncode;
    @FXML private Button btnSideBarDecode;
    @FXML private Button btnSideBarGenerateRSAKeys;


    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }

    public void handleClicks(ActionEvent actionEvent) {


        if (actionEvent.getSource() == btnSideBarEncode) {

            try {
                EncoderFX encoderFX = new EncoderFX();
                Stage currentStage = (Stage) btnSideBarEncode.getScene().getWindow();
                encoderFX.display(currentStage);

            } catch(Exception e) {
                e.printStackTrace();
            }

        }
        else if (actionEvent.getSource() == btnSideBarDecode) {
            System.out.println("Sidebar Decode Button Pressed");
        }else if (actionEvent.getSource() == btnSideBarGenerateRSAKeys) {
            try {
                RSAKeyGeneratorFX rsaKeyGeneratorFX = new RSAKeyGeneratorFX();
                Stage currentStage = (Stage) btnSideBarGenerateRSAKeys.getScene().getWindow();
                rsaKeyGeneratorFX.display(currentStage);

            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void openChooseCoverImage(){

    }

    public void doMinimizeApplication()
    {
        Stage stage = (Stage) btnMainMinimize.getScene().getWindow();
        stage.setIconified(true);
    }

    public void doCloseApplication()
    {
        System.out.println("Exit Button Pressed");
        Stage stage = (Stage) btnMainExit.getScene().getWindow();
        stage.close();
    }

    public void openGithub(ActionEvent event)
    {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/Master-COLLiDER"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
