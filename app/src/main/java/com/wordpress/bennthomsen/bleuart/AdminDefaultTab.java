package com.wordpress.bennthomsen.bleuart;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class AdminDefaultTab extends Fragment {

    public Messages messages = new Messages();

    private OnFragmentInteractionListener mListener;

    public AdminDefaultTab() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_default_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final Button buttonOpenDoor1 = getView().findViewById(R.id.buttonOpen);
        final Button buttonOpenDoor2 = getView().findViewById(R.id.buttonOpen1);
        final Button buttonCloseDoor1 = getView().findViewById(R.id.buttonClose);
        final Button buttonCloseDoor2 = getView().findViewById(R.id.buttonClose1);
        TextView nameOfUser = getView().findViewById(R.id.nameOfUser);

        nameOfUser.setText(Controller.getName());

        if(!Controller.getStatusOfDoor1()) {
            buttonCloseDoor1.setEnabled(false);
            buttonCloseDoor1.setAlpha(.3f);
        }
        else{
            buttonOpenDoor1.setEnabled(false);
            buttonOpenDoor1.setAlpha(.3f);
        }
        if(!Controller.getStatusOfDoor2()) {
            buttonCloseDoor2.setEnabled(false);
            buttonCloseDoor2.setAlpha(.3f);
        }
        else{
            buttonOpenDoor2.setEnabled(false);
            buttonOpenDoor2.setAlpha(.3f);
        }


        buttonOpenDoor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messages.openDoor("1");

            }
        });
        buttonOpenDoor2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messages.openDoor("2");

            }
        });
        buttonCloseDoor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messages.closeDoor("1");

            }
        });
        buttonCloseDoor2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messages.closeDoor("2");

            }
        });

    }


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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
