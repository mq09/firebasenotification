package gembalabaik2018.application.com.newsgembalabaik;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.Locale;

import gembalabaik2018.application.com.newsgembalabaik.ModelFirebase.ClassFirebase;
import gembalabaik2018.application.com.newsgembalabaik.ModelFirebase.NewsFirebase;

public class NewsEditActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private EditText piBodyNews, piTypeNews;
    private ImageView imgPreview;
    private StorageReference storageRef;
    private ProgressBar loadingBar;
    private boolean shouldAllowBack = true;


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
        setContentView(R.layout.activity_edit_news);
        if (FirebaseApp.getApps(getApplicationContext()).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        Intent intent = getIntent();
        final String idNews = intent.getStringExtra("idNews");
        final String bodyNews = intent.getStringExtra("bodyNews");
        final String createdAt = intent.getStringExtra("createdAt");
        final String urlImage = intent.getStringExtra("urlImage");
        final String typeNews = intent.getStringExtra("typeNews");
        final String classId = intent.getStringExtra("classId");
        final Integer lastIdx = 1000 - intent.getIntExtra("lastIdx",1000);
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.sf_islogin), Context.MODE_PRIVATE);
        final String sfTypeUser = sharedPref.getString(getString(R.string.sf_islogin), "parent");
        final String sfUserName = sharedPref.getString("userName", "undefined");

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://gembalabaik-aac4a.firebaseio.com/");

        Query query = mDatabase.child("class");
        FirebaseListOptions<ClassFirebase> options =
                new FirebaseListOptions.Builder<ClassFirebase>()
                        .setQuery(query, ClassFirebase.class)
                        .setLayout(android.R.layout.simple_list_item_1)
                        .setLifecycleOwner(this)
                        .build();

        final FirebaseListAdapter<ClassFirebase> firebaseListAdapter = new FirebaseListAdapter<ClassFirebase>(options) {
            @Override
            protected void populateView(View v, ClassFirebase model, int position) {
                TextView txtClassName = v.findViewById(android.R.id.text1);
                txtClassName.setText(model.getClassName());
            }

        };
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final LayoutInflater inflater = this.getLayoutInflater();

        piBodyNews = findViewById(R.id.piBodyNews);
        piBodyNews.setText(bodyNews);
        piTypeNews = findViewById(R.id.piTypeNews);
        piTypeNews.setText(typeNews);
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
                String bodyNews = piBodyNews.getText().toString();
                String typeNews = piTypeNews.getText().toString();
                String createdBy = sfUserName;

                if (bodyNews.isEmpty() || typeNews.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Field tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.setVisibility(View.VISIBLE);
                    icAddNews.setEnabled(false);
                    shouldAllowBack = false;
                    writeNews(classId, idNews, bodyNews, urlImage, typeNews, createdAt, createdBy, lastIdx, lastIdx);
                }

            }
        });

    }

    private void writeNews(String classId, String idNews, String bodyNews, String urlImage, String typeNews, String createdAt, String createdBy, Integer lastIdxNews, Integer lastIdxNotif) {
        String bfilename = "img" + idNews;
        NewsFirebase newsFirebase = new NewsFirebase("",bodyNews, typeNews, bfilename, createdAt, createdBy, lastIdxNews);
        mDatabase.child("newsAll").child(idNews).setValue(newsFirebase);
        mDatabase.child("news").child(classId).child(idNews).setValue(newsFirebase);
        Toast.makeText(NewsEditActivity.this, "Sukses tambah news",
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
