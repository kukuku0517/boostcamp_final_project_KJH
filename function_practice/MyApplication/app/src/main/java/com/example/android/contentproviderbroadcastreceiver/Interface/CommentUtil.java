package com.example.android.contentproviderbroadcastreceiver.Interface;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.android.contentproviderbroadcastreceiver.Data.GroupData.SmsUnitData;
import com.example.android.contentproviderbroadcastreceiver.Data.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.Main.CommentDialogFragment;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmObject;

import static android.R.attr.id;

/**
 * Created by samsung on 2017-08-07.
 */

public class CommentUtil {
    private static CommentUtil instance;

    public static CommentUtil getInstance(){
       if(instance==null){
           instance=new CommentUtil();
       }
       return instance;
    }

//    public void show(AppCompatActivity activity, long id, Class c){
//        FragmentManager fm = activity.getSupportFragmentManager();
//        Bundle args = new Bundle();
//        args.putString("class", c.getCanonicalName());
//        args.putLong("id", id);
//        args.putParcelable();
//        Log.d("dialog", SmsUnitData.class.getCanonicalName());
//        CommentDialogFragment dialogFragment = new CommentDialogFragment();
//        dialogFragment.setArguments(args);
//        dialogFragment.show(fm, "fragment_dialog_test");
//    }
    public void show(AppCompatActivity activity, Parcelable p){
        FragmentManager fm = activity.getSupportFragmentManager();
        Bundle args = new Bundle();
//        args.putString("class", c.getCanonicalName());
//        args.putLong("id", id);
        args.putParcelable("item",p);
        Log.d("dialog", SmsUnitData.class.getCanonicalName());
        CommentDialogFragment dialogFragment = new CommentDialogFragment();
        dialogFragment.setArguments(args);
        dialogFragment.show(fm, "fragment_dialog_test");
    }



}
