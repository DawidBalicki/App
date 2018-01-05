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

public class AdminShowUsersTab extends Fragment {

    public Messages messages = new Messages();

    private OnFragmentInteractionListener mListener;

    public AdminShowUsersTab() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
           return inflater.inflate(R.layout.fragment_admin_show_users_tab, container, false);
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

                TableRow rowLabels = new TableRow(view.getContext());
                TextView empty = new TextView(view.getContext());
                empty.setWidth(350);

                TextView door1Label = new TextView(view.getContext());
                door1Label.setWidth(175);
                door1Label.setText("Laboratory");
                door1Label.setTextSize(15);
                door1Label.setTextColor(getResources().getColor(R.color.BLETextColor));
                door1Label.setTypeface(Typeface.SERIF, Typeface.BOLD);
                door1Label.setGravity(Gravity.CENTER_HORIZONTAL);

                TextView door2Label = new TextView(view.getContext());
                door2Label.setText("Storage room");
                door2Label.setWidth(175);
                door2Label.setTextSize(15);
                door2Label.setTextColor(getResources().getColor(R.color.BLETextColor));
                door2Label.setTypeface(Typeface.SERIF, Typeface.BOLD);
                door2Label.setGravity(Gravity.CENTER_HORIZONTAL);

                rowLabels.addView(empty);
                rowLabels.addView(door1Label);
                rowLabels.addView(door2Label);

                table.addView(rowLabels);

                messages.displayUsers();

            }

        });

    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
