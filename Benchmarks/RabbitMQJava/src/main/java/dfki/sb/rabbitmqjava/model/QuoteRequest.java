package dfki.sb.rabbitmqjava.model;

/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2014 German Research Center for Artificial Intelligence (DFKI)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
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
