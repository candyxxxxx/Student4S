package shushuxu.cityu.com.StudentAssessment;

import com.avos.avoscloud.AVUser;

/**
 * Created by shushu on 8/14/16.
 */
public class NetworkUtil {

    private AVUser user = AVUser.getCurrentUser();

    private int userId = user.getInt("id_");

    public int getUserId() {
        return userId;
    }

    public AVUser getUser() {
        return user;
    }

}
