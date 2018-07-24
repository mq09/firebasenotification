package gembalabaik2018.application.com.newsgembalabaik;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import gembalabaik2018.application.com.newsgembalabaik.ModelFirebase.NotifFirebase;

public class NotifFragment extends Fragment {

    private DatabaseReference mDatabase;
    private ListView lvNotif;

    public static NotifFragment newInstance() {
        NotifFragment fragment = new NotifFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (FirebaseApp.getApps(this.getContext()).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_notif, container, false);
        lvNotif = rootView.findViewById(R.id.lvNotif);
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://gembalabaik-aac4a.firebaseio.com/");
        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.sf_islogin), Context.MODE_PRIVATE);
        final String sfTypeUser = sharedPref.getString(getString(R.string.sf_islogin), "parent");
        final String sfIdUser = sharedPref.getString("idUser", "undefined");
        final String sfClassId = sharedPref.getString("classId", "undefined");
        Query query;
        if (!sfTypeUser.equalsIgnoreCase("admin")) {
            query = mDatabase.child("notifParent").child(sfClassId).orderByChild("idx").limitToLast(1000);
        } else {
            query = mDatabase.child("notifAdmin").orderByChild("idx").limitToLast(1000);
        }

        FirebaseListOptions<NotifFirebase> options =
                new FirebaseListOptions.Builder<NotifFirebase>()
                        .setQuery(query, NotifFirebase.class)
                        .setLayout(android.R.layout.simple_list_item_2)
                        .setLifecycleOwner(this)
                        .build();

        final FirebaseListAdapter<NotifFirebase> firebaseListAdapter = new FirebaseListAdapter<NotifFirebase>(options) {
            @Override
            protected void populateView(View v, NotifFirebase model, int position) {
                TextView txtNotifBody = v.findViewById(android.R.id.text1);
                txtNotifBody.setText(model.getCreatedBy()+" "+model.getNotifBody());
                TextView txtCreatedBy = v.findViewById(android.R.id.text2);
                txtCreatedBy.setText(model.getCreatedAt());
            }

        };

        lvNotif.setAdapter(firebaseListAdapter);
        lvNotif.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String idNews = firebaseListAdapter.getItem(position).getIdNews();
                String bodyNews = firebaseListAdapter.getItem(position).getBodyNews();
                String createdAt = firebaseListAdapter.getItem(position).getCreatedAt();
                String urlImage = firebaseListAdapter.getItem(position).getUrlImage();
                Intent intent = new Intent(getContext(), CommentActivity.class);
                intent.putExtra("idNews", idNews);
                intent.putExtra("bodyNews", bodyNews);
                intent.putExtra("createdAt", createdAt);
                intent.putExtra("urlImage", urlImage);
                startActivity(intent);
            }
        });
        return rootView;
    }
}
