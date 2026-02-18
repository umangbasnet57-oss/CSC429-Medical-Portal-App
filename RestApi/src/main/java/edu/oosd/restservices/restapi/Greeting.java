package edu.oosd.restservices.restapi;

import org.springframework.hateoas.RepresentationModel;

public class Greeting extends RepresentationModel<Greeting>{
    long id;
    String content = "Welcome to Greeting API";

    public Greeting(){}
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
