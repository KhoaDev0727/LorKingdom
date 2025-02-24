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
    private String Comment;
    private Timestamp reviewAt;
    private int Status;

    public Review(int reviewID, int accountID, int productID, String imgReview, int Rating, String comment, int Status, Timestamp reviewAt) {
        this.reviewID = reviewID;
        this.accountID = accountID;
        this.productID = productID;
        this.imgReview = imgReview;
        this.Rating = Rating;
        this.Comment = comment;
        this.reviewAt = reviewAt;
        this.Status = Status;
    }

    public Review(int accountID, int productID, String imgReview, int Rating, String Comment ) {
        this.accountID = accountID;
        this.productID = productID;
        this.imgReview = imgReview;
        this.Rating = Rating;
        this.Comment = Comment;

    }

    public Review(int accountID, int productID, String imgReview, int Rating, String Comment, Timestamp reviewAt) {
        this.accountID = accountID;
        this.productID = productID;
        this.imgReview = imgReview;
        this.Rating = Rating;
        this.Comment = Comment;
        this.reviewAt = reviewAt;
    }
    
    
    public int getReviewID() {
        return reviewID;
    }

    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getImgReview() {
        return imgReview;
    }

    public void setImgReview(String imgReview) {
        this.imgReview = imgReview;
    }

    public int getRating() {
        return Rating;
    }

    public void setRating(int Rating) {
        this.Rating = Rating;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String Comment) {
        this.Comment = Comment;
    }

    public Timestamp getReviewAt() {
        return reviewAt;
    }

    public void setReviewAt(Timestamp reviewAt) {
        this.reviewAt = reviewAt;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

}
