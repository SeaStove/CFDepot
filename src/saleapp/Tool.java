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
public class Tool {
    private String toolCode;
    private String brand;
    private String toolType;
    
    /**
     *
     */
    public Tool(){
        
    }
    
    /**
     *
     * @param givenToolCode
     * @param givenBrand
     * @param givenToolType
     */
    public Tool (String givenToolCode, String givenBrand, String givenToolType){
        toolCode = givenToolCode;
        brand = givenBrand;
        toolType = givenToolType;
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
    public String getBrand() {
        return brand;
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
     * @param toolCode
     */
    public void setToolCode(String toolCode) {
        this.toolCode = toolCode;
    }

    /**
     *
     * @param brand
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     *
     * @param toolType
     */
    public void setToolType(String toolType) {
        this.toolType = toolType;
    }
}
