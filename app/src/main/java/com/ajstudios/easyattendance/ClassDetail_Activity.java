package com.ajstudios.easyattendance;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ajstudios.easyattendance.Adapter.StudentsListAdapter;
import com.ajstudios.easyattendance.realm.Attendance_Reports;
import com.ajstudios.easyattendance.realm.Attendance_Students_List;
import com.ajstudios.easyattendance.realm.Students_List;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

public class ClassDetail_Activity extends AppCompatActivity {

    private ImageView themeImage;
    private TextView className, total_students, place_holder;
    private CardView addStudent, reports_open;
    private Button submit_btn;
    private EditText student_name, reg_no, mobile_no;
    private LinearLayout layout_attendance_taken;
    private RecyclerView mRecyclerview;


    String room_ID, subject_Name, class_Name;

    public static final String TAG = "ClassDetail_Activity";

    Realm realm;
    RealmAsyncTask transaction;
    RealmChangeListener realmChangeListener;

    private Handler handler = new Handler();
    StudentsListAdapter mAdapter;

    ProgressBar progressBar;
    Dialog lovelyCustomDialog;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_detail_);

        getWindow().setExitTransition(null);
        Realm.init(this);

        final String theme = getIntent().getStringExtra("theme");
        class_Name = getIntent().getStringExtra("className");
        subject_Name = getIntent().getStringExtra("subjectName");
        room_ID = getIntent().getStringExtra("classroom_ID");


        Toolbar toolbar = findViewById(R.id.toolbar_class_detail);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_disease_detail);
        collapsingToolbarLayout.setTitle(subject_Name);

        themeImage = findViewById(R.id.image_disease_detail);
        className = findViewById(R.id.classname_detail);
        total_students = findViewById(R.id.total_students_detail);
        layout_attendance_taken = findViewById(R.id.attendance_taken_layout);
        layout_attendance_taken.setVisibility(View.GONE);
        addStudent = findViewById(R.id.add_students);
        reports_open = findViewById(R.id.reports_open_btn);
        className.setText(class_Name);
        mRecyclerview = findViewById(R.id.recyclerView_detail);
        progressBar = findViewById(R.id.progressbar_detail);
        place_holder = findViewById(R.id.placeholder_detail);
        place_holder.setVisibility(View.GONE);
        submit_btn = findViewById(R.id.submit_attendance_btn);
        submit_btn.setVisibility(View.GONE);

        switch (theme) {
            case "0":
                themeImage.setImageResource(R.drawable.asset_bg_paleblue);
                break;
            case "1":
                themeImage.setImageResource(R.drawable.asset_bg_green);

                break;
            case "2":
                themeImage.setImageResource(R.drawable.asset_bg_yellow);

                break;
            case "3":
                themeImage.setImageResource(R.drawable.asset_bg_palegreen);

                break;
            case "4":
                themeImage.setImageResource(R.drawable.asset_bg_paleorange);

                break;
            case "5":
                themeImage.setImageResource(R.drawable.asset_bg_white);
                break;

        }

        //---------------------------------

        Runnable r = new Runnable() {
            @Override
            public void run() {
                RealmInit();
                progressBar.setVisibility(View.GONE);
            }
        };
        handler.postDelayed(r, 500);

        //----------------------------------------

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                long count = realm.where(Students_List.class)
                        .equalTo("class_id", room_ID)
                        .count();
                final String size, size2;
                final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ClassDetail_Activity.this);
                size = String.valueOf(preferences.getAll().size());
                size2 = String.valueOf(count);

                if (size.equals(size2)){
                    submitAttendance();
                }else {
                    Toast.makeText(ClassDetail_Activity.this, "Select all........", Toast.LENGTH_SHORT).show();
                }

            }
        });

        reports_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassDetail_Activity.this, Reports_Activity.class);
                intent.putExtra("class_name", class_Name);
                intent.putExtra("subject_name", subject_Name);
                intent.putExtra("room_ID", room_ID);
                startActivity(intent);
            }
        });



        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                    LayoutInflater inflater = LayoutInflater.from(ClassDetail_Activity.this);
                    final View view1 = inflater.inflate(R.layout.popup_add_student, null);
                    student_name = view1.findViewById(R.id.name_student_popup);
                    reg_no = view1.findViewById(R.id.regNo_student_popup);
                    mobile_no = view1.findViewById(R.id.mobileNo_student_popup);

                    lovelyCustomDialog = new LovelyCustomDialog(ClassDetail_Activity.this)
                            .setView(view1)
                            .setTopColorRes(R.color.theme_light)
                            .setTitle("Add Student")
                            .setIcon(R.drawable.ic_baseline_person_add_24)
                            .setCancelable(false)
                            .setListener(R.id.add_btn_popup, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    String name = student_name.getText().toString();
                                    String regNo = reg_no.getText().toString();
                                    String mobNo = mobile_no.getText().toString();

                                    if (isValid()){
                                    addStudentMethod(name, regNo, mobNo);
                                    }else{
                                        Toast.makeText(ClassDetail_Activity.this, "Please fill all the details..", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            })
                            .setListener(R.id.cancel_btn_popup, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    lovelyCustomDialog.dismiss();
                                }
                            })
                            .show();

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void RealmInit(){

        Realm.init(this);
        realm = Realm.getDefaultInstance();
        final String date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
        realmChangeListener = new RealmChangeListener() {
            @Override
            public void onChange(Object o) {
                long count = realm.where(Students_List.class)
                        .equalTo("class_id", room_ID)
                        .count();

                total_students.setText("Total Students : " + count);

                long reports_size = realm.where(Attendance_Reports.class)
                        .equalTo("date_and_classID", date+room_ID)
                        .count();
                if (!(reports_size==0)){
                    layout_attendance_taken.setVisibility(View.VISIBLE);
                    submit_btn.setVisibility(View.GONE);
                }else {
                    layout_attendance_taken.setVisibility(View.GONE);
                    submit_btn.setVisibility(View.VISIBLE);

                    if (!(count==0)){
                        submit_btn.setVisibility(View.VISIBLE);
                        place_holder.setVisibility(View.GONE);
                    }else if (count==0) {
                        submit_btn.setVisibility(View.GONE);
                        place_holder.setVisibility(View.VISIBLE);
                    }

                }

            }
        };
        realm.addChangeListener(realmChangeListener);
        RealmResults<Students_List> students ;
        students = realm.where(Students_List.class)
                .equalTo("class_id", room_ID)
                .sort("name_student", Sort.ASCENDING)
                .findAllAsync();


        long count = realm.where(Students_List.class)
                .equalTo("class_id", room_ID)
                .count();
        long reports_size = realm.where(Attendance_Reports.class)
                .equalTo("date_and_classID", date+room_ID)
                .count();


        if (!(reports_size==0)){
            layout_attendance_taken.setVisibility(View.VISIBLE);
            submit_btn.setVisibility(View.GONE);
        }else if (reports_size==0) {

            layout_attendance_taken.setVisibility(View.GONE);
            submit_btn.setVisibility(View.VISIBLE);

            if (!(count==0)){
                submit_btn.setVisibility(View.VISIBLE);
                place_holder.setVisibility(View.GONE);
            }else if (count==0){
                submit_btn.setVisibility(View.GONE);
                place_holder.setVisibility(View.VISIBLE);
            }
        }


        total_students.setText("Total Students : " + count);

        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        String extraClick = "";
        mAdapter = new StudentsListAdapter( students,ClassDetail_Activity.this, date+room_ID, extraClick);
        mRecyclerview.setAdapter(mAdapter);

    }

    public void submitAttendance(){

        final ProgressDialog progressDialog = new ProgressDialog(ClassDetail_Activity.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        final String date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
                final RealmResults<Attendance_Students_List> list_students ;

                list_students = realm.where(Attendance_Students_List.class)
                        .equalTo("date_and_classID", date+room_ID)
                        .sort("studentName", Sort.ASCENDING)
                        .findAllAsync();

                final RealmList<Attendance_Students_List> list = new RealmList<>();
                list.addAll(list_students);

                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                final String dateOnly = String.valueOf(calendar.get(Calendar.DATE));
                @SuppressLint("SimpleDateFormat")
                final String monthOnly = new SimpleDateFormat("MMM").format(calendar.getTime());

                try {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            Attendance_Reports attendance_reports = realm.createObject(Attendance_Reports.class);
                            attendance_reports.setClassId(room_ID);
                            attendance_reports.setAttendance_students_lists(list);
                            attendance_reports.setDate(date);
                            attendance_reports.setDateOnly(dateOnly);
                            attendance_reports.setMonthOnly(monthOnly);
                            attendance_reports.setDate_and_classID(date+room_ID);
                            attendance_reports.setClassname(class_Name);
                            attendance_reports.setSubjName(subject_Name);

                        }
                    });
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.commit();
                    Toast.makeText(ClassDetail_Activity.this, "Attendance Submitted", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();


                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(ClassDetail_Activity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                }


    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        super.onDestroy();
    }

    public void addStudentMethod(final String studentName, final String regNo, final String mobileNo) {

        final ProgressDialog progressDialog = new ProgressDialog(ClassDetail_Activity.this);
        progressDialog.setMessage("Creating class..");
        progressDialog.show();

        transaction = realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Students_List students_list = realm.createObject(Students_List.class);
                String id = studentName+regNo;
                students_list.setId(id);
                students_list.setName_student(studentName);
                students_list.setRegNo_student(regNo);
                students_list.setMobileNo_student(mobileNo);
                students_list.setClass_id(room_ID);

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                lovelyCustomDialog.dismiss();
                realm.refresh();
                realm.setAutoRefresh(true);
                Toast.makeText(ClassDetail_Activity.this, "Student Added", Toast.LENGTH_SHORT).show();

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                progressDialog.dismiss();
                lovelyCustomDialog.dismiss();
                Toast.makeText(ClassDetail_Activity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public boolean isValid(){

        if (student_name.getText().toString().isEmpty() || reg_no.getText().toString().isEmpty() || mobile_no.getText().toString().isEmpty()){
            return false;
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_class_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}