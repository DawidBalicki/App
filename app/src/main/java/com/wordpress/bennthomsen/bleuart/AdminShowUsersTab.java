package com.wordpress.bennthomsen.bleuart;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AdminShowUsersTab.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdminShowUsersTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminShowUsersTab extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public Messages messages = new Messages();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AdminShowUsersTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminShowUsersTab.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminShowUsersTab newInstance(String param1, String param2) {
        AdminShowUsersTab fragment = new AdminShowUsersTab();
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
           return inflater.inflate(R.layout.fragment_admin_show_users_tab, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        Button buttonShowUsers = getView().findViewById(R.id.buttonShowUsers);
        final TableLayout table = getView().findViewById(R.id.tab);

        buttonShowUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                table.removeAllViewsInLayout();

                TableRow rowDayLabels = new TableRow(view.getContext());
                TextView empty = new TextView(view.getContext());
                empty.setWidth(350);

                TextView day1Label = new TextView(view.getContext());
                day1Label.setWidth(175);
                day1Label.setText("Laboratory");
                day1Label.setTextSize(15);
                day1Label.setTextColor(getResources().getColor(R.color.BLETextColor));
                day1Label.setTypeface(Typeface.SERIF, Typeface.BOLD);
                day1Label.setGravity(Gravity.CENTER_HORIZONTAL);

                TextView day2Label = new TextView(view.getContext());
                day2Label.setText("Storage room");
                day2Label.setWidth(175);
                day2Label.setTextSize(15);
                day2Label.setTextColor(getResources().getColor(R.color.BLETextColor));
                day2Label.setTypeface(Typeface.SERIF, Typeface.BOLD);
                day2Label.setGravity(Gravity.CENTER_HORIZONTAL);

                rowDayLabels.addView(empty);
                rowDayLabels.addView(day1Label);
                rowDayLabels.addView(day2Label);

                table.addView(rowDayLabels);

                messages.displayUsers();

            }

        });

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
