package com.ajstudios.easyattendance.Adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.ajstudios.easyattendance.R;
import com.ajstudios.easyattendance.realm.Students_List;
import com.ajstudios.easyattendance.viewholders.ViewHolder_students;

import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class StudentsListAdapter extends RealmRecyclerViewAdapter<Students_List, ViewHolder_students> {

    private final Activity mActivity;
    RealmResults<Students_List> mList;
    String stuID, mroomID;
    Realm realm = Realm.getDefaultInstance();

    public StudentsListAdapter(RealmResults<Students_List> list, Activity context, String roomID, String extraClick) {

        super(context, list, true);

        mActivity = context;
        mList = list;
        mroomID =roomID;
    }

    @NonNull
    @Override
    public ViewHolder_students onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_attendance_adapter, parent, false);
        return new ViewHolder_students(itemView, mActivity, mList, mroomID);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_students holder, final int position) {
        Students_List temp = getItem(position);
        holder.student_name.setText(temp.getName_student());
        holder.student_regNo.setText(temp.getRegNo_student());


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        stuID = temp.getRegNo_student();
        String value = preferences.getString(stuID, null);
        if (value==null){

        }else {
            if (value.equals("Present")) {
                holder.radioButton_present.setChecked(true);
            } else {
                holder.radioButton_absent.setChecked(true);
            }
        }
    }

}
