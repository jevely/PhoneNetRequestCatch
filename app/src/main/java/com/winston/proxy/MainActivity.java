package com.winston.proxy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.security.KeyChain;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import androidx.annotation.Nullable;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.util.List;


public class MainActivity extends BaseActivity {
    private static final int INSTALL_CERT = 5000;
    private String mUa;
    private WebView webView;
private EditText edit;
    private final String permission1 = "android.permission.READ_EXTERNAL_STORAGE";
    private final String permission2 = "android.permission.WRITE_EXTERNAL_STORAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebView.setWebContentsDebuggingEnabled(true);
        mUa = WebSettings.getDefaultUserAgent(this).replace("; wv", "").replace(" Version/4.0", "");
        initView();
        installCert();
        new Thread(() -> ProxyManager.getInstance().start()).start();


        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = edit.getText().toString();
                if(TextUtils.isEmpty(path)){
                    webView.loadUrl("https://d1ya3pnv.com/MV3qpdrN?campaign=10139&sub_aff={app_id}&device_id={device_id}&sub_sub={task_id}&sub_aff3=EZ");
                }else{
                    webView.loadUrl(path);
                }

            }
        });

//        checkPermission();
    }

    private void checkPermission() {
        requestPermission(this, new String[]{permission1, permission2}, 1);
    }

    private void initView() {
        webView = findViewById(R.id.mwebview);
        edit = findViewById(R.id.edit);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }
        });
    }

    private void installCert() {
        SharedPreferences config = getSharedPreferences("config", MODE_PRIVATE);
        boolean installCert = config.getBoolean("install_cert", false);
        if (!installCert) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        byte[] keychainBytes;
                        InputStream is = null;
                        try {
                            is = getAssets().open("littleproxy-mitm.pem");
                            keychainBytes = IOUtils.toByteArray(is);
                        } finally {
                            if (is != null) {
                                is.close();
                            }
                        }
                        Intent intent = KeyChain.createInstallIntent();
                        intent.putExtra(KeyChain.EXTRA_CERTIFICATE, keychainBytes);
                        intent.putExtra(KeyChain.EXTRA_NAME, "Proxy");
                        startActivityForResult(intent, INSTALL_CERT);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            new Thread(runnable).start();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INSTALL_CERT) {
            if (resultCode == -1) {
                SharedPreferences config = getSharedPreferences("config", MODE_PRIVATE);
                config.edit().putBoolean("install_cert", true).apply();
            }
        }
    }

    @Override
    public void requestSuccess(int requestCode, List<String> permission) {
        super.requestSuccess(requestCode, permission);
        if (permission.contains(permission1) && permission.contains(permission2)) {
        }
    }
}
