package shushuxu.cityu.com.StudentAssessment;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

/**
 * Created by shushu on 7/15/16.
 */
public class GlobalApplication extends Application
{
    private static GlobalApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.initialize(this, "3n1DiWnFTpdvn13dHyoQd0u7-gzGzoHsz", "eWCT1Ws7VkkdHAvPqjUQnSsr");
    }

    public GlobalApplication()
    {
        super();
        instance = this;
    }

    public static Application getContext()
    {
        return instance;
    }
}
