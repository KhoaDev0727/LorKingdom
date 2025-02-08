/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.Timestamp;

/**
 *
 * @author Truong Van Khang - CE181852
 */
public class Review {

    private int reviewID;
    private int accountID;
    private int productID;
    private String imgReview;
    private int Rating;
    private String comment;
    private Timestamp reviewAt;
    private int IsFlagged;

    public Review(int reviewID, int accountID, int productID, String imgReview, int Rating, String comment, int IsFlagged, Timestamp reviewAt) {
        this.reviewID = reviewID;
        this.accountID = accountID;
        this.productID = productID;
        this.imgReview = imgReview;
        this.Rating = Rating;
        this.comment = comment;
        this.reviewAt = reviewAt;
        this.IsFlagged = IsFlagged;
    }

    public int getIsFlagged() {
        return IsFlagged;
    }

    public void setIsFlagged(int IsFlagged) {
        this.IsFlagged = IsFlagged;
    }

    public int getReviewID() {
        return reviewID;
    }

    public int getAccountID() {
        return accountID;
    }

    public int getProductID() {
        return productID;
    }

    public String getImgReview() {
        return imgReview;
    }

    public int getRating() {
        return Rating;
    }

    public String getComment() {
        return comment;
    }

    public Timestamp getReviewAt() {
        return reviewAt;
    }

}
