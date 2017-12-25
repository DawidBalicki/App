package com.wordpress.bennthomsen.bleuart;

import android.app.Activity;
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
        // Inflate the layout for this fragment



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



        //getActivity().setContentView(table);

        buttonShowUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // table.setStretchAllColumns(true);
                //table.setShrinkAllColumns(true);

                TableRow rowTitle = new TableRow(view.getContext());
                rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);

                TableRow rowDayLabels = new TableRow(view.getContext());
                TableRow rowHighs = new TableRow(view.getContext());
                TableRow rowLows = new TableRow(view.getContext());
                TableRow rowConditions = new TableRow(view.getContext());
                rowConditions.setGravity(Gravity.CENTER);

                TextView empty = new TextView(view.getContext());



                // labels column
                TextView highsLabel = new TextView(view.getContext());
                highsLabel.setText("Mirek");
                highsLabel.setWidth(300);
                highsLabel.setTextSize(30);
                highsLabel.setTypeface(Typeface.SERIF, Typeface.BOLD);
                highsLabel.setGravity(Gravity.CENTER);


                TextView lowsLabel = new TextView(view.getContext());
                lowsLabel.setText("Dawid");
                lowsLabel.setTextSize(30);
                lowsLabel.setTypeface(Typeface.SERIF, Typeface.BOLD);
                lowsLabel.setGravity(Gravity.CENTER);



                rowDayLabels.addView(empty);
                rowHighs.addView(highsLabel);
                rowLows.addView(lowsLabel);

                ImageView day2Conditions = new ImageView(view.getContext());
                day2Conditions.setImageResource(R.drawable.yes_icon);
                day2Conditions.setScaleX((float) 0.28);
                day2Conditions.setScaleY((float) 0.28);
                day2Conditions.setScaleType(ImageView.ScaleType.FIT_XY);
                day2Conditions.setY(-120);


                ImageView day1Conditions = new ImageView(view.getContext());
                day1Conditions.setImageResource(R.drawable.no_icon);
                day1Conditions.setScaleX((float) 0.28);
                day1Conditions.setScaleY((float) 0.28);
                day1Conditions.setY(-120);

                // day 1 column
                TextView day1Label = new TextView(view.getContext());

                day1Label.setWidth(160);
                day1Label.setText("Laboratory");

                day1Label.setTextColor(getResources().getColor(R.color.BLETextColor));
                day1Label.setTypeface(Typeface.SERIF, Typeface.BOLD);
                day1Label.setGravity(Gravity.CENTER_HORIZONTAL);

                TextView day1High = new TextView(view.getContext());
                day1High.setWidth(100);
                day1High.setText("28°F");
                day1High.setGravity(Gravity.CENTER_HORIZONTAL);

                TextView day1Low = new TextView(view.getContext());
                day1Low.setWidth(100);
                day1Low.setText("15°F");
                day1Low.setGravity(Gravity.CENTER_HORIZONTAL);

                rowDayLabels.addView(day1Label);
                rowHighs.addView(day1High);
                rowLows.addView(day1Low);


                // day2 column
                TextView day2Label = new TextView(view.getContext());
                day2Label.setText("Storage room");
                day2Label.setWidth(170);
                day2Label.setTextColor(getResources().getColor(R.color.BLETextColor));
                day2Label.setTypeface(Typeface.SERIF, Typeface.BOLD);
                day2Label.setGravity(Gravity.CENTER_HORIZONTAL);

                TextView day2High = new TextView(view.getContext());
                day2High.setText("26°F");
                day2High.setWidth(100);
                day2High.setGravity(Gravity.CENTER_HORIZONTAL);

//                TextView day2Low = new TextView(view.getContext());
//                day2Low.setText("14°F");
//                day2Low.setWidth(100);
//                day2Low.setGravity(Gravity.CENTER_HORIZONTAL);

                rowDayLabels.addView(day2Label);
                rowHighs.addView(day1Conditions);
                rowLows.addView(day2Conditions);


                table.addView(rowTitle);
                table.addView(rowDayLabels);
                table.addView(rowHighs);
                table.addView(rowLows);

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
