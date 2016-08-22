package shushuxu.cityu.com.StudentAssessment.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shushu on 7/9/16.
 */
public class ProbeCategory {

    private String mCategoryName;
    private List<String> mCategoryItem = new ArrayList<String>();

    public ProbeCategory(String mCategroyName) {
        mCategoryName = mCategroyName;
    }

    public String getmCategoryName() {
        return mCategoryName;
    }

    public void addItem(String pItemName) {
        mCategoryItem.add(pItemName);
    }

    /**
     * @param pPosition
     * @return
     */
    public String getItem(int pPosition) {
        // Category arranged at 1st position;
        if (pPosition == 0) {
            return mCategoryName;
        } else {
            return mCategoryItem.get(pPosition - 1);
        }
    }

    /**
     * get all Item number, CategoryName also included
     * @return
     */
    public int getItemCount() {
        return mCategoryItem.size() + 1;
    }
}
