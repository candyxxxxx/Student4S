package shushuxu.cityu.com.StudentAssessment.UI.Fragment.FirstTab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import shushuxu.cityu.com.StudentAssessment.Adapter.ProbeAdapter;
import shushuxu.cityu.com.StudentAssessment.Base.BaseFragment;
import shushuxu.cityu.com.StudentAssessment.Entity.ProbeCategory;
import shushuxu.cityu.com.StudentAssessment.Event.TabSelectedEvent;
import shushuxu.cityu.com.StudentAssessment.R;
import shushuxu.cityu.com.StudentAssessment.UI.Fragment.MainFragment;


/**
 * Created by shushu on 7/8/16.
 */
public class StudyPagerFragment extends BaseFragment {

    private ListView mListView;
    private ProbeAdapter mAdapter;
    private ArrayList<ProbeCategory> mItems;

    private boolean mInAtTop = true;

    public static StudyPagerFragment newInstance() {

        Bundle args = new Bundle();

        StudyPagerFragment fragment = new StudyPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_first_pager_study, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        EventBus.getDefault().register(this);

        mListView = (ListView) view.findViewById(R.id.study_listView);

        initData();

        mAdapter = new ProbeAdapter(_mActivity, mItems);

        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProbeAdapter.ViewHolder viewHolder = (ProbeAdapter.ViewHolder) view.getTag();
                // 改变CheckBox的状态
                viewHolder.enabledProbeCheckbox.toggle();
                // 将CheckBox的选中状况记录下来
                ProbeAdapter.getIsSelected().put(position, viewHolder.enabledProbeCheckbox.isChecked());

                boolean isChecked = viewHolder.enabledProbeCheckbox.isChecked();
                if(isChecked){
                    viewHolder.probeEnabled.setText("Enabled");
                } else {
                    viewHolder.probeEnabled.setText("Disabled");
                }
                Log.d("MainActivity", "Click: --------------");
            }
        });

    }

    private void initData() {
        mItems = new ArrayList<>();

        ProbeCategory categoryOne = new ProbeCategory("Studying");
        categoryOne.addItem("Lecture");
        categoryOne.addItem("Self-study");
//
//        ProbeCategory categoryTwo = new ProbeCategory("Social Activity");
//        categoryTwo.addItem("Conversation");
//        categoryTwo.addItem("Party time");
//
//        ProbeCategory categoryFour = new ProbeCategory("Sleeping");
//        categoryFour.addItem("Sleeping");

        mItems.add(categoryOne);
//        mItems.add(categoryTwo);
//        mItems.add(categoryFour);

    }

    /**
     * Reselected Tab
     */
    @Subscribe
    public void onTabSelectedEvent(TabSelectedEvent event) {
        if (event.position != MainFragment.FIRST) return;

//        if (mInAtTop) {
//            mRefreshLayout.setRefreshing(true);
//            onRefresh();
//        } else {
//            scrollToTop();
//        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mListView.setAdapter(null);
        EventBus.getDefault().unregister(this);
    }
}
