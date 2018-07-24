package gembalabaik2018.application.com.newsgembalabaik;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Calendar;
import java.util.Locale;

import gembalabaik2018.application.com.newsgembalabaik.ModelFirebase.ClassFirebase;
import gembalabaik2018.application.com.newsgembalabaik.ModelFirebase.UserFirebase;

public class ClassroomAddParentActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_parent_classroom);
        Intent intent = getIntent();
        final String idClass = intent.getStringExtra("idClass");
        final String classname = intent.getStringExtra("className");
        final String totalChild = intent.getStringExtra("totalChild");
        final String teacher = intent.getStringExtra("teacher");
        final String mentor1 = intent.getStringExtra("mentor1");
        final String mentor2 = intent.getStringExtra("mentor2");
        final String createdAt = intent.getStringExtra("createdAt");

        if(FirebaseApp.getApps(getApplicationContext()).isEmpty()){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.sf_islogin), Context.MODE_PRIVATE);
        final String sfTypeUser = sharedPref.getString(getString(R.string.sf_islogin), "parent");
        final String sfUserName = sharedPref.getString("userName", "undefined");
        final String sfIdUser = sharedPref.getString("idUser", "undefined");
        final TextView txtClassName = findViewById(R.id.txtClassName);
        txtClassName.setText(classname);
        final EditText piTeacher = findViewById(R.id.piTeacher);
        piTeacher.setText(teacher);
        final EditText piTotalChild = findViewById(R.id.piTotalChild);
        piTotalChild.setText(totalChild);
        final EditText piMentor1 = findViewById(R.id.piMentor1);
        piMentor1.setText(mentor1);
        final EditText piMentor2 = findViewById(R.id.piMentor2);
        piMentor2.setText(mentor2);

        ListView lvClassParent = findViewById(R.id.lvClassParent);
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://gembalabaik-aac4a.firebaseio.com/");
        final Query query = FirebaseDatabase.getInstance().getReference().child("classParent").child(idClass).child("parent");

        FirebaseListOptions<UserFirebase> options =
                new FirebaseListOptions.Builder<UserFirebase>()
                        .setQuery(query, UserFirebase.class)
                        .setLayout(R.layout.adapter_parent)
                        .setLifecycleOwner(this)
                        .build();

        final FirebaseListAdapter<UserFirebase> firebaseListAdapter = new FirebaseListAdapter<UserFirebase>(options){
            @Override
            protected void populateView(View v, UserFirebase model, int position) {
                TextView txtParentName = v.findViewById(R.id.txtParentName);
                txtParentName.setText(model.getUserName());
                TextView txtPhone = v.findViewById(R.id.txtPhone);
                txtPhone.setText("Phone : "+model.getPhoneNumber());
                TextView txtChildName = v.findViewById(R.id.txtChildName);
                txtChildName.setText("Child : "+model.getChildName());
            }
        };
        lvClassParent.setAdapter(firebaseListAdapter);
        lvClassParent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final String key = firebaseListAdapter.getRef(position).getKey();

                String parentName = firebaseListAdapter.getItem(position).getUserName();
                String phone = firebaseListAdapter.getItem(position).getPhoneNumber();
                String childName = firebaseListAdapter.getItem(position).getChildName();

                Intent intent = new Intent(ClassroomAddParentActivity.this, ParentEditActivity.class);
                intent.putExtra("idParent", key);
                intent.putExtra("idClass", idClass);
                intent.putExtra("parentName", parentName);
                intent.putExtra("phone", phone);
                intent.putExtra("childName", childName);
                startActivity(intent);
            }
        });

        TextView txtSaveClass = findViewById(R.id.txtSaveClass);
        txtSaveClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String classname = txtClassName.getText().toString();
                String totalChild = piTotalChild.getText().toString();
                String mentor1 = piMentor1.getText().toString();
                String mentor2 = piMentor2.getText().toString();
                String teacher = piTeacher.getText().toString();
                if (classname.isEmpty() || totalChild.isEmpty() || mentor1.isEmpty() || mentor2.isEmpty() || teacher.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Field tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    updateClass(idClass, classname,  totalChild, teacher, mentor1, mentor2, createdAt, "admin");
                }

            }
        });
        final AlertDialog.Builder dialogConfirmDelete = new AlertDialog.Builder(this);
        TextView txtDeleteClass = findViewById(R.id.txtDeleteClass);
        txtDeleteClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogConfirmDelete
                        .setTitle("Delete News")
                        .setMessage("Do you really want to delete class?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                String key = mDatabase.child("class").child(idClass).getKey();
                                mDatabase.child("class").child(key).removeValue();
                                Toast.makeText(getApplicationContext(), "Berhasil hapus class", Toast.LENGTH_SHORT).show();
                                finish();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final LayoutInflater inflater = this.getLayoutInflater();

        TextView txtAddParent = findViewById(R.id.txtAddParent);
        txtAddParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View dialogView = inflater.inflate(R.layout.dialog_addparent, null);
                dialogBuilder.setView(dialogView);
                final EditText piParentName = dialogView.findViewById(R.id.piParentName);
                final EditText piChildName = dialogView.findViewById(R.id.piChildName);
                final EditText piPhoneNumber = dialogView.findViewById(R.id.piPhoneNumber);
                Button btnSave = dialogView.findViewById(R.id.btnSave);
                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String parentName = piParentName.getText().toString();
                        String childName = piChildName.getText().toString();
                        String createdBy = sfUserName;
                        String createdAt = getDateTime();
                        String phoneNumber = piPhoneNumber.getText().toString();
                        String idUser = getUniqIdParent();
                        if (parentName.isEmpty() || childName.isEmpty() || phoneNumber.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Field tidak boleh kosong", Toast.LENGTH_SHORT).show();
                        } else {
                            writeNewParent(idClass, idUser, phoneNumber,  parentName, childName, createdAt, createdBy);
                            alertDialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    private void updateClass(String id, String className, String totalChild, String teacher, String mentor1, String mentor2, String createdAt, String createdBy) {
        ClassFirebase classFirebase = new ClassFirebase(id, className, totalChild, teacher, mentor1, mentor2,createdAt, createdBy);
        mDatabase.child("class").child(id).setValue(classFirebase);
        Toast.makeText(ClassroomAddParentActivity.this, "Sukses "+id,
                Toast.LENGTH_SHORT).show();
    }

    private void writeNewParent(String idClass, String idUser, String phoneNumber, String parentName, String childName, String createdAt, String createdBy) {
        UserFirebase userFirebase = new UserFirebase(idClass, phoneNumber, parentName, childName, "parent", createdAt, createdBy);

        mDatabase.child("user").child(idUser).setValue(userFirebase);
        mDatabase.child("classParent").child(idClass).child("parent").child(idUser).setValue(userFirebase);
        Toast.makeText(ClassroomAddParentActivity.this, "Sukses tambah parent",
                Toast.LENGTH_SHORT).show();
    }

    private String getUniqIdParent() {

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        String date = DateFormat.format("ddMMyyyy", calendar).toString();
        String time = DateFormat.format("HHmmss", calendar).toString();
        return "u_" + date + time;
    }

    private String getUniqId() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        String date = DateFormat.format("ddMMyyyy", calendar).toString();
        String time = DateFormat.format("HHmmss", calendar).toString();
        return "mp_"+date + time;
    }

    private String getDateTime() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        String date = DateFormat.format("dd-MM-yyyy", calendar).toString();
        String time = DateFormat.format("HH:mm:ss", calendar).toString();
        return date +" " + time;
    }
}
