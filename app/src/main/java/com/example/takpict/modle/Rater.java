package com.example.takpict.modle;

public class Rater {

    private String id;
    private String rate;

    public Rater() {
    }

    public Rater(String id, String rate) {
        this.id = id;
        this.rate = rate;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
