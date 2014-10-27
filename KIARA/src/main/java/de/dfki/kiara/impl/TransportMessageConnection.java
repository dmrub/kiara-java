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
package de.dfki.kiara.impl;

import de.dfki.kiara.Message;
import de.dfki.kiara.Protocol;
import de.dfki.kiara.TransportConnection;
import de.dfki.kiara.TransportMessage;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class TransportMessageConnection extends AbstractMessageConnection {

    private static final Logger logger = LoggerFactory.getLogger(TransportMessageConnection.class);
    private final Protocol protocol;

    public TransportMessageConnection(TransportConnection transportConnection, ServiceMethodBinding serviceMethodBinding, Protocol protocol) {
        super(transportConnection, Collections.singletonList(serviceMethodBinding));
        this.protocol = protocol;
    }

    @Override
    public void onMessage(TransportMessage tmessage) {
        if (tmessage == null) {
            logger.error("Received null transport message");
            return;
        }

        try {
            Message message = protocol.createMessageFromData(tmessage.getPayload());

            synchronized (messageMap) {
                messageMap.put(message, tmessage);
            }

            processMessage(message);
        } catch (Exception ex) {
            logger.error("Message processing failed", ex);
        }
    }

}
