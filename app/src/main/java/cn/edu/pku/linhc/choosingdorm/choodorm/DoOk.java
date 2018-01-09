package cn.edu.pku.linhc.choosingdorm.choodorm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import cn.edu.pku.linhc.choosingdorm.R;
import cn.edu.pku.linhc.choosingdorm.util.NetUtil;

/**
 * Created by test on 2018/1/5.
 */

@SuppressLint("Registered")
public class DoOk extends Activity implements View.OnClickListener {
    private Button mBackBtn;

    //OnClickListener 加载.java文件至对应之xml文件
    //继承自Activity类、 重写OnCreate方法
    //@SuppressLint("CutPasteId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在Activity中通过调用setContentView方法加载布局
        setContentView(R.layout.dook);
        //加入屏幕旋转变化
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        //在Activity中测试网络，调用方法检测如有网络用Toast.makeText显示文字网络OK，若没有则显示网络挂了
        if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
            Log.d("new", "网络OK");
            Toast.makeText(DoOk.this, "网络OK！", Toast.LENGTH_LONG).show();
        } else {
            Log.d("new", "网络挂了");
            Toast.makeText(
                    DoOk.this, "网络挂了！", Toast.LENGTH_LONG).show();
        }

        init();

    }

    //初始化控件
    public void init() {
        mBackBtn = (Button) findViewById(R.id.back_to_login_btn);
        mBackBtn.setOnClickListener(this);

        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
    }

    //点击登入时确认账号密码是否正确后"传回相应提示或反应"
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back_to_login_btn) {
            Intent intentBack = new Intent(DoOk.this, Login.class);
            startActivity(intentBack);

        }
    }

}