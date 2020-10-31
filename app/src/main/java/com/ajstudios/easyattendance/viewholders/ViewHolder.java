package com.ajstudios.easyattendance.viewholders;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.ajstudios.easyattendance.ClassDetail_Activity;
import com.ajstudios.easyattendance.R;
import com.ajstudios.easyattendance.realm.Class_Names;

import io.realm.RealmResults;

import static androidx.core.app.ActivityOptionsCompat.makeSceneTransitionAnimation;

public class ViewHolder extends RecyclerView.ViewHolder {

    public final TextView class_name;
    public final TextView subject_name;
    public final TextView total_students;
    public ImageView imageView_bg;
    public RelativeLayout frameLayout;
    public CardView cardView;



    public Activity mActivity;
    RealmResults<Class_Names> mList;

    public ViewHolder(@NonNull final View itemView, Activity MainActivity, RealmResults<Class_Names> list) {
        super(itemView);

        class_name = itemView.findViewById(R.id.className_adapter);
        subject_name = itemView.findViewById(R.id.subjectName_adapter);
        imageView_bg = itemView.findViewById(R.id.imageClass_adapter);
        frameLayout = itemView.findViewById(R.id.frame_bg);
        cardView = itemView.findViewById(R.id.cardView_adapter);
        total_students = itemView.findViewById(R.id.totalStudents_adapter);

        mActivity = MainActivity;
        mList = list;


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ClassDetail_Activity.class);
                intent.putExtra("theme", mList.get(getAdapterPosition()).getPosition_bg());
                intent.putExtra("className", mList.get(getAdapterPosition()).getName_class());
                intent.putExtra("subjectName", mList.get(getAdapterPosition()).getName_subject());
                intent.putExtra("classroom_ID", mList.get(getAdapterPosition()).getId());
                Pair<View, String> p1 = Pair.create((View) cardView, "ExampleTransition");
                ActivityOptionsCompat optionsCompat = makeSceneTransitionAnimation(mActivity, p1);
                view.getContext().startActivity(intent, optionsCompat.toBundle());
            }
        });

    }

}
