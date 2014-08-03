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

package de.dfki.kiara.netty;

import de.dfki.kiara.Handler;
import de.dfki.kiara.Kiara;
import de.dfki.kiara.RunningService;
import de.dfki.kiara.Transport;
import de.dfki.kiara.TransportConnection;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Slf4JLoggerFactory;
import java.security.cert.CertificateException;
import javax.net.ssl.SSLException;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public abstract class AbstractTransport implements Transport, RunningService {

    private static final boolean SSL = System.getProperty("ssl") != null;

    private final EventLoopGroup group = new NioEventLoopGroup();

    static {
        InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());
    }

    public AbstractTransport() {
        Kiara.addRunningService(this);
    }

    @Override
    public void shutdownGracefully() {
        group.shutdownGracefully();
    }

    protected final EventLoopGroup getEventLoopGroup() {
        return group;
    }

    protected SslContext createServerSslContext() throws CertificateException, SSLException {
        if (SSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            return SslContext.newServerContext(ssc.certificate(), ssc.privateKey());
        } else {
            return null;
        }
    }

    public abstract ChannelHandler createServerChildHandler(Handler<TransportConnection> connectionHandler);

}
