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

package de.dfki.kiara.jos;

import de.dfki.kiara.util.MessageDispatcher;
import de.dfki.kiara.util.Pipeline;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class JosMessageDispatcher implements MessageDispatcher {

    private final long messageId;
    private final BlockingQueue<Object> messageQueue;

    JosMessageDispatcher(JosProtocol protocol, long messageId) {
        this.messageId = messageId;
        this.messageQueue = new ArrayBlockingQueue<>(1);
    }

    public BlockingQueue<Object> getQueue() {
        return messageQueue;
    }

    @Override
    public Object process(Object input) throws Exception {
        if (!(input instanceof JosMessage)) {
            return input;
        }

        JosMessage message = (JosMessage) input;

        if (messageId == message.getId()) {
            messageQueue.add(message);
            return null; // stop processing
        }
        return input;
    }

}
