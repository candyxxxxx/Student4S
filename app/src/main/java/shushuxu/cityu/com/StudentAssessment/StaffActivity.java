package shushuxu.cityu.com.StudentAssessment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;

import java.util.List;

public class StaffActivity extends AppCompatActivity {

    private static final int FIND_SUCCESS = 0;

    private static final int FIND_FAILED = 1;

    private EditText editText;
    private Toolbar toolbar;

    private long total_sleep;

    private long total_social;

    private long total_study;

    private long total_sports;

    private long total_sleep_score;

    private long total_social_score;

    private long total_study_score;

    private long total_sports_score;

    private long avg_sleep;

    private long avg_social;

    private long avg_study;

    private long avg_sports;

    private long avg_sleep_score;

    private long avg_social_score;

    private long avg_study_score;

    private long avg_sports_score;

    private double GPA;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FIND_SUCCESS:
                    Intent intent = new Intent(StaffActivity.this, StdInfoActivity.class);
                    intent.putExtra("sleep", avg_sleep);
                    intent.putExtra("social", avg_social);
                    intent.putExtra("study", avg_study);
                    intent.putExtra("sports", avg_sports);

                    intent.putExtra("sleep_score", avg_sleep_score);
                    intent.putExtra("social_score", avg_social_score);
                    intent.putExtra("study_score", avg_study_score);
                    intent.putExtra("sports_score", avg_sports_score);
                    intent.putExtra("GPA", GPA);

                    startActivity(intent);
                    break;
                case FIND_FAILED:
                    Toast.makeText(StaffActivity.this, "No such student", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_first);

        editText = (EditText) findViewById(R.id.stdname);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("Student 4S");
        initToolbarMenu(toolbar);
    }

    public void search(View view) {
        String input = editText.getText().toString();
        int std_num = Integer.parseInt(input);

        AVQuery<AVObject> query = new AVQuery<>("_User");
        query.whereEqualTo("id_", std_num);
        query.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null)
                    GPA = avObject.getDouble("GPA");
                else
                    e.printStackTrace();
            }
        });

        AVQuery<AVObject> query_g = new AVQuery<>("Student");
        query_g.whereEqualTo("id_", std_num);
        query_g.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                if (e == null) {
                    for (int i=0; i<list.size(); i++) {
                        AVObject avObject = list.get(i);
                        long sleep = avObject.getLong("sleep");
                        long social = avObject.getLong("social");
                        long study = avObject.getLong("study");
                        long sports = avObject.getLong("sports");
                        long sports_score = avObject.getLong("sports_score");
                        long sleep_score = avObject.getLong("sleep_score");
                        long social_score = avObject.getLong("social_score");
                        long study_score = avObject.getLong("study_score");

                        total_sleep += sleep;
                        total_sleep_score += sleep_score;
                        total_social += social;
                        total_social_score += social_score;
                        total_sports += sports;
                        total_sports_score += sports_score;
                        total_study += study;
                        total_study_score += study_score;
                    }

                    avg_sleep = total_sleep / list.size();
                    avg_social = total_social / list.size();
                    avg_sports = total_sports / list.size();
                    avg_study = total_study / list.size();

                    avg_sleep_score = total_sleep_score / list.size();
                    avg_social_score = total_social_score / list.size();
                    avg_sports_score = total_sports_score / list.size();
                    avg_study_score = total_study_score / list.size();

                    Message msg = new Message();
                    msg.what = FIND_SUCCESS;
                    handler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = FIND_FAILED;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    protected void initToolbarMenu(Toolbar toolbar) {
        toolbar.inflateMenu(R.menu.hierarchy);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_hierarchy:
                        AlertDialog.Builder builder = new AlertDialog.Builder(StaffActivity.this);
                        builder.setMessage("Are you sure to exit?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        AVUser.logOut();
                                        Intent intent = new Intent(StaffActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                })
                                .create()
                                .show();
                        break;
                }
                return true;
            }
        });
    }


}
