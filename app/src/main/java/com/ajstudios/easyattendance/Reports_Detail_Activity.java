package com.ajstudios.easyattendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ajstudios.easyattendance.Adapter.Reports_Detail_Adapter;
import com.ajstudios.easyattendance.realm.Attendance_Students_List;

import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class Reports_Detail_Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    Reports_Detail_Adapter mAdapter;

    TextView subj, className, toolbar_title;

    Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports__detail);
        Realm.init(this);
        realm = Realm.getDefaultInstance();

        String room_ID = getIntent().getStringExtra("ID");
        String classname = getIntent().getStringExtra("class");
        String subjName = getIntent().getStringExtra("subject");
        String date = getIntent().getStringExtra("date");

        Toolbar toolbar = findViewById(R.id.toolbar_reports_detail);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerView_reports_detail);
        subj = findViewById(R.id.subjName_report_detail);
        className = findViewById(R.id.classname_report_detail);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(date);
        subj.setText(subjName);
        className.setText(classname);



        RealmResults<Attendance_Students_List> list = realm.where(Attendance_Students_List.class)
                            .equalTo("date_and_classID", room_ID)
                            .sort("studentName", Sort.ASCENDING)
                            .findAllAsync();


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new Reports_Detail_Adapter( list,Reports_Detail_Activity.this, room_ID);
        recyclerView.setAdapter(mAdapter);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.only_dot, menu);
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