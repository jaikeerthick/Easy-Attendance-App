package com.ajstudios.easyattendance.viewholders;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajstudios.easyattendance.R;
import com.ajstudios.easyattendance.Reports_Detail_Activity;
import com.ajstudios.easyattendance.realm.Attendance_Reports;

import io.realm.RealmResults;

import static androidx.core.app.ActivityOptionsCompat.makeSceneTransitionAnimation;

public class ViewHolder_reports extends RecyclerView.ViewHolder {


    public TextView month;
    public TextView date;

    public Activity mActivity;
    RealmResults<Attendance_Reports> mList;

    public ViewHolder_reports(@NonNull final View itemView, Activity MainActivity, final RealmResults<Attendance_Reports> list) {
        super(itemView);

        month = itemView.findViewById(R.id.month_report_adapter);
        date = itemView.findViewById(R.id.date_report_adapter);

        mActivity = MainActivity;
        mList = list;


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Reports_Detail_Activity.class);
                intent.putExtra("ID", mList.get(getAbsoluteAdapterPosition()).getDate_and_classID());
                intent.putExtra("date", mList.get(getAbsoluteAdapterPosition()).getDate());
                intent.putExtra("subject", mList.get(getAbsoluteAdapterPosition()).getSubjName());
                intent.putExtra("class", mList.get(getAbsoluteAdapterPosition()).getClassname());
                view.getContext().startActivity(intent);
            }
        });

    }

}
