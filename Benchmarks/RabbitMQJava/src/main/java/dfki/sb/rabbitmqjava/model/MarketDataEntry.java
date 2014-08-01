package dfki.sb.rabbitmqjava.model;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;

/**
 *
 * @author Shahzad
 */
public class MarketDataEntry implements Serializable{
    private int mdUpdateAction;
    private int mdPriceLevel;
    private double mdEntryType;
    private int openCloseSettleFlag;
    private int securityIDSource;
    private int securityID;
    private int rptSeq;
    private double mdEntryPx;
    private int mdEntryTime;
    private int mdEntrySize;  // in original i32
    private int numberOfOrders;
    private double tradingSessionID;
    private double netChgPrevDay;
    private int tradeVolume;
    private double tradeCondition;
    private double tickDirection;
    private double quoteCondition;
    private int aggressorSide;
    private double matchEventIndicator;
    private double dummy1;
    private int dummy2;

    /**
     * @return the mdUpdateAction
     */
    public int getMdUpdateAction() {
        return mdUpdateAction;
    }

    /**
     * @param mdUpdateAction the mdUpdateAction to set
     */
    public void setMdUpdateAction(int mdUpdateAction) {
        this.mdUpdateAction = mdUpdateAction;
    }

    /**
     * @return the mdPriceLevel
     */
    public int getMdPriceLevel() {
        return mdPriceLevel;
    }

    /**
     * @param mdPriceLevel the mdPriceLevel to set
     */
    public void setMdPriceLevel(int mdPriceLevel) {
        this.mdPriceLevel = mdPriceLevel;
    }

    /**
     * @return the mdEntryType
     */
    public double getMdEntryType() {
        return mdEntryType;
    }

    /**
     * @param mdEntryType the mdEntryType to set
     */
    public void setMdEntryType(double mdEntryType) {
        this.mdEntryType = mdEntryType;
    }

    /**
     * @return the openCloseSettleFlag
     */
    public int getOpenCloseSettleFlag() {
        return openCloseSettleFlag;
    }

    /**
     * @param openCloseSettleFlag the openCloseSettleFlag to set
     */
    public void setOpenCloseSettleFlag(int openCloseSettleFlag) {
        this.openCloseSettleFlag = openCloseSettleFlag;
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
     * @return the rptSeq
     */
    public int getRptSeq() {
        return rptSeq;
    }

    /**
     * @param rptSeq the rptSeq to set
     */
    public void setRptSeq(int rptSeq) {
        this.rptSeq = rptSeq;
    }

    /**
     * @return the mdEntryPx
     */
    public double getMdEntryPx() {
        return mdEntryPx;
    }

    /**
     * @param mdEntryPx the mdEntryPx to set
     */
    public void setMdEntryPx(double mdEntryPx) {
        this.mdEntryPx = mdEntryPx;
    }

    /**
     * @return the mdEntryTime
     */
    public int getMdEntryTime() {
        return mdEntryTime;
    }

    /**
     * @param mdEntryTime the mdEntryTime to set
     */
    public void setMdEntryTime(int mdEntryTime) {
        this.mdEntryTime = mdEntryTime;
    }

    /**
     * @return the mdEntrySize
     */
    public int getMdEntrySize() {
        return mdEntrySize;
    }

    /**
     * @param mdEntrySize the mdEntrySize to set
     */
    public void setMdEntrySize(int mdEntrySize) {
        this.mdEntrySize = mdEntrySize;
    }

    /**
     * @return the numberOfOrders
     */
    public int getNumberOfOrders() {
        return numberOfOrders;
    }

    /**
     * @param numberOfOrders the numberOfOrders to set
     */
    public void setNumberOfOrders(int numberOfOrders) {
        this.numberOfOrders = numberOfOrders;
    }

    /**
     * @return the tradingSessionID
     */
    public double getTradingSessionID() {
        return tradingSessionID;
    }

    /**
     * @param tradingSessionID the tradingSessionID to set
     */
    public void setTradingSessionID(double tradingSessionID) {
        this.tradingSessionID = tradingSessionID;
    }

    /**
     * @return the netChgPrevDay
     */
    public double getNetChgPrevDay() {
        return netChgPrevDay;
    }

    /**
     * @param netChgPrevDay the netChgPrevDay to set
     */
    public void setNetChgPrevDay(double netChgPrevDay) {
        this.netChgPrevDay = netChgPrevDay;
    }

    /**
     * @return the tradeVolume
     */
    public int getTradeVolume() {
        return tradeVolume;
    }

    /**
     * @param tradeVolume the tradeVolume to set
     */
    public void setTradeVolume(int tradeVolume) {
        this.tradeVolume = tradeVolume;
    }

    /**
     * @return the tradeCondition
     */
    public double getTradeCondition() {
        return tradeCondition;
    }

    /**
     * @param tradeCondition the tradeCondition to set
     */
    public void setTradeCondition(double tradeCondition) {
        this.tradeCondition = tradeCondition;
    }

    /**
     * @return the tickDirection
     */
    public double getTickDirection() {
        return tickDirection;
    }

    /**
     * @param tickDirection the tickDirection to set
     */
    public void setTickDirection(double tickDirection) {
        this.tickDirection = tickDirection;
    }

    /**
     * @return the quoteCondition
     */
    public double getQuoteCondition() {
        return quoteCondition;
    }

    /**
     * @param quoteCondition the quoteCondition to set
     */
    public void setQuoteCondition(double quoteCondition) {
        this.quoteCondition = quoteCondition;
    }

    /**
     * @return the aggressorSide
     */
    public int getAggressorSide() {
        return aggressorSide;
    }

    /**
     * @param aggressorSide the aggressorSide to set
     */
    public void setAggressorSide(int aggressorSide) {
        this.aggressorSide = aggressorSide;
    }

    /**
     * @return the matchEventIndicator
     */
    public double getMatchEventIndicator() {
        return matchEventIndicator;
    }

    /**
     * @param matchEventIndicator the matchEventIndicator to set
     */
    public void setMatchEventIndicator(double matchEventIndicator) {
        this.matchEventIndicator = matchEventIndicator;
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
