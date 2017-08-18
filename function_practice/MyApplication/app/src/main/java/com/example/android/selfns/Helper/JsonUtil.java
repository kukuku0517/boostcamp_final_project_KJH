package com.example.android.selfns.Helper;

import com.example.android.selfns.LoginView.UserDTO;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by samsung on 2017-08-18.
 */

public class JsonUtil {
    private static JsonUtil instance;

    public static JsonUtil getInstance(){
        if(instance!=null){
            instance=new JsonUtil();
        }
        return instance;
    }

//    public
//
//
//    JSONArray friends = new JSONArray(sItem.getFriends());
//    final int fsize = friends.length();
//    final int[] fcount = {0};
//
//                                                for (int j = 0; j < fsize; j++) {
//        final ArrayList<UserDTO> users = new ArrayList<>();
//        JSONObject friend = friends.getJSONObject(j);
//        String uid = friend.get("id").toString();
//        DatabaseReference fRef = FirebaseHelper.getInstance(context).getUserRef(uid);
//
//

    }
