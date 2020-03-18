package com.winston.proxy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 检测权限
     *
     * @param permission 权限
     * @return true为有权限   false为没有权限
     */
    public Boolean checkPermission(String permission) {
        int selfPermission = ContextCompat.checkSelfPermission(this, permission);
        return selfPermission != PackageManager.PERMISSION_DENIED;
    }

    /**
     * 申请权限
     *
     * @param activity    activity
     * @param permissions 权限（支持多个）
     */
    public void requestPermission(Activity activity, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    /**
     * 申请权限
     *
     * @param activity activity
     */
    public void requestPermission(Activity activity, String permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, new String[]{permissions}, requestCode);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<String> success = new ArrayList<>();
        List<String> error = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                error.add(permissions[i]);
            } else {
                success.add(permissions[i]);
            }
        }
        requestSuccess(requestCode, success);
        requestError(requestCode, error);
    }

    public void requestSuccess(int requestCode, List<String> permission) {

    }

    public void requestError(int requestCode, List<String> permission) {

    }

}
