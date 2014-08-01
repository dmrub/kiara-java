/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
