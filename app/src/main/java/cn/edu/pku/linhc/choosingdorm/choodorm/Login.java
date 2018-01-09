package cn.edu.pku.linhc.choosingdorm.choodorm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import cn.edu.pku.linhc.choosingdorm.R;
import cn.edu.pku.linhc.choosingdorm.util.NetUtil;

import static org.xmlpull.v1.XmlPullParser.END_DOCUMENT;
import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_DOCUMENT;
import static org.xmlpull.v1.XmlPullParser.START_TAG;


/**
 * Created by test on 2018/1/5.
 */


@SuppressLint("Registered")
public class Login extends Activity implements View.OnClickListener {
    public String username;
    public String password;
    private EditText mUsername, mPassword;
    private ImageView mUsernameClear, mPasswordClear, mDeveloperLogo;
    private TextView mUsernameError, mPwdError;
    private ImageView mLoginBtn;
    private long[] mHints = new long[10];

    //OnClickListener 加载.java文件至对应之xml文件
    //继承自Activity类、 重写OnCreate方法
    //@SuppressLint("CutPasteId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在Activity中通过调用setContentView方法加载布局
        setContentView(R.layout.login);
        //加入屏幕旋转变化
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        mLoginBtn = (ImageView) findViewById(R.id.login_icon);
        mLoginBtn.setOnClickListener(this);

        //在Activity中测试网络，调用方法检测如有网络用Toast.makeText显示文字网络OK，若没有则显示网络挂了
        if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
            Log.d("new", "网络OK");
            Toast.makeText(Login.this, "网络OK！", Toast.LENGTH_LONG).show();
        } else {
            Log.d("new", "网络挂了");
            Toast.makeText(
                    Login.this, "网络挂了！", Toast.LENGTH_LONG).show();
        }

        init();

    }

    //初始化控件
    public void init(){
        mUsername = findViewById(R.id.u_edit_text);
        mPassword = findViewById(R.id.p_edit_text);
        mUsernameError = findViewById(R.id.u_error);
        mUsernameError.setVisibility(View.INVISIBLE);
        mPwdError = findViewById(R.id.p_error);
        mPwdError.setVisibility(View.INVISIBLE);
        mLoginBtn = findViewById(R.id.login_icon);
        mLoginBtn.setOnClickListener(this);
        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);

        mUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0){
                    mUsernameError.setVisibility(View.INVISIBLE);
                    mPwdError.setVisibility(View.INVISIBLE);
                }
            }
        });

    }


//    //与后台判断账号密码是否正确 记忆以自动登入
//    public void judgeChecked(){
//        if (SharedPreferences.getBoolean("RMB_PWD",false)){
//            mRmbPwd.setChecked(true);
//            mUsername.setText(SharedPreferences.getString("USERNAME",""));
//            mPassword.setText(SharedPreferences.getString("PASSWORD",""));
//            if (sharedPreferences.getBoolean("AUTO_LOGIN",false)){
//                mAutoLogin.setChecked(true);
//                login();
//            }
//        }
//    }


    //读取登入数据
    private void login() {
        username = mUsername.getText().toString();
        password = mPassword.getText().toString();

        if (!username.equals("") && !password.equals("")) {
//            if (mRmbPwd.isChecked()) {
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString("USERNAME", username);
//                editor.putString("PASSWORD", password);
//                editor.commit();
//            }

            final String address = "https://api.mysspku.com/index.php/V1/MobileCourse/Login?username=" + username + "&password=" + password;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpURLConnection con = null;
                    int errorCode = -1;
                    try {
                        URL url = null;
                        try {
                            url = new URL(address);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        try {
                            con = (HttpURLConnection) (url != null ? url.openConnection() : null);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            assert con != null;
                            con.setRequestMethod("GET");
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        }
                        con.setConnectTimeout(8000);
                        con.setReadTimeout(8000);
                        InputStream in = con.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder response = new StringBuilder();
                        String str;
                        While((str = reader.readLine()) != null);
                        {
                            response.append(str);
                            Log.d("new", str);
                        }
                        String responseStr = response.toString();
                        Log.d("new", responseStr);

                        errorCode = getErrorCode(responseStr);

                        Message message = new Message();
                        message.what=2;
                        message.obj=errorCode;
                        mHandler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (con != null) {
                            con.disconnect();
                        }
                    }
                }

                private void While(boolean b) {

                }
            }).start();
        } else {
            Toast.makeText(Login.this, "请输入完整", Toast.LENGTH_LONG).show();
        }
    }

    //输入错误后处理
    protected void loginBackHint(int errorCode){
        if (errorCode == 0){
            Intent jumoIntent = new Intent(Login.this,Info.class);
            jumoIntent.putExtra("PERSID",username);
            startActivity(jumoIntent);
            finish();
        }else if (errorCode == 40001){
            mUsernameError.setVisibility(View.VISIBLE);
            mUsername.setText("");
            mPassword.setText("");
        }else if (errorCode == 40002){
            mPwdError.setVisibility(View.VISIBLE);
            mPassword.setText("");
        }else if (errorCode == 40009){
            Toast.makeText(Login.this,"输入错误，请连络相关管理者",Toast.LENGTH_LONG)
.show();
        mUsername.setText("");
        mPassword.setText("");
        }

    }

    //传送登入消息
    private Handler mHandler = new Handler(){
      @Override
        public void handleMessage(Message msg){
          switch (msg.what){
              case 2:
                  int errorCode = (int) msg.obj;
                  loginBackHint(errorCode);
                  break;
                  default:
                      break;
          }
      }
    };



    //配置登入错误连结
    public int getErrorCode(String jsonData){
        int errorCode = -1;
        try{
            JSONObject jsonObject = new JSONObject(jsonData);
            if(jsonObject!=null){
                errorCode = jsonObject.getInt("errcode");
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return errorCode;
    }

            //点击登入时确认账号密码是否正确后"传回相应提示或反应"
            @Override
            public void onClick (View v){

                if (v.getId() == R.id.login_icon) {
                    login();
                }
                if (v.getId() == R.id.title_home) {
                    System.arraycopy(mHints, 1, mHints, 0, mHints.length - 1);
                    mHints[mHints.length - 1] = SystemClock.uptimeMillis();
                    if (SystemClock.uptimeMillis() - mHints[0] < 2000) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                                .setTitle("")
                                .setMessage("40001:账号不存在" + "\n" + "40002:密码有误" + "\n" + "400009:参数错误")
                                .setNeutralButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        builder.show();
                    }
                }
            }
        }






