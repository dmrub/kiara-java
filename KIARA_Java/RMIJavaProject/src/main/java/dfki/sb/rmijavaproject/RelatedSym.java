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
public class RelatedSym implements Serializable{
    double symbol;
    long orderQuantity;
    int side;
    long transactTime;
    int quoteType;
    int securityID;
    int securityIDSource;
    double dummy1;
    int dummy2;
}
