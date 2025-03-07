package Util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;


public class TextBase {
	private ArrayList<String[]> texts = new ArrayList<>();
	
	public final static int emotionIndex = 0;
	public final static int speakerIndex = 1;
	public final static int textIndex = 2;
	public final static int soundEffectIndex = 3;
	public final static int talkingSoungIndex = 4;
	public final static int readingStatusIndex = 5;
	public final static int quesion1Index = 6;
	public final static int answer1Index = 7;
	public final static int quesion2Index = 8;
	public final static int answer2Index = 9;
	public final static int maxTextIndex = 10;
	
	public TextBase() {}
	
	public TextBase(String url) {
	    try {
	        URL resource = getClass().getResource(url);
	        if (resource != null) {
	            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.openStream()))) {
	                String line;
	                
	                while ((line = reader.readLine()) != null) {
	                    line = line.replace("\\n", "\n").trim(); // ตัดช่องว่างหัวท้าย
	                    String[] splitTexts = line.split("\\$", -1); // ใช้ -1 เพื่อให้ array มีครบทุก
	                    
	                    texts.add(splitTexts);
	                }
	            }
	        } else {
	            System.out.println("Error: File not found at path: " + url);
	        }
	    } catch (Exception err) {
	        System.out.println("Error reading file: " + err.getMessage());
	        err.printStackTrace();
	    }
	}
	
	public TextBase(ArrayList<String[]> texts) {
		setTexts(texts);
	}
	
	public ArrayList<String[]> getStoryTexts() {
		return texts;
	}

	public void setTexts(ArrayList<String[]> texts) {
		this.texts = texts;
	}
}
