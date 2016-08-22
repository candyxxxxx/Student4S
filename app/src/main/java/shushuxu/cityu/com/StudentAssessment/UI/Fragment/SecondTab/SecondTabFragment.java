package shushuxu.cityu.com.StudentAssessment.UI.Fragment.SecondTab;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import shushuxu.cityu.com.StudentAssessment.Base.BaseLazyMainFragment;
import shushuxu.cityu.com.StudentAssessment.Entity.SensoredData;
import shushuxu.cityu.com.StudentAssessment.NetworkUtil;
import shushuxu.cityu.com.StudentAssessment.R;
import shushuxu.cityu.com.StudentAssessment.RecognitionBehavior.ProcessActivity;
import shushuxu.cityu.com.StudentAssessment.RecognitionBehavior.RecognitionVoice;


/**
 * Created by shushu on 7/8/16.
 */
public class SecondTabFragment extends BaseLazyMainFragment {

    private String TAG = "SecondTabFragment";

    private static final int CAL = 0;

    private ArrayList<SensoredData> actList;
    private ArrayList<SensoredData> voiceList;
    private ArrayList<Long> screenList;
    private ArrayList<Long> locationList;

    private PieChart pieChart;
    private BarChart barChart;
    private long[] data = new long[4];
//    private long[] data = {120, 35, 350, 27};
    private final static String[] behaviors = {"Study", "Sports", "Sleep", "Social"};

    private TextView study, sports, sleep, social;

    private RecognitionVoice recognitionVoice;

    private ProcessActivity processActivity;

    private ProgressDialog dialog;

    private long[] eva;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == CAL) {
                dialog.dismiss();
                //chart view
                initPieChart(data);
                initBarChart(data);

                study.setText("Study: " + eva[0]);
                sports.setText("Sports: " + eva[1]);
                sleep.setText("Sleep: " + eva[2]);
                social.setText("Social: " + eva[3]);
            }
        }
    };

    public static SecondTabFragment newInstance() {

        Bundle args = new Bundle();

        SecondTabFragment fragment = new SecondTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_second, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        Button show = (Button) view.findViewById(R.id.show_pie);

        pieChart = (PieChart) view.findViewById(R.id.pie_chart);
        barChart = (BarChart) view.findViewById(R.id.bar_chart);

        study = (TextView) view.findViewById(R.id.study);
        sports = (TextView) view.findViewById(R.id.sports);
        sleep = (TextView) view.findViewById(R.id.sleep);
        social = (TextView) view.findViewById(R.id.social);

        recognitionVoice = new RecognitionVoice(getContext());
        processActivity = new ProcessActivity(getContext());

    show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new ProgressDialog(getContext());
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setTitle("Calculating");
                dialog.setMessage("Please Wait");
                dialog.setIndeterminate(false);
                dialog.setCancelable(false);
                dialog.show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        calData();
                        Message msg = new Message();
                        msg.what = CAL;
                        handler.sendMessage(msg);
                    }
                }).start();
            }
        });
    }

    private void calData() {

        try {
            recognitionVoice.getAlldata();
            data = processActivity.recogFinal(processActivity.processAct(), processActivity.processVoice(),
                    processActivity.processScreen(),processActivity.processLocation());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        eva = evaluate(data);

        NetworkUtil networkUtil = new NetworkUtil();
        int userId = networkUtil.getUserId();

        AVObject avObject = new AVObject("Student");

        avObject.put("id_", userId);

        avObject.put("study", data[0]);
        avObject.put("sports", data[1]);
        avObject.put("sleep", data[2]);
        avObject.put("social", data[3]);

        avObject.put("study_score", eva[0]);
        avObject.put("sports_score", eva[1]);
        avObject.put("sleep_score", eva[2]);
        avObject.put("social_score", eva[3]);

        long time=System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Date d1=new Date(time);
        //String t1=format.format(d1);
        Log.e("msg", "d1");
        avObject.put("date", d1);

        avObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    // 存储成功
                    Log.d(TAG, "Success");
                } else {
                    // 失败的话，请检查网络环境以及 SDK 配置是否正确
                    Log.d(TAG,e + " ");
                }
            }
        });



//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                processActivity.removeData("recogactivity");
//                processActivity.removeData("recogvoice");
//                processActivity.removeData("recoglocation");
//                processActivity.removeData("microphone");
//                processActivity.removeData("screen");
//            }
//        }).start();
    }

    @Override
    protected void initLazyView(@Nullable Bundle savedInstanceState) {

    }

    public void initPieChart(long[] datas)
    {
        List<PieEntry> entries = new ArrayList<PieEntry>();

        entries.add(new PieEntry(data[0], "Study"));
        entries.add(new PieEntry(data[1], "Sport"));
        entries.add(new PieEntry(data[2], "Sleep"));
        entries.add(new PieEntry(data[3], "Social"));

        PieDataSet set = new PieDataSet(entries, "Behavior Result");
        set.setColors(new int[] { R.color.orange1, R.color.orange2, R.color.orange3, R.color.orange4}, getContext());
        set.setValueFormatter(new PercentFormatter());
        set.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        PieData data = new PieData(set);
        pieChart.setData(data);
        pieChart.invalidate();

        pieChart.setDescription(" ");
        pieChart.setNoDataText("Update Data");
        pieChart.setUsePercentValues(true);
        pieChart.setCenterText("Behavior");

        pieChart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);

        pieChart.animateXY(3000, 3000);
    }

    public void initBarChart(long[] datas)
    {
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

        for (int i = 0; i < behaviors.length; i++) {
            entries.add(new BarEntry(i, (float) (data[i] / 60.0), behaviors[i]));
        }

        BarDataSet barDataSet = new BarDataSet(entries, "Hours");
        barDataSet.setLabel("Study");
        ArrayList<String> labels = new ArrayList<>();
        labels.add("Study");
        labels.add("Sports");
        labels.add("Sleep");
        labels.add("Social");

        barDataSet.setColors(new int[] {R.color.orange1, R.color.orange2, R.color.orange3, R.color.orange4}, getContext());

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.8f);
        barChart.setData(barData);

        //barChart.setFitBars(true); // make the x-axis fit exactly all bars
        barChart.invalidate(); // refresh

        barChart.setDrawValueAboveBar(true);

        barChart.setDescription("");

        // scaling can now only be done on x- and y-axis separately
       // barChart.setPinchZoom(false);

        //barChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

        barChart.animateY(3000);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLabels(true);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.RED);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);

        xAxis.getLabelRotationAngle();
        xAxis.setXOffset(0f);
        xAxis.setYOffset(0f);
    // set a custom value formatter
        //xAxis.setValueFormatter(new MyXAxisValueFormatter(label));

        //YAxis yAxis = barChart.getRendererLeftYAxis();

    }

    public long[] evaluate(long[] data){
        long[] evaluate = new long[4];
        for(int i = 0; i < 4; i++)
        {
            switch (i)
            {
                case 0:
                    Double ave = data[i]/6.0;
                    if(ave <= 1)
                        evaluate[0] = ave.intValue() * 100;
                    else
                        evaluate[0] = 100;
                break;
                case 1:
                    if(data[i] >= 20 && data[i] <= 45)
                    {
                        evaluate[i] = (data[i] - 20) * 5;
                    } else if(data[i] < 20)
                    {
                        evaluate[i] = 0;
                    } else
                        evaluate[i] = 100;
                break;
                case 2:
                    Double sleep_hour = data[i] / 60.0;
                    if(sleep_hour != 0) {
                        Double exp = 1 / (Math.exp(((sleep_hour - 7.0) * (sleep_hour - 7.0)) / 32.0)) * 100;
                        Log.d(TAG, exp + "");
                        evaluate[i] = exp.intValue();
                    }
                    else {
                        evaluate[i] = 0;
                    }
                break;
                case 3:
                    double ratio = data[i]/ 1440.0;
                    if (data[i] == 0)
                        evaluate[i] = 0;
                    else if(ratio <= 0.35)
                    {
                        Double score = ratio * 100 / 0.35;
                        evaluate[i] = score.intValue();
                    } else
                        evaluate[i] = 100;
                break;
            }
        }
        return evaluate;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dialog.dismiss();
    }
}
