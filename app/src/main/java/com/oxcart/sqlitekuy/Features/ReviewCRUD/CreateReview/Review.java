package com.oxcart.sqlitekuy.Features.ReviewCRUD.CreateReview;

public class Review {
    private long id;
    private String reviwer_name;
    private int ratting;
    private String comment;

    public Review(long id, String reviwer_name, int ratting, String comment) {
        this.id = id;
        this.reviwer_name = reviwer_name;
        this.ratting = ratting;
        this.comment = comment;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getReviwer_name() {
        return reviwer_name;
    }

    public void setReviwer_name(String reviwer_name) {
        this.reviwer_name = reviwer_name;
    }

    public int getRatting() {
        return ratting;
    }

    public void setRatting(int ratting) {
        this.ratting = ratting;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
