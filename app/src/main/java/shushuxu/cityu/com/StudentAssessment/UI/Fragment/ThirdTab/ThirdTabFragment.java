package shushuxu.cityu.com.StudentAssessment.UI.Fragment.ThirdTab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import shushuxu.cityu.com.StudentAssessment.Adapter.HomeAdapter;
import shushuxu.cityu.com.StudentAssessment.Base.BaseLazyMainFragment;
import shushuxu.cityu.com.StudentAssessment.Entity.Article;
import shushuxu.cityu.com.StudentAssessment.Event.StartBrotherEvent;
import shushuxu.cityu.com.StudentAssessment.R;


/**
 * Created by shushu on 7/8/16.
 */
public class ThirdTabFragment extends BaseLazyMainFragment implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout mRefreshLayout;

    private RecyclerView mRecy;
    private Toolbar mToolbar;

    private HomeAdapter mAdapter;

    private boolean mAtTop = true;

    private int mScrollTotal;

    private String[] mTitles = new String[]{
            "10 Study Tips to Achieve your Goals in 2016",
            "5 Tips for Better Study-Life Balance",
            "New features for reviews and experiments in Google Play Developer Console app",
            "Researchers identify amount of exercise needed to lower risk of 5 diseases",
            "Sleep apnea may worsen liver disease for obese teens"
    };

    private String[] mContents = new String[]{
            "This is particularly true of students that are looking to make the most of their study time and get better grades at school. " +
                    "That’s why we’ve put together a list of study tips to help you develop a learning strategy that will allow you to achieve your study goals in 2016",
            "If you're finding it more challenging than ever to juggle the demands of your job and the rest of your life, you're not alone.",
            "With over one million apps published through the Google Play Developer Console, we know how important it is " +
                    "to publish with confidence, acquire users, learn about them, and manage your business. Whether reacting " +
                    "to a critical performance issue or responding to a negative review, checking on your apps when and where you need to is invaluable.",
            "Doctors often tout how exercise can help ward off disease, but previous research hasn’t concluded just " +
                    "how much physical activity is needed to reap those benefits",
            "For teens with non-alcoholic fatty liver disease (NAFLD), breathing disruptions during sleep may worsen " +
                    "scarring in the liver, according to a new study."
    };


    public static ThirdTabFragment newInstance() {

        Bundle args = new Bundle();

        ThirdTabFragment fragment = new ThirdTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_third, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);

        mRecy = (RecyclerView) view.findViewById(R.id.recy);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);

        mToolbar.setTitle("Discovery");
        initToolbarMenu(mToolbar);
    }

    @Override
    protected void initLazyView(@Nullable Bundle savedInstanceState) {
        mRefreshLayout.setOnRefreshListener(this);

        mAdapter = new HomeAdapter(_mActivity);
        LinearLayoutManager manager = new LinearLayoutManager(_mActivity);
        mRecy.setLayoutManager(manager);
        mRecy.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 通知MainActivity跳转至DetailFragment
                EventBus.getDefault().post(new StartBrotherEvent(DetailFragment.newInstance(mAdapter.getItem(position).getTitle())));
            }

//            @Override
//            public void onItemClick(int position, View view, RecyclerView.ViewHolder vh) {
//            }
        });

        // Init Datas
        List<Article> articleList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Article article = new Article(mTitles[i], mContents[i]);
            articleList.add(article);
        }
        mAdapter.setDatas(articleList);

        mRecy.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mScrollTotal += dy;
                if (mScrollTotal <= 0) {
                    mAtTop = true;
                } else {
                    mAtTop = false;
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
            }
        }, 2500);
    }


}
