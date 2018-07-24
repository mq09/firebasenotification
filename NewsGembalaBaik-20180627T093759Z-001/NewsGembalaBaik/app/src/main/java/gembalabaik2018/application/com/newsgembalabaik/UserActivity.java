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

import gembalabaik2018.application.com.newsgembalabaik.ModelFirebase.AdminFirebase;

public class UserActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private ImageView icAdd, icSearchParent;
    private ListView lvParent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_user);
        if (FirebaseApp.getApps(this.getApplicationContext()).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        lvParent =  findViewById(R.id.lvParent);
        icAdd =   findViewById(R.id.icAdd);
        icAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, UserAddActivtiy.class);
                startActivity(intent);
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://gembalabaik-aac4a.firebaseio.com/");

        Query query = FirebaseDatabase.getInstance().getReference().child("admin").orderByChild("userName");

        FirebaseListOptions<AdminFirebase> options =
                new FirebaseListOptions.Builder<AdminFirebase>()
                        .setQuery(query, AdminFirebase.class)
                        .setLayout(android.R.layout.simple_list_item_2)
                        .setLifecycleOwner(this)
                        .build();

        final FirebaseListAdapter<AdminFirebase> firebaseListAdapter = new FirebaseListAdapter<AdminFirebase>(options) {
            @Override
            protected void populateView(View v, AdminFirebase model, int position) {

                    TextView txtParentName = v.findViewById(android.R.id.text1);
                    txtParentName.setText(model.getUserName());
                    TextView txtPhone = v.findViewById(android.R.id.text2);
                    txtPhone.setText("Phone : "+model.getPhoneNumber());

            }

        };
        lvParent.setAdapter(firebaseListAdapter);

        lvParent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String idUser = firebaseListAdapter.getRef(position).getKey();

                String userName = firebaseListAdapter.getItem(position).getUserName();
                String phone = firebaseListAdapter.getItem(position).getPhoneNumber();

                Intent intent = new Intent(UserActivity.this, UserEditActivity.class);
                intent.putExtra("idUser", idUser);
                intent.putExtra("userName", userName);
                intent.putExtra("phone", phone);
                startActivity(intent);
            }
        });

    }
}
