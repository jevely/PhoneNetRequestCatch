package com.winston.proxy;

import android.util.Log;

import com.winston.proxy.tool.Logger;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.filters.RequestFilter;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

import java.util.Map;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

public class ProxyManager implements RequestFilter {
    private static final String TAG = ProxyManager.class.getSimpleName();
    private static final boolean DEBUG = true;
    public static final int LISTEN_PORT = 9999;
    private static ProxyManager mInstance = null;
    private BrowserMobProxy proxy;

    private static void LOG(String message) {
        if (DEBUG) {
            Log.d(TAG, message);
        }
    }

    private ProxyManager() {
        proxy = new BrowserMobProxyServer();
    }

    public synchronized static ProxyManager getInstance() {
        if (mInstance == null) {
            mInstance = new ProxyManager();
        }
        return mInstance;
    }

    public void start() {
        proxy.addRequestFilter(this);
        proxy.start(LISTEN_PORT);
    }

    @Override
    public HttpResponse filterRequest(HttpRequest request, HttpMessageContents contents, HttpMessageInfo messageInfo) {
        String url = messageInfo.getUrl();
        String method = request.getMethod().toString();
        HttpHeaders headers = request.headers();

        Logger.d("拦截的url:" + method + " - " + url);
        Logger.d("request.getUri()) = " + request.getUri());
        Logger.d("messageInfo.getOriginalUrl() = " + messageInfo.getOriginalUrl());
        Logger.d("messageInfo.getUrl() = " + messageInfo.getUrl());
        Logger.d("contents.getContentType() = " + contents.getContentType());
        Logger.d("contents.getTextContents() = " + contents.getTextContents());
        Logger.d("contents.getBinaryContents() = " + contents.getBinaryContents().toString());
        for (Map.Entry<String, String> header : headers) {
            Logger.d(header.getKey() + " - " + header.getValue());
//            if (header.getKey().equals("X-Requested-With")) {// handle requests from "every" WebView
//
//            }
        }
        Logger.d("------------------");
        return null;
    }
}
