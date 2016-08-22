package shushuxu.cityu.com.StudentAssessment.Base;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.avos.avoscloud.AVUser;

import me.yokeyword.fragmentation.SupportFragment;
import shushuxu.cityu.com.StudentAssessment.LoginActivity;
import shushuxu.cityu.com.StudentAssessment.R;


/**
 * Created by shushu on 7/7/16.
 */
public class BaseFragment extends SupportFragment {

    private static final String TAG = "Demo";

    protected void initToolbarMenu(Toolbar toolbar) {
        toolbar.inflateMenu(R.menu.hierarchy);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_hierarchy:
//                        _mActivity.showFragmentStackHierarchyView();
//                        _mActivity.logFragmentStackHierarchy(TAG);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Are you sure to exit?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        AVUser.logOut();
                                        Intent intent = new Intent(getContext(), LoginActivity.class);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                })
                                .create()
                                .show();;
                        break;
                }
                return true;
            }
        });
    }
}
