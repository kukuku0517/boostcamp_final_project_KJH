package com.example.android.selfns.DailyView.ViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.android.selfns.Data.DTO.Detail.DatePinDTO;
import com.example.android.selfns.Data.DTO.Retrofit.FriendDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.Helper.DateHelper;
import com.example.android.selfns.Interface.DatePinClickListener;
import com.example.android.selfns.Data.DTO.Retrofit.UserDTO;
import com.example.android.selfns.R;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by samsung on 2017-08-09.
 */

public class VHPin extends DayViewHolder {

    @BindView(R.id.pin_date)
    TextView date;
    @BindView(R.id.pin_year)
    TextView year;
    @BindView(R.id.pin_weekday)
    TextView weekday;

    @BindView(R.id.pin_view)
    View view;


    private Context context;
    public VHPin(View view, Context context) {
        super(view);
        this.context=context;
        setmListener(context, nListener);
    }

    @Override
    public void bindType(final BaseDTO item) {
        final DatePinDTO datePinData = (DatePinDTO) item;
        year.setText( DateHelper.getInstance().toDateString("yyyy MM월", datePinData.getDate()));
        date.setText( DateHelper.getInstance().toDateString("dd", datePinData.getDate()));
        weekday.setText(DateHelper.getInstance().toWeekdayString(datePinData.getDate()));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePinClickListener listener = (DatePinClickListener) mListener;
                listener.onDatePinClick(datePinData);
            }
        });
    }
    @Override
    public void bindTag(ArrayList<FriendDTO> users) {

    }}
