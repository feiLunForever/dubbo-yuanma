package com.xxx;

import org.apache.dubbo.common.extension.SPI;

/**
 * 通讯协议接口
 */
@SPI
public interface IProtocol {
    //发送请求
    void sendRequest(String message);
}