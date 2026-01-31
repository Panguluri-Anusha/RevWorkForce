package org.example.revworkforce.model;

import java.util.Date;

public class PerformanceReview {

    private int reviewId;
    private int employeeId;
    private int reviewYear;

    private String keyDeliverables;
    private String accomplishments;
    private String areasOfImprovement;

    private Integer selfRating;       // can be null
    private Integer managerRating;    // can be null

    private String managerFeedback;
    private String status;

    private int reviewedBy;
    private int rating;

    private String comment;
    private Date reviewDate;

    // ================= Getters and Setters =================

    public int getReviewId() { return reviewId; }
    public void setReviewId(int reviewId) { this.reviewId = reviewId; }

    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    public int getReviewYear() { return reviewYear; }
    public void setReviewYear(int reviewYear) { this.reviewYear = reviewYear; }

    public String getKeyDeliverables() { return keyDeliverables; }
    public void setKeyDeliverables(String keyDeliverables) { this.keyDeliverables = keyDeliverables; }

    public String getAccomplishments() { return accomplishments; }
    public void setAccomplishments(String accomplishments) { this.accomplishments = accomplishments; }

    public String getAreasOfImprovement() { return areasOfImprovement; }
    public void setAreasOfImprovement(String areasOfImprovement) { this.areasOfImprovement = areasOfImprovement; }

    public Integer getSelfRating() { return selfRating; }
    public void setSelfRating(Integer selfRating) { this.selfRating = selfRating; }

    public Integer getManagerRating() { return managerRating; }
    public void setManagerRating(Integer managerRating) { this.managerRating = managerRating; }

    public String getManagerFeedback() { return managerFeedback; }
    public void setManagerFeedback(String managerFeedback) { this.managerFeedback = managerFeedback; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getReviewedBy() { return reviewedBy; }
    public void setReviewedBy(int reviewedBy) { this.reviewedBy = reviewedBy; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }



    public Date getReviewDate() { return reviewDate; }
    public void setReviewDate(Date reviewDate) { this.reviewDate = reviewDate; }


        // ======= ADD THIS METHOD =======
        @Override
        public String toString() {
            return "Review ID: " + reviewId +
                    ", Employee ID: " + employeeId +
                    ", Year: " + reviewYear +
                    "\nKey Deliverables: " + keyDeliverables +
                    "\nAccomplishments: " + accomplishments +
                    "\nAreas of Improvement: " + areasOfImprovement +
                    "\nSelf Rating: " + (selfRating != null ? selfRating : "N/A") +
                    "\nManager Rating: " + (managerRating != null ? managerRating : "N/A") +
                    "\nManager Feedback: " + (managerFeedback != null ? managerFeedback : "N/A") +
                    "\nStatus: " + status +
                    "\nReview Date: " + reviewDate +
                    "\nReviewed By: " + reviewedBy +
                    "\nComment: " + (comment != null ? comment : "N/A") +
                    "\n--------------------------";
        }


    private String managerComment; // make sure this exists

    public void setManagerComment(String managerComments) {
        this.managerComment = managerComments; // ✅ store value
    }

    public String getManagerComments() {
        return managerComment;
    }



}


