package com.zou.server;

import com.zou.handler.ProxyCoreHandler;
import com.zou.handler.Socks5InitialRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.socksx.v5.Socks5CommandRequestDecoder;
import io.netty.handler.codec.socksx.v5.Socks5InitialRequestDecoder;
import io.netty.handler.codec.socksx.v5.Socks5ServerEncoder;

public class YutuProxyServer {
    public static void start(){
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup work = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss,work)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel){
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //socks编码器
                            pipeline.addLast(Socks5ServerEncoder.DEFAULT);
                            //socks 初始化解码器,包括客户端的身份验证方法和版本号等信息。
                            pipeline.addLast(new Socks5InitialRequestDecoder());
                            //返回认证方式,建立连接初始化
                            pipeline.addLast(new Socks5InitialRequestHandler());
                            //用于解码 socks 协议的命令请求消息，包括客户端请求的目标地址、端口号和请求类型等信息。
                            pipeline.addLast(new Socks5CommandRequestDecoder());
                            //代理核心实现
                            pipeline.addLast(new ProxyCoreHandler());
                        }
                    });

            try {
                ChannelFuture future = serverBootstrap.bind(8888).sync();
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }
}
