package com.example.main;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import android.widget.TextView;
import android.widget.Toast;

import com.example.bluetooth.BluetoothService;
import com.example.bluetooth.DeviceListActivity;
import com.example.bluetoothassist.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

import android.text.format.DateFormat;

public class MainActivity  extends Activity   {

    private static int RESULT_LOAD_IMAGE = 9;
    public static final int REC_DATA = 2;
    public static final int CONNECTED_DEVICE_NAME = 4;
    public static final int BT_TOAST = 5;
    public static final int MAIN_TOAST = 6;
    // 标志字符串常量
    public static final String DEVICE_NAME = "device name";
    public static final String TOAST = "toast";
    private static final String TAG = "MainActivity";
    private static final boolean DEBUG = true;
    // 意图请求码
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    static boolean isHEXsend = false, isHEXrec = false;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    timeThread timeTask = null;
    String[] hex_string_table = new String[256];

    private TextView RecDataView,tvModel,tvTempValue,tvHumiValue;
    private TextView dis_0,dis_1,dis_2,dis_3,dis_4,dis_5,dis_6,dis_7;

    private Button Button01, Button02, Button03, Button04;
    private Button Button05, Button06, Button07, Button08;
    private Button Button09, Button10, Button11, Button12;
    private Button Button13, Button14, Button15, Button16;
    private Button Button17, Button18, Button19, Button20;

    private LinearLayout lay10,lay11,lay12,lay13,lay14,lay15,lay16,lay17,lay18,lay19,lay20;
    private byte[] bs_send = new byte[1];


    private ImageView imageView;

    private String modeByte="00";
    private String ps="00";
    private String ts="00";
    int start = 0; //开始 停止
    private File root ;

    //图片路径
    private String picturePath="null";

    // 已连接设备的名字
    private String mConnectedDeviceName = null;
    //蓝牙连接服务对象
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothService mConnectService = null;
    /**
     * 自定义按键长按监听方法，进入定义按键的对话框
     */

    private OnLongClickListener ButtonLongClickListener = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                case R.id.Button01:
                    //new defineButtonDialog(MainActivity.this, Button01, Str2Send1).show();
                    break;
                case R.id.Button02:
                   //new defineButtonDialog(MainActivity.this, Button02, Str2Send2).show();
                    break;
                case R.id.Button03:
                    //new defineButtonDialog(MainActivity.this, Button03, Str2Send3).show();
                    break;

            }
            return false;
        }

    };
    /**
     * 所有按键的监听方法，
     * 分别根据按键ID处理其相应的事件
     */
    private OnClickListener ButtonClickListener = new OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.Button01:
                    //sendMessage(null, "1234");
                    bs_send[0] = '0';
                    mConnectService.write(bs_send);
                    break;

                case R.id.Button02:
                    bs_send[0] = '1';
                    mConnectService.write(bs_send);
                    break;

                case R.id.Button03:
                    bs_send[0] = '2';
                    mConnectService.write(bs_send);
                    break;

                case R.id.Button04:
                    bs_send[0] = '3';
                    mConnectService.write(bs_send);
                    break;

                case R.id.Button05:
                    bs_send[0] = '4';
                    mConnectService.write(bs_send);
                    break;
            }
        }
    };

    /**
     * 定时选择框组件监听方法
     * 开启相应的时间任务
     */
    private OnCheckedChangeListener checkBoxListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        }
    };
    private int align_num = 0;//对齐字节数
    private String target_device_name = null;
    // 用于从线程获取信息的Handler对象
    private final Handler mHandler = new Handler() {
        StringBuffer sb = new StringBuffer();
        byte[] bs;
        float sWidth;
        int b, i, lineWidth = 0, align_i = 0;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REC_DATA:

                        //处理下位机发送的数据
                        bs = (byte[]) msg.obj;//接收到的数据转成byte[]
                    try{

                        String dd=new String(bs,"UTF-8");

                        //tvModel.setText(dd);  //截取第一个字符串的内容 看是否是H开头
                        int home = bs[0];
                        if(home=='0')
                        {
                            int MQ3= bs[1];
                            int MQ7 = bs[2];的
                            dis_1.setText("酒精："  + MQ3  +  "%");
                            dis_2.setText("一氧化碳："  + MQ7 + "%");
                        }
                    } catch (Exception e) {

                    }

                   //RecDataView.append(sb);
                    break;
                case CONNECTED_DEVICE_NAME:
                    // 提示已连接设备名
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "已连接到"
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    MainActivity.this.setTitle("设备已连接");//蓝牙连接成功  //标题框显示  LDJ
                    break;
                case BT_TOAST:
                    if (mConnectedDeviceName != null)
                        Toast.makeText(getApplicationContext(), "与" + mConnectedDeviceName +
                                msg.getData().getString(TOAST), Toast.LENGTH_SHORT).show();
                    else Toast.makeText(getApplicationContext(), "与" + target_device_name +
                            msg.getData().getString(TOAST), Toast.LENGTH_SHORT).show();
                    MainActivity.this.setTitle("设备未连接");//蓝牙连接失败     //标题框显示  LDJ
                    mConnectedDeviceName = null;
                    break;
                case MAIN_TOAST:
                    Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    //跳入文件，首先执行下面函数，进行初始化控件
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //以下是实例化控件
        //imageView = (ImageView) findViewById(R.id.imgView);
        lay10=(LinearLayout) findViewById(R.id.Ly10);
        lay11=(LinearLayout) findViewById(R.id.Ly11);
        lay12=(LinearLayout) findViewById(R.id.Ly12);
        lay13=(LinearLayout) findViewById(R.id.Ly13);
        lay14=(LinearLayout) findViewById(R.id.Ly14);
        lay15=(LinearLayout) findViewById(R.id.Ly15);
        lay16=(LinearLayout) findViewById(R.id.Ly16);
        lay17=(LinearLayout) findViewById(R.id.Ly17);
        lay18=(LinearLayout) findViewById(R.id.Ly18);

        dis_1 = (TextView)findViewById(R.id.TextView01);
        dis_2 = (TextView)findViewById(R.id.TextView02);
        dis_3 = (TextView)findViewById(R.id.TextView03);
        dis_4 = (TextView)findViewById(R.id.TextView04);
        dis_5 = (TextView)findViewById(R.id.TextView05);
        dis_6 = (TextView)findViewById(R.id.TextView06);
        dis_7 = (TextView)findViewById(R.id.TextView07);

        //找到按键控件
        Button01 = (Button) findViewById(R.id.Button01);
        Button02 = (Button) findViewById(R.id.Button02);
        Button03 = (Button) findViewById(R.id.Button03);
        Button04 = (Button) findViewById(R.id.Button04);
        Button05 = (Button) findViewById(R.id.Button05);

        setupListener();   //按键进入监听

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); //创建蓝牙
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }//savaData();

        this.setTitle("设备未连接");        //标题框显示  LDJ

        //设置底边栏状态和相关显示
        showHome();//显示主页

        new TimeThread().start(); //启动新的线程  显示时间

        root = Environment.getExternalStorageDirectory();
    }


    @Override
    public void onStart() {
        super.onStart();
        if (DEBUG) Log.i(TAG, "++ ON START ++");
        // 查看请求打开蓝牙
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } //否则创建蓝牙连接服务对象
        else if (mConnectService == null) {
            mConnectService = new BluetoothService(mHandler);
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();

        if (mConnectService != null) {
            if (mConnectService.getState() == BluetoothService.IDLE) {
                //监听其他蓝牙主设备
                mConnectService.acceptWait();
            }
        }
    }

    //显示首页
    private void showHome()
    {
        lay10.setVisibility(View.VISIBLE);
        lay11.setVisibility(View.VISIBLE);
        lay12.setVisibility(View.VISIBLE);
        lay13.setVisibility(View.VISIBLE);
        lay14.setVisibility(View.VISIBLE);
        lay15.setVisibility(View.VISIBLE);
        lay16.setVisibility(View.VISIBLE);
        lay17.setVisibility(View.VISIBLE);
        lay18.setVisibility(View.VISIBLE);
       // RecDataView.setVisibility(View.VISIBLE);
    }

    /**
     * 使用FileWriter进行文本内容的追加
     *
     * @param content
     */
    private void addTxtToFileWrite(String content) {
        FileWriter writer = null;
        try {
            //FileWriter(file, true),第二个参数为true是追加内容，false是覆盖
            writer = new FileWriter(root + "/userr.txt", false);
            writer.write("\r\n");//换行
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //从SD卡读取文件
    public String readFromFile() {
        //读的时候要用字符流   万一里面有中文
        BufferedReader reader = null;
        FileInputStream fis;
        StringBuilder sbd = new StringBuilder();
        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "SD卡未就绪", Toast.LENGTH_SHORT).show();
            return "";
        }

        try {
            fis = new FileInputStream(root + "/userr.txt");
            reader = new BufferedReader(new InputStreamReader(fis));
            String row;
            while ((row = reader.readLine()) != null) {
                sbd.append(row);
            }
        } catch (FileNotFoundException e) {
          //  Toast.makeText(this, "文件不存在", Toast.LENGTH_SHORT).show();
            //e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sbd.toString();
    }

    /**
     * 设置自定按键及其他固定按键的监听方法
     */
    private void setupListener() {

        //为按钮添加点击事件
        Button01.setOnClickListener(ButtonClickListener);
        Button02.setOnClickListener(ButtonClickListener);
        Button03.setOnClickListener(ButtonClickListener);
        Button04.setOnClickListener(ButtonClickListener);
        Button05.setOnClickListener(ButtonClickListener);
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        if (DEBUG) Log.i(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (DEBUG) Log.i(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (DEBUG) Log.e(TAG, "onDestroy");
        // Stop the Bluetooth connection
        if (mConnectService != null) mConnectService.cancelAllBtThread();
        if (timeTask != null) timeTask.interrupt();

        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 按键触发发送字符串
     *
     * @param Str2Send 欲发送的字符串.
     */
    private void sendMessage(Button callButton, String Str2Send) {

        byte[] bs;
        if (!isHEXsend) {
            //Asc码发送 zgw 2018/12/17 0017 10:25:19
            bs = Str2Send.getBytes();
            mConnectService.write(bs);
        } else {
            //16进制发送 zgw 2018/12/17 0017 10:25:27
            for (char c : Str2Send.toCharArray()) {
                if (!((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F') || c == ' ')) {
                    Toast.makeText(this, "发送内容含非法字符", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            String[] ss = Str2Send.split(" ");
            bs = new byte[1];
            for (String s : ss) {
                if (s.length() != 0) {
                    bs[0] = (byte) (int) Integer.valueOf(s, 16);
                    mConnectService.write(bs);
                }
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "onActivityResult");
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    // 提取蓝牙地址数据
                    String address = data.getExtras().getString(DeviceListActivity.DEVICE_ADDRESS);
                    // 获取设备
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                    target_device_name = device.getName();
                    if (target_device_name.equals(mConnectedDeviceName)) {
                        Toast.makeText(this, "已连接" + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // 提示正在连接设备
                    Toast.makeText(this, "正在连接" + target_device_name, Toast.LENGTH_SHORT).show();
                    // 连接设备
                    mConnectService.connect(device);
                }
                break;
            case REQUEST_ENABLE_BT:
                // 请求打开蓝牙被用户拒绝时提示
                if (resultCode == Activity.RESULT_OK) {
                    mConnectService = new BluetoothService(mHandler);
                } else {
                    Toast.makeText(this, "拒绝打开蓝牙", Toast.LENGTH_SHORT).show();
                    //finish();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Connect:
                // 查看请求打开蓝牙
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                    return true;
                }
                // 打开设备蓝牙设备列表活动
                Intent serverIntent = new Intent(this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                return true;
        }
        return false;
    }

    private class timeThread extends Thread {
        private int sleeptime;

        timeThread(int militime) {
            super();
            sleeptime = militime;
        }

        @Override
        public void run() {
            while (!isInterrupted()) {
                if (DEBUG) Log.i("myDebug", "timeThread start");
                // sendMessage(null, sendContent.getText().toString());
                //mHandler.obtainMessage(MainActivity.REC_DATA,buffer.length,-1,buffer).sendToTarget();
                try {
                    Thread.sleep(sleeptime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
            if (DEBUG) Log.i("myDebug", "timeThread end");
        }
    }


    class TimeThread extends Thread {
        @Override
        public void run()
        {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 1;  //消息(一个整型值)

                    mmHandler.sendMessage(msg);// 每隔1秒发送一个msg给mHandler

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    private Handler mmHandler = new Handler(){
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    long sysTime = System.currentTimeMillis();//获取系统时间
                    //CharSequence sysTimeStr = DateFormat.format("yyyy年MM月dd日 HH:mm:ss", sysTime);//时间显示格式
                    //CharSequence sysTimeStr = DateFormat.format("HH:mm:ss", sysTime);//时间显示格式
                    break;

                default:                    break;
            }
        }
    };

}
