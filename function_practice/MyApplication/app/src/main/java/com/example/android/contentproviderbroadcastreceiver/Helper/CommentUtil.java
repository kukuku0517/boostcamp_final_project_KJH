package com.example.android.contentproviderbroadcastreceiver.Helper;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.android.contentproviderbroadcastreceiver.GroupView.Data.SmsUnitData;
import com.example.android.contentproviderbroadcastreceiver.ExtraView.Comment.CommentDialogFragment;
import com.example.android.contentproviderbroadcastreceiver.Interface.MyRealmObject;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmObject;

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
    public void show(AppCompatActivity activity, long id, int type){

        FragmentManager fm = activity.getSupportFragmentManager();
        Bundle args = new Bundle();
//        args.putString("class", c.getCanonicalName());
//        args.putLong("id", id);
        args.putLong("id",id);
        args.putInt("type",type);
        Log.d("dialog", SmsUnitData.class.getCanonicalName());
        CommentDialogFragment dialogFragment = new CommentDialogFragment();
        dialogFragment.setArguments(args);
        dialogFragment.show(fm, "fragment_dialog_test");
    }

    public void highlight(final MyRealmObject item){
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                item.setHighlight();
            }
        });
    }

    public void deleteItem(final RealmObject item){
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                item.deleteFromRealm();
            }
        });
    }


}
