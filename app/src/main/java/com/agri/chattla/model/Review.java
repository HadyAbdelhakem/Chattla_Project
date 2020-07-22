package com.agri.chattla.model;

public class Review {

    float rate;
    String review;
    String username;

    public Review(float rate, String review, String username) {
        this.rate = rate;
        this.review = review;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Review() {
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
