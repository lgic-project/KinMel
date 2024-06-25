package com.example.kinmelsellerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentSettingsPage extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting, container, false);

        // Find the TextView by ID
        TextView tvHelp = view.findViewById(R.id.tvHelp);
        TextView tvProfileSetting = view.findViewById(R.id.tvProfileSetting);
        TextView tvUpdatePassword = view.findViewById(R.id.tvUpdatePassword);

        tvHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to SellerHelpActivity
                Intent intent = new Intent(getActivity(), SellerHelpActivity.class);
                startActivity(intent);
            }
        });
        // Set OnClickListener for tvProfileSetting
        tvProfileSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to UpdateProfileActivity
                Intent intent = new Intent(getActivity(), UpdateProfileActivity.class);
                startActivity(intent);
            }
        });

        tvUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to UpdateProfileActivity
                Intent intent = new Intent(getActivity(), UpdatePasswordActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
