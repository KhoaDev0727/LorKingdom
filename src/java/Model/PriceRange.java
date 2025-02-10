/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author Truong Van Khang - CE181852
 */
public class PriceRange {

    private int PriceRangeID;
    private int priceFrom;
    private int priceTo;

    public PriceRange(int PriceRangeID, int priceFrom, int priceTo) {
        this.PriceRangeID = PriceRangeID;
        this.priceFrom = priceFrom;
        this.priceTo = priceTo;
    }

    public int getPriceRangeID() {
        return PriceRangeID;
    }

    public void setPriceRangeID(int PriceRangeID) {
        this.PriceRangeID = PriceRangeID;
    }

    public int getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(int priceFrom) {
        this.priceFrom = priceFrom;
    }

    public int getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(int priceTo) {
        this.priceTo = priceTo;
    }
    
    
}
