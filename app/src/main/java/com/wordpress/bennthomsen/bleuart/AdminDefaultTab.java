package com.wordpress.bennthomsen.bleuart;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AdminDefaultTab.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdminDefaultTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminDefaultTab extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public Messages messages = new Messages();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AdminDefaultTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminDefaultTab.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminDefaultTab newInstance(String param1, String param2) {
        AdminDefaultTab fragment = new AdminDefaultTab();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

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
        Button buttonStateOfDoor1 = getView().findViewById(R.id.buttonStateOfDoor);
        Button buttonStateOfDoor2 = getView().findViewById(R.id.buttonStateOfDoor1);
        TextView textViewStateOfDoor1 = getView().findViewById(R.id.stateOfDoor);
        TextView textViewStateOfDoor2 = getView().findViewById(R.id.stateOfDoor1);
        TextView nameOfUser = getView().findViewById(R.id.nameOfUser);

        nameOfUser.setText(Controller.getName());

        buttonOpenDoor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messages.openDoor("1");
                buttonCloseDoor1.setEnabled(true);
                buttonCloseDoor1.setAlpha(1);
                buttonOpenDoor1.setEnabled(false);
                buttonOpenDoor1.setAlpha(.3f);

            }
        });
        buttonOpenDoor2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messages.openDoor("2");
                buttonCloseDoor2.setEnabled(true);
                buttonCloseDoor2.setAlpha(1);
                buttonOpenDoor2.setEnabled(false);
                buttonOpenDoor2.setAlpha(.3f);

            }
        });
        buttonCloseDoor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messages.closeDoor("1");
                buttonCloseDoor1.setEnabled(false);
                buttonCloseDoor1.setAlpha(.3f);
                buttonOpenDoor1.setEnabled(true);
                buttonOpenDoor1.setAlpha(1);

            }
        });
        buttonCloseDoor2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messages.closeDoor("2");
                buttonCloseDoor2.setEnabled(false);
                buttonCloseDoor2.setAlpha(.3f);
                buttonOpenDoor2.setEnabled(true);
                buttonOpenDoor2.setAlpha(1);

            }
        });
        buttonStateOfDoor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messages.checkTheStateOfDoor("1");

            }
        });
        buttonStateOfDoor2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messages.checkTheStateOfDoor("2");

            }
        });
    }

  //  @Override
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
