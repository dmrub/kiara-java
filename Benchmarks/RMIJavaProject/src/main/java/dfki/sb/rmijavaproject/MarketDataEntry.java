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
package dfki.sb.rmijavaproject;

import java.io.Serializable;

/**
 *
 * @author Shahzad
 */
public class MarketDataEntry implements Serializable{
    int mdUpdateAction;
    int mdPriceLevel;
    double mdEntryType;
    int openCloseSettleFlag;
    int securityIDSource;
    int securityID;
    int rptSeq;
    double mdEntryPx;
    int mdEntryTime;
    int mdEntrySize;  // in original i32
    int numberOfOrders;
    double tradingSessionID;
    double netChgPrevDay;
    int tradeVolume;
    double tradeCondition;
    double tickDirection;
    double quoteCondition;
    int aggressorSide;
    double matchEventIndicator;
    double dummy1;
    int dummy2;
}
