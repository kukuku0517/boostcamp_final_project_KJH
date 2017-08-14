package com.example.android.selfns.Helper;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.android.selfns.GroupView.Data.SmsUnitData;
import com.example.android.selfns.ExtraView.Comment.CommentDialogFragment;
import com.example.android.selfns.Interface.MyRealmCommentableObject;
import com.example.android.selfns.Interface.MyRealmObject;

import io.realm.Realm;
import io.realm.RealmObject;



/**
 * Created by samsung on 2017-08-07.
 */

public class ItemInteractionUtil {
    private static ItemInteractionUtil instance;
    private static Context context;

    public static ItemInteractionUtil getInstance(Context c) {
        if (instance == null) {
            instance = new ItemInteractionUtil();
        }
        context = c;
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
    public void show(AppCompatActivity activity, long id, int type) {

        FragmentManager fm = activity.getSupportFragmentManager();
        Bundle args = new Bundle();
//        args.putString("class", c.getCanonicalName());
//        args.putLong("id", id);
        args.putLong("id", id);
        args.putInt("type", type);
        Log.d("dialog", SmsUnitData.class.getCanonicalName());
        CommentDialogFragment dialogFragment = new CommentDialogFragment();
        dialogFragment.setArguments(args);
        dialogFragment.show(fm, "fragment_dialog_test");
    }

    public void highlight(final MyRealmCommentableObject item) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Log.d("highlight", "asdf");
                item.setHighlight();
            }
        });
    }

    public void deleteItem(final RealmObject item) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                item.deleteFromRealm();
            }
        });
    }

    public void shareItem(final MyRealmObject item) {
        int type = item.getType();
        FirebaseHelper.getInstance(context).setPost(type,item);
    }


}
