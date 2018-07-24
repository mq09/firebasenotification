package gembalabaik2018.application.com.newsgembalabaik;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import gembalabaik2018.application.com.newsgembalabaik.ModelFirebase.NewsFirebase;

public class NewsFragment extends Fragment {

    private DatabaseReference mDatabase;
    private ImageView icAdd;
    private ListView lvNews;
    private LinearLayoutManager mLayoutManager;
    private String classIdUser = "";

    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();

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
        final View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.sf_islogin), Context.MODE_PRIVATE);
        final String sfTypeUser = sharedPref.getString(getString(R.string.sf_islogin), "parent");
        final String sfIdUser = sharedPref.getString("idUser", "undefined");
        final String sfClassId = sharedPref.getString("classId", "undefined");

        icAdd = getActivity().findViewById(R.id.icAdd);
        lvNews = rootView.findViewById(R.id.lvNews);

        final ImageView icSearchNews = getActivity().findViewById(R.id.icSearch);
        if (!sfTypeUser.equalsIgnoreCase("admin")) {
            icAdd.setVisibility(View.GONE);
            icSearchNews.setVisibility(View.GONE);
        }
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://gembalabaik-aac4a.firebaseio.com/");
        final TextView txtHidden = rootView.findViewById(R.id.txtHidden);
        FirebaseStorage storage = FirebaseStorage.getInstance();

        final StorageReference storageRef = storage.getReference().child("news");
        Query query;
        if (!sfTypeUser.equalsIgnoreCase("admin")) {
            query = mDatabase.child("newsParent").child(sfClassId).orderByChild("idx").limitToLast(1000);
        } else {
            query = mDatabase.child("newsAdmin").orderByChild("idx").limitToLast(1000);
        }

        FirebaseListOptions<NewsFirebase> options =
                new FirebaseListOptions.Builder<NewsFirebase>()
                        .setQuery(query, NewsFirebase.class)
                        .setLayout(R.layout.adapter_news)
                        .setLifecycleOwner(this)
                        .build();

        final FirebaseListAdapter<NewsFirebase> firebaseListAdapter = new FirebaseListAdapter<NewsFirebase>(options) {
            @Override
            protected void populateView(View v, NewsFirebase model, int position) {
                TextView txtBodyNews = v.findViewById(R.id.txtBodyNews);
                TextView txtCreatedAt = v.findViewById(R.id.txtCreatedAt);
                ImageView imgView = v.findViewById(R.id.imgView);
                txtBodyNews.setText(model.getBodyNews());
                txtCreatedAt.setText(model.getCreatedAt());
                try {
                    Glide.with(getContext())
                            .load(storageRef.child(model.getUrlImage()))
                            .into(imgView);
                } catch (Exception e) {

                }
            }
        };

        icAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NewsAddActivity.class);
                int lastIdx = firebaseListAdapter.getCount();
                intent.putExtra("lastIdx", lastIdx);
                startActivity(intent);
            }
        });
        lvNews.invalidateViews();
        lvNews.setAdapter(firebaseListAdapter);

        lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String idNews = firebaseListAdapter.getRef(position).getKey();
                String bodyNews = firebaseListAdapter.getItem(position).getBodyNews();
                String createdAt = firebaseListAdapter.getItem(position).getCreatedAt();
                String urlImage = firebaseListAdapter.getItem(position).getUrlImage();
                String typeNews = firebaseListAdapter.getItem(position).getTypeNews();
                String classId = firebaseListAdapter.getItem(position).getClassId();
                int lastIdx = firebaseListAdapter.getCount();
                Intent intent = new Intent(getContext(), CommentActivity.class);
                intent.putExtra("idNews", idNews);
                intent.putExtra("bodyNews", bodyNews);
                intent.putExtra("classId", classId);
                intent.putExtra("typeNews", typeNews);
                intent.putExtra("createdAt", createdAt);
                intent.putExtra("urlImage", urlImage);
                intent.putExtra("lastIdx", lastIdx);
                startActivity(intent);
            }
        });
        return rootView;
    }
}
