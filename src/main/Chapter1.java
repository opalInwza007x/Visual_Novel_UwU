package main;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import Util.Chapter;
import Util.TextBase;

public class Chapter1 extends Chapter {
    private ImageView friendImage;
    private ImageView cashenImage;

    @Override
    public void startChapter(Stage primaryStage) {
        stateSetup(primaryStage);
    }
    
    @Override
    protected void stateSetup(Stage primaryStage) {
        playBackgroundMusic("/resources/sound/bgChap1T.mp3");
        setStoryTexts("/resources/texts/Chapter1.txt");

        ImageView background = setupBackground("/resources/background/BackgroundChapter1.jpg");
        TextFlow textBox = createTextFlow();
        Button nextButton = createNextButton(primaryStage, textBox);

        friendImage = createSpeakerImage("เพื่อน");
        cashenImage = createSpeakerImage("คเชน");
        updateSpeakerVisibility();

        // Stack text box background and text
        StackPane textBoxStack = createTextBoxStack(textBox);
        StackPane textBoxWithButton = createTextBoxWithButton(textBoxStack, nextButton);

        // Speaker images container
        HBox speakerPane = new HBox(80, cashenImage, friendImage);
        speakerPane.setAlignment(Pos.BOTTOM_CENTER);

        stackPane = new StackPane(background, speakerPane);
        
        // Timeline is created but NOT played yet
        timeline = createTimeline(textBox);
        // Remove the timeline.play() call from here

        // Setup root directly
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        root.getChildren().addAll(stackPane, textBoxWithButton);

        // Setup scene directly
        enterAnimation(root, textBox);  // Pass textBox to enterAnimation
        primaryStage.setScene(new Scene(root, 968, 648, Color.BLACK));
        primaryStage.setTitle("Visual Novel - Chapter 1");
    }
    
    @Override
    public ImageView createSpeakerImage(String speaker) {
        String imagePath;
        int width, height;

        switch (speaker) {
            case "คเชน":
                imagePath = "/resources/cashen/cashen_normal.png";
                width = 200;
                height = 300;
                break;
            case "เพื่อน":
                imagePath = "/resources/friend/friend_normal.png";
                width = 260;
                height = 300;
                break;
            case "อาริสา":
                imagePath = "/resources/arisa/Arisa_shy3_darkMarkMark.png";
                width = 220;
                height = 310;
                break;
            default:
                System.out.println("Unknown speaker: " + speaker);
                return new ImageView(); // TODO Unknown speaker
        }

        ImageView img = createImageView(imagePath, width, height);
        
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.BLACK);
        dropShadow.setRadius(10);
        dropShadow.setSpread(0.1);
        img.setEffect(dropShadow);

        return img;
    }

    @Override
    public void updateCharacterImages() {
        String currentSpeaker = storyTexts.getStoryTexts().get(currentTextIndex)[1];
        String emotion = storyTexts.getStoryTexts().get(currentTextIndex)[0];

        if (currentSpeaker.equals("คเชน")) {
            cashenImage.setImage(new Image(getClass().getResource(getImagePath("คเชน", emotion)).toExternalForm()));
        } else {
            friendImage.setImage(new Image(getClass().getResource(getImagePath("เพื่อน", emotion)).toExternalForm()));
        }
    }

    @Override
    public void updateSpeakerVisibility() {
        String currentSpeaker = storyTexts.getStoryTexts().get(currentTextIndex)[1];
        
        // Update speaker visibility without animations
        if (currentSpeaker.equals("คเชน")) {
            cashenImage.setOpacity(1.0);
            friendImage.setOpacity(0.6);
        } else {
            cashenImage.setOpacity(0.6);
            friendImage.setOpacity(1.0);
        }
    }

    @Override
    public void setStoryTexts(String url) {
        storyTexts = new TextBase(url);
    }
    
    @Override
    public void goToNextChapter(Stage primaryStage) {
    	// exitAnimation(primaryStage);
    	backgroundMusic.stop();
    	
    	Chapter2 chapter2 = new Chapter2();
        chapter2.startChapter(primaryStage);
    }
}