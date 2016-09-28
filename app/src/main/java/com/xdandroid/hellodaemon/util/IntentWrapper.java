package com.xdandroid.hellodaemon.util;

import android.content.*;
import android.content.pm.*;

import com.xdandroid.hellodaemon.*;

import java.util.*;

/**
 * Created by xingda on 16-9-13.
 */

public class IntentWrapper {

    //华为 受保护的应用
    public static final int HUAWEI = 100;
    //小米 自启动管理
    public static final int XIAOMI = 101;
    //小米 神隐模式 (建议只在 App 的核心功能需要后台连接网络/后台定位的情况下使用)
    public static final int XIAOMI_GOD = 102;
    //三星 5.0+ 智能管理器
    public static final int SAMSUNG = 103;
    //魅族 自启动管理
    public static final int MEIZU = 104;
    //魅族 待机耗电管理
    public static final int MEIZU_GOD = 105;

    public static final List<IntentWrapper> sIntentWrapperList;

    static {
        sIntentWrapperList = new ArrayList<>();

        //华为 受保护的应用
        Intent huaweiIntent = new Intent();
        huaweiIntent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
        sIntentWrapperList.add(new IntentWrapper(huaweiIntent, IntentWrapper.HUAWEI));

        //小米 自启动管理
        Intent xiaomiIntent = new Intent();
        xiaomiIntent.setAction("miui.intent.action.OP_AUTO_START");
        xiaomiIntent.addCategory(Intent.CATEGORY_DEFAULT);
        sIntentWrapperList.add(new IntentWrapper(xiaomiIntent, IntentWrapper.XIAOMI));

        //小米 神隐模式 (建议只在 App 的核心功能需要后台连接网络/后台定位的情况下使用)
        Intent xiaomiGodIntent = new Intent();
        xiaomiGodIntent.setComponent(new ComponentName("com.miui.powerkeeper", "com.miui.powerkeeper.ui.HiddenAppsContainerManagementActivity"));
        sIntentWrapperList.add(new IntentWrapper(xiaomiGodIntent, IntentWrapper.XIAOMI_GOD));

        //三星 5.0+ 智能管理器
        Intent samsungIntent = MainActivity.sApp.getPackageManager().getLaunchIntentForPackage("com.samsung.android.sm");
        if (samsungIntent != null) sIntentWrapperList.add(new IntentWrapper(samsungIntent, IntentWrapper.SAMSUNG));

        //魅族 自启动管理
        Intent meizuIntent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        meizuIntent.addCategory(Intent.CATEGORY_DEFAULT);
        meizuIntent.putExtra("packageName", MainActivity.sApp.getPackageName());
        sIntentWrapperList.add(new IntentWrapper(meizuIntent, IntentWrapper.MEIZU));

        //魅族 待机耗电管理
        Intent meizuGodIntent = new Intent();
        meizuGodIntent.setComponent(new ComponentName("com.meizu.safe", "com.meizu.safe.powerui.AppPowerManagerActivity"));
        sIntentWrapperList.add(new IntentWrapper(meizuGodIntent, IntentWrapper.MEIZU_GOD));
    }

    private IntentWrapper(Intent intent, int type) {
        this.intent = intent;
        this.type = type;
    }

    private Intent intent;
    //以上常量
    public int type;

    /**
     * 安全地启动一个Activity
     */
    public void startActivity(Context context) {
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断本机上是否有能处理当前Intent的Activity
     */
    public boolean doesActivityExists(Context context) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list != null && list.size() > 0;
    }
}