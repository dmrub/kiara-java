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
package dfki.sb.tcpsocketproject.model;

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
