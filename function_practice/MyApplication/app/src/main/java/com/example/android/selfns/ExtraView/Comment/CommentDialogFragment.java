package com.example.android.selfns.ExtraView.Comment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.selfns.Interface.MyRealmCommentableObject;
import com.example.android.selfns.Interface.MyRealmObject;
import com.example.android.selfns.Helper.RealmClassHelper;
import com.example.android.selfns.R;

import io.realm.Realm;

/**
 * Created by samsung on 2017-08-07.
 */

public class CommentDialogFragment extends DialogFragment {


    private MyRealmCommentableObject item;
    private Realm realm;

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
            realm = Realm.getDefaultInstance();
            Class c = RealmClassHelper.getInstance().getClass(args.getInt("type"));
            long id = args.getLong("id");
            item = (MyRealmCommentableObject) realm.where(c).equalTo("id", id).findFirst();


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

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        item.setComment(String.valueOf(et.getText()));
                        dismiss();
                    }
                });
            }
        });

        getDialog().getWindow().setAttributes(p);

        return view;
    }
}