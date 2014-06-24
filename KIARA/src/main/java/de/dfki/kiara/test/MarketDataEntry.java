/*
 * Copyright (C) 2014 German Research Center for Artificial Intelligence (DFKI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.dfki.kiara.test;

import java.io.Serializable;

public class MarketDataEntry implements Serializable {

    public int mdUpdateAction;
    public int mdPriceLevel;
    public double mdEntryType;
    public int openCloseSettleFlag;
    public int securityIDSource;
    public int securityID;
    public int rptSeq;
    public double mdEntryPx;
    public int mdEntryTime;
    public int mdEntrySize;  // in original i32
    public int numberOfOrders;
    public double tradingSessionID;
    public double netChgPrevDay;
    public int tradeVolume;
    public double tradeCondition;
    public double tickDirection;
    public double quoteCondition;
    public int aggressorSide;
    public double matchEventIndicator;
    public double dummy1;
    public int dummy2;
}
