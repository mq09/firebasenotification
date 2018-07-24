package gembalabaik2018.application.com.newsgembalabaik;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import gembalabaik2018.application.com.newsgembalabaik.ModelFirebase.ClassFirebase;

public class ClassRoomActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private ImageView icAdd, icSearchClassroom;
    private ListView lvClassroom;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_class);
        if(FirebaseApp.getApps(this.getApplicationContext()).isEmpty()){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        lvClassroom = findViewById(R.id.lvClassroom);
        icAdd = findViewById(R.id.icAdd);
        icAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClassRoomActivity.this, ClassroomAddActivity.class);
                startActivity(intent);
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final Query query = mDatabase.child("class");
        FirebaseListOptions<ClassFirebase> options =
                new FirebaseListOptions.Builder<ClassFirebase>()
                        .setQuery(query, ClassFirebase.class)
                        .setLayout(android.R.layout.simple_list_item_2)
                        .setLifecycleOwner(this)
                        .build();

        final FirebaseListAdapter<ClassFirebase> firebaseListAdapter = new FirebaseListAdapter<ClassFirebase>(options){
            @Override
            protected void populateView(View v, ClassFirebase model, int position) {
                TextView txtClassName = v.findViewById(android.R.id.text1);
                txtClassName.setText(model.getClassName());
                TextView txtTotalChild = v.findViewById(android.R.id.text2);
                txtTotalChild.setText("Total Anak : "+model.getTotalChild());
            }
        };

        lvClassroom.setAdapter(firebaseListAdapter);
        lvClassroom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String idClass = firebaseListAdapter.getRef(position).getKey();

                String className = firebaseListAdapter.getItem(position).getClassName();
                String totalChild = firebaseListAdapter.getItem(position).getTotalChild();
                String teacher = firebaseListAdapter.getItem(position).getTeacher();
                String mentor1 = firebaseListAdapter.getItem(position).getMentor1();
                String mentor2 = firebaseListAdapter.getItem(position).getMentor2();
                String createdAt = firebaseListAdapter.getItem(position).getCreatedAt();

                Intent intent = new Intent(ClassRoomActivity.this, ClassroomAddParentActivity.class);
                intent.putExtra("idClass", idClass);
                intent.putExtra("className", className);
                intent.putExtra("totalChild", totalChild);
                intent.putExtra("teacher", teacher);
                intent.putExtra("mentor1", mentor1);
                intent.putExtra("mentor2", mentor2);
                intent.putExtra("createdAt", createdAt);
                startActivity(intent);
            }
        });

    }
}
