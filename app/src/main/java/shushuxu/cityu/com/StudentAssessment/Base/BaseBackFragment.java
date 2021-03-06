package shushuxu.cityu.com.StudentAssessment.Base;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;
import shushuxu.cityu.com.StudentAssessment.R;


/**
 * Created by shushu on 7/8/16.
 */
public class BaseBackFragment extends SwipeBackFragment {

    private static final String TAG = "Demo";

    protected void initToolbarNav(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.onBackPressed();
            }
        });

        initToolbarMenu(toolbar);
    }

    protected void initToolbarMenu(Toolbar toolbar) {
        toolbar.inflateMenu(R.menu.hierarchy);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_hierarchy:
                        _mActivity.showFragmentStackHierarchyView();
                        _mActivity.logFragmentStackHierarchy(TAG);
                        break;
                }
                return true;
            }
        });
    }

}
