package de.kacperbak;

public class JsonMessage {

    private String content;

    public JsonMessage() {
    }

    public JsonMessage(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
