package shushuxu.cityu.com.StudentAssessment.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import shushuxu.cityu.com.StudentAssessment.Entity.ProbeCategory;
import shushuxu.cityu.com.StudentAssessment.R;


/**
 * Created by shushu on 7/8/16.
 */
public class ProbeAdapter extends BaseAdapter {

    private Context mContext;

    private LayoutInflater mInflater;

    private static final int TYPE_CATEGORY_ITEM = 0;

    private static final int TYPE_ITEM = 1;

    private ArrayList<ProbeCategory> mListData;

    // control the status of CheckBox
    private static HashMap<Integer,Boolean> isSelected;

    // Construct ProbeAdapter
    public ProbeAdapter(Context context, ArrayList<ProbeCategory> pData){
        this.mContext = context;
        mListData = pData;
        mInflater = LayoutInflater.from(context);
        isSelected = new HashMap<Integer, Boolean>();
        // 初始化数据
        initSelected();
    }

//    // initial isSelected
//    private void initSelected() {
//        for (int i = 0; i < mItems.size(); i++){
//            getIsSelected().put(i, false);
//        }
//    }

    // initial isSelected
    private void initSelected() {

        int categoryFirstIndex = 0;
        int pos = 0;

        for (ProbeCategory category : mListData){
            int size = category.getItemCount();
            pos++;
            // 在当前分类中的索引值
            int categoryIndex = pos - categoryFirstIndex;
            // item在当前分类内
            for(; categoryIndex < size; categoryIndex++){
                getIsSelected().put(pos, true);
                pos++;
                Log.d("ProbeAdapter","---------------here");
            }
            // 索引移动到当前分类结尾，即下一个分类第一个元素索引
            categoryFirstIndex += size;
        }
    }

    @Override
    public int getCount() {
        int count = 0;

        if (null != mListData) {
            //  所有分类中item的总和是ListVIew  Item的总个数
            for (ProbeCategory category : mListData) {
                count += category.getItemCount();
            }
        }
        return count;
    }

    @Override
    public Object getItem(int position) {

        // 异常情况处理
        if (null == mListData || position <  0|| position > getCount()) {
            return null;
        }

        // 同一分类内，第一个元素的索引值
        int categoryFirstIndex = 0;

        for (ProbeCategory category : mListData) {
            int size = category.getItemCount();
            // 在当前分类中的索引值
            int categoryIndex = position - categoryFirstIndex;
            // item在当前分类内
            if (categoryIndex < size) {
                return  category.getItem( categoryIndex );
            }
            // 索引移动到当前分类结尾，即下一个分类第一个元素索引
            categoryFirstIndex += size;
        }

        return null;
    }

    @Override
    public int getItemViewType(int position) {
        // 异常情况处理
        if (null == mListData || position <  0|| position > getCount()) {
            return TYPE_ITEM;
        }

        int categoryFirstIndex = 0;

        for (ProbeCategory category : mListData) {
            int size = category.getItemCount();
            // 在当前分类中的索引值
            int categoryIndex = position - categoryFirstIndex;
            if (categoryIndex == 0) {
                return TYPE_CATEGORY_ITEM;
            }

            categoryFirstIndex += size;
        }

        return TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        int itemViewType = getItemViewType(position);

        switch (itemViewType) {
            case TYPE_CATEGORY_ITEM:
                if (null == convertView) {
                    convertView = mInflater.inflate(R.layout.item_probe_separator, null);
                }

                TextView textView = (TextView) convertView.findViewById(R.id.probe_separator);
                String  itemValue = (String) getItem(position);
                textView.setText( itemValue );
                break;

            case TYPE_ITEM:

                if (null == convertView) {
                    convertView = mInflater.inflate(R.layout.item_probe, null);

                    holder = new ViewHolder();
                    holder.probeName = (TextView) convertView.findViewById(R.id.probe_name);
                    holder.probeEnabled = (TextView) convertView.findViewById(R.id.probe_enabled);
                    holder.enabledProbeCheckbox = (CheckBox) convertView.findViewById(R.id.enabledProbeCheckbox);

                    // 为view设置标签
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                // 绑定数据
                holder.probeName.setText((String)getItem(position));

                holder.enabledProbeCheckbox.setChecked(getIsSelected().get(position));
                boolean isChecked = holder.enabledProbeCheckbox.isChecked();
                if(isChecked){
                    holder.probeEnabled.setText("Enabled");
                } else {
                    holder.probeEnabled.setText("Disabled");
                }


                break;
        }

//        if(convertView == null){
//            holder = new ViewHolder();
//            convertView = mInflater.inflate(R.layout.item_probe, null);
//            holder.probeName = (TextView) convertView.findViewById(R.id.probe_name);
//            holder.probeEnabled = (TextView) convertView.findViewById(R.id.probe_enabled);
//            holder.enabledProbeCheckbox = (CheckBox) convertView.findViewById(R.id.enabledProbeCheckbox);
//
//            // 为view设置标签
//            convertView.setTag(holder);
//        } else{
//            // 取出holder
//            holder = (ViewHolder) convertView.getTag();
//        }

//        // 设置list中TextView的显示
//        holder.probeName.setText(mItems.get(position));
//        holder.dataName.setText(mItems.get(position));
//
//        // 根据isSelected来设置checkbox的选中状况
//        holder.enabledProbeCheckbox.setChecked(getIsSelected().get(position));
//        boolean isChecked = holder.enabledProbeCheckbox.isChecked();
//        if(isChecked){
//            holder.probeEnabled.setText("Enabled");
//        } else {
//            holder.probeEnabled.setText("Disabled");
//        }

        return convertView;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItemViewType(position) != TYPE_CATEGORY_ITEM;
    }

    // Checkbox
    public static HashMap<Integer,Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer,Boolean> isSelected){
        ProbeAdapter.isSelected = isSelected;
    }

    public class ViewHolder {
        public TextView probeName;
        public TextView probeEnabled;
        public CheckBox enabledProbeCheckbox;
    }
}
