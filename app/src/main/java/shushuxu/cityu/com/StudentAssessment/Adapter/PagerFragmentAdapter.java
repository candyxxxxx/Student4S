package shushuxu.cityu.com.StudentAssessment.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import shushuxu.cityu.com.StudentAssessment.UI.Fragment.FirstTab.SleepPagerFragment;
import shushuxu.cityu.com.StudentAssessment.UI.Fragment.FirstTab.SocialPagerFragment;
import shushuxu.cityu.com.StudentAssessment.UI.Fragment.FirstTab.StudyPagerFragment;
import shushuxu.cityu.com.StudentAssessment.UI.Fragment.FirstTab.SportsPagerFragment;


/**
 * Created by shushu on 7/8/16.
 */
public class PagerFragmentAdapter extends FragmentPagerAdapter {
    private String[] mTab = new String[]{"Study", "Sports", "Sleep", "Social"};

    public PagerFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return StudyPagerFragment.newInstance();
        } else if (position == 1){
            return SportsPagerFragment.newInstance();
        } else if (position == 2){
            return SleepPagerFragment.newInstance();
        } else{
            return SocialPagerFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return mTab.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTab[position];
    }
}
