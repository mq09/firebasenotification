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

import gembalabaik2018.application.com.newsgembalabaik.ModelFirebase.AdminFirebase;
import gembalabaik2018.application.com.newsgembalabaik.ModelFirebase.UserFirebase;

public class UserEditActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private EditText piPhoneNo, piUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_edit_user);
        Intent intent = getIntent();
        final String idUser = intent.getStringExtra("idUser");
        final String userName = intent.getStringExtra("userName");
        final String phone = intent.getStringExtra("phone");

        if(FirebaseApp.getApps(getApplicationContext()).isEmpty()){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.sf_islogin), Context.MODE_PRIVATE);
        final String sfTypeUser = sharedPref.getString(getString(R.string.sf_islogin), "parent");
        final String sfUserName = sharedPref.getString("userName", "undefined");
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://gembalabaik-aac4a.firebaseio.com/");
        piPhoneNo = findViewById(R.id.piPhoneNo);
        piPhoneNo.setText(phone);
        piUsername = findViewById(R.id.piUsername);
        piUsername.setText(userName);

        TextView txtSaveParent = findViewById(R.id.txtSaveParent);
        txtSaveParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String createdAt = getDateTime();
                String phoneNumber = piPhoneNo.getText().toString();
                String userName = piUsername.getText().toString();
                String createdBy = sfUserName;
                if (phoneNumber.isEmpty() || userName.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Field tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    updateUser(idUser, phoneNumber,  userName, "", createdAt, createdBy);
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
                                String key = mDatabase.child("user").child(idUser).getKey();
                                mDatabase.child("user").child(key).removeValue();
                                Toast.makeText(getApplicationContext(), "Berhasil hapus user", Toast.LENGTH_SHORT).show();
                                finish();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
    }

    private void updateUser(String idUser ,String email, String parentName, String childName, String createdAt, String createdBy) {
        UserFirebase userFirebase = new UserFirebase("-All-", email, parentName, childName, "admin", createdAt, createdBy);
        AdminFirebase adminFirebase = new AdminFirebase(email, parentName);
        mDatabase.child("user").child(idUser).setValue(userFirebase);
        mDatabase.child("admin").child(idUser).setValue(adminFirebase);
        Toast.makeText(UserEditActivity.this, "Sukses update user",
                Toast.LENGTH_SHORT).show();
        finish();
    }

    private String getDateTime() {

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        String date = DateFormat.format("dd-MM-yyyy", calendar).toString();
        String time = DateFormat.format("HH:mm:ss", calendar).toString();
        return date +" "+ time;
    }
}
