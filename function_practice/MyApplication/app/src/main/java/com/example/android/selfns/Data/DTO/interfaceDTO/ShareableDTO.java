package com.example.android.selfns.Data.DTO.interfaceDTO;

import java.util.ArrayList;

/**
 * Created by samsung on 2017-08-15.
 */

public interface ShareableDTO extends BaseDTO {

    boolean isShare();

    void setShare(boolean share);

    ArrayList<String> friend = new ArrayList<>();

    ArrayList<String> getFriend();

    void setFriend(ArrayList<String> friend);
}
