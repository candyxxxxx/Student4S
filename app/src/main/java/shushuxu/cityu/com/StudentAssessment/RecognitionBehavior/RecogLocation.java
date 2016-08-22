package shushuxu.cityu.com.StudentAssessment.RecognitionBehavior;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.ArrayList;
import java.util.List;

import shushuxu.cityu.com.StudentAssessment.ubhave.datastore.db.UnencryptedDataTables;

/**
 * Created by shushu on 8/4/16.
 */
public class RecogLocation extends UnencryptedDataTables{

    private static UnencryptedDataTables recogLocationHelper;

    private String TAG = "RecogLocation";

    public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;

    Point2D vertex1 = new Point2D(114.184029, 22.341064);
    Point2D vertex2 = new Point2D(114.182564, 22.340378);
    Point2D vertex3 = new Point2D(114.184235, 22.337771);
    Point2D vertex4 = new Point2D(114.186616, 22.339049);
    Point2D vertex5 = new Point2D(114.18349, 22.34047);
    Point2D vertex6 = new Point2D(114.184109, 22.338732);
    Point2D vertex7 = new Point2D(114.185412, 22.337813);

//    Point2D vertex1 = new Point2D(114.029028, 22.537296);
//    Point2D vertex2 = new Point2D(114.027456, 22.537083);
//    Point2D vertex3 = new Point2D(114.027707, 22.536023);
//    Point2D vertex4 = new Point2D(114.029306, 22.536153);
    private List<Point2D> pts = null;

    public RecogLocation(Context context) {
        super(context);
        recogLocationHelper = new UnencryptedDataTables(context);

        mLocationClient = new LocationClient(context);
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        InitLocation();
        pts = new ArrayList<>();
        pts.add(vertex1);
        pts.add(vertex2);
        pts.add(vertex3);
        pts.add(vertex4);
        pts.add(vertex5);
        pts.add(vertex6);
        pts.add(vertex7);
        mLocationClient.start();
    }

    private void InitLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式

        option.setCoorType("bd09ll");// 百度手机地图对外接口中的坐标系默认是bd09ll，如果配合百度地图产品的话，需要注意坐标系对应问题

        option.setScanSpan(60000);// 设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true); // 返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true); // 返回的定位结果包含手机机头的方向
        option.setOpenGps(true); // 打开GPS
        // String 值为all时，表示返回地址信息，其他值都表示不返回地址信息(官方指南说有这个方法，但类中却没有，不知道为什么)
        // option.setAddrType("all");
        option.setProdName("com.example.textandroid"); // 设置产品线名称，百度建议
        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            Point2D loc = new Point2D(location.getLongitude(), location.getLatitude());
            boolean data = RecogLocation.IsPtInPoly(loc, pts);
            Log.d(TAG, location.getLongitude() + " " + location.getLatitude() + " " + data);
            RecogLocation.recordData(data);
        }
    }

    public static synchronized void recordData(boolean data)
    {
        SQLiteDatabase db = recogLocationHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("data", data);
        values.put("timeStampKey", System.currentTimeMillis());
        db.insert("recoglocation", null, values);
    }

    /**
     * 判断点是否在多边形内
     * @param point 检测点
     * @param pts   多边形的顶点
     * @return      点在多边形内返回true,否则返回false
     */
    public static boolean IsPtInPoly(Point2D point, List<Point2D> pts){

        int N = pts.size();
        boolean boundOrVertex = true; //如果点位于多边形的顶点或边上，也算做点在多边形内，直接返回true
        int intersectCount = 0;//cross points count of x
        double precision = 2e-10; //浮点类型计算时候与0比较时候的容差
        Point2D p1, p2;//neighbour bound vertices
        Point2D p = point; //当前点

        p1 = pts.get(0);//left vertex
        for(int i = 1; i <= N; ++i){//check all rays
            if(p.equals(p1)){
                return boundOrVertex;//p is an vertex
            }

            p2 = pts.get(i % N);//right vertex
            if(p.x < Math.min(p1.x, p2.x) || p.x > Math.max(p1.x, p2.x)){//ray is outside of our interests
                p1 = p2;
                continue;//next ray left point
            }

            if(p.x > Math.min(p1.x, p2.x) && p.x < Math.max(p1.x, p2.x)){//ray is crossing over by the algorithm (common part of)
                if(p.y <= Math.max(p1.y, p2.y)){//x is before of ray
                    if(p1.x == p2.x && p.y >= Math.min(p1.y, p2.y)){//overlies on a horizontal ray
                        return boundOrVertex;
                    }

                    if(p1.y == p2.y){//ray is vertical
                        if(p1.y == p.y){//overlies on a vertical ray
                            return boundOrVertex;
                        }else{//before ray
                            ++intersectCount;
                        }
                    }else{//cross point on the left side
                        double xinters = (p.x - p1.x) * (p2.y - p1.y) / (p2.x - p1.x) + p1.y;//cross point of y
                        if(Math.abs(p.y - xinters) < precision){//overlies on a ray
                            return boundOrVertex;
                        }

                        if(p.y < xinters){//before ray
                            ++intersectCount;
                        }
                    }
                }
            }else{//special case when ray is crossing through the vertex
                if(p.x == p2.x && p.y <= p2.y){//p crossing over p2
                    Point2D p3 = pts.get((i+1) % N); //next vertex
                    if(p.x >= Math.min(p1.x, p3.x) && p.x <= Math.max(p1.x, p3.x)){//p.x lies between p1.x & p3.x
                        ++intersectCount;
                    }else{
                        intersectCount += 2;
                    }
                }
            }
            p1 = p2;//next ray left point
        }

        if(intersectCount % 2 == 0){//偶数在多边形外
            return false;
        } else { //奇数在多边形内
            return true;
        }

    }

    class Point2D {
        private double x;
        private double y;

        public Point2D(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
