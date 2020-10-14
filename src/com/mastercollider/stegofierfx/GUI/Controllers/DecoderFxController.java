/*******************************************************************************
 * Master-COLLiDER CONFIDENTIAL
 * @author Probal D. Saikia.
 * Github.com/Master-COLLiDER
 * Copyright (c) 2020 - 2020.
 * NOTICE: This file is subject to the terms and conditions defined
 *  in file 'LICENSE.txt' which is part of this source code package.
 *
 ******************************************************************************/

package com.mastercollider.stegofierfx.GUI.Controllers;


import com.jfoenix.controls.JFXCheckBox;
import com.mastercollider.stegofierfx.CLI.CLIExecutor;
import com.mastercollider.stegofierfx.CLI.DecodeCLIParameters;
import com.mastercollider.stegofierfx.Color.ColorChannel;
import com.mastercollider.stegofierfx.Decoder.Decoder;
import com.mastercollider.stegofierfx.Encryption.EncryptionType;
import com.mastercollider.stegofierfx.GUI.FX.EncoderFX;
import com.mastercollider.stegofierfx.GUI.FX.RSAKeyGeneratorFX;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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

public class DecoderFxController implements Initializable {

    File selectedCoverImageFile = null;
    File selectedPrivateKey = null;
    File selectedOutputFile = null;

    ColorChannel decodedColorChannel = null;
    EncryptionType decodedEncryptionType = null;
    int decodedNoOfLSB = -1;


    @FXML public Label labelDecryptionHeader;
    @FXML public Label labelNoOFLSB;
    @FXML public Label labelSelectedCoverImage;
    @FXML public Button btnBrowseCoverImage;
    @FXML public Label labelInvalidCoverImage;
    @FXML public TextArea textAreaOutput;
    @FXML public VBox groupDecryption;
    @FXML public HBox groupPassword;
    @FXML public PasswordField passwordField;
    @FXML public HBox groupPrivateKey;
    @FXML public Label labelSelectedPrivateKey;
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

        jfxCheckboxOutput.setOnAction(event -> {
            doImageDetailsUpdate();
            if (jfxCheckboxOutput.isSelected())
            {
                groupOutputFile.setDisable(false);
            }else {
                groupOutputFile.setDisable(true);
            }
        });
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
        labelInvalidCoverImage.setVisible(false);
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        FileChooser.ExtensionFilter extFilterBMP = new FileChooser.ExtensionFilter("Bitmap files (*.bmp)", "*.BMP");
        fileChooser.getExtensionFilters().addAll( extFilterPNG,extFilterBMP);
        File temp = fileChooser.showOpenDialog((Stage) btnBrowseCoverImage.getScene().getWindow());
        boolean isStego = false;
        if (temp!=null)
        {
            System.out.println(temp);
            try {
                Decoder stegoChecker = new Decoder(temp.toString());
                if (!(isStego=stegoChecker.isStegoImage()))
                {

                    labelInvalidCoverImage.setTextFill(Color.RED);
                    labelInvalidCoverImage.setText("\""+temp.getName()+"\" is not a Stego Image!");
                    labelInvalidCoverImage.setVisible(true);
                }
                if (isStego){
                    labelInvalidCoverImage.setTextFill(Color.GREEN);
                    labelInvalidCoverImage.setText("Encoded Data Detected!");
                    labelInvalidCoverImage.setVisible(true);
                    selectedCoverImageFile = temp;
                    labelSelectedCoverImage.setText(selectedCoverImageFile.toString());
                    stegoChecker.DecodeHeader();
                    this.decodedEncryptionType = stegoChecker.getEncryptionType();
                    this.decodedColorChannel = stegoChecker.getColorChannel();
                    this.decodedNoOfLSB = stegoChecker.getNo_of_LSB();
                    BufferedImage bufferedImage = ImageIO.read(selectedCoverImageFile);
                    Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                    imageViewCoverImage.setImage(image);
                    groupImageDetails.setDisable(false);
                    jfxCheckboxOutput.setDisable(false);
                    if (decodedEncryptionType !=EncryptionType.NOT_ENCRYPTED)
                    {
                        labelDecryptionHeader.setDisable(false);
                        if (decodedEncryptionType == EncryptionType.RSA){
                            groupPrivateKey.setDisable(false);
                            groupPassword.setDisable(true);
                        }else {
                            groupPrivateKey.setDisable(true);
                            groupPassword.setDisable(false);
                        }
                    }else {
                        labelDecryptionHeader.setDisable(true);
                        groupPrivateKey.setDisable(true);
                        groupPassword.setDisable(true);
                    }

                    doImageDetailsUpdate();
                }
            } catch ( IOException e) {
                labelInvalidCoverImage.setText("Invalid Image");
                labelInvalidCoverImage.setVisible(true);
            }
        }
    }

    public void openChooseOutputFile(){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilterTxt = new FileChooser.ExtensionFilter("Text File (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().addAll( extFilterTxt);
        File temp = fileChooser.showSaveDialog(((Stage) btnBrowseOutput.getScene().getWindow()));
        if (temp!=null)
        {

            System.out.println(temp.toString());
            selectedOutputFile = temp;
            labelSelectedOutput.setText(selectedOutputFile.toString());
            doImageDetailsUpdate();
        }
    }
    public void openChoosePrivateKey(){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilterSFPVTK = new FileChooser.ExtensionFilter("Stegofier RSA Private Key (*.sfpvtk)", "*.SFPVTK");
        fileChooser.getExtensionFilters().addAll( extFilterSFPVTK);
        File temp = fileChooser.showOpenDialog(((Stage) btnBrowsePrivateKey.getScene().getWindow()));
        if (temp!=null)
        {
            System.out.println(temp.toString());
            selectedPrivateKey = temp;
            labelSelectedPrivateKey.setText(selectedPrivateKey.toString());
            doImageDetailsUpdate();
        }
    }

    public void doImageDetailsUpdate()
    {
        labelDecodingError.setVisible(false);
        labelDecryptionError.setVisible(false);

        if (selectedCoverImageFile != null){
            if (decodedEncryptionType  == EncryptionType.AES256){
                if (passwordField.getLength()>0){
                    if (jfxCheckboxOutput.isSelected()) {
                        if (selectedOutputFile!=null){
                            btnStartDecoding.setDisable(false);
                        }
                        else {
                            btnStartDecoding.setDisable(true);
                            labelDecodingError.setText("Please Select an Output File or uncheck");
                            labelDecodingError.setVisible(true);
                        }
                    }else{
                        btnStartDecoding.setDisable(false);
                    }
                }else {
                    btnStartDecoding.setDisable(true);
                    labelDecryptionError.setText("Enter password for decryption");
                    labelDecodingError.setText("Enter password for decryption");
                    labelDecodingError.setVisible(true);
                    labelDecryptionError.setVisible(true);
                }
            }else
            if (decodedEncryptionType  == EncryptionType.TRIPLEDES){
                if (passwordField.getLength()>=24){
                    if (jfxCheckboxOutput.isSelected()) {
                        if (selectedOutputFile!=null){
                            btnStartDecoding.setDisable(false);
                        }
                        else {
                            btnStartDecoding.setDisable(true);
                            labelDecodingError.setText("Please Select an Output File or uncheck");
                            labelDecodingError.setVisible(true);
                        }
                    }else{
                        btnStartDecoding.setDisable(false);
                    }
                }else {
                    btnStartDecoding.setDisable(true);
                    labelDecryptionError.setText("Enter 24 character password for decryption");
                    labelDecodingError.setText("Enter 24 character password for decryption");
                    labelDecodingError.setVisible(true);
                    labelDecryptionError.setVisible(true);
                }
            }else if (decodedEncryptionType  == EncryptionType.RSA){
                if (selectedPrivateKey!=null){
                    if (jfxCheckboxOutput.isSelected()) {
                        if (selectedOutputFile!=null){
                            btnStartDecoding.setDisable(false);
                        }
                        else {
                            btnStartDecoding.setDisable(true);
                            labelDecodingError.setText("Please Select an Output File or uncheck");
                            labelDecodingError.setVisible(true);
                        }
                    }else{
                        btnStartDecoding.setDisable(false);
                    }
                }else {
                    btnStartDecoding.setDisable(true);
                    labelDecryptionError.setText("Select the Private key for decryption");
                    labelDecodingError.setText("Select the Private key for decryption");
                    labelDecodingError.setVisible(true);
                    labelDecryptionError.setVisible(true);
                }
            }else{
                if (jfxCheckboxOutput.isSelected()) {
                    if (selectedOutputFile!=null){
                        btnStartDecoding.setDisable(false);
                    }
                    else {
                        btnStartDecoding.setDisable(true);
                        labelDecodingError.setText("Please Select an Output File or uncheck");
                        labelDecodingError.setVisible(true);
                        }
                    }else{
                        btnStartDecoding.setDisable(false);
                }
            }
        }else {
            btnStartDecoding.setDisable(true);
        }

        labelNoOFLSB.setText((decodedNoOfLSB+1)+" Bits");
        labelEncryptionType.setText(decodedEncryptionType.name());
        labelColorChannel.setText(decodedColorChannel.name());
    }

    public void doStartDecoding()
    {
        System.out.println("Carrier Image : "+selectedCoverImageFile);
        System.out.println("Output File : "+selectedOutputFile);
        System.out.println("Password : "+passwordField.getText());
        System.out.println("Selected PrivateKey : "+selectedPrivateKey);

        DecodeCLIParameters decodeArgs = new DecodeCLIParameters();
        decodeArgs.coverImageFile = selectedCoverImageFile.toPath();
        if (selectedOutputFile!=null)
            decodeArgs.outputFile = selectedOutputFile.toPath();
        if (selectedPrivateKey!=null)
            decodeArgs.privateKeyFile = selectedPrivateKey.toPath();
        if (!passwordField.getText().equals("")|| passwordField.getText()!=null)
             decodeArgs.password = passwordField.getText();

        try {
            String message = CLIExecutor.decode(decodeArgs);
            textAreaOutput.setText(message);
        } catch (Exception e) {
            labelDecodingError.setVisible(true);
            labelDecodingError.setText("Wrong Password or Private Key");
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
