package shushuxu.cityu.com.StudentAssessment.UI.Fragment.FirstTab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import shushuxu.cityu.com.StudentAssessment.Base.BaseFragment;
import shushuxu.cityu.com.StudentAssessment.R;


/**
 * Created by shushu on 7/8/16.
 */
public class SportsPagerFragment extends BaseFragment {

    public static SportsPagerFragment newInstance() {

        Bundle args = new Bundle();
        SportsPagerFragment fragment = new SportsPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_first_pager_sports, container, false);
        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.sportsCheckbox);
        final TextView textView = (TextView) view.findViewById(R.id.sports_enabled);
        checkBox.setChecked(true);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    textView.setText("Enabled");
                    //checkBox.setChecked(true);
                } else {
                    textView.setText("Disabled");
                    //checkBox.setChecked(false);
                }
            }
        });
        return view;
    }
}
