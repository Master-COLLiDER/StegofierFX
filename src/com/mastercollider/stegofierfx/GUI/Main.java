package com.mastercollider.stegofierfx.GUI;


import com.mastercollider.stegofierfx.GUI.FX.EncoderFX;
import com.mastercollider.stegofierfx.GUI.FX.SplashScreen;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

//
//        EncoderFX encoderFx = new EncoderFX();
//        encoderFx.start(primaryStage);
        SplashScreen splashScreen = new SplashScreen();
        splashScreen.start(primaryStage);

    }

    public static void main(String[] args) {
        launch(args);
    }
}
