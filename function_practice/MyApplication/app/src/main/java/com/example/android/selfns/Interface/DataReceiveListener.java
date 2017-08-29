package com.example.android.selfns.Interface;

import com.example.android.selfns.Data.DTO.Retrofit.FriendDTO;
import com.example.android.selfns.Data.DTO.Retrofit.UserDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by samsung on 2017-08-22.
 */

public interface DataReceiveListener<T> {
    void onReceive(T response);

}
