/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saleapp;

import java.text.NumberFormat;

/**
 *
 * @author Robert Stovall
 */
public class RentalAgreement {

    //the data types were assigned with SQLite in mind, since SQLite doesn't have Date objects
    private String toolCode;
    private String toolType;
    private String toolBrand;
    private int rentalDays;
    private String checkoutDate;
    private String dueDate;
    private double rentalCharge;
    private int chargeDays;
    private double preDiscountCharge;
    private int discountPercent;
    private double discountAmount;
    private double finalCharge;

    /**
     *
     * @param toolCode
     * @param toolType
     * @param toolBrand
     * @param rentalDays
     * @param checkoutDate
     * @param dueDate
     * @param rentalCharge
     * @param chargeDays
     * @param preDiscountCharge
     * @param discountPercent
     * @param discountAmount
     * @param finalCharge
     */
    public RentalAgreement(String toolCode, String toolType,
            String toolBrand, int rentalDays, String checkoutDate, String dueDate, double rentalCharge,
            int chargeDays, double preDiscountCharge, int discountPercent,
            double discountAmount, double finalCharge) {
        this.toolCode = toolCode;
        this.toolType = toolType;
        this.toolBrand = toolBrand;
        this.rentalDays = rentalDays;
        this.checkoutDate = checkoutDate;
        this.dueDate = dueDate;
        this.rentalCharge = rentalCharge;
        this.chargeDays = chargeDays;
        this.preDiscountCharge = preDiscountCharge;
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
        this.finalCharge = finalCharge;
    }

    RentalAgreement() {
    }

    /**
     *
     * @param toolCode
     */
    public void setToolCode(String toolCode) {
        this.toolCode = toolCode;
    }

    /**
     *
     * @param toolType
     */
    public void setToolType(String toolType) {
        this.toolType = toolType;
    }

    /**
     *
     * @param toolBrand
     */
    public void setToolBrand(String toolBrand) {
        this.toolBrand = toolBrand;
    }

    /**
     *
     * @param rentalDays
     */
    public void setRentalDays(int rentalDays) {
        this.rentalDays = rentalDays;
    }

    /**
     *
     * @param checkoutDate
     */
    public void setCheckoutDate(String checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    /**
     *
     * @param dueDate
     */
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    /**
     *
     * @param rentalCharge
     */
    public void setRentalCharge(double rentalCharge) {
        this.rentalCharge = rentalCharge;
    }

    /**
     *
     * @param chargeDays
     */
    public void setChargeDays(int chargeDays) {
        this.chargeDays = chargeDays;
    }

    /**
     *
     * @param preDiscountCharge
     */
    public void setPreDiscountCharge(double preDiscountCharge) {
        this.preDiscountCharge = preDiscountCharge;
    }

    /**
     *
     * @param discountPercent
     */
    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    /**
     *
     * @param discountAmount
     */
    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    /**
     *
     * @param finalCharge
     */
    public void setFinalCharge(double finalCharge) {
        this.finalCharge = finalCharge;
    }

    /**
     *
     * @return
     */
    public String getToolCode() {
        return toolCode;
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
    public String getToolBrand() {
        return toolBrand;
    }

    /**
     *
     * @return
     */
    public int getRentalDays() {
        return rentalDays;
    }

    /**
     *
     * @return
     */
    public String getCheckoutDate() {
        return checkoutDate;
    }

    /**
     *
     * @return
     */
    public String getDueDate() {
        return dueDate;
    }

    /**
     *
     * @return
     */
    public double getRentalCharge() {
        return rentalCharge;
    }

    /**
     *
     * @return
     */
    public int getChargeDays() {
        return chargeDays;
    }

    /**
     *
     * @return
     */
    public double getPreDiscountCharge() {
        return preDiscountCharge;
    }

    /**
     *
     * @return
     */
    public int getDiscountPercent() {
        return discountPercent;
    }

    /**
     *
     * @return
     */
    public double getDiscountAmount() {
        return discountAmount;
    }

    /**
     *
     * @return
     */
    public double getFinalCharge() {
        return finalCharge;
    }

    //used to print in various parts of the UI
    void printRentalAgreement() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String formattedRentalCharge = formatter.format(rentalCharge);
        String formattedPreDiscountCharge = formatter.format(preDiscountCharge);
        String formattedDiscountAmount = formatter.format(discountAmount);
        String formattedFinalCharge = formatter.format(finalCharge);
        
        System.out.printf("\n~RENTAL AGREEMENT~\n"
                + "Tool Code: %s\n"
                + "Tool Type: %s\n"
                + "Tool Brand: %s\n"
                + "Rental Days: %d\n"
                + "Check Out Date: %s\n"
                + "Due Date: %s\n"
                + "Daily Rental Charge: %s\n"
                + "Charge Days: %d\n"
                + "Pre-discount Charge: %s\n"
                + "Discount Percent: %d\n"
                + "Discount Amount: %s\n"
                + "Final Charge: %s\n\n", toolCode, toolType, toolBrand, rentalDays, checkoutDate, dueDate, formattedRentalCharge,
                chargeDays, formattedPreDiscountCharge, discountPercent, formattedDiscountAmount, formattedFinalCharge);
    }

}
