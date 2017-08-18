package com.example.android.selfns.DailyView;

import android.content.Context;
import android.icu.text.DecimalFormat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.android.selfns.Data.RealmData.UnitData.CallData;
import com.example.android.selfns.Data.RealmData.UnitData.CustomData;
import com.example.android.selfns.Data.RealmData.UnitData.GpsData;
import com.example.android.selfns.Data.RealmData.UnitData.NotifyData;
import com.example.android.selfns.Data.RealmData.UnitData.SmsData;
import com.example.android.selfns.Data.RealmData.UnitData.SmsTradeData;
import com.example.android.selfns.Helper.DateHelper;
import com.example.android.selfns.Helper.RealmHelper;
import com.example.android.selfns.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.rackspira.kristiawan.rackmonthpicker.RackMonthPicker;
import com.rackspira.kristiawan.rackmonthpicker.listener.DateMonthDialogListener;
import com.rackspira.kristiawan.rackmonthpicker.listener.OnCancelMonthDialogListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmObject;
import io.realm.RealmResults;

import static android.R.attr.data;
import static android.R.attr.start;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.media.CamcorderProfile.get;

public class StatsActivity extends AppCompatActivity {

    @BindView(R.id.stat_month)
    Button monthBtn;
    Context context = this;

    @BindView(R.id.place_chart)
    BarChart placeChart;
    @BindView(R.id.people_chart)
    BarChart peopleChart;

    RealmResults<RealmObject> callResults;
    RealmResults<RealmObject> customResults;
    RealmResults<RealmObject> gpsResults;
    RealmResults<RealmObject> notifyResults;
    RealmResults<RealmObject> photoResults;
    RealmResults<RealmObject> smsResults;
    RealmResults<RealmObject> smsTradeResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        ButterKnife.bind(this);

        long startOfMonth, endOfMonth;
        Calendar thisMonth = DateHelper.getInstance().getMonth(System.currentTimeMillis());
        startOfMonth = thisMonth.getTimeInMillis();
        thisMonth.add(Calendar.MONTH, 1);
        endOfMonth = thisMonth.getTimeInMillis();

        loadMonthlyData(startOfMonth, endOfMonth);

        calGpsStatsData();
        calPeopleStatsData();

        monthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RackMonthPicker rackMonthPicker = new RackMonthPicker(context)
                        .setPositiveButton(new DateMonthDialogListener() {
                            @Override
                            public void onDateMonth(int month, int startDate, int endDate, int year, String monthLabel) {
                                monthBtn.setText(String.valueOf(month));
                                Calendar c = new GregorianCalendar();
                                c.set(Calendar.YEAR, year);
                                c.set(Calendar.MONTH, year);
                                c.set(Calendar.DATE, startDate);
                                long start = c.getTimeInMillis();
                                c.add(Calendar.MONTH, 1);
                                long end = c.getTimeInMillis();

                                calGpsStatsData();
                            }
                        })
                        .setNegativeButton(new OnCancelMonthDialogListener() {
                            @Override
                            public void onCancel(AlertDialog dialog) {

                            }
                        });
                rackMonthPicker.show();

            }
        });

    }

    private void loadMonthlyData(long startOfMonth, long endOfMonth) {
        callResults = RealmHelper.getInstance().DataLoad(CallData.class, "date", startOfMonth, endOfMonth);
        customResults = RealmHelper.getInstance().DataLoad(CustomData.class, "date", startOfMonth, endOfMonth);
        gpsResults = RealmHelper.getInstance().DataLoad(GpsData.class, "date", startOfMonth, endOfMonth);
        notifyResults = RealmHelper.getInstance().DataLoad(NotifyData.class, "date", startOfMonth, endOfMonth);
        smsResults = RealmHelper.getInstance().DataLoad(SmsData.class, "date", startOfMonth, endOfMonth);
        smsTradeResults = RealmHelper.getInstance().DataLoad(SmsTradeData.class, "date", startOfMonth, endOfMonth);
        gpsResults = RealmHelper.getInstance().DataLoad(GpsData.class, "date", startOfMonth, endOfMonth);
    }

    private void addToHash(HashMap<String, Integer> hashMap, String key, int point) {
        if (hashMap.containsKey(key)) {
            hashMap.put(key, hashMap.get(key) + point);
        } else {
            hashMap.put(key, point);
        }
    }

    private void calPhotoStatsData() {

    }

    private void calPeopleStatsData() {
        HashMap<String, Integer> peopleHash = new HashMap<>();

        for (RealmObject d : smsResults) {
            SmsData data = (SmsData) d;
            addToHash(peopleHash, data.getPerson(), 1);
        }
        for (RealmObject d : notifyResults) {
            NotifyData data = (NotifyData) d;
            addToHash(peopleHash, data.getPerson(), 1);
        }

        for (RealmObject d : callResults) {
            CallData data = (CallData) d;
            addToHash(peopleHash, data.getPerson(), (int) (data.getDuration() / 60) * 5);
        }

        List<String> sortedKeys = sortByValue(peopleHash);
        String[] sortedTopKeys = getTopKeys(sortedKeys, 5);

        List<BarEntry> entryList = new ArrayList<>();

        for (int i = 0; i < sortedTopKeys.length; i++) {
            entryList.add(new BarEntry(i, Float.parseFloat(String.valueOf(peopleHash.get(sortedKeys.get(i))))));
        }


        BarDataSet barDataSet = new BarDataSet(entryList, "횟수");
        BarData data = new BarData(barDataSet);

        XAxis xAxis = peopleChart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(sortedTopKeys));

        peopleChart.setData(data);


    }

    private String[] getTopKeys(List<String> sortedKeys, int s) {
        int size = sortedKeys.size() < s ? sortedKeys.size() : s;

        String[] values = new String[size];
        for (int i = 0; i < size; i++) {
            values[i] = sortedKeys.get(i);
        }
        return values;
    }

    private void calGpsStatsData() {
        HashMap<String, Integer> gpsHash = new HashMap<>();
        for (RealmObject d : gpsResults) {
            GpsData data = (GpsData) d;
            addToHash(gpsHash, data.getPlace(), 1);
        }


        List<String> sortedKeys = sortByValue(gpsHash);

        List<BarEntry> entryList = new ArrayList<>();


        int count = 0;
        while (count < 5) {
            count++;
            entryList.add(new BarEntry(count, Float.parseFloat(String.valueOf(gpsHash.get(sortedKeys.get(count))))));
        }

        BarDataSet barDataSet = new BarDataSet(entryList, "횟수");
//        List<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
//        dataSets.add(barDataSet);
        BarData data = new BarData(barDataSet);

        String[] values = new String[5];
        for (int i = 0; i < 5; i++) {
            values[i] = sortedKeys.get(i);
        }

        XAxis xAxis = placeChart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(values));

        placeChart.setData(data);
    }

    public static List sortByValue(final Map map) {
        List<String> list = new ArrayList();
        list.addAll(map.keySet());

        Collections.sort(list, new Comparator() {

            public int compare(Object o1, Object o2) {
                Object v1 = map.get(o1);
                Object v2 = map.get(o2);

                return ((Comparable) v2).compareTo(v1);
            }

        });
//        Collections.reverse(list); // 주석시 오름차순
        return list;
    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter {

        private String[] mValues;

        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            return mValues[(int) value];
        }


    }

}
