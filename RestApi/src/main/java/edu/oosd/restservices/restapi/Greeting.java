package edu.oosd.restservices.restapi;

public class Greeting {
    long id;
    String content;

    public Greeting(long id, String content){
        setId(id);
        setContent(content);
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
