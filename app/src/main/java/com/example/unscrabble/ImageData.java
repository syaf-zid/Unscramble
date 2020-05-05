package com.example.unscrabble;

public class ImageData {
    private String answer;
    private int id;
    private int image;

    ImageData(int id, String answer, int image) {
        this.answer = answer;
        this.id = id;
        this.image = image;
    }

    public int getID() {
        return id;
    }

    public String getAnswer() {
        return answer;
    }

    public int getImage() {
        return image;
    }
}
