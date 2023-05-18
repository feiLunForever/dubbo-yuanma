package com.xxx;

import org.apache.dubbo.common.extension.ExtensionLoader;

import java.util.Iterator;
import java.util.ServiceLoader;

public class Main {

    public static void main(String[] args) {
        dubbo_spi();
    }

    public static void spi() {

        ServiceLoader<Log> serviceLoader = ServiceLoader.load(Log.class); //第一步：调用ServiceLoader.load加载实现类

        Iterator<Log> iterator = serviceLoader.iterator(); //第二步：通过遍历获取实现类
        
        while (iterator.hasNext()) {
            
            Log log = iterator.next();
            
            log.log("JDK SPI");
        }
    }

    public static void dubbo_spi() {

        //获取ExtensionLoader对象
        ExtensionLoader<IProtocol> extensionLoader = ExtensionLoader.getExtensionLoader(IProtocol.class);
        //通过key获取对象的TcpProtocol
        IProtocol tcpProtocol = extensionLoader.getExtension("tcp");
        tcpProtocol.sendRequest("Hello!");
        //通过key获取对象的HttpProtocol
        IProtocol httpProtocol = extensionLoader.getExtension("http");
        httpProtocol.sendRequest("Hello!");
    }
    
} 