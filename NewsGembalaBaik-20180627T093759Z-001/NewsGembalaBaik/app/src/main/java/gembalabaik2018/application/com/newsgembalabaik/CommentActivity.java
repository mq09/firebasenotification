package gembalabaik2018.application.com.newsgembalabaik;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import gembalabaik2018.application.com.newsgembalabaik.ModelFirebase.CommentFirebase;
import gembalabaik2018.application.com.newsgembalabaik.ModelFirebase.NotifFirebase;

public class CommentActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private TextView txtBodyNews, txtEditNews, txtDeleteNews;
    private ImageView icAddComment, imgView;
    private EditText piComment;
    private ListView lvComment;
    private List<String> list;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_comment);
        if(FirebaseApp.getApps(getApplicationContext()).isEmpty()){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        Intent intent = getIntent();
        final String idNews = intent.getStringExtra("idNews");
        final String bodyNews = intent.getStringExtra("bodyNews");
        final String createdAt = intent.getStringExtra("createdAt");
        final String typeNews = intent.getStringExtra("typeNews");
        final String urlImage = intent.getStringExtra("urlImage");
        final Integer lastIdx =  intent.getIntExtra("lastIdx", 0);

        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.sf_islogin), Context.MODE_PRIVATE);
        final String sfTypeUser = sharedPref.getString(getString(R.string.sf_islogin), "parent");
        final String sfUserName = sharedPref.getString("userName", "undefined");
        final String sfClassId = sharedPref.getString("classId", "undefined");

        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://gembalabaik-aac4a.firebaseio.com/");
        final Query qNotif = mDatabase.child("notif");
        final Query query = mDatabase.child("comment").child(idNews).orderByChild("idx");

        FirebaseListOptions<CommentFirebase> options =
                new FirebaseListOptions.Builder<CommentFirebase>()
                        .setQuery(query, CommentFirebase.class)
                        .setLayout(R.layout.adapter_comment)
                        .setLifecycleOwner(this)
                        .build();

        final FirebaseListAdapter<CommentFirebase> firebaseListAdapter = new FirebaseListAdapter<CommentFirebase>(options){
            @Override
            protected void populateView(View v, CommentFirebase model, int position) {
                TextView txtUserComment = v.findViewById(R.id.txtUserComment);
                txtUserComment.setText(model.getCreatedBy());
                TextView txtCreatedAt = v.findViewById(R.id.txtCreatedAt);
                txtCreatedAt.setText(model.getCreatedAt());
                TextView txtPostComment = v.findViewById(R.id.txtPostComment);
                txtPostComment.setText(model.getBodyComment());
            }
        };

        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference().child("news");
        final AlertDialog.Builder dialogBuilderPreviewImage = new AlertDialog.Builder(this);
        final LayoutInflater inflater = this.getLayoutInflater();

        imgView = findViewById(R.id.imgView);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = inflater.inflate(R.layout.dialog_previewimage, null);
                dialogBuilderPreviewImage.setView(dialogView);
                final ImageView imgPreview = dialogView.findViewById(R.id.imgPreview);
                try{
                    Glide.with(getApplicationContext() )
                            .load(storageRef.child(urlImage))
                            .into(imgPreview);

                } catch (Exception e ){

                }
                final AlertDialog alertDialog = dialogBuilderPreviewImage.create();
                alertDialog.show();
            }
        });
        try{
            Glide.with(getApplicationContext() )
                    .load(storageRef.child(urlImage))
                    .into(imgView);

        } catch (Exception e ){

        }

        txtEditNews = findViewById(R.id.txtEditNews);
        txtEditNews.setVisibility(View.GONE);
        txtEditNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewsEditActivity.class);
                intent.putExtra("idNews", idNews);
                intent.putExtra("bodyNews", bodyNews);
                intent.putExtra("typeNews", typeNews);
                intent.putExtra("createdAt", createdAt);
                intent.putExtra("urlImage", urlImage);
                intent.putExtra("lastIdx", lastIdx);
                startActivity(intent);
            }
        });
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        txtDeleteNews = findViewById(R.id.txtDeleteNews);
        txtDeleteNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder
                        .setTitle("Delete News")
                        .setMessage("Do you really want to delete news?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                String key = mDatabase.child("news").child(idNews).getKey();
                                mDatabase.child("newsAdmin").child(key).removeValue();
                                mDatabase.child("newsParent").child("cls_09062018042755").child(key).removeValue();
                                mDatabase.child("newsParent").child("cls_09062018043413").child(key).removeValue();
                                mDatabase.child("newsParent").child("cls_09062018043432").child(key).removeValue();
                                mDatabase.child("newsParent").child("cls_09062018043635").child(key).removeValue();
                                mDatabase.child("newsParent").child("cls_09062018043651").child(key).removeValue();
                                mDatabase.child("newsParent").child("cls_09062018043707").child(key).removeValue();
                                mDatabase.child("newsParent").child("cls_09062018043723").child(key).removeValue();
                                mDatabase.child("newsParent").child("cls_09062018043740").child(key).removeValue();
                                mDatabase.child("newsParent").child("cls_09062018043756").child(key).removeValue();
                                Toast.makeText(getApplicationContext(), "Berhasil hapus news", Toast.LENGTH_SHORT).show();
                                finish();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
        if(sfTypeUser.equalsIgnoreCase("parent")){
            txtDeleteNews.setVisibility(View.GONE);
            txtEditNews.setVisibility(View.GONE);
        }
        txtBodyNews =  findViewById(R.id.txtBodyNews);
        txtBodyNews.setText(bodyNews);
        lvComment = findViewById(R.id.lvComment);
        lvComment.setAdapter(firebaseListAdapter);
        lvComment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String key = firebaseListAdapter.getRef(position).getKey();
                mDatabase.child("comment").child(key).removeValue();
            }
        });
        piComment = findViewById(R.id.piComment);
        list = new ArrayList<String>();
        list.add("ASU");
        list.add("BAJINGAN");
        list.add("BANGSAT");
        list.add("BEJAD");
        list.add("BEJAT");
        list.add("BENCONG");
        list.add("BODOH");
        list.add("BOLOT");
        list.add("BRENGSEK");
        list.add("BUDEK");
        list.add("DADA");
        list.add("GEBLEK");
        list.add("GEMBEL");
        list.add("GILA");
        list.add("GUE");
        list.add("GW");
        list.add("IDIOT");
        list.add("JABLAY");
        list.add("KEPARAT");
        list.add("KOLOT");
        list.add("KONTOL");
        list.add("KUNYUK");
        list.add("LOE");
        list.add("LONTE");
        list.add("LU");
        list.add("MAHO");
        list.add("NGENTOT");
        list.add("PELACUR");
        list.add("PEREK");
        list.add("SARAP");
        list.add("SETAN");
        list.add("SIALAN");
        list.add("SINTING");
        list.add("SOMPRET");
        list.add("TAHI");
        list.add("TAI");
        list.add("TELMI");
        list.add("TOLOL");
        list.add("TULI");

        icAddComment = findViewById(R.id.icAddComment);
        icAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String bodyComment = piComment.getText().toString();
                final String createdAt = getDateTime();
                final String createdBy = sfUserName;
                final Integer lastIdxComment = 1000 - firebaseListAdapter.getCount();
                if (bodyComment.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Field tidak boleh kosong", Toast.LENGTH_SHORT);
                } else {
                    final int[] lastIdxNotif = {0};
                    String[] items = bodyComment.split(" ");
                    boolean filterOke = true;
                    for (String item : items)
                    {
                        for (int i = 0; i < list.size(); i++) {
                            if (item.equalsIgnoreCase(list.get(i))) {
                                Log.d("TAGTAG", "Isi sama hehe");
                                filterOke = false;
                            }
                        }
                    }
                    if (filterOke) {
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
                                writeComment(idNews, bodyComment,bodyNews,urlImage, createdAt, createdBy, lastIdxComment, lastIdxNotifNew);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Tidak boleh ada kata-kata kotor", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }

    private void writeComment(String idNews, String bodyComment, String bodyNews, String urlImage, String createdAt, String createdBy, Integer lastIdx, Integer lastIdxNotif) {

        CommentFirebase commentFirebase = new CommentFirebase(idNews, bodyComment, createdAt, createdBy, lastIdx);
        String idComment =getUniqId();
        mDatabase.child("comment").child(idNews).child(idComment).setValue(commentFirebase);
        String notifBody = bodyComment;
        String idNotif = getUniqIdNotif();

        NotifFirebase notifFirebase = new NotifFirebase(idNews, "mengomentari "+notifBody, bodyNews, urlImage, createdAt, createdBy, lastIdxNotif);
        mDatabase.child("notif").child(idNotif).setValue(notifFirebase);
        mDatabase.child("notifAll").child(idNotif).setValue(notifFirebase);
        piComment.setText("");
        Toast.makeText(CommentActivity.this, "Sukses",
                Toast.LENGTH_SHORT).show();

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
        return "comment_"+date + time;
    }

    private String getDateTime() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        String date = DateFormat.format("dd-MM-yyyy", calendar).toString();
        String time = DateFormat.format("HH:mm:ss", calendar).toString();
        return date +" "+ time;
    }
}
