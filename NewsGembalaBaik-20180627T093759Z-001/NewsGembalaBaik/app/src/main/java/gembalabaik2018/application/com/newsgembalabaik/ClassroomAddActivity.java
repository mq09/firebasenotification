package gembalabaik2018.application.com.newsgembalabaik;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Locale;

import gembalabaik2018.application.com.newsgembalabaik.ModelFirebase.ClassFirebase;
import gembalabaik2018.application.com.newsgembalabaik.ModelFirebase.ClassParentFirebase;

public class ClassroomAddActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private Button btnSave, btnCancel;
    private EditText piClassName, piTotalChild, piTeacher, piMentor1, piMentor2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_classroom);
        if(FirebaseApp.getApps(getApplicationContext()).isEmpty()){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.sf_islogin), Context.MODE_PRIVATE);
        final String sfTypeUser = sharedPref.getString(getString(R.string.sf_islogin), "parent");
        final String sfUserName = sharedPref.getString("userName", "undefined");

        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://gembalabaik-aac4a.firebaseio.com/");
        piClassName = findViewById(R.id.piClassName);
        piTotalChild = findViewById(R.id.piTotalChild);
        piTeacher = findViewById(R.id.piTeacher);
        piMentor1 = findViewById(R.id.piMentor1);
        piMentor2 = findViewById(R.id.piMentor2);

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String createdAt = getDateTime();
                String className = piClassName.getText().toString();
                String totalChild = piTotalChild.getText().toString();
                String teacher = piTeacher.getText().toString();
                String mentor1 = piMentor1.getText().toString();
                String mentor2 = piMentor2.getText().toString();
                String createdBy = sfUserName;
                if (className.isEmpty() || totalChild.isEmpty() || teacher.isEmpty() || mentor1.isEmpty() || mentor2.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Field tidak boleh kosong ", Toast.LENGTH_SHORT).show();
                } else {
                    String idClass = getUniqId();
                    writeNewClass(idClass, className,  totalChild, teacher, mentor1, mentor2,createdAt, createdBy);
                }

            }
        });

        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void writeNewClass(String idClass, String className, String totalChild, String teacher, String mentor1, String mentor2, String createdAt, String createdBy) {
        ClassParentFirebase classParentFirebase = new ClassParentFirebase(idClass, createdAt, createdBy);
        ClassFirebase classFirebase = new ClassFirebase(idClass,className, totalChild, teacher, mentor1, mentor2,createdAt, createdBy);
        mDatabase.child("class").child(idClass).setValue(classFirebase);
        mDatabase.child("classParent").child(idClass).setValue(classParentFirebase);
        Toast.makeText(ClassroomAddActivity.this, "Sukses tambah class",
                Toast.LENGTH_SHORT).show();
        finish();
    }

    private String getUniqId() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        String date = DateFormat.format("ddMMyyyy", calendar).toString();
        String time = DateFormat.format("HHmmss", calendar).toString();
        return "cls_"+ date + time;
    }

    private String getDateTime() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        String date = DateFormat.format("dd-MM-yyyy", calendar).toString();
        String time = DateFormat.format("HH:mm:ss", calendar).toString();
        return date +" "+ time;
    }
}
