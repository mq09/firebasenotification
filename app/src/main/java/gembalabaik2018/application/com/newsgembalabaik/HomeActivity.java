package gembalabaik2018.application.com.newsgembalabaik;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    private TextView txtHeader;
    private ImageView icSearch, icAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.sf_islogin), Context.MODE_PRIVATE);
        final String sfTypeUser = sharedPref.getString(getString(R.string.sf_islogin), "parent");
        final String sfIdUser = sharedPref.getString("idUser", "undefined");
        getSupportActionBar().hide();
        setContentView(R.layout.activity_home);
        txtHeader = findViewById(R.id.txtHeader);
        icSearch = findViewById(R.id.icSearch);
        icAdd = findViewById(R.id.icAdd);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, NewsFragment.newInstance());
        transaction.commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.action_news:
                        txtHeader.setText("News");
                        if(sfTypeUser.equalsIgnoreCase("admin")){
                            icSearch.setVisibility(View.VISIBLE);
                            icAdd.setVisibility(View.VISIBLE);
                        }
                        selectedFragment = NewsFragment.newInstance();
                        break;
                    case R.id.action_notif:
                        txtHeader.setText("Notif");
                        icSearch.setVisibility(View.GONE);
                        icAdd.setVisibility(View.GONE);
                        selectedFragment = NotifFragment.newInstance();
                        break;
                    case R.id.action_setting:
                        txtHeader.setText("Setting");
                        icSearch.setVisibility(View.GONE);
                        icAdd.setVisibility(View.GONE);
                        selectedFragment = SettingFragment.newInstance();
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                return true;
            }
        });


    }
}
