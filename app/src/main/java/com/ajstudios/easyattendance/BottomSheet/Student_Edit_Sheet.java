package com.ajstudios.easyattendance.BottomSheet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.ajstudios.easyattendance.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class Student_Edit_Sheet extends BottomSheetDialogFragment {

    public String _name, _regNo, _mobNo;
    public EditText name_student, regNo_student, mobNo_student;
    public CardView call;

    public Student_Edit_Sheet(String stuName, String regNo, String mobileNo) {
        _name = stuName;
        _regNo = regNo;
        _mobNo = mobileNo;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.bottomsheet_student_edit, container, false);

        name_student = v.findViewById(R.id.stu_name_edit);
        regNo_student = v.findViewById(R.id.stu_regNo_edit);
        mobNo_student = v.findViewById(R.id.stu_mobNo_edit);
        call = v.findViewById(R.id.call_edit);

        name_student.setText(_name);
        regNo_student.setText(_regNo);
        mobNo_student.setText(_mobNo);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "tel:" + _mobNo.trim();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
        });

        return v;
    }
}
