package com.iproxier.ipx4andorid.vpn.core;

import com.iproxier.ipx4andorid.vpn.tunnel.Config;
import com.iproxier.ipx4andorid.vpn.tunnel.RawTunnel;
import com.iproxier.ipx4andorid.vpn.tunnel.Tunnel;
import com.iproxier.ipx4andorid.vpn.tunnel.httpconnect.HttpConnectConfig;
import com.iproxier.ipx4andorid.vpn.tunnel.httpconnect.HttpConnectTunnel;

import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class TunnelFactory {

    public static Tunnel wrap(SocketChannel channel, Selector selector) {
        return new RawTunnel(channel, selector);
    }

    public static Tunnel createTunnelByConfig(InetSocketAddress destAddress, Selector selector) throws Exception {
        if (destAddress.isUnresolved()) {
            Config config = ProxyConfig.Instance.getDefaultTunnelConfig(destAddress);
            if (config instanceof HttpConnectConfig) {
                return new HttpConnectTunnel((HttpConnectConfig) config, selector);
            }
            throw new Exception("The config is unknow.");
        } else {
            return new RawTunnel(destAddress, selector);
        }
    }

}
