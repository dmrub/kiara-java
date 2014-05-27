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
