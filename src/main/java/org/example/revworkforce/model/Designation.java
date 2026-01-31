package org.example.revworkforce.model;


public class Designation {

    private int designationId;
    private String designationName;

    // Constructor
    public Designation() {
    }

    // Getters and Setters
    public int getDesignationId() {
        return designationId;
    }

    public void setDesignationId(int designationId) {
        this.designationId = designationId;
    }

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    @Override
    public String toString() {
        return designationName;
    }
}
