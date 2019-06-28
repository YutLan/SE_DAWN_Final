package com.example.DAWN.UI;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.DAWN.CommonService.Data;
import com.example.DAWN.CommonService.ThreadForUDP;
import com.example.DAWN.R;
import com.example.DAWN.UserManament.UserDataManager;

import java.util.concurrent.TimeUnit;

public class Register extends AppCompatActivity {
    private EditText mAccount;                        //用户名编辑
    private EditText mPwd;                            //密码编辑
    private EditText mPwdCheck;                       //密码编辑
    //mAccount.setBackgroundColor(Color.argb(255, 0, 255, 0));

    private Button mSureButton;                       //确定按钮
    private Button mCancelButton;                     //取消按钮
    private UserDataManager mUserDataManager;         //用户数据管理类

    // AsyncTask for UDP-Client
    static class AsyncConUDP extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... msg) {
            ThreadForUDP R1 = new ThreadForUDP ("Thread-UDP-LOGIN");
            R1.start (msg[0]);
            return null;
        }
    }

    private Boolean sendRegister(String userID, String pwd) throws InterruptedException {
        new AsyncConUDP ().execute ("register!"+userID+"!"+pwd);
        TimeUnit.MILLISECONDS.sleep (1000);
        if(Data.accountStatus.containsKey ("isRegisterValid")){
            return Data.accountStatus.get ("isRegisterValid");
        }
        System.out.println ("Register Failed");
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        mAccount = (EditText) findViewById(R.id.resetpwd_edit_name);
        mPwd = (EditText) findViewById(R.id.resetpwd_edit_pwd_old);
        mPwdCheck = (EditText) findViewById(R.id.resetpwd_edit_pwd_new);

        mSureButton = (Button) findViewById(R.id.register_btn_sure);
        mCancelButton = (Button) findViewById(R.id.register_btn_cancel);

        mSureButton.setOnClickListener(m_register_Listener);      //注册界面两个按钮的监听事件
        mCancelButton.setOnClickListener(m_register_Listener);

        ImageView image = (ImageView) findViewById(R.id.logoforRe);             //使用ImageView显示logo
        image.setImageResource(R.drawable.logoforreg);


        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();                              //建立本地数据库
        }

    }
    View.OnClickListener m_register_Listener = new View.OnClickListener() {    //不同按钮按下的监听事件选择
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.register_btn_sure:                       //确认按钮的监听事件
                    try {
                        register_check();
                    } catch (InterruptedException e) {
                        e.printStackTrace ();
                    }
//                    onDestroy();
                    break;
                case R.id.register_btn_cancel:                     //取消按钮的监听事件,由注册界面返回登录界面
                    Intent intent_Register_to_Login = new Intent(Register.this, Login.class) ;    //切换User Activity至Login Activity
                    startActivity(intent_Register_to_Login);
                    finish();
//                    onDestroy();
                    break;
            }
        }
    };
    public void register_check() throws InterruptedException {                                //确认按钮的监听事件
        if (isUserNameAndPwdValid()) {
            String userName = mAccount.getText().toString().trim();
            String userPwd = mPwd.getText().toString().trim();
            String userPwdCheck = mPwdCheck.getText().toString().trim();
            //检查用户是否存在
            int count=mUserDataManager.findUserByName(userName);
            System.out.println("count"+count);
            //用户已经存在时返回，给出提示文字
            //System.out.println("");

            if(count>0)
            {
                Toast.makeText(this, getString(R.string.name_already_exist),Toast.LENGTH_SHORT).show();
                return ;
            }
            if(userPwd.equals(userPwdCheck)==false)
            {     //两次密码输入不一样
                Toast.makeText(this, getString(R.string.pwd_not_the_same),Toast.LENGTH_SHORT).show();
                return ;
            } else {
//                UserData mUser = new UserData(userName, userPwd);
//                System.out.println(mUser);
//                mUserDataManager.openDataBase();
//                long flag = mUserDataManager.insertUserData(mUser); //新建用户信息
//                if (flag == -1) {
//                    System.out.println("flag -1");
//                    Toast.makeText(this, getString(R.string.register_fail),Toast.LENGTH_SHORT).show();
//                }else{
//                    if(sendRegister (userName, userPwd)) {
//                        System.out.println ("sendRegister ");
//                        Toast.makeText (this, getString (R.string.register_success), Toast.LENGTH_SHORT).show ();
//                        Intent intent_Register_to_Login = new Intent (Register.this, Login.class);    //切换User Activity至Login Activity
//                        startActivity (intent_Register_to_Login);
//                        finish ();
//                    }
//                }
                if(sendRegister (userName, userPwd)) {
                    System.out.println ("sendRegister ");
                    Toast.makeText (this, getString (R.string.register_success), Toast.LENGTH_SHORT).show ();
                    Intent intent_Register_to_Login = new Intent (Register.this, Login.class);    //切换User Activity至Login Activity
                    startActivity (intent_Register_to_Login);
                    finish ();
                }else {
                    Toast.makeText (this, getString (R.string.register_fail), Toast.LENGTH_SHORT).show ();
                }

            }
        }
    }
    public boolean isUserNameAndPwdValid() {
        if (mAccount.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.account_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (mPwd.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.pwd_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        }else if(mPwdCheck.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.pwd_check_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
