package shushuxu.cityu.com.StudentAssessment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class StdInfoActivity extends AppCompatActivity {

    private TextView std_sleep, std_social, std_study, std_sports, std_GPA;

    private BarChart barChart;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_second);

        std_sleep = (TextView) findViewById(R.id.std_sleep);
        std_social = (TextView) findViewById(R.id.std_social);
        std_sports = (TextView) findViewById(R.id.std_sports);
        std_study = (TextView) findViewById(R.id.std_study);
        std_GPA = (TextView) findViewById(R.id.stdGPA);

        barChart = (BarChart) findViewById(R.id.bar_chart_staff);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("Student 4S");
        initToolbarMenu(toolbar);

        Intent intent = getIntent();
        std_sleep.setText("Sleep score is: " + String.valueOf(intent.getLongExtra("sleep_score", -1)));
        std_social.setText("Social score is: " + String.valueOf(intent.getLongExtra("social_score", -1)));
        std_sports.setText("Sports score is: " + String.valueOf(intent.getLongExtra("sports_score", -1)));
        std_study.setText("Study score is: " + String.valueOf(intent.getLongExtra("study_score", -1)));
        std_GPA.setText("GPA: " + String.valueOf(intent.getDoubleExtra("GPA", -1)));

        initBarChart(intent);
    }

    public void initBarChart(Intent intent)
    {
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

        entries.add(new BarEntry(0.5f, (float) intent.getLongExtra("sleep", -1) / 60, "Sleep"));
        entries.add(new BarEntry(1.5f, (float)intent.getLongExtra("social", -1) / 60, "Social"));
        entries.add(new BarEntry(2.5f, (float)intent.getLongExtra("sports", -1) / 60, "Sports"));
        entries.add(new BarEntry(3.5f, (float)intent.getLongExtra("study", -1) / 60, "Study"));

        BarDataSet barDataSet = new BarDataSet(entries, "Hours");

        barDataSet.setColors(new int[] {R.color.orange1, R.color.orange2, R.color.orange3, R.color.orange4}, StdInfoActivity.this);

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.8f);
        barChart.setData(barData);

        // make the x-axis fit exactly all bars
        //barChart.setFitBars(true);
        // refresh
        barChart.invalidate();

        barChart.setDrawValueAboveBar(true);

        barChart.setDescription("Average Hours");

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

//        xAxis.getLabelRotationAngle();
//        xAxis.setXOffset(0f);
//        xAxis.setYOffset(0f);
        // set a custom value formatter
        //xAxis.setValueFormatter(new MyXAxisValueFormatter(label));

        //YAxis yAxis = barChart.getRendererLeftYAxis();

    }

    protected void initToolbarMenu(Toolbar toolbar) {
        toolbar.inflateMenu(R.menu.hierarchy);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_hierarchy:
                        AlertDialog.Builder builder = new AlertDialog.Builder(StdInfoActivity.this);
                        builder.setMessage("Are you sure to exit?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        AVUser.logOut();
                                        Intent intent = new Intent(StdInfoActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
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
