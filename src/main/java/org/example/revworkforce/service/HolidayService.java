package org.example.revworkforce.service;


import org.example.revworkforce.dao.HolidayDAO;
import org.example.revworkforce.model.Holiday;

import java.util.List;

public class HolidayService {

    private HolidayDAO holidayDAO = new HolidayDAO();

    public List<Holiday> getAllHolidays() {
        return holidayDAO.getAllHolidays();
    }

    public boolean addHoliday(Holiday holiday) {
        return holidayDAO.addHoliday(holiday);
    }

    public boolean removeHoliday(int holidayId) {
        return holidayDAO.removeHoliday(holidayId);
    }
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
