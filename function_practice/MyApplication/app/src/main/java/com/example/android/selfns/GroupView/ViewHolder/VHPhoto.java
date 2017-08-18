package com.example.android.selfns.GroupView.ViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.selfns.DailyView.ViewHolder.DayViewHolder;
import com.example.android.selfns.Data.DTO.Detail.PhotoDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.Helper.ItemInteractionUtil;
import com.example.android.selfns.Interface.PhotoItemClickListener;
import com.example.android.selfns.LoginView.UserDTO;
import com.example.android.selfns.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * Created by samsung on 2017-07-26.
 */

public class VHPhoto extends DayViewHolder {
    @BindView(R.id.photo_iv)
    ImageView iv;
    @BindView(R.id.photo_date)
    TextView date;
    @BindView(R.id.photo_location)
    TextView location;
    @BindView(R.id.photo_button)
    Button button;
    Context context;

    public VHPhoto(View view, Context context) {
        super(view);
        this.context = context;
    }

    @Override
    public void bindType(final BaseDTO item) {
        final PhotoDTO callData = (PhotoDTO) item;
        DateFormat sdFormat = new SimpleDateFormat("hh : mm");
        Date d = new Date(callData.getDate());
        String tempDate = sdFormat.format(d);
        date.setText(tempDate);
        location.setText(callData.getPlace());
        Glide.with(context).load(callData.getPath()).into(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               PhotoItemClickListener listener = (PhotoItemClickListener) context;
                listener.onPhotoItemClick(item);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ItemInteractionUtil.getInstance(context).deletePhotoItem(callData);
            }
        });
        //TODO sharebtn
//        shareBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ItemInteractionUtil.getInstance(context).shareItem(item);
//            }
//        });
    }   List<UserDTO> items = new ArrayList<>();
    @Override
    public void bindTag(ArrayList<UserDTO> users) {
        this.items = users;
    }
}
