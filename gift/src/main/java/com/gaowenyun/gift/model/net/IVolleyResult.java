package com.gaowenyun.gift.model.net;

/**
 *
 * 网络请求结果接口
 */
public interface IVolleyResult {
    void success(String resultStr);
    void failure();
}
