package shushuxu.cityu.com.StudentAssessment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import shushuxu.cityu.com.StudentAssessment.Base.BaseActivity;
import shushuxu.cityu.com.StudentAssessment.RecognitionBehavior.RecogActivity;
import shushuxu.cityu.com.StudentAssessment.RecognitionBehavior.RecogLocation;
import shushuxu.cityu.com.StudentAssessment.UI.Fragment.MainFragment;


public class MainActivity extends BaseActivity {

    private RecogActivity recogActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            loadRootFragment(R.id.fl_container, MainFragment.newInstance());
        }

        // Recog Activity ----------------------------
        recogActivity = new RecogActivity(this);
        Intent service_intent = new Intent(this, DetectionService.class);
        startService(service_intent);
        recogActivity.startRecogAcitivity();

        // Recog Voice ----------------------------
        RecogLocation recogLocation = new RecogLocation(this);

    }



    @Override
    public void onBackPressedSupport() {
        // 对于 3个类别的主Fragment内的回退back逻辑,已经在其onBackPressedSupport里各自处理了
        super.onBackPressedSupport();
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置横向(和安卓4.x动画相同)
        return new DefaultHorizontalAnimator();
    }
}
