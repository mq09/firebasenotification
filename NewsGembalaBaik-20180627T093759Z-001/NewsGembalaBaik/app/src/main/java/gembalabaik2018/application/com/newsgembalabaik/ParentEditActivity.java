package gembalabaik2018.application.com.newsgembalabaik;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Locale;

import gembalabaik2018.application.com.newsgembalabaik.ModelFirebase.UserFirebase;

public class ParentEditActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private EditText piPhoneNo, piParentName, piChildName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_edit_parent);
        Intent intent = getIntent();
        final String idParent = intent.getStringExtra("idParent");
        final String idClass = intent.getStringExtra("idClass");
        final String parentName = intent.getStringExtra("parentName");
        final String phone = intent.getStringExtra("phone");
        final String childName = intent.getStringExtra("childName");
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.sf_islogin), Context.MODE_PRIVATE);
        final String sfTypeUser = sharedPref.getString(getString(R.string.sf_islogin), "parent");
        final String sfUserName = sharedPref.getString("userName", "undefined");
        final String sfIdUser = sharedPref.getString("idUser", "undefined");
        if(FirebaseApp.getApps(getApplicationContext()).isEmpty()){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://gembalabaik-aac4a.firebaseio.com/");
        piPhoneNo = findViewById(R.id.piPhoneNo);
        piPhoneNo.setText(phone);
        piParentName = findViewById(R.id.piParentName);
        piParentName.setText(parentName);
        piChildName = findViewById(R.id.piChildName);
        piChildName.setText(childName);
        TextView txtSaveParent = findViewById(R.id.txtSaveParent);
        txtSaveParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String createdAt = getDateTime();
                String phoneNumber = piPhoneNo.getText().toString();
                String parentName = piParentName.getText().toString();
                String childName = piChildName.getText().toString();
                String createdBy = sfUserName;
                if (phoneNumber.isEmpty() || parentName.isEmpty() || childName.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Field tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    writeNewParent(idClass, idParent, phoneNumber,  parentName, childName, createdAt, createdBy);
                    finish();
                }
            }
        });
        final AlertDialog.Builder dialogConfirmDelete = new AlertDialog.Builder(this);
        TextView txtDeleteParent = findViewById(R.id.txtDeleteParent);
        txtDeleteParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogConfirmDelete
                        .setTitle("Delete User")
                        .setMessage("Do you really want to delete user?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                String key = mDatabase.child("parent").child(idParent).getKey();
                                mDatabase.child("user").child(key).removeValue();
                                mDatabase.child("classParent").child(idClass).child("parent").child(idParent).removeValue();
                                Toast.makeText(getApplicationContext(), "Berhasil hapus user", Toast.LENGTH_SHORT).show();
                                finish();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
    }
    private void writeNewParent(String idClass, String idUser, String phoneNumber, String parentName, String childName, String createdAt, String createdBy) {
        UserFirebase userFirebase = new UserFirebase(idClass, phoneNumber, parentName, childName, "parent", createdAt, createdBy);

        mDatabase.child("user").child(idUser).setValue(userFirebase);
        mDatabase.child("classParent").child(idClass).child("parent").child(idUser).setValue(userFirebase);
        Toast.makeText(ParentEditActivity.this, "Sukses tambah parent",
                Toast.LENGTH_SHORT).show();
    }

    private String getDateTime() {

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        String date = DateFormat.format("dd-MM-yyyy", calendar).toString();
        String time = DateFormat.format("HH:mm:ss", calendar).toString();
        return date +" "+ time;
    }
}
