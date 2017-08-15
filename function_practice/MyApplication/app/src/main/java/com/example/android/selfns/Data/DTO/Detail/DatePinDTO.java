package com.example.android.selfns.Data.DTO.Detail;
import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.Helper.RealmClassHelper;

/**
 * Created by samsung on 2017-08-15.
 */

public class DatePinDTO implements BaseDTO {

    private long date;

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public int getType() {
        return RealmClassHelper.DATE_PIN_DATA;
    }

    @Override
    public long getDate() {
        return date;
    }

    @Override
    public long getId() {
        return 0;
    }

}
