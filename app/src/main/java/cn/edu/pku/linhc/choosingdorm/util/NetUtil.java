package cn.edu.pku.linhc.choosingdorm.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by test on 2018/1/5.
 */

        import android.annotation.SuppressLint;
        import android.content.Context;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;

        import android.annotation.SuppressLint;
        import android.content.Context;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;

//创建一网络工具类的类
public class NetUtil {
    //定义静态网络状态的参数，无网络为0；有无线网络为1；移动数据网络为2
    public static final int NETWORN_NONE =0;
    private static final int NETWORN_WIFI= 1;
    private static final int NETWORN_MOBILE =2;
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public static int getNetworkState(Context context){
        NetUtil.context = context;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        assert connManager != null;
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        //判断若网络状态为无连线返回NETWORN_NONE
        //                                WIFI连线返回NETWORN_WIFI
        //                                行动数据网络返回NETWORN_MOBILE
        if (networkInfo == null){
            return  NETWORN_NONE;
        }

        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE){
            return NETWORN_MOBILE;
        }else if (nType == ConnectivityManager.TYPE_WIFI){
            return NETWORN_WIFI;
        }
        return NETWORN_NONE;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        NetUtil.context = context;
    }
}
