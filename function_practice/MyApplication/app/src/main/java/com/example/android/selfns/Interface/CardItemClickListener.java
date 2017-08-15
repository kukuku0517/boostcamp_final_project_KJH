package com.example.android.selfns.Interface;


import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;

/**
 * Created by samsung on 2017-08-01.
 */

public interface CardItemClickListener {
    void onNotifyItemClick(BaseDTO item);

    void onSmsGroupItemClick(BaseDTO item);

    void onSmsTradeItemClick(BaseDTO item);

    void onPhotoGroupItemClick(BaseDTO item);

    void onGpsItemClick(BaseDTO item);

    void onCallItemClick(BaseDTO item);

    void onGpsGroupItemClick(BaseDTO item);

    void onCustomItemClick(BaseDTO item);



}
