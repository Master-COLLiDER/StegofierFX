package com.mastercollider.stegofierfx.GUI.Controllers;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXSlider;
import com.mastercollider.stegofierfx.GUI.FX.DecoderFX;
import com.mastercollider.stegofierfx.GUI.FX.RSAKeyGeneratorFX;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class EncoderFxController implements Initializable {

    File selectedCoverImageFile =  null;
    File selectedOutputImageFile = null;


    @FXML
    private Button btnMainMinimize;
    @FXML
    private Button btnMainExit;
    @FXML
    private Button btnSideBarEncode;
    @FXML
    private Button btnSideBarDecode;
    @FXML
    private Button btnSideBarGenerateRSAKeys;

    @FXML private Button btnBrowseCoverImage;
    @FXML private Label labelSelectedCoverImage;
    @FXML private Label labelInvalidCoverImage;
    @FXML private Button btnBrowseOutputFile;
    @FXML private Label labelSelectedOutputFile;
    @FXML private Label labelInvalidOutputImage;
    @FXML private javafx.scene.control.TextArea textAreaMessage;
    @FXML private Label labelMessageCharacterLength;
    @FXML private Label labelInvalidMessage;
    @FXML private Label labelEncryptionHeader;
    @FXML private Label labelEncryptionErrorMessage;
    @FXML private VBox groupEncryption;
    @FXML private JFXCheckBox jfxCheckboxEncryptMessage;
    @FXML private HBox groupEncryptionType;
    @FXML private RadioButton radioButtonAES;
    @FXML private RadioButton radioButtonTripleDES;
    @FXML private RadioButton RadioButtonRSA;
    @FXML private HBox groupPassword;
    @FXML private PasswordField passwordField;
    @FXML private HBox groupPublicKey;
    @FXML private Label labelSelectedPublicKey;
    @FXML private Button btnBrowsePublicKey;
    @FXML private ImageView ImageviewEncoder;
    @FXML private GridPane groupImageDetails;
    @FXML private Label labelTextSize;
    @FXML private Label labelEncryptedTextSize;
    @FXML private Label labelMaxRequiredPixels;
    @FXML private Label labelTotalAvailablePixels;
    @FXML private VBox groupAditionalOptions;
    @FXML private JFXCheckBox jfxCheckBoxRed;
    @FXML private JFXCheckBox jfxCheckBoxGreen;
    @FXML private JFXCheckBox jfxCheckBoxBlue;
    @FXML private JFXSlider jfxSliderLSB;
    @FXML private Label labelEncodingError;
    @FXML private Button btnStartEncoding;


    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }


    public void handleClicks(ActionEvent actionEvent) {

        if (actionEvent.getSource() == btnSideBarEncode) {
            System.out.println("SideBar Encode Button Pressed");
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
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        FileChooser.ExtensionFilter extFilterBMP = new FileChooser.ExtensionFilter("Bitmap files (*.bmp)", "*.BMP");
        fileChooser.getExtensionFilters().addAll( extFilterPNG,extFilterBMP);
        File temp = fileChooser.showOpenDialog((Stage) btnBrowseCoverImage.getScene().getWindow());
        if (temp!=null)
        {
            System.out.println(temp.toString());
            selectedCoverImageFile = temp;
            labelSelectedCoverImage.setText(selectedCoverImageFile.toString());
            try {
                BufferedImage bufferedImage = ImageIO.read(selectedCoverImageFile);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                ImageviewEncoder.setImage(image);

            } catch (IOException e) {
                e.printStackTrace();
                labelSelectedCoverImage.setText("Failed to Load");
            }finally {
                if (selectedOutputImageFile!=null){
                    groupEncryption.setDisable(false);
                    groupImageDetails.setDisable(false);
                    groupAditionalOptions.setDisable(false);
                    btnStartEncoding.setDisable(false);
                }
            }
        }
    }
    public void openChooseOutputImage(){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        FileChooser.ExtensionFilter extFilterBMP = new FileChooser.ExtensionFilter("Bitmap files (*.bmp)", "*.BMP");
        fileChooser.getExtensionFilters().addAll( extFilterPNG,extFilterBMP);
        File temp = fileChooser.showSaveDialog(((Stage) btnBrowseOutputFile.getScene().getWindow()));
        if (temp!=null)
        {
            System.out.println(temp.toString());
            selectedOutputImageFile = temp;
            labelSelectedOutputFile.setText(selectedOutputImageFile.toString());

            if (selectedCoverImageFile!=null){
                groupEncryption.setDisable(false);
                groupImageDetails.setDisable(false);
                groupAditionalOptions.setDisable(false);
                btnStartEncoding.setDisable(false);
            }
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
