package com.wordpress.bennthomsen.bleuart;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class AdminCreateAccountTab extends Fragment {

    public Messages messages = new Messages();

    private OnFragmentInteractionListener mListener;

    public AdminCreateAccountTab() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_create_account_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        final EditText editTextLogin  = getView().findViewById(R.id.editTextLogin);
        final EditText editTextPassword  = getView().findViewById(R.id.editTextPassword);
        final CheckBox checkBoxDoor1 = getView().findViewById(R.id.checkBoxDoor1);
        final CheckBox checkBoxDoor2 = getView().findViewById(R.id.checkBoxDoor2);
        Button buttonSaveAccount = getView().findViewById(R.id.buttonSaveAccount);

        buttonSaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean cancelBoolean = false;
                String login = editTextLogin.getText().toString();
                String password = editTextPassword.getText().toString();
                Boolean accessDoor1 = checkBoxDoor1.isChecked();
                Boolean accessDoor2 = checkBoxDoor2.isChecked();

                // Check for a valid password, if the user entered one.
                if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
                    editTextPassword.setError(getString(R.string.error_invalid_password));
                    editTextPassword.requestFocus();
                    cancelBoolean = true;
                }

                // Check for a valid email address.
                if (TextUtils.isEmpty(login) ) {
                    editTextLogin.setError(getString(R.string.error_field_required));
                    editTextLogin.requestFocus();
                    cancelBoolean = true;
                } else if (!isLoginValid(login)) {
                    editTextLogin.setError(getString(R.string.error_invalid_email));
                    editTextLogin.requestFocus();
                    cancelBoolean = true;
                }
                if(!checkBoxDoor1.isChecked() && !checkBoxDoor2.isChecked() ){
                    Toast.makeText(getActivity(), getString(R.string.error_door_required), Toast.LENGTH_SHORT).show();
                    checkBoxDoor1.requestFocus();
                    cancelBoolean = true;
                }

                if (cancelBoolean) {
                    editTextPassword.requestFocus();
                } else {
                    messages.createAccount(login, password, accessDoor1, accessDoor2);
                    editTextLogin.getText().clear();
                    editTextPassword.getText().clear();
                    checkBoxDoor1.setChecked(false);
                    checkBoxDoor2.setChecked(false);
                    editTextLogin.requestFocus();

                }
            }
        });

    }

    private boolean isLoginValid(String login) {

        return login.length() > 4 && login.length() < 12  && !login.contains(":");
    }

    private boolean isPasswordValid(String password) {

        return password.length() > 4 && password.length() < 12 && !password.contains(":");
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
