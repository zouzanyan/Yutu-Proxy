package com.zou;

import com.zou.server.YutuProxyServer;

public class YutuApplication {
    public static void main(String[] args) {
        System.out.println("netty server启动成功");
        YutuProxyServer.start();
    }
}