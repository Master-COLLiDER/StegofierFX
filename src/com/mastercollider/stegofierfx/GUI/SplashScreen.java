/*******************************************************************************
 * Master-COLLiDER CONFIDENTIAL
 * @author Probal D. Saikia.
 * Github.com/Master-COLLiDER
 * Copyright (c) 2020 - 2020.
 * NOTICE: This file is subject to the terms and conditions defined
 *  in file 'LICENSE.txt' which is part of this source code package.
 *
 ******************************************************************************/

package com.mastercollider.stegofierfx.GUI;

import com.mastercollider.stegofierfx.GUI.EncoderFX;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class SplashScreen extends Application {



    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("layouts/SplashScreenLayout.fxml"));
        Parent root = loader.load();
        stage.setTitle("StegofierFX 1.0");
        Image icon = new Image(EncoderFX.class.getResourceAsStream("images/logo_250x250.png"));
        stage.getIcons().add(icon);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root));

        stage.show();

        PauseTransition splashScreenDelay = new PauseTransition(Duration.seconds(3));
        splashScreenDelay.setOnFinished(f -> {

            openEncoderFx(new Stage());
            stage.close();

        });
        splashScreenDelay.playFromStart();
    }

    void openEncoderFx(Stage stage){
        EncoderFX encoderFx = new EncoderFX();
        try {
            encoderFx.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
