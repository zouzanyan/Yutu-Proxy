package com.zou.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TargetToClientHandler extends ChannelInboundHandlerAdapter {
    private ChannelHandlerContext channelHandlerContext;
    public TargetToClientHandler(ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContext = channelHandlerContext;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 把目标服务器的返回数据写回client与proxy建立的channel上
        channelHandlerContext.channel().writeAndFlush(msg);
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        channelHandlerContext.channel().close();
    }
}
