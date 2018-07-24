package gembalabaik2018.application.com.newsgembalabaik;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private Button btnLogin;
    private EditText piPhoneNumber, piVerifyCode;
    private FirebaseAuth mAuth;
    private String TAG = "Login-Firebase";
    private String mVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private DatabaseReference mDatabase;
    private boolean isRegistered = false;
    private String typeUser = "";
    private String userName = "";
    private String idUser = "";
    private String classId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.sf_islogin), Context.MODE_PRIVATE);
        String sfTypeUser = sharedPref.getString(getString(R.string.sf_islogin), "parent");
        piPhoneNumber = findViewById(R.id.email);
        initFireBaseCallbacks();
        piVerifyCode = findViewById(R.id.piVerifyCode);
        piPhoneNumber = findViewById(R.id.piPhoneNumber);
        btnLogin = findViewById(R.id.btnLogin);

        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://gembalabaik-aac4a.firebaseio.com/");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phoneNumber = "+62" + piPhoneNumber.getText().toString();
                Query query = FirebaseDatabase.getInstance().getReference().child("user");

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Log.e(TAG, "=======" + postSnapshot.child("phoneNumber").getValue() + " ------ " + piPhoneNumber.getText().toString());
                            String phoneDb = postSnapshot.child("phoneNumber").getValue().toString();
                            if (phoneDb.equalsIgnoreCase(piPhoneNumber.getText().toString())) {
                                isRegistered = true;
                                idUser = postSnapshot.getRef().getKey();
                                typeUser = postSnapshot.child("typeUser").getValue().toString();
                                userName = postSnapshot.child("userName").getValue().toString();
                                classId = postSnapshot.child("classId").getValue().toString();

                            }
                        }
                        loginFirebase(phoneNumber, isRegistered);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(MainActivity.this, "No hp anda belum terdaftar", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    private void confirmVerifyCode() {
        piPhoneNumber.setText("");
        setContentView(R.layout.activity_confirmation_code);
        piVerifyCode = findViewById(R.id.piVerifyCode);
        TextView txtChangeNumber = findViewById(R.id.txtChangeNumber);
        txtChangeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                piVerifyCode.setText("");
                setContentView(R.layout.activity_main);
            }
        });

        Button btnVerify = findViewById(R.id.btnVerify);
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verifyCode = piVerifyCode.getText().toString();
                final PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verifyCode);
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Verification Success", Toast.LENGTH_SHORT).show();
                                    signInWithPhoneCredential(credential);
                                } else {
                                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                        Toast.makeText(MainActivity.this, "Verification Failed, Invalid credentials", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });
    }

    ;

    private void initFireBaseCallbacks() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Toast.makeText(MainActivity.this, "Verification Complete", Toast.LENGTH_SHORT).show();
                signInWithPhoneCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(MainActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                Toast.makeText(MainActivity.this, "Code Sent", Toast.LENGTH_SHORT).show();
                mVerificationId = verificationId;
                confirmVerifyCode();
            }
        };
    }

    /*
     * Check if user already login
     */
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private void signInWithPhoneCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success");
                    FirebaseUser user = task.getResult().getUser();
                    Context context = getApplicationContext();
                    SharedPreferences sharedPref = context.getSharedPreferences(
                            getString(R.string.sf_islogin), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(getString(R.string.sf_islogin), typeUser);
                    editor.putString("userName", userName);
                    editor.putString("idUser", idUser);
                    editor.putString("classId", classId);
                    editor.commit();

                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();


                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                }
            }
        });
    }

    private void loginFirebase(String phoneNumber, boolean isRegistered) {

        if (isRegistered) {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNumber,        // Phone number to verify
                    10,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mCallbacks);        // OnVerificationStateChangedCallbacks

        } else {
            Toast.makeText(MainActivity.this, "No hp anda belum terdaftar", Toast.LENGTH_SHORT).show();
        }

    }

}
