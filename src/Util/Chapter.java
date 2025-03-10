package Util;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;

public abstract class Chapter implements HaveBackgroundMusic, HaveText {
    protected TextBase storyTexts;
    protected int currentTextIndex = 0;
    protected Timeline timeline = new Timeline();
    protected MediaPlayer backgroundMusic, effectPlayer, effectTalking;
    protected StackPane stackPane;
    protected StackPane choiceBoxStack = null;
    private Font speakerFont;
    private Font contentFont;
    private Text speakerTextNode;
    private Text contentTextNode;

    protected abstract void startChapter(Stage primaryStage);
    protected abstract void stateSetup(Stage primaryStage);
    protected abstract void updateCharacterImages();
    protected abstract ImageView createSpeakerImage(String speaker);
    protected abstract void updateSpeakerVisibility();
    protected abstract void setStoryTexts(String url);
    protected abstract void goToNextChapter(Stage primaryStage);
    
    @Override
    public void playBackgroundMusic(String url) {
        try {
            URL resource = getClass().getResource(url);
            if (resource != null) {
                Media media = new Media(resource.toExternalForm());
                backgroundMusic = new MediaPlayer(media);
                backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE); // เล่นวนลูป
                backgroundMusic.setVolume(0.7); // ตั้งค่าความดัง (0.0 - 1.0)
                backgroundMusic.play();
            } else {
                System.out.println("Error: Background music file not found!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadSoundEffect(List<String> emotions) {
        try {
            for (String emotion : emotions) {
                URL soundURL = getClass().getResource("/resources/sound/" + emotion + ".mp3");
                if (soundURL != null) {
                    effectPlayer = new MediaPlayer(new Media(soundURL.toExternalForm()));
                } else {
                    System.out.println("Error: Sound file for " + emotion + " not found!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected ImageView setupBackground(String url) {
        ImageView background = createImageView(url, 968, 486);
        DropShadow backgroundEffect = new DropShadow(15, Color.BLACK);
        backgroundEffect.setSpread(0.1);
        background.setEffect(backgroundEffect);
        return background;
    }

    protected StackPane createTextBoxStack(TextFlow textBox) {
        Rectangle textBoxBg = createTextBoxBackground();
        StackPane textBoxStack = new StackPane(textBoxBg, textBox);
        textBoxStack.setPadding(new Insets(0, 10, 10, 10));
        return textBoxStack;
    }

    protected StackPane createTextBoxWithButton(StackPane textBoxStack, Button nextButton) {
        StackPane textBoxWithButton = new StackPane(textBoxStack, nextButton);
        StackPane.setAlignment(nextButton, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(nextButton, new Insets(0, 30, 20, 0));
        return textBoxWithButton;
    }
    
    protected ImageView createImageView(String path, double width, double height) {
        return configureImageView(new ImageView(new Image(getClass().getResource(path).toExternalForm())), width, height);
    }

    private ImageView configureImageView(ImageView imageView, double width, double height) {
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        return imageView;
    }

    protected Button createButton(String text, String color, int fontSize) {
        return configureButton(new Button(text), color, fontSize);
    }

    private Button configureButton(Button button, String color, int fontSize) {
        button.setStyle(String.format("-fx-background-color: %s; -fx-text-fill: white; -fx-font-size: %dpx;", color, fontSize));
        return button;
    }
    
    protected TextFlow createTextFlow() {
        TextFlow textFlow = new TextFlow();
        textFlow.setPrefHeight(162);
        textFlow.setPadding(new Insets(20, 30, 20, 30));
        textFlow.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 18px; -fx-text-fill: white;");
        return textFlow;
    }
    
    protected Button createNextButton(Stage primaryStage, TextFlow textBox) {
        Button nextButton = createButton("Next", "linear-gradient(to bottom, #ff5e62, #ff9966)", 18);
        nextButton.setStyle(nextButton.getStyle() + 
            "; -fx-background-radius: 25; -fx-text-fill: white; -fx-font-weight: bold; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 5, 0, 0, 1);");
        nextButton.setOnAction(event -> handleNextText(primaryStage, textBox, 0));
        return nextButton;
    }
    
    protected Rectangle createTextBoxBackground() {
        Stop[] stops = new Stop[]{
            new Stop(0, Color.WHITE),
            new Stop(1, Color.PINK)
        };
        RadialGradient gradient = new RadialGradient(
            0, 0, 0.5, 0.5, 0.7, true, CycleMethod.NO_CYCLE, stops
        );
        Rectangle textBoxBg = new Rectangle(948, 142);
        textBoxBg.setFill(gradient);
        DropShadow dropShadow = new DropShadow(15, Color.rgb(255, 105, 180, 0.7));
        dropShadow.setSpread(0.05);
        textBoxBg.setEffect(dropShadow);
        return textBoxBg;
    }

    @Override
    public void handleNextText(Stage primaryStage, TextFlow textBox, int fromAnswerBox) {
        // If animation is running and user clicks Next, just show full text immediately
        if (fromAnswerBox == 0 && isRunning()) {
            timeline.stop();
            // Replace with direct text update without animation
            updateTextBoxInstantly(textBox);
            return;
        }
        
        if (fromAnswerBox == 0) {
            if (!"ask2".equals(storyTexts.getStoryTexts().get(currentTextIndex)[TextBase.readingStatusIndex])) {
                currentTextIndex++;
            }
        } 
        else {
            currentTextIndex += fromAnswerBox;
        }
        
        if (currentTextIndex < storyTexts.getStoryTexts().size()) {
            updateSpeakerVisibility();
            playEffectSound(storyTexts.getStoryTexts().get(currentTextIndex)[TextBase.soundEffectIndex]);
            updateCharacterImages();
            
            if ("ask2".equals(storyTexts.getStoryTexts().get(currentTextIndex)[TextBase.readingStatusIndex])) {
                createAnswerBoxFor2(primaryStage, textBox);
            }
            
            timeline.stop();
            timeline = createTimeline(textBox);
            timeline.play();
        } else {
            goToNextChapter(primaryStage);
        }
    }

    private void initializeTextNodes() {
        speakerFont = loadFont(20);
        contentFont = loadFont(18);
        
        speakerTextNode = new Text();
        speakerTextNode.setFill(Color.RED);
        speakerTextNode.setFont(speakerFont);
        
        contentTextNode = new Text();
        contentTextNode.setFont(contentFont);
    }

    protected void updateTextBoxInstantly(TextFlow textBox) {
        if (speakerTextNode == null) {
            initializeTextNodes();
        }
        
        String[] currentTextData = storyTexts.getStoryTexts().get(currentTextIndex);
        
        speakerTextNode.setText(currentTextData[TextBase.speakerIndex] + "\n");
        contentTextNode.setText(currentTextData[TextBase.textIndex]);
        
        textBox.getChildren().setAll(speakerTextNode, contentTextNode);
    }

	protected String getImagePath(String speaker, String emotion) {
        switch (speaker) {
            case "คเชน": return "/resources/cashen/cashen_" + emotion + ".png";
            case "เพื่อน": return "/resources/friend/friend_" + emotion + ".png";
            case "อาริสา": return "/resources/arisa/arisa_" + emotion + ".png";
            case "ชายแปลกหน้า": return "/resources/fatherInLaw/fatherInLaw_" + emotion + ".png";
            case "พ่อตา": return "/resources/fatherInLaw/father&motherInLaw_" + emotion + ".png";
            case "แม่ยาย": return "/resources/fatherInLaw/father&motherInLaw_" + emotion + ".png";
            default: return "/resources/default.png";
        }
    }

    protected void playEffectSound(String effect) {
        if (effectPlayer != null) effectPlayer.stop();
        URL effectURL = getClass().getResource("/resources/sound/" + effect + ".mp3");
        if (effectURL != null) {
            effectPlayer = new MediaPlayer(new Media(effectURL.toExternalForm()));
            effectPlayer.play();
        } else {
            System.out.println("Error: Effect sound file " + effect + " not found!");
        }
    }
    
    @Override
    public void playTalkingSound(String talking) {
        if (effectTalking == null || !effectTalking.getMedia().getSource().contains(talking)) {
            URL talkingURL = getClass().getResource("/resources/sound/talking_" + talking + ".mp3");
            if (talkingURL != null) {
                effectTalking = new MediaPlayer(new Media(talkingURL.toExternalForm()));
                effectTalking.setVolume(0.5);
            } else {
                System.out.println("Error: Talking sound file " + talking + "not found!");
                return;
            }
        }
        
        if (effectTalking.getStatus() != MediaPlayer.Status.PLAYING) {
            effectTalking.play();
        }
    }
    
    private Font loadFont(int size) {
        return Font.loadFont(getClass().getResourceAsStream("/resources/font/Prompt-ExtraLight.ttf"), size);
    }

    public Timeline createTimeline(TextFlow textBox) {
    	if (speakerFont == null || contentFont == null) {
    		speakerFont = loadFont(20);
    	    contentFont = loadFont(18);
        }
    	
        String currentSpeaker = storyTexts.getStoryTexts().get(currentTextIndex)[TextBase.speakerIndex];
        String currentText = storyTexts.getStoryTexts().get(currentTextIndex)[TextBase.textIndex];

        textBox.getChildren().clear();
        Text speakerText = new Text(currentSpeaker + "\n");
        speakerText.setFill(Color.RED);
        speakerText.setFont(speakerFont);

        Text contentText = new Text();
        contentText.setFont(contentFont);
        textBox.getChildren().addAll(speakerText, contentText);

        Timeline timeline = new Timeline();
        for (int i = 0; i < currentText.length(); i++) {
            final int index = i;
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(33 * (i + 1)), e -> {
                contentText.setText(contentText.getText() + currentText.charAt(index));
                if (index % 4 == 0) {
                	playTalkingSound(storyTexts.getStoryTexts().get(currentTextIndex)[TextBase.talkingSoungIndex]);
                }
            }));
        }
        return timeline;
    }

    protected void showNextScene(Stage primaryStage) {
        if (backgroundMusic != null) backgroundMusic.stop();
        primaryStage.setScene(new Scene(new StackPane(), 968, 648));
    }
    
    @Override
    public boolean isRunning() {
        return (timeline.getStatus() == Animation.Status.RUNNING);
    }

    private boolean isAnswerBoxVisible() {
        return choiceBoxStack != null && stackPane.getChildren().contains(choiceBoxStack);
    }

    public void createAnswerBoxFor2(Stage primaryStage, TextFlow textBox) {
        // Remove existing answer box if it exists
        if (choiceBoxStack != null && stackPane.getChildren().contains(choiceBoxStack)) {
            stackPane.getChildren().remove(choiceBoxStack);
        }
        
        Button answerButton1 = createButton(
            storyTexts.getStoryTexts().get(currentTextIndex)[TextBase.answer1Index],
            "rgba(0, 128, 255, 0.8)",
            18
        );
        
        Button answerButton2 = createButton(
            storyTexts.getStoryTexts().get(currentTextIndex)[TextBase.answer2Index],
            "rgba(0, 128, 255, 0.8)",
            18
        );

        String buttonStyle = "-fx-background-radius: 30; -fx-text-fill: white; -fx-font-weight: bold; " +
                             "-fx-padding: 15 30; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 5, 0, 0, 1);";

        answerButton1.setStyle(answerButton1.getStyle() + buttonStyle);
        answerButton2.setStyle(answerButton2.getStyle() + buttonStyle);

        VBox answerBox = new VBox(10);

        // Set up vertical box for buttons
        answerBox.setAlignment(Pos.CENTER);
        answerBox.getChildren().addAll(answerButton1, answerButton2);
        answerBox.setPadding(new Insets(20));
        answerBox.setSpacing(20);

        // Create background for choice box
        Rectangle choiceBoxBg = new Rectangle(300, 150);
        choiceBoxBg.setArcWidth(30);
        choiceBoxBg.setArcHeight(30);
        choiceBoxBg.setFill(Color.rgb(20, 20, 60, 0.8));

        // Add glow effect to the choice box
        DropShadow choiceBoxShadow = new DropShadow();
        choiceBoxShadow.setColor(Color.CORNFLOWERBLUE);
        choiceBoxShadow.setRadius(20);
        choiceBoxShadow.setSpread(0.2);
        choiceBoxBg.setEffect(choiceBoxShadow);

        // Stack the choice box elements
        choiceBoxStack = new StackPane(choiceBoxBg, answerBox);

        StackPane.setAlignment(choiceBoxStack, Pos.CENTER);
        stackPane.getChildren().add(choiceBoxStack);

        // Set up button actions
        answerButton1.setOnAction(event -> {
            if (textBox != null) {
                textBox.getChildren().clear();
            }

            stackPane.getChildren().remove(choiceBoxStack);
            choiceBoxStack = null; // Clear the reference
            
            handleNextText(primaryStage, textBox, Integer.parseInt(storyTexts.getStoryTexts().get(currentTextIndex)[TextBase.quesion1Index]));
        });

        answerButton2.setOnAction(event -> {
            if (textBox != null) {
                textBox.getChildren().clear();
            }

            stackPane.getChildren().remove(choiceBoxStack);
            choiceBoxStack = null; // Clear the reference
            
            handleNextText(primaryStage, textBox, Integer.parseInt(storyTexts.getStoryTexts().get(currentTextIndex)[TextBase.quesion2Index]));
        });
    }
    
    protected void enterAnimation(VBox root, TextFlow textBox) {
        // Immediately hide all existing content
        for (int i = 0; i < stackPane.getChildren().size(); i++) {
            stackPane.getChildren().get(i).setVisible(false);
        }
        
        // Create chapter title text
        Text chapterTitle = new Text("Chapter " + getChapterNumber());
        chapterTitle.setFont(Font.font("Serif", FontWeight.BOLD, 60));
        chapterTitle.setFill(Color.WHITE);
        
        // Add glow effect to title
        DropShadow glow = new DropShadow();
        glow.setColor(Color.WHITE);
        glow.setRadius(15);
        glow.setSpread(0.3);
        chapterTitle.setEffect(glow);
        
        // Create a black overlay rectangle for the background
        Rectangle blackOverlay = new Rectangle(968, 1500, Color.BLACK);
        
        // Add title and overlay to stackPane at the top level
        stackPane.getChildren().add(blackOverlay);
        stackPane.getChildren().add(chapterTitle);
        
        // Ensure the title is centered
        StackPane.setAlignment(chapterTitle, Pos.CENTER);
        
        // Fade in chapter title
        FadeTransition titleFadeIn = new FadeTransition(Duration.seconds(2), chapterTitle);
        titleFadeIn.setFromValue(0);
        titleFadeIn.setToValue(1);
        
        // Pause to display the title
        PauseTransition displayPause = new PauseTransition(Duration.seconds(3));
        
        // Fade out title
        FadeTransition titleFadeOut = new FadeTransition(Duration.seconds(1.5), chapterTitle);
        titleFadeOut.setFromValue(1);
        titleFadeOut.setToValue(0);
        
        // Create sequential transition for the chapter intro
        SequentialTransition chapterIntro = new SequentialTransition(
            titleFadeIn,
            displayPause,
            titleFadeOut
        );
        
        // After the chapter intro finishes, fade in the actual game scene
        chapterIntro.setOnFinished(e -> {
            // Remove the chapter intro elements
            stackPane.getChildren().remove(blackOverlay);
            stackPane.getChildren().remove(chapterTitle);
            
            // Make all original elements visible again
            for (int i = 0; i < stackPane.getChildren().size(); i++) {
                stackPane.getChildren().get(i).setVisible(true);
            }
            
            // Fade in the game scene
            FadeTransition sceneFadeIn = new FadeTransition(Duration.seconds(1.5), root);
            sceneFadeIn.setFromValue(0);
            sceneFadeIn.setToValue(1);
            
            // IMPORTANT: Only start the text timeline after the scene fade-in is complete
            sceneFadeIn.setOnFinished(event -> {
                // Only now do we start the text timeline
                timeline.play();
            });
            
            sceneFadeIn.play();
        });
        
        // Set initial opacity of root to 1 for the black screen with chapter text
        root.setOpacity(1.0);
        
        // Play the chapter intro animation
        chapterIntro.play();
    }
    
    protected int getChapterNumber() {
        String className = this.getClass().getSimpleName();
        if (className.contains("Chapter")) {
            try {
                return Integer.parseInt(className.replace("Chapter", ""));
            } 
            catch (NumberFormatException e) {
                return 1;
            }
        }
        return 1;
    }
}