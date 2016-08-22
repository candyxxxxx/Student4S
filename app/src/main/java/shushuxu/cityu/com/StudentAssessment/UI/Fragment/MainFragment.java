package shushuxu.cityu.com.StudentAssessment.UI.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import me.yokeyword.fragmentation.SupportFragment;
import shushuxu.cityu.com.StudentAssessment.Base.BaseFragment;
import shushuxu.cityu.com.StudentAssessment.Event.StartBrotherEvent;
import shushuxu.cityu.com.StudentAssessment.Event.TabSelectedEvent;
import shushuxu.cityu.com.StudentAssessment.R;
import shushuxu.cityu.com.StudentAssessment.UI.Fragment.FirstTab.FirstTabFragment;
import shushuxu.cityu.com.StudentAssessment.UI.Fragment.SecondTab.SecondTabFragment;
import shushuxu.cityu.com.StudentAssessment.UI.Fragment.ThirdTab.ThirdTabFragment;
import shushuxu.cityu.com.StudentAssessment.UI.View.BottomBar;
import shushuxu.cityu.com.StudentAssessment.UI.View.BottomBarTab;


/**
 * Created by shushu on 7/8/16.
 */
public class MainFragment extends BaseFragment {

    private static final int REQ_MSG = 10;

    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;

    private SupportFragment[] mFragments = new SupportFragment[3];

    private BottomBar mBottomBar;


    public static MainFragment newInstance() {

        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        if (savedInstanceState == null) {
            mFragments[FIRST] = FirstTabFragment.newInstance();
            mFragments[SECOND] = SecondTabFragment.newInstance();
            mFragments[THIRD] = ThirdTabFragment.newInstance();

            loadMultipleRootFragment(R.id.fl_tab_container, FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRD]);
        } else {
            // get mFragments的引用
            mFragments[FIRST] = findChildFragment(FirstTabFragment.class);
            mFragments[SECOND] = findChildFragment(SecondTabFragment.class);
            mFragments[THIRD] = findChildFragment(ThirdTabFragment.class);
        }

        initView(view);
        return view;
    }

    private void initView(View view) {
        EventBus.getDefault().register(this);
        mBottomBar = (BottomBar) view.findViewById(R.id.bottomBar);

        mBottomBar
                .addItem(new BottomBarTab(_mActivity, R.drawable.ic_message_white_24dp, "Detection"))
                .addItem(new BottomBarTab(_mActivity, R.drawable.ic_account_circle_white_24dp, "Me"))
                .addItem(new BottomBarTab(_mActivity, R.drawable.ic_discover_white_24dp, "Discover"));

        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                showHideFragment(mFragments[position], mFragments[prePosition]);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
                // Use EventBus to decoupling, reception in FirstPagerFragment,FirstHomeFragment
                // Interaction: reselect tab, if tab isn't at top of stack, then move to top, otherwise, refresh
                EventBus.getDefault().post(new TabSelectedEvent(position));
            }
        });
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == REQ_MSG && resultCode == RESULT_OK) {

        }
    }

    /**
     * start other BrotherFragment
     */
    @Subscribe
    public void startBrother(StartBrotherEvent event) {
        start(event.targetFragment);
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
