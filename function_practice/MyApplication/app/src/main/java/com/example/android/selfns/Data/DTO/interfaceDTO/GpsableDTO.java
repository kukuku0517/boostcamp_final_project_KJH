package com.example.android.selfns.Data.DTO.interfaceDTO;
/**
 * Created by samsung on 2017-08-15.
 */

public interface GpsableDTO extends BaseDTO {
    double getLat();

    double getLng();

    String getPlace();


    String getOriginId();

    void setOriginId(String originId);

}
