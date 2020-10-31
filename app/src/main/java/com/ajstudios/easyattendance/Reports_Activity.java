package com.ajstudios.easyattendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ajstudios.easyattendance.Adapter.ReportsAdapter;
import com.ajstudios.easyattendance.realm.Attendance_Reports;

import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;

public class Reports_Activity extends AppCompatActivity {

    String subjectName, className, room_ID;
    RecyclerView recyclerView;
    Realm realm;

    ReportsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        Realm.init(this);
        subjectName = getIntent().getStringExtra("subject_name");
        className = getIntent().getStringExtra("class_name");
        room_ID = getIntent().getStringExtra("room_ID");

        recyclerView = findViewById(R.id.recyclerView_reports);

        Toolbar toolbar = findViewById(R.id.toolbar_reports);
        setSupportActionBar(toolbar);
        toolbar.setTitle(subjectName);
        toolbar.setSubtitle(className);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        RealmResults<Attendance_Reports> results;
        realm = Realm.getDefaultInstance();
        results = realm.where(Attendance_Reports.class)
                .equalTo("classId", room_ID)
                .findAll();


        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);

        recyclerView.setLayoutManager(gridLayoutManager);

        mAdapter = new ReportsAdapter( results,Reports_Activity.this, room_ID);
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