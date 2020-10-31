package com.ajstudios.easyattendance.viewholders;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ajstudios.easyattendance.BottomSheet.Student_Edit_Sheet;
import com.ajstudios.easyattendance.R;
import com.ajstudios.easyattendance.realm.Attendance_Reports;
import com.ajstudios.easyattendance.realm.Attendance_Students_List;
import com.ajstudios.easyattendance.realm.Students_List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

import static androidx.core.app.ActivityOptionsCompat.makeSceneTransitionAnimation;

public class ViewHolder_students extends RecyclerView.ViewHolder{

    public Activity mActivity;
    RealmResults<Students_List> mList;

    public final TextView student_name;
    public final TextView student_regNo;
    public LinearLayout layout;
    public String stuName, regNo, mobileNo, mRoomID;
    public RadioGroup radioGroup;
    public RadioButton radioButton_present, radioButton_absent;

    Realm realm;
    RealmChangeListener realmChangeListener;

    public ViewHolder_students(@NonNull final View itemView, Activity MainActivity, RealmResults<Students_List> list, final String roomID) {
        super(itemView);

        student_name = itemView.findViewById(R.id.student_name_adapter);
        student_regNo = itemView.findViewById(R.id.student_regNo_adapter);
        radioGroup = itemView.findViewById(R.id.radioGroup);
        radioButton_present = itemView.findViewById(R.id.radio_present);
        radioButton_absent = itemView.findViewById(R.id.radio_absent);
        layout = itemView.findViewById(R.id.layout_click);

        mActivity = MainActivity;
        mList = list;
        mRoomID = roomID;



        Realm.init(mActivity);
        realm = Realm.getDefaultInstance();
        realmChangeListener = new RealmChangeListener() {
            @Override
            public void onChange(Object o) {
                long reports_size = realm.where(Attendance_Reports.class)
                        .equalTo("date_and_classID", roomID)
                        .count();
                if (!(reports_size==0)){
                    radioGroup.setVisibility(View.GONE);
                }else if (reports_size==0) {
                    radioGroup.setVisibility(View.VISIBLE);
                }
            }
        };
        realm.addChangeListener(realmChangeListener);
        long reports_size = realm.where(Attendance_Reports.class)
                .equalTo("date_and_classID", roomID)
                .count();
        if (!(reports_size==0)){
            radioGroup.setVisibility(View.GONE);
        }else if (reports_size==0) {
            radioGroup.setVisibility(View.VISIBLE);
        }



        radioButton_present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String attendance = "Present";
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(mList.get(getAbsoluteAdapterPosition()).getRegNo_student(), attendance);
                editor.apply();



                final Attendance_Students_List attendance_students_list = new Attendance_Students_List();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        attendance_students_list.setStudentName((mList.get(getAbsoluteAdapterPosition()).getName_student()));
                        attendance_students_list.setAttendance(attendance);
                        attendance_students_list.setMobNo((mList.get(getAbsoluteAdapterPosition()).getMobileNo_student()));
                        attendance_students_list.setStudentRegNo(mList.get(getAbsoluteAdapterPosition()).getRegNo_student());
                        attendance_students_list.setClassID(mList.get(getAbsoluteAdapterPosition()).getClass_id());
                        attendance_students_list.setDate_and_classID(mRoomID);
                        attendance_students_list.setUnique_ID(mList.get(getAbsoluteAdapterPosition()).getRegNo_student()+mRoomID);

                        realm.copyToRealmOrUpdate(attendance_students_list);
                    }
                });




            }
        });
        radioButton_absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String attendance = "Absent";
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(mList.get(getAbsoluteAdapterPosition()).getRegNo_student(), attendance);
                editor.apply();

                final Attendance_Students_List attendance_students_list = new Attendance_Students_List();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        attendance_students_list.setStudentName((mList.get(getAbsoluteAdapterPosition()).getName_student()));
                        attendance_students_list.setAttendance(attendance);
                        attendance_students_list.setMobNo((mList.get(getAbsoluteAdapterPosition()).getMobileNo_student()));
                        attendance_students_list.setStudentRegNo(mList.get(getAbsoluteAdapterPosition()).getRegNo_student());
                        attendance_students_list.setClassID(mList.get(getAbsoluteAdapterPosition()).getClass_id());
                        attendance_students_list.setDate_and_classID(mRoomID);
                        attendance_students_list.setUnique_ID(mList.get(getAbsoluteAdapterPosition()).getRegNo_student()+mRoomID);

                        realm.copyToRealmOrUpdate(attendance_students_list);
                    }
                });

            }
        });


        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stuName = mList.get(getAbsoluteAdapterPosition()).getName_student();
                regNo = mList.get(getAbsoluteAdapterPosition()).getRegNo_student();
                mobileNo = mList.get(getAbsoluteAdapterPosition()).getMobileNo_student();
                Student_Edit_Sheet student_edit_sheet = new Student_Edit_Sheet(stuName, regNo, mobileNo);
                student_edit_sheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetTheme);
                student_edit_sheet.show(((FragmentActivity)view.getContext()).getSupportFragmentManager(), "BottomSheet");
            }
        });

    }


}
