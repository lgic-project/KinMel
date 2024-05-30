package com.example.kinmel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
public class AccountFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.buyersetting, container, false);

        TextView tvProfileSetting = view.findViewById(R.id.tvProfileSetting);
        TextView logoutBtn = view.findViewById(R.id.tvLogout1);
        TextView passwordChange = view.findViewById(R.id.btnUpdatePassword);
        TextView tvHelp = view.findViewById(R.id.tvHelp);

        Log.d("SettingsPage", "logoutBtn: " + logoutBtn);


        tvProfileSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserProfileActivity.class);
                startActivity(intent);
            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SettingsPage", "Logout button clicked");
                showMessageDialog("LogOut", "Do you sure want to logout?");
            }
        });

        passwordChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UpdatePasswordActivity.class);
                getActivity().startActivity(intent);
            }
        });

        TextView btnUpdatePassword = view.findViewById(R.id.btnUpdatePassword);
        btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UpdatePasswordActivity.class);
                startActivity(intent);
            }
        });
        //help
        tvHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HelpActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
    private void showMessageDialog(String title, String message) {
        Log.d("SettingsPage", "showMessageDialog called");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Perform logout operation here
                Log.d("SettingsPage", "Logout operation performed");

                // Remove token from SharedPreferences
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("token");
                editor.apply();

                // Perform any necessary cleanup operations here
                // ...
                Intent intent = new Intent(getActivity(), BuyerLogin.class);
                startActivity(intent);
            getActivity().finish();
            }
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
        Log.d("SettingsPage", "AlertDialog should be showing now");
    }
}
