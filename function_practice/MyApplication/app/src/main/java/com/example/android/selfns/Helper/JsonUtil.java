package com.example.android.selfns.Helper;

import com.example.android.selfns.Data.DTO.interfaceDTO.ShareableDTO;
import com.example.android.selfns.Data.DTO.Retrofit.UserDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by samsung on 2017-08-18.
 */

public class JsonUtil {
    private static JsonUtil instance;

    public static JsonUtil getInstance() {
        if (instance == null) {
            instance = new JsonUtil();
        }
        return instance;
    }

    public ArrayList<UserDTO> friendJsonTOArray(ShareableDTO item) {
        ArrayList<UserDTO> users = new ArrayList<>();

        try {
            JSONArray friends = new JSONArray(item.getFriends());

            for (int j = 0; j < friends.length(); j++) {
                JSONObject friend = friends.getJSONObject(j);
                UserDTO user = new UserDTO();
                user.setUid(friend.get("id").toString());
                user.setPhotoUrl(friend.get("photoUrl").toString());
                user.setName(friend.get("name").toString());
                users.add(user);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return users;

    }
}