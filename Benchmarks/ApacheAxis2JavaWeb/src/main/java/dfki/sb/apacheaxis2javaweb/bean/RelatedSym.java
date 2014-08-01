/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dfki.sb.apacheaxis2javaweb.bean;

import java.io.Serializable;

/**
 *
 * @author Shahzad
 */
public class RelatedSym implements Serializable{
    private double symbol;
    private long orderQuantity;
    private int side;
    private long transactTime;
    private int quoteType;
    private int securityID;
    private int securityIDSource;
    private double dummy1;
    private int dummy2;

    /**
     * @return the symbol
     */
    public double getSymbol() {
        return symbol;
    }

    /**
     * @param symbol the symbol to set
     */
    public void setSymbol(double symbol) {
        this.symbol = symbol;
    }

    /**
     * @return the orderQuantity
     */
    public long getOrderQuantity() {
        return orderQuantity;
    }

    /**
     * @param orderQuantity the orderQuantity to set
     */
    public void setOrderQuantity(long orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    /**
     * @return the side
     */
    public int getSide() {
        return side;
    }

    /**
     * @param side the side to set
     */
    public void setSide(int side) {
        this.side = side;
    }

    /**
     * @return the transactTime
     */
    public long getTransactTime() {
        return transactTime;
    }

    /**
     * @param transactTime the transactTime to set
     */
    public void setTransactTime(long transactTime) {
        this.transactTime = transactTime;
    }

    /**
     * @return the quoteType
     */
    public int getQuoteType() {
        return quoteType;
    }

    /**
     * @param quoteType the quoteType to set
     */
    public void setQuoteType(int quoteType) {
        this.quoteType = quoteType;
    }

    /**
     * @return the securityID
     */
    public int getSecurityID() {
        return securityID;
    }

    /**
     * @param securityID the securityID to set
     */
    public void setSecurityID(int securityID) {
        this.securityID = securityID;
    }

    /**
     * @return the securityIDSource
     */
    public int getSecurityIDSource() {
        return securityIDSource;
    }

    /**
     * @param securityIDSource the securityIDSource to set
     */
    public void setSecurityIDSource(int securityIDSource) {
        this.securityIDSource = securityIDSource;
    }

    /**
     * @return the dummy1
     */
    public double getDummy1() {
        return dummy1;
    }

    /**
     * @param dummy1 the dummy1 to set
     */
    public void setDummy1(double dummy1) {
        this.dummy1 = dummy1;
    }

    /**
     * @return the dummy2
     */
    public int getDummy2() {
        return dummy2;
    }

    /**
     * @param dummy2 the dummy2 to set
     */
    public void setDummy2(int dummy2) {
        this.dummy2 = dummy2;
    }
}
