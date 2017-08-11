package com.example.android.contentproviderbroadcastreceiver.DailyView.ViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.contentproviderbroadcastreceiver.DetailView.Data.DatePinData;
import com.example.android.contentproviderbroadcastreceiver.GroupView.Data.SmsGroupData;
import com.example.android.contentproviderbroadcastreceiver.Helper.DateHelper;
import com.example.android.contentproviderbroadcastreceiver.Interface.DatePinClickListener;
import com.example.android.contentproviderbroadcastreceiver.Interface.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;

/**
 * Created by samsung on 2017-08-09.
 */

public class VHPin extends DayViewHolder {

    @BindView(R.id.pin_date)
    TextView date;
    @BindView(R.id.pin_year)
    TextView year;

    @BindView(R.id.pin_view)
    View view;


    public VHPin(View view, Context context) {
        super(view);
        setmListener(context, nListener);
    }

    @Override
    public void bindType(final MyRealmObject item) {
        final DatePinData datePinData = (DatePinData) item;
        year.setText( DateHelper.getInstance().toDateString("yyyy", datePinData.getDate()));
        date.setText( DateHelper.getInstance().toDateString("MM월 dd일", datePinData.getDate()));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePinClickListener listener = (DatePinClickListener) mListener;
                listener.onDatePinClick(datePinData);
            }
        });
    }
}
