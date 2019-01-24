/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saleapp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.InvalidParameterException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import saleapp.Database.ToolsDatabase;

/**
 *
 * @author Robert Stovall
 */
public class RentalCalculator {

    public static RentalAgreement rentTool(String toolCode, int rentalDayCount, int discountPercent, String checkoutDate) {

        ToolsDatabase db = null;
        RentalAgreement rentalAgreement = null;
        db = new ToolsDatabase();
        
        //First step is to check if the constraints are met
        if (rulesChecker(rentalDayCount, discountPercent)) {
            //then we want to get the tool info from our db
            ArrayList<Tool> tools = db.selectAllTools();
            ArrayList<ToolPrice> toolPrices = db.selectAllPrices();
            Tool checkoutTool = null;
            ToolPrice checkoutPrice = null;

            //we'll use these two loops to find our tool and price match
            for (Tool tool : tools) {
                if (tool.getToolCode().equals(toolCode)) {
                    checkoutTool = tool;
                }
            }

            if (checkoutTool != null) {
                for (ToolPrice toolPrice : toolPrices) {
                    if (toolPrice.getToolType().equals(checkoutTool.getToolType())) {
                        checkoutPrice = toolPrice;
                    }
                }
            } else {
                System.out.println("Error: Could not find specified checkout tool.");
                return null;
            }

            try {
                int chargeDays = getChargeDays(checkoutPrice, checkoutDate, rentalDayCount);

                //used for adding some simplicity to the assignment operations below
                double costBeforeDiscount = truncateDouble(checkoutPrice.getDailyCharge() * chargeDays);
                double discountAmount = truncateDouble(costBeforeDiscount * ((double) discountPercent / 100));
                double costAfterDiscount = truncateDouble(costBeforeDiscount - discountAmount);
                
                //once we know for sure all the data is correct, we assign it to a rental agreement option
                rentalAgreement = new RentalAgreement();
                rentalAgreement.setToolCode(toolCode);
                rentalAgreement.setToolBrand(checkoutTool.getBrand());
                rentalAgreement.setToolType(checkoutTool.getToolType());
                rentalAgreement.setRentalDays(rentalDayCount);
                rentalAgreement.setCheckoutDate(checkoutDate);
                rentalAgreement.setDueDate(getDueDate(rentalDayCount, checkoutDate));
                rentalAgreement.setRentalCharge(truncateDouble(checkoutPrice.getDailyCharge()));
                rentalAgreement.setChargeDays(chargeDays);
                rentalAgreement.setPreDiscountCharge(costBeforeDiscount);
                rentalAgreement.setDiscountPercent(discountPercent);
                rentalAgreement.setDiscountAmount(discountAmount);
                rentalAgreement.setFinalCharge(truncateDouble(costAfterDiscount));
            } catch (ParseException e) {
                System.out.println("Erorr: " + e.getMessage());
            }

        } else {
            return null;
        }
        return rentalAgreement;
    }

    private static boolean rulesChecker(int rentalDayCount, int discountPercent) throws InvalidParameterException {
        if (rentalDayCount < 1) {
            System.out.println("Error: Rental Days must be 1 or greater.");
            throw new InvalidParameterException();
        }
        if (discountPercent > 100 || discountPercent < 0) {
            System.out.println("Error: Discount percent must be a whole number in the range of 0 - 100.");
            throw new InvalidParameterException();
        }
        return true;
    }

    private static int getChargeDays(ToolPrice toolPrice, String checkoutDate, int rentalDayCount) throws ParseException {
        int chargeDays = 0;
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy");
        
        Date checkoutDateFormatted = df.parse(checkoutDate);
        df.set2DigitYearStart(checkoutDateFormatted);
        Calendar cal = Calendar.getInstance();
        cal.setTime(checkoutDateFormatted);

        //loops through the amount of rental days, and incriments chargeDays if applicable
        for (int i = 0; i < rentalDayCount; i++) {
            cal.add(Calendar.DATE, 1);
            int currentDay = cal.get(Calendar.DAY_OF_YEAR);
            int laborDay = getLaborDay(cal.get(Calendar.YEAR));
            int independenceDay = getIndependenceDay(cal.get(Calendar.YEAR));
            
            //holiday exceptions
            if (!toolPrice.getHolidayCharge() && (currentDay == laborDay || currentDay == independenceDay)) {
                continue;
            }
            //weekday
            if (toolPrice.getWeekdayCharge() && cal.get(Calendar.DAY_OF_WEEK) > 1 && cal.get(Calendar.DAY_OF_WEEK) < 7) {
                chargeDays++;
                //weekend
            } else if (toolPrice.getWeekendCharge() && (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
                chargeDays++;
            }
        }

        return chargeDays;
    }

    /**
     * Independence Day, July 4th - If falls on weekend, it is observed on the
     * closest weekday (if Sat, then Friday before, if Sunday, then Monday
     * after)
     *
     * @param year
     * @return
     */
    private static int getIndependenceDay(int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, Calendar.JULY);
        cal.set(Calendar.DAY_OF_MONTH, 4);
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            cal.add(Calendar.DATE, -1);
        } else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            cal.add(Calendar.DATE, 1);
        }

        return cal.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Labor Day - First Monday in September
     *
     * @param month
     * @param year
     * @return day of the year
     */
    private static int getLaborDay(int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
        cal.set(Calendar.MONTH, Calendar.SEPTEMBER);
        cal.set(Calendar.YEAR, year);
            Date laborDayFormatted = cal.getTime();

        return cal.get(Calendar.DAY_OF_YEAR);
    }

    /**
     *
     * @param rentalDayCount
     * @param checkoutDate
     * @return day of the year
     * @throws ParseException
     */
    private static String getDueDate(int rentalDayCount, String checkoutDate) throws ParseException {
        Calendar cal = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("M/d/yy");
        cal.setTime(df.parse(checkoutDate));
        cal.add(Calendar.DATE, rentalDayCount);
        Date date = cal.getTime();
        return df.format(date);
    }

    private static Double truncateDouble(Double givenDouble) {
        return BigDecimal.valueOf(givenDouble)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

}
