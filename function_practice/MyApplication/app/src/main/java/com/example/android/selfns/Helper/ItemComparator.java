package com.example.android.selfns.Helper;

import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;

import java.util.Comparator;

/**
 * Created by samsung on 2017-08-15.
 */

public class ItemComparator implements Comparator<BaseDTO> {

    @Override
    public int compare(BaseDTO o1, BaseDTO o2) {
        return (int) (o1.getDate()-o2.getDate());
    }
}
