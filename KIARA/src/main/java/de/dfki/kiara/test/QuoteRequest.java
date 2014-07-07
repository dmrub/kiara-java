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
import java.util.List;

/**
 *
 * @author Shahzad
 */
public class QuoteRequest implements Serializable {

    public boolean isEcho;
    public int counter;
    public int securityID;
    public double applVersionID;
    public double messageType;
    public double senderCompID;
    public int msgSeqNum;
    public int sendingTime;
    public double quoteReqID;
    public List<RelatedSym> related;
}
