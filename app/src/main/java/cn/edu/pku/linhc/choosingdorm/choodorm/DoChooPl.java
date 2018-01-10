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
public class DoChooPl extends Activity implements View.OnClickListener {
    private int num,buildingNo;
    public String stuid, id1, id2, id3, vcode1, vcode2, vcode3, res;
    private EditText mStuId, mId1, mId2, mId3, mVcode1, mVcode2, mVcode3, mBuildingNo;
    private EditText mNum, mVcode;
    private String[] dormRes;
    private Button mCheckBtn;
    private TextView mErrorHint;


    //OnClickListener 加载.java文件至对应之xml文件
    //继承自Activity类、 重写OnCreate方法
    //@SuppressLint("CutPasteId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在Activity中通过调用setContentView方法加载布局
        setContentView(R.layout.dochoopl);
        //加入屏幕旋转变化
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        //在Activity中测试网络，调用方法检测如有网络用Toast.makeText显示文字网络OK，若没有则显示网络挂了
        if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
            Log.d("new", "网络OK");
            Toast.makeText(DoChooPl.this, "网络OK！", Toast.LENGTH_LONG).show();
        } else {
            Log.d("new", "网络挂了");
            Toast.makeText(
                    DoChooPl.this, "网络挂了！", Toast.LENGTH_LONG).show();
        }

        init();

    }

    //初始化控件
    public void init() {
        mNum=findViewById(R.id.num_pl_edit_text);
        mStuId = findViewById(R.id.id_sing_edit_text);
        mId1=findViewById(R.id.id1_pl_edit_text);
        mId2=findViewById(R.id.id2_pl_edit_text);
        mId3=findViewById(R.id.id3_pl_edit_text);
        mVcode1=findViewById(R.id.code1_pl_edit_text);
        mVcode2=findViewById(R.id.code2_pl_edit_text);
        mVcode3=findViewById(R.id.code3_pl_edit_text);
        mBuildingNo = findViewById(R.id.buildingNo_sing_edit_text);
        stuid = getIntent().getStringExtra("STUDENTID");
        mErrorHint = findViewById(R.id.errorHint);

        mCheckBtn = (Button) findViewById(R.id.goto_do_ok_btn);
        mCheckBtn.setOnClickListener(this);

        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
    }

    //点击确认进入OK"
    @Override
    public void onClick (View v){
        if (v.getId() == R.id.goto_do_ok_btn) {
            okbutton();
            Intent intentOkPl = new Intent(DoChooPl.this,DoOk.class);
            startActivity(intentOkPl);

        }
    }

    public String infoToJson() {
        stuid = mStuId.getText().toString();
        id1 = mId1.getText().toString();
        vcode1 = mVcode1.getText().toString();
        id2 = mId2.getText().toString();
        vcode2 = mVcode2.getText().toString();
        id3 = mId3.getText().toString();
        vcode3 = mVcode3.getText().toString();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("num", num);
            jsonObject.put("stuid", stuid);
            jsonObject.put("stu1id", id1);
            jsonObject.put("vcode1", vcode1);
            jsonObject.put("stu2id", id2);
            jsonObject.put("vcode2", vcode2);
            jsonObject.put("stu3id", id3);
            jsonObject.put("buildingNo", buildingNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("Dorm_infoJsonStr",jsonObject.toString());
        return jsonObject.toString();
    }

    //读取读入数据
    private void okbutton() {
        final String address = "https://api.mysspku.com/index.php/V1/MobileCourse/SelectRoom";
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
                        con.setRequestMethod("POST");
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    }
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    InputStream in = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    while((str = reader.readLine()) != null);
                    {
                        response.append(str);
                        Log.d("new", str);
                    }
                    String responseStr = response.toString();
                    Log.d("new", responseStr);

                    errorCode = getErrorCode(responseStr);

                    Message message = new Message();
                    message.what=4;
                    message.obj=errorCode;
                    mHandler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    //输入错误后处理
    protected void okBackHint(int errorCode){
        if (errorCode == 0){
            Log.d("RESULT","成功");
            Toast.makeText(DoChooPl.this,"选择成功",Toast.LENGTH_LONG).show();
            Intent jumoIntent = new Intent(DoChooPl.this,Info.class);
            jumoIntent.putExtra("STUDID",stuid);
            startActivity(jumoIntent);
            finish();
        }else if (errorCode == 40001){
            Log.d("RESULT","40001");
            mErrorHint.setText("学号错误");
            mErrorHint.setVisibility(View.VISIBLE);
        }else if (errorCode == 40002){
            Log.d("RESULT","40002");
            mErrorHint.setText("错误");
            mErrorHint.setVisibility(View.VISIBLE);
        }else if (errorCode == 40009){
            Log.d("RESULT","40009");
            mErrorHint.setText("错误");
            mErrorHint.setVisibility(View.VISIBLE);
        }
    }

    //传送登入消息
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 4:
                    int errorCode = (int) msg.obj;
                    okBackHint(errorCode);
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


}






