package com.example.android.selfns.Data.DTO.interfaceDTO;

import java.util.ArrayList;

/**
 * Created by samsung on 2017-08-15.
 */

public interface ShareableDTO extends BaseDTO {


 long get_id() ;

void set_id(long _id);


int getShare();

void setShare(int share) ;

    String getFriends();

    void setFriends(String friends);

    String getFid();

    void setFid(String fid);

    long getTimestamp() ;

    void setTimestamp(long timestamp);
}
