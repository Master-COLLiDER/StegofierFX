package com.mastercollider.stegofierfx.GUI.Controllers;


import com.mastercollider.stegofierfx.Encryption.RSA.RSAKeyPairGenerator;
import com.mastercollider.stegofierfx.GUI.FX.DecoderFX;
import com.mastercollider.stegofierfx.GUI.FX.EncoderFX;
import com.mastercollider.stegofierfx.GUI.FX.RSAKeyGeneratorFX;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

public class RSAKeysGeneratorFxController implements Initializable {


    File selectedDirectory = null;

    @FXML
    private Label labelSelectedDir;
    @FXML
    private Button btnMainMinimize;

    @FXML
    private Button btnMainExit;

    @FXML
    private Button btnGeneratorBrowse;

    @FXML
    private Button btnGenerate;

    @FXML
    private Button btnSideBarEncode;

    @FXML
    private Button btnSideBarDecode;

    @FXML
    private Button btnSideBarGenerateRSAKeys;


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
            try {
                DecoderFX decoderFX = new DecoderFX();
                Stage currentStage = (Stage) btnSideBarDecode.getScene().getWindow();
                decoderFX.display(currentStage);

            } catch(Exception e) {
                e.printStackTrace();
            }
        }else if (actionEvent.getSource() == btnSideBarGenerateRSAKeys) {
            System.out.println("SideBar Generate Button Pressed");
        }else if (actionEvent.getSource() == btnGeneratorBrowse){
            System.out.println("Browse Directory");
            selectOutputDirectory();
        }else if (actionEvent.getSource() == btnGenerate){
            System.out.println("Generate RSA Key Pairs");
            startGeneration();
        }
    }

    private void startGeneration() {
        try {
            RSAKeyPairGenerator.GenerateKeyToFiles(selectedDirectory.toString()+"\\");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("RSA Key Pairs Generation Successful!!!");
            alert.setTitle("Success");
            ButtonType okButton = new ButtonType("Ok", ButtonBar.ButtonData.FINISH);
            alert.getButtonTypes().setAll(okButton);
            alert.showAndWait().ifPresent(type -> {
                if (type.getButtonData()== ButtonBar.ButtonData.FINISH)
                {
                    RSAKeyGeneratorFX rsaKeyGeneratorFX = new RSAKeyGeneratorFX();
                    rsaKeyGeneratorFX.display((Stage)btnGenerate.getScene().getWindow());
                }
            });

        } catch (NoSuchAlgorithmException | IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("RSA Key Pairs Generation Failed!!!");
            alert.setTitle("Error");

            alert.showAndWait();
        }
        System.out.println("Generated");
    }


    private void selectOutputDirectory(){

        DirectoryChooser directoryChooser = new DirectoryChooser();
        File temp = directoryChooser.showDialog((Stage) btnGeneratorBrowse.getScene().getWindow());

        if (temp != null)
        {
            System.out.println(temp);
            selectedDirectory = temp;
            labelSelectedDir.setText(selectedDirectory.toString());
            btnGenerate.setDisable(false);
        }
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
