package org.example.carpooling.models.dto;

import jakarta.validation.constraints.NotNull;

public class FeedbackDtoIn {
    @NotNull(message = "Rating cannot be empty")
    private double rating;

    private String comment;

    public FeedbackDtoIn(double rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
