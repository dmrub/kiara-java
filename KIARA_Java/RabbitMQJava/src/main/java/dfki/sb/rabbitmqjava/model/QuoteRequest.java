package dfki.sb.rabbitmqjava.model;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Shahzad
 */
public class QuoteRequest implements Serializable{
    public boolean isEcho;
    public int counter;
    public int securityID;
    public double applVersionID;
    public double messageType;
    public double senderCompID;
    public int msgSeqNum;
    public int sendingTime;
    public double quoteReqID;
    public RelatedSym[] related;

    /**
     * @return the isEcho
     */
    public boolean isIsEcho() {
        return isEcho;
    }

    /**
     * @param isEcho the isEcho to set
     */
    public void setIsEcho(boolean isEcho) {
        this.isEcho = isEcho;
    }

    /**
     * @return the counter
     */
    public int getCounter() {
        return counter;
    }

    /**
     * @param counter the counter to set
     */
    public void setCounter(int counter) {
        this.counter = counter;
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
     * @return the applVersionID
     */
    public double getApplVersionID() {
        return applVersionID;
    }

    /**
     * @param applVersionID the applVersionID to set
     */
    public void setApplVersionID(double applVersionID) {
        this.applVersionID = applVersionID;
    }

    /**
     * @return the messageType
     */
    public double getMessageType() {
        return messageType;
    }

    /**
     * @param messageType the messageType to set
     */
    public void setMessageType(double messageType) {
        this.messageType = messageType;
    }

    /**
     * @return the senderCompID
     */
    public double getSenderCompID() {
        return senderCompID;
    }

    /**
     * @param senderCompID the senderCompID to set
     */
    public void setSenderCompID(double senderCompID) {
        this.senderCompID = senderCompID;
    }

    /**
     * @return the msgSeqNum
     */
    public int getMsgSeqNum() {
        return msgSeqNum;
    }

    /**
     * @param msgSeqNum the msgSeqNum to set
     */
    public void setMsgSeqNum(int msgSeqNum) {
        this.msgSeqNum = msgSeqNum;
    }

    /**
     * @return the sendingTime
     */
    public int getSendingTime() {
        return sendingTime;
    }

    /**
     * @param sendingTime the sendingTime to set
     */
    public void setSendingTime(int sendingTime) {
        this.sendingTime = sendingTime;
    }

    /**
     * @return the quoteReqID
     */
    public double getQuoteReqID() {
        return quoteReqID;
    }

    /**
     * @param quoteReqID the quoteReqID to set
     */
    public void setQuoteReqID(double quoteReqID) {
        this.quoteReqID = quoteReqID;
    }

    /**
     * @return the related
     */
    public RelatedSym[] getRelated() {
        return related;
    }

    /**
     * @param related the related to set
     */
    public void setRelated(RelatedSym[] related) {
        this.related = related;
    }
}
