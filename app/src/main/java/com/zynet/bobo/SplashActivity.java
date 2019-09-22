package com.zynet.bobo;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.zynet.bobo.base.BaseMvpActivity;
import com.zynet.bobo.base.BaseMvpPresenter;
import com.zynet.bobo.utils.ToastUtil;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;


/**
 * @author Bobo
 * @date 2019/9/21
 * describe
 */

@RuntimePermissions
public class SplashActivity extends BaseMvpActivity {
    private Handler mHandler;
    private CloseRunnable runnable;

    @Override
    protected BaseMvpPresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {
        mHandler = new Handler(getMainLooper());
        runnable = new CloseRunnable();
        SplashActivityPermissionsDispatcher.getMultiWithPermissionCheck(this);
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission
            .READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void getMulti() {
        mHandler.postDelayed(runnable, 2000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SplashActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode,
                grantResults);
    }

    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission
            .READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showRationale(final PermissionRequest request) {
        request.proceed();
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission
            .READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void multiDenied() {
        //您已经拒绝权限,继续申请
        ToastUtil.showMessage("您已经拒绝权限,程序自动退出");
        finish();
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission
            .READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void multiNeverAsk() {
        new AlertDialog.Builder(this)
                .setMessage("您已经拒绝请求权限,请到设置页面打开权限")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startSetting(SplashActivity.this, getPackageName());
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).show();
    }

    private void startSetting(Context context, String packageName) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", packageName, null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", packageName);
        }
        context.startActivity(intent);
    }


    class CloseRunnable implements Runnable {

        @Override
        public void run() {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(runnable);
    }


}
