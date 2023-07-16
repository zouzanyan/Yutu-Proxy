package com.zou.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.socksx.SocksVersion;
import io.netty.handler.codec.socksx.v5.DefaultSocks5InitialResponse;
import io.netty.handler.codec.socksx.v5.Socks5AuthMethod;
import io.netty.handler.codec.socksx.v5.Socks5InitialRequest;
import io.netty.handler.codec.socksx.v5.Socks5InitialResponse;

public class Socks5InitialRequestHandler extends SimpleChannelInboundHandler<Socks5InitialRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Socks5InitialRequest socks5InitialRequest) throws Exception {
        if (socks5InitialRequest.decoderResult().isFailure()) {
            channelHandlerContext.fireChannelRead(socks5InitialRequest);
        } else {
            if (socks5InitialRequest.version().equals(SocksVersion.SOCKS5)) {
                Socks5InitialResponse initialResponse = new DefaultSocks5InitialResponse(Socks5AuthMethod.NO_AUTH);
                channelHandlerContext.writeAndFlush(initialResponse);
            }
        }
    }
}
