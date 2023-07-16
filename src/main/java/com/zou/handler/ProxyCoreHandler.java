package com.zou.handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.socksx.v5.*;

public class ProxyCoreHandler extends SimpleChannelInboundHandler<DefaultSocks5CommandRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DefaultSocks5CommandRequest defaultSocks5CommandRequest) throws Exception {

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                //response
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new TargetToClientHandler(channelHandlerContext));
                    }
                });
        ChannelFuture future = bootstrap.connect(defaultSocks5CommandRequest.dstAddr(), defaultSocks5CommandRequest.dstPort());
        future.addListener(new ChannelFutureListener() {

            //request
            public void operationComplete(ChannelFuture future){
                if (future.isSuccess()) {

                    channelHandlerContext.pipeline().addLast(new ClientToTargetHandler(future));
                    Socks5CommandResponse commandResponse = new DefaultSocks5CommandResponse(Socks5CommandStatus.SUCCESS, Socks5AddressType.IPv4);
                    channelHandlerContext.writeAndFlush(commandResponse);
                } else {
                    Socks5CommandResponse commandResponse = new DefaultSocks5CommandResponse(Socks5CommandStatus.FAILURE, Socks5AddressType.IPv4);
                    channelHandlerContext.writeAndFlush(commandResponse);
                }
            }

        });


    }
}
