package org.example.revworkforce.model;

import java.time.LocalDate;

public class Holiday {

    private int holidayId;
    private String holidayName;
    private LocalDate holidayDate;
    private String description;

    public Holiday() {
    }

    public Holiday(int holidayId, String holidayName, LocalDate holidayDate, String description) {
        this.holidayId = holidayId;
        this.holidayName = holidayName;
        this.holidayDate = holidayDate;
        this.description = description;
    }

    // Getters & Setters
    public int getHolidayId() {
        return holidayId;
    }

    public void setHolidayId(int holidayId) {
        this.holidayId = holidayId;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    public LocalDate getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(LocalDate holidayDate) {
        this.holidayDate = holidayDate;
    }

    public String getDescription() {
        return description;  // ✅ THIS FIXES getDescription() issue
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
