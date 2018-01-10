package cn.edu.pku.linhc.choosingdorm.choodorm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import cn.edu.pku.linhc.choosingdorm.bean.DormInfo;
import cn.edu.pku.linhc.choosingdorm.bean.PersInfo;
import cn.edu.pku.linhc.choosingdorm.util.NetUtil;

/**
 * Created by test on 2018/1/5.
 */

@SuppressLint("Registered")
public class Info extends Activity implements View.OnClickListener {
    String id, Vcode, gender, dormr;
    TextView mId , mName, mGender, mVcode, mRoom, mBuilding, mLocation, mGrade;
    TextView mb5, mb13, mb14, mb8, mb9;
    Button mSingBtn, mPlBtn;
    ImageView mMap;
    private SharedPreferences sharedPreferences;


    //OnClickListener 加载.java文件至对应之xml文件
    //继承自Activity类、 重写OnCreate方法
    //@SuppressLint("CutPasteId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在Activity中通过调用setContentView方法加载布局
        setContentView(R.layout.info);
        //加入屏幕旋转变化
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);



        //在Activity中测试网络，调用方法检测如有网络用Toast.makeText显示文字网络OK，若没有则显示网络挂了
        if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
            Log.d("new", "网络OK");
            Toast.makeText(Info.this, "网络OK！", Toast.LENGTH_LONG).show();
        } else {
            Log.d("new", "网络挂了");
            Toast.makeText(
                    Info.this, "网络挂了！", Toast.LENGTH_LONG).show();
        }

        init();

        Intent intent = this.getIntent();
        getInfo(intent.getStringExtra("STUID"));
    }

    //将控件和物件联络
    public void init() {
        mId = findViewById(R.id.home);
        mName = findViewById(R.id.input_name);
        mGender = findViewById(R.id.input_gender);
        mVcode = findViewById(R.id.input_Vcode);
        mRoom = findViewById(R.id.input_dormnum);
        mBuilding = findViewById(R.id.input_buildingnum);
        mLocation = findViewById(R.id.input_distruct);
        mGrade = findViewById(R.id.input_grade);
        mb5 = findViewById(R.id.input_5);
        mb13 = findViewById(R.id.input_5);
        mb14 = findViewById(R.id.input_5);
        mb8 = findViewById(R.id.input_5);
        mb9 = findViewById(R.id.input_5);
        mSingBtn = (Button) findViewById(R.id.goto_dochoosing_btn);
        mSingBtn.setOnClickListener(this);

        mPlBtn = (Button) findViewById(R.id.goto_dochoopl_btn);
        mPlBtn.setOnClickListener(this);

        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
    }

    public void getInfo(String stuId) {
        final String address = "https://api.mysspku.com/index.php/V1/MobileCourse/getDetail?ustuid=" + stuId;
        new Thread(new Runnable() {
            @Override
            public void run() {
                PersInfo student1 = new PersInfo();
                HttpURLConnection httpURLConnection = null;
                try {
                    URL url = null;
                    int errorCode = -1;
                    try {
                        url = new URL(address);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    try {
                        httpURLConnection = (HttpURLConnection) (url != null ? url.openConnection() : null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        assert httpURLConnection != null;
                        httpURLConnection.setRequestMethod("GET");
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    }
                    httpURLConnection.setConnectTimeout(8000);
                    httpURLConnection.setReadTimeout(8000);
                    InputStream in = httpURLConnection.getInputStream();
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
                    student1 = parseInfo(responseStr);

                    Message message = new Message();
                    message.what=2;
                    message.obj=student1;
                    mHandler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

     //输入错误后处理
     public PersInfo parseInfo(String jsonStr)throws JSONException{
         PersInfo student3 = new PersInfo();
         JSONObject jsonObject = new JSONObject(jsonStr);
         if(jsonObject!=null){
             if (jsonObject.getInt("errcode")==0){
                 JSONObject data = jsonObject.getJSONObject("data");
                 student3.setName(data.getString("name"));
                 student3.setId(data.getString("studentid"));
                 student3.setGender(data.getString("gender"));
                 student3.setVcode(data.getString("vcode"));
                 if (data.has("room")){
                     Log.d("Dorm_hasRoom","true");
                student3.setRoom(data.getString("room"));
                student3.setBuilding(data.getString("building"));
             }else{
                 Log.d("Dorm_hasRoom", String.valueOf(false));
                 student3.setRoom("未选择");
                 student3.setBuilding("无");
                 }
                 student3.setLocation(data.getString("location"));
                 student3.setGrade(data.getString("grade"));
             }
         }else {
             Log.d("dorm_error","解析错误");
         }

         return student3;
     }

     //初始化更新控件
    public void refreshInfo(PersInfo persInfo){
        id = persInfo.getId();
        Vcode = persInfo.getVcode();
        gender = persInfo.getGender();
        
        mId.setText(persInfo.getId());
        mName.setText(persInfo.getName());
        mGender.setText(persInfo.getGender());
        mVcode.setText(persInfo.getVcode());
        if (!persInfo.getRoom().equals("未选择")){
            mRoom.setTextColor(Color.BLACK);
            mBuilding.setTextColor(Color.BLACK);
        }else {
            mRoom.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        }
        mRoom.setText(persInfo.getRoom());
        mBuilding.setText(persInfo.getBuilding());
        mGrade.setText(persInfo.getGrade());
    }

    //获取宿舍数据
    public void getDormInfo(int gender) {
        final String address = "https://api.mysspku.com/index.php/V1/MobileCourse/getRoom?gender=" + gender;
        new Thread(new Runnable() {
            @Override
            public void run() {
                DormInfo dormInfo = new DormInfo();
                HttpURLConnection httpURLConnection = null;
                try {
                    URL url = null;
                    try {
                        url = new URL(address);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    try {
                        httpURLConnection = (HttpURLConnection) (url != null ? url.openConnection() : null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        assert httpURLConnection != null;
                        httpURLConnection.setRequestMethod("GET");
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    }
                    httpURLConnection.setConnectTimeout(8000);
                    httpURLConnection.setReadTimeout(8000);
                    InputStream in = httpURLConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    while((str = reader.readLine()) != null);
                    {
                        response.append(str);
                    }
                    String responseStr = response.toString();
                    Log.d("new", responseStr);
                    dormInfo = parsedorminfoInfo(responseStr);
                    Log.d("new", "完成宿舍解析信息");
                    Message message1 = new Message();
                    message1.what=3;
                    message1.obj=dormInfo;
                    mHandler.sendMessage(message1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //解析宿舍数据
    public DormInfo parsedorminfoInfo(String jsonStr){
        Log.d("Dorm_jsonStr",jsonStr);
        DormInfo dormInfo = new DormInfo();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonStr);
        }catch (JSONException e){
            e.printStackTrace();
        }
        try {
            if (jsonObject.getInt("errcode")==0){
                Log.d("Dorm_parse","解析宿舍信息");
                JSONObject data = jsonObject.getJSONObject("data");
                dormInfo.setFive(data.getString("5"));
                dormInfo.setThirteen(data.getString("13"));
                dormInfo.setFourteen(data.getString("14"));
                dormInfo.setEight(data.getString("8"));
                dormInfo.setNine(data.getString("9"));
                dormr = data.getString("5"+";"+data.getString("13")+";"+data.getString("14")+";"+data.getString("8")+";"+data.getString("9"));
            }else {
                Log.d("Dorm_error",jsonObject.getString("errorcode"));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return dormInfo;
    }

    //将更新数据传入控件
    public void refreshDormInfoInfo(DormInfo dormInfo){
        Log.d("Dorm_test",dormInfo.getFive());
        mb5.setText(dormInfo.getFive());
        mb13.setText(dormInfo.getThirteen());
        mb14.setText(dormInfo.getFourteen());
        mb8.setText(dormInfo.getEight());
        mb9.setText(dormInfo.getNine());
    }

    //传送选择宿舍消息
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 2:
                    PersInfo student2 = (PersInfo) msg.obj;
                    String gender = student2.getGender();
                    Log.d("Gender_dorm",gender);
                    if (gender.equals("男")){
                        getDormInfo(1);
                        Log.d("Gender1_dorm",gender);
                    }else if (gender.equals("女")){
                        getDormInfo(2);
                        Log.d("Gender2_dorm",gender);
                    }
                    refreshInfo(student2);
                    break;

                case 3:
                    DormInfo dormInfo = (DormInfo) msg.obj;
                    refreshDormInfoInfo(dormInfo);
                    break;

                default:
                    break;
            }
        }
    };


    //点击单人选宿舍时进入单人选择宿舍页面，点击多人选宿舍时进入多人选择宿舍页面
    @Override
    public void onClick (View v){

        if (v.getId() == R.id.goto_dochoosing_btn) {
            if (mRoom.getText().equals("未选择")){
                Intent intentChoose = new Intent(Info.this,DoChooSing.class);
                intentChoose.putExtra("STUDENTID",id);
                intentChoose.putExtra("STUDENTCODE",Vcode);
                intentChoose.putExtra("DORMR",dormr);

                startActivity(intentChoose);
            }
        }

        if (v.getId() == R.id.goto_dochoopl_btn) {
            if (mRoom.getText().equals("未选择")){
                Intent intentChoose = new Intent(Info.this,DoChooPl.class);
                intentChoose.putExtra("STUDENTID",id);
                intentChoose.putExtra("STUDENTCODE",Vcode);
                intentChoose.putExtra("DORMR",dormr);

                startActivity(intentChoose);
            }
        }
    }

}






