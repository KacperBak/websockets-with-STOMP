package de.kacperbak;

import java.io.Serializable;

public class SerializedMessage implements Serializable{
    
    private String content;

    public SerializedMessage() {
    }

    public SerializedMessage(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
