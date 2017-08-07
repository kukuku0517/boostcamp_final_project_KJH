package com.example.android.contentproviderbroadcastreceiver.Main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.contentproviderbroadcastreceiver.Data.GroupData.SmsUnitData;
import com.example.android.contentproviderbroadcastreceiver.Data.MyRealmParcelableObject;
import com.example.android.contentproviderbroadcastreceiver.Data.PhotoData;
import com.example.android.contentproviderbroadcastreceiver.R;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by samsung on 2017-08-07.
 */

public class CommentDialogFragment extends DialogFragment {

    private Class<?> cls;
    private long id;
private  MyRealmParcelableObject item ;
    public CommentDialogFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
//            try {
//                cls = Class.forName(args.getString("class"));
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//            id = args.getLong("id", -1);
      item = args.getParcelable("item");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comment_fragment, container);
        Button button = (Button) view.findViewById(R.id.comment_btn);
        final EditText et = (EditText) view.findViewById(R.id.comment_et);

        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();
        p.width = ViewGroup.LayoutParams.MATCH_PARENT;
        p.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Log.d("asdf", String.valueOf(id));
                        item.setComment(String.valueOf(et.getText()));
                        dismiss();
                    }
                });
            }
        });

        getDialog().getWindow().setAttributes(p);
        Log.d("dialog", String.valueOf(cls));
        Log.d("dialog", String.valueOf(id));

        return view;
    }
}