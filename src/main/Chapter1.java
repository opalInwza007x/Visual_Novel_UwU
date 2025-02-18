package main;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class Chapter1 {
	private String[][] storyTexts = {
	    {"เพื่อน", "เห้ย คเชน มีเรื่องไรป่าว เห็นนั่งเหม่ออยู่คนเดียวมาสักพักละ","whoosh"},
	    {"คเชน", "อ๋อ ไม่มีไร ช่วงนี้เครียดเรื่องเงินว่ะ","pop"},
	    {"เพื่อน", "ที่บ้านการเงินไม่ดีหรอเพื่อน ไม่เห็นเคยเล่าให้ฟังเลย","whoosh"},
	    {"คเชน", "ป่าว พอดีอยากหาตังไปเติมซื้อ Minecraft ไอดีแท้น่ะ","pop"},
	    {"เพื่อน", "...","whoosh"},
	    {"เพื่อน", "ช่างเหอะ ช่วงนี้ได้ยินเรื่องเพื่อนผู้หญิงห้องข้าง ๆ เราป้ะ","pop"},
	    {"คเชน", "ทำไม ? มีไรหรอ ?","whoosh"},
	    {"เพื่อน", "เธอก็เป็นเพื่อนรุ่นเดียวกับเรานี่แหละแถมน่ารักดีด้วย แต่ติดตรงที่เธอมีบางอย่างแปลก ๆ นี่สิ","pop"},
	    {"คเชน", "บางอย่างแปลก ๆ ?","whoosh"},
	    {"เพื่อน", "เธอน่ะ ห่อเนื้อวัว Thai Wagyu แบบ medium rare มากินที่โรงเรียนทุกวันเลย","pop"},
	    {"เพื่อน", "ไม่อยากจะคิดเลยว่าทำไมเธอไม่กินซอยจุ๊กันนะ ของขึ้นชื่อบ้านเราแท้ ๆ เพราะอย่างงี้ไงเธอถึงไม่มีเพื่อนคบซักคนอะ","whoosh"},
	    {"คเชน", "อ๋อ… จำได้ละ แต่กินเนื้อดิบ ๆ มันก็ไม่ดีนะเว้ย อย่างน้อยก็ควรทำเนื้อให้สุกก่อนกินสิ เธอทำถูกแล้วล่ะ ถือว่าเป็นการปลูกฝังค่านิยมดี ๆ ให้รุ่นน้องในรร. ด้วยไง","pop"},
	    {"คเชน", "Dev เกมนี้คิดบทมาดีจริง ๆ","wow"},
	    {"เพื่อน", "พูดถึงใครอะ ?","pop"},
	    {"คเชน", "เอาเถอะ... แต่จริง ๆ ที่เธอไม่มีเพื่อนอาจจะเป็นเพราะมีแต่คนแบบนายอยู่ในโรงเรียนก็ได้นะ","whoosh"},
	    {"เพื่อน", ":ArchettoLOOKATYOU: :ArchettoLOOKATYOU:","pop"},
	    {"เพื่อน", "เชื่อครับไม่โม้... แต่ฉันได้ยินมาว่าเธอก็พยายามหาเพื่อนอยู่นะ แต่ไม่มีใครอยากคุยกับเธอสักคนเลย","whoosh"},
	    {"เพื่อน", "มีแต่คนบอกว่าเวลาอยู่กับเธอแล้วมันไม่สบายใจ","pop"},
	    {"เพื่อน", "ฉันก็รู้สึกคล้ายกัน ใครจะอยู่ก็อยู่ ข้าไม่อยู่แล้ว บรรยากาศรอบตัวเธอมันเหมือนเหตุการณ์ในหนังผีที่เราไปดูมาเมื่อตอนนั้นเลย","whoosh"},
	    {"คเชน", "เพ้อเจ้อ ดูธี่หยดแล้วหลอน","wow"},
	    {"คเชน", "ไปนั่งที่เถอะ อาจารย์เข้าห้องมาแล้ว","whoosh"}
	};
	
	private int currentTextIndex = 0;
    private Timeline timeline;
    private ImageView friendImage;
    private ImageView cashenImage;	
    private MediaPlayer backgroundMusic;
    private MediaPlayer effectPlayer;
    
    public void startChapter(Stage primaryStage) {
    	playBackgroundMusic();
    	loadSoundEffect();
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        
       
        ImageView background = createImageView("/resources/classroomTest.jpg", 968, 486);
        TextArea textBox = createTextArea();
        Button nextButton = createButton("Next", "rgba(255, 0, 0, 0.7)", 16);
        friendImage = createSpeakerImage("เพื่อน");
        cashenImage = createSpeakerImage("คเชน");
        updateSpeakerVisibility();
        nextButton.setOnAction(event -> handleNextText(primaryStage, textBox));
        
        StackPane textBoxWithButton = new StackPane(textBox, nextButton);
        StackPane.setAlignment(nextButton, Pos.BOTTOM_RIGHT);
        HBox speakerPane = new HBox(50, friendImage, cashenImage);
        speakerPane.setAlignment(Pos.BOTTOM_CENTER);
        StackPane stackPane = new StackPane(background, speakerPane);
        timeline = createTimeline(textBox);
        timeline.play();
        root.getChildren().addAll(stackPane, textBoxWithButton);
        primaryStage.setScene(new Scene(root, 968, 648, Color.BLACK));
    }
    
    private void playBackgroundMusic() {
        try {
            URL resource = getClass().getResource("/resources/bgChap1.mp4");
            if (resource != null) {
                Media media = new Media(resource.toExternalForm());
                backgroundMusic = new MediaPlayer(media);
                backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE); // เล่นวนลูป
                backgroundMusic.setVolume(0.5); // ตั้งค่าความดัง (0.0 - 1.0)
                backgroundMusic.play();
            } else {
                System.out.println("Error: Background music file not found!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadSoundEffect() {
    	 try {
    	        URL whooshURL = getClass().getResource("/resources/whoosh.mp3");
    	        URL popURL = getClass().getResource("/resources/pop.mp4");
    	        URL wowURL = getClass().getResource("/resources/wow.mp4");
    	        if (whooshURL != null && popURL != null &&  wowURL != null) {
    	            effectPlayer = new MediaPlayer(new Media(whooshURL.toExternalForm()));
    	        } else {
    	            System.out.println("Error: Effect sound files not found!");
    	        }
    	    } catch (Exception e) {
    	        e.printStackTrace();
    	    }
    }
    
    private ImageView createImageView(String path, double width, double height) {
        ImageView imageView = new ImageView(new Image(getClass().getResource(path).toExternalForm()));
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        return imageView;
    }
    
    private Button createButton(String text, String color, int fontSize) {
        Button button = new Button(text);
        button.setStyle(String.format("-fx-background-color: %s; -fx-text-fill: white; -fx-font-size: %dpx;", color, fontSize));
        return button;
    }
    
    private TextArea createTextArea() {
        TextArea textBox = new TextArea();
        textBox.setEditable(false);
        textBox.setWrapText(true);
        textBox.setPadding(new Insets(10));
        textBox.setFont(Font.font(18));
        textBox.setPrefHeight(162);
        return textBox;
    }
  
    private void handleNextText(Stage primaryStage, TextArea textBox) {
        if (currentTextIndex < storyTexts.length - 1) {
            currentTextIndex++;
            textBox.clear();
            updateSpeakerVisibility();
            playEffectSound(storyTexts[currentTextIndex][2]); // เล่นเสียงเอฟเฟกต์
            timeline.stop();
            timeline = createTimeline(textBox);
            timeline.play();
        } else {
            showNextScene(primaryStage);
        }
    }
    
    private void playEffectSound(String effect) {
        if (effectPlayer != null) {
            effectPlayer.stop(); // หยุดเสียงเก่าก่อนเล่นใหม่
        }
        String effectPath = null;
        if( effect == "whoosh") {
        	 effectPath = "/resources/" + effect + ".mp3"; // เช่น "whoosh.mp3" หรือ "pop.mp3"
        }else {
        	 effectPath = "/resources/" + effect + ".mp4"; 
        }
        
        URL effectURL = getClass().getResource(effectPath);
        if (effectURL != null) {
            effectPlayer = new MediaPlayer(new Media(effectURL.toExternalForm()));
            effectPlayer.play();
        } else {
            System.out.println("Error: Effect sound file " + effect + " not found!");
        }
    }
   
    private ImageView createSpeakerImage(String speaker) {
        String imagePath = (speaker.equals("คเชน") ? "/resources/cashen_normal.jpeg" : "/resources/friend.jpeg");
        ImageView img = createImageView(imagePath, 200, 300);
        return img;
    }
    
    private void updateSpeakerVisibility() {
        String currentSpeaker = storyTexts[currentTextIndex][0];
        if (currentSpeaker.equals("คเชน")) {
            cashenImage.setOpacity(1.0);
            friendImage.setOpacity(0.8);
        } else {
            cashenImage.setOpacity(0.8);
            friendImage.setOpacity(1.0);
        }
    }
    
    private Timeline createTimeline(TextArea textBox) {
        String currentSpeaker = storyTexts[currentTextIndex][0];
        String currentText = storyTexts[currentTextIndex][1];
        Timeline timeline = new Timeline();
        textBox.appendText(currentSpeaker + " : ");
        for (int i = 0; i < currentText.length(); i++) {
            final int index = i;
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(33 * (i + 1)), e -> {
                textBox.appendText(String.valueOf(currentText.charAt(index)));
            }));
        }
        return timeline;
    }
    
    private void showNextScene(Stage primaryStage) {
    	 if (backgroundMusic != null) {
    	        backgroundMusic.stop(); // หยุดเสียงก่อนเปลี่ยนฉาก
    	    }
        StackPane nextSceneRoot = new StackPane();
        nextSceneRoot.setStyle("-fx-background-color: black;");
        primaryStage.setScene(new Scene(nextSceneRoot, 968, 648));
    }
}