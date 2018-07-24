package gembalabaik2018.application.com.newsgembalabaik;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Locale;

import gembalabaik2018.application.com.newsgembalabaik.ModelFirebase.ClassFirebase;
import gembalabaik2018.application.com.newsgembalabaik.ModelFirebase.NewsFirebase;
import gembalabaik2018.application.com.newsgembalabaik.ModelFirebase.NotifFirebase;

public class NewsAddActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private EditText piBodyNews, piTypeNews;
    private ImageView imgPreview;
    private StorageReference storageRef;
    private ProgressBar loadingBar;
    private boolean shouldAllowBack = true;
    private String classId = "";

    @Override
    public void onBackPressed() {
        if (shouldAllowBack) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_news);
        if (FirebaseApp.getApps(getApplicationContext()).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        Intent intent = getIntent();
        final Integer lastIdx = 1000 - intent.getIntExtra("lastIdx",1000);
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.sf_islogin), Context.MODE_PRIVATE);
        final String sfTypeUser = sharedPref.getString(getString(R.string.sf_islogin), "parent");
        final String sfUserName = sharedPref.getString("userName", "undefined");
        final String sfClassId = sharedPref.getString("classId", "undefined");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://gembalabaik-aac4a.firebaseio.com/");
        final Query qNotif = mDatabase.child("notifAdmin");
        Query query = mDatabase.child("class").orderByChild("className");
        FirebaseListOptions<ClassFirebase> options =
                new FirebaseListOptions.Builder<ClassFirebase>()
                        .setQuery(query, ClassFirebase.class)
                        .setLayout(android.R.layout.simple_list_item_2)
                        .setLifecycleOwner(this)
                        .build();

        final FirebaseListAdapter<ClassFirebase> firebaseListAdapter = new FirebaseListAdapter<ClassFirebase>(options) {
            @Override
            protected void populateView(View v, ClassFirebase model, int position) {
                TextView txtClassName = v.findViewById(android.R.id.text1);
                txtClassName.setText(model.getClassName());
                TextView txtClassId = v.findViewById(android.R.id.text2);
                txtClassId.setVisibility(View.GONE);
                txtClassId.setText(model.getClassId());
            }

        };

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final LayoutInflater inflater = this.getLayoutInflater();
        loadingBar = findViewById(R.id.progressBar);
        imgPreview = findViewById(R.id.imgPreview);
        piBodyNews = findViewById(R.id.piBodyNews);
        piTypeNews = findViewById(R.id.piTypeNews);

        piTypeNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = inflater.inflate(R.layout.dialog_select_class, null);
                dialogBuilder.setView(dialogView);
                final ListView lvAllClass = dialogView.findViewById(R.id.lvAllClass);
                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
                lvAllClass.setAdapter(firebaseListAdapter);
                lvAllClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String selected = ((TextView) view.findViewById(android.R.id.text1)).getText().toString();
                        classId = ((TextView) view.findViewById(android.R.id.text2)).getText().toString();
                        Log.d("TAGTAG", classId);
                        piTypeNews.setText(selected);
                        alertDialog.dismiss();
                    }
                });

            }
        });
        final ImageButton icAddNews = findViewById(R.id.icAddNews);
        icAddNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String createdAt = getDateTime();
                final String idNews = getUniqId();
                final String bodyNews = piBodyNews.getText().toString();
                final String typeNews = piTypeNews.getText().toString();
                final String createdBy = sfUserName;

                if (bodyNews.isEmpty() || typeNews.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Field tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.setVisibility(View.VISIBLE);
                    icAddNews.setEnabled(false);
                    shouldAllowBack = false;
                    final int[] lastIdxNotif = {0};
                    qNotif.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int idxNotif = 0;
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                idxNotif++;
                                Log.d("TAGTAG", "Data snapshot"+idxNotif);
                            }
                            lastIdxNotif[0] =idxNotif;
                            Log.d("TAGTAG", "Last idx notif : "+ String.valueOf(lastIdxNotif[0]));
                            Integer lastIdxNotifNew = 1000 - lastIdxNotif[0];
                            uploadImage(sfClassId, idNews, bodyNews, typeNews, createdAt, createdBy, lastIdx, lastIdxNotifNew);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

            }
        });

        Button btnPickImage = findViewById(R.id.btnPickImage);
        btnPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });

    }

    private void uploadImage(final String sfClassId, final String idNews, final String bodyNews, final String typeNews, final String createdAt, final String createdBy, final Integer lastIdx, final Integer lastIdxNotif) {
        try {
            Bitmap bitmap = ((BitmapDrawable) imgPreview.getDrawable()).getBitmap();

            int imageWidth = bitmap.getWidth();
            int imageHeight = bitmap.getHeight();
            int newHeight = (imageHeight * 480)/imageWidth;
            bitmap = Bitmap.createScaledBitmap(bitmap, 480, newHeight,true);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

            byte[] data = baos.toByteArray();
            Log.d("TAGTAG", String.valueOf(data.length));
            final String bfilename = "img" + idNews;
            UploadTask uploadTask = storageRef.child("news").child(bfilename).putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...

                    writeNews(sfClassId, classId,idNews, bodyNews, bfilename, typeNews, createdAt, createdBy, lastIdx, lastIdxNotif);
                }
            });
        } catch (Exception e){
            writeNews(sfClassId, classId,idNews, bodyNews, "", typeNews, createdAt, createdBy, lastIdx, lastIdxNotif);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    imgPreview.setImageURI(selectedImage);
                }

                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    imgPreview.setImageURI(selectedImage);
                }
                break;
        }
    }

    private void writeNews(final String sfClassId, String classId, final String idNews, final String bodyNews, String urlImage, final String typeNews, final String createdAt, final String createdBy, Integer lastIdxNews, final Integer lastIdxNotif) {
        String bfilename = "img" + idNews;
        final NewsFirebase newsFirebase = new NewsFirebase(classId ,bodyNews, typeNews, bfilename, createdAt, createdBy, lastIdxNews);


        String notifBody = bodyNews;
        final String idNotif = getUniqIdNotif();
        final NotifFirebase notifFirebase = new NotifFirebase(idNews, "membuat news "+notifBody, bodyNews, urlImage,createdAt, createdBy, lastIdxNotif);

        if (typeNews.equalsIgnoreCase("-All-")) {
            Query qClass = FirebaseDatabase.getInstance().getReferenceFromUrl("https://gembalabaik-aac4a.firebaseio.com/").child("class");
            qClass.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String classId = postSnapshot.child("classId").getValue().toString();
                        mDatabase.child("newsParent").child(classId).child(idNews).setValue(newsFirebase);
                        mDatabase.child("newsAdmin").child(idNews).setValue(newsFirebase);
                        mDatabase.child("notifParent").child(classId).child(idNotif).setValue(notifFirebase);
                        mDatabase.child("notifAdmin").child(idNotif).setValue(notifFirebase);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {
            mDatabase.child("newsParent").child(classId).child(idNews).setValue(newsFirebase);
            mDatabase.child("newsAdmin").child(idNews).setValue(newsFirebase);
            mDatabase.child("notifAdmin").child(idNotif).setValue(notifFirebase);
            mDatabase.child("notifParent").child(classId).child(idNotif).setValue(notifFirebase);
        }
        loadingBar.setVisibility(View.GONE);

        Toast.makeText(NewsAddActivity.this, "Sukses tambah news",
                Toast.LENGTH_SHORT).show();
        finish();
    }

    private String getUniqIdNotif(){
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        String date = DateFormat.format("ddMMyyyy", calendar).toString();
        String time = DateFormat.format("HHmmss", calendar).toString();
        return "notif_"+date + time;
    }

    private String getUniqId() {

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        String date = DateFormat.format("ddMMyyyy", calendar).toString();
        String time = DateFormat.format("HHmmss", calendar).toString();
        return "news_" + date + time;
    }

    private String getDateTime() {

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        String date = DateFormat.format("dd-MM-yyyy", calendar).toString();
        String time = DateFormat.format("HH:mm:ss", calendar).toString();
        return date + " " + time;
    }
}
