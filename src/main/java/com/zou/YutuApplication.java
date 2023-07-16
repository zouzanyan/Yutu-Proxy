package com.zou;

import com.zou.server.YutuProxyServer;

public class YutuApplication {
    public static void main(String[] args) {
        YutuProxyServer.start();
        System.out.println("netty server启动成功");
    }
}