/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saleapp;

/**
 *
 * @author Robert Stovall
 */
public class ToolPrice {
    private String toolType;
    private double dailyCharge;
    private boolean weekdayCharge;
    private boolean weekendCharge;
    private boolean holidayCharge;
    
    /**
     *
     * @param toolType
     * @param dailyCharge
     * @param weekdayCharge
     * @param weekendCharge
     * @param holidayCharge
     */
    public ToolPrice(String toolType, double dailyCharge, boolean weekdayCharge, boolean weekendCharge, boolean holidayCharge){
        this.toolType = toolType;
        this.dailyCharge = dailyCharge;
        this.weekdayCharge = weekdayCharge;
        this.weekendCharge = weekendCharge;
        this.holidayCharge = holidayCharge;
    }

    /**
     *
     * @return
     */
    public String getToolType() {
        return toolType;
    }
    
    /**
     *
     * @return
     */
    public double getDailyCharge() {
        return dailyCharge;
    }

    /**
     *
     * @return
     */
    public boolean getWeekdayCharge() {
        return weekdayCharge;
    }

    /**
     *
     * @return
     */
    public boolean getWeekendCharge() {
        return weekendCharge;
    }

    /**
     *
     * @return
     */
    public boolean getHolidayCharge() {
        return holidayCharge;
    }
    
    /**
     *
     * @param toolType
     */
    public void setToolType(String toolType){
        this.toolType = toolType;
    }

    /**
     *
     * @param dailyCharge
     */
    public void setDailyCharge(double dailyCharge) {
        this.dailyCharge = dailyCharge;
    }

    /**
     *
     * @param weekdayCharge
     */
    public void setWeekdayCharge(boolean weekdayCharge) {
        this.weekdayCharge = weekdayCharge;
    }

    /**
     *
     * @param weekendCharge
     */
    public void setWeekendCharge(boolean weekendCharge) {
        this.weekendCharge = weekendCharge;
    }

    /**
     *
     * @param holidayCharge
     */
    public void setHolidayCharge(boolean holidayCharge) {
        this.holidayCharge = holidayCharge;
    }
    
    
}
