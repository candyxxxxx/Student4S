package shushuxu.cityu.com.StudentAssessment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;

import shushuxu.cityu.com.StudentAssessment.Base.BaseActivity;

/**
 * Created by shushu on 7/7/16.
 */
public class LoginActivity extends BaseActivity {

    private static final int LOGIN_SUCCESS = 1;

    private EditText username;

    private EditText password;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LOGIN_SUCCESS:
                    if (AVUser.getCurrentUser().get("type").equals("student")) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else if (AVUser.getCurrentUser().get("type").equals("staff")) {
                        Intent intent = new Intent(LoginActivity.this, StaffActivity.class);
                        startActivity(intent);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AVUser current_user = AVUser.getCurrentUser();
        if (current_user != null) {
            if (current_user.get("type").equals("student")) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            } else if (current_user.get("type").equals("staff")) {
                Intent intent = new Intent(LoginActivity.this, StaffActivity.class);
                startActivity(intent);
            }
            this.finish();
        }

        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.passwd_edit);

    }

    public void enter(View view)
    {
        AVUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e == null) {
                    Message message = new Message();
                    message.what = LOGIN_SUCCESS;
                    handler.sendMessage(message);
                } else  {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "Not collect to the network!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
