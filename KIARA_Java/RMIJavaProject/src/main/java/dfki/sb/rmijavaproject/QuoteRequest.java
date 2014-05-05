/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dfki.sb.rmijavaproject;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Shahzad
 */
public class QuoteRequest implements Serializable{
    boolean isEcho;
    int counter;
    int securityID;
    double applVersionID;
    double messageType;
    double senderCompID;
    int msgSeqNum;
    int sendingTime;
    double quoteReqID;
    List<RelatedSym> related;
}
