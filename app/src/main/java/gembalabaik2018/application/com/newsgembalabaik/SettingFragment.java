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
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class SettingFragment extends Fragment {
    private ImageView icParent, icClass, icProfile, icLogout;
    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.sf_islogin), Context.MODE_PRIVATE);
        String sfTypeUser = sharedPref.getString(getString(R.string.sf_islogin), "parent");

        icParent =  rootView.findViewById(R.id.icParent);
        icParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UserActivity.class);
                startActivity(intent);
            }
        });

        icClass = rootView.findViewById(R.id.icClass);
        icClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ClassRoomActivity.class);
                startActivity(intent);
            }
        });

        icProfile =  rootView.findViewById(R.id.icProfile);
        icProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        icLogout = rootView.findViewById(R.id.icLogout);
        icLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });
        if(!sfTypeUser.equalsIgnoreCase("admin")){
            icClass.setVisibility(View.GONE);
            icParent.setVisibility(View.GONE);
        }
        return rootView;
    }
}
