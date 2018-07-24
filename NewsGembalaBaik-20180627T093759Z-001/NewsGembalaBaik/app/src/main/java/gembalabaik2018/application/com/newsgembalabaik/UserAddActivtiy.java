package gembalabaik2018.application.com.newsgembalabaik;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Locale;

import gembalabaik2018.application.com.newsgembalabaik.ModelFirebase.AdminFirebase;
import gembalabaik2018.application.com.newsgembalabaik.ModelFirebase.UserFirebase;

public class UserAddActivtiy extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private Button btnSave, btnCancel;
    private EditText piPhoneNo, piUsername;
    private ImageView icAddParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_user);
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
        piUsername = findViewById(R.id.piUsername);
        icAddParent = findViewById(R.id.icAddParent);
        icAddParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String createdAt = getDateTime();
                String phoneNumber = piPhoneNo.getText().toString();
                String userName = piUsername.getText().toString();
                String childName = "";
                String createdBy = sfUserName;
                if (phoneNumber.isEmpty() || userName.isEmpty() ) {
                    Toast.makeText(getApplicationContext(), "Field tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    writeNewUser(phoneNumber,  userName, childName, createdAt, createdBy);
                }
            }
        });
    }


    private void writeNewUser(String phoneNumber, String parentName, String childName, String createdAt, String createdBy) {
        UserFirebase userFirebase = new UserFirebase("-All-",phoneNumber, parentName, childName, "admin", createdAt, createdBy);
        AdminFirebase adminFirebase = new AdminFirebase(phoneNumber, parentName);
        String id = getUniqId();
        mDatabase.child("user").child(id).setValue(userFirebase);
        mDatabase.child("admin").child(id).setValue(adminFirebase);
        Toast.makeText(UserAddActivtiy.this, "Sukses tambah user",
                Toast.LENGTH_SHORT).show();
        finish();
    }

    private String getUniqId() {

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        String date = DateFormat.format("ddMMyyyy", calendar).toString();
        String time = DateFormat.format("HHmmss", calendar).toString();
        return "u_" + date + time;
    }

    private String getDateTime() {

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        String date = DateFormat.format("dd-MM-yyyy", calendar).toString();
        String time = DateFormat.format("HH:mm:ss", calendar).toString();
        return date +" "+ time;
    }
}
