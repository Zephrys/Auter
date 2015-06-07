package com.zephrys.auter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;


public class MainActivity extends ActionBarActivity {

    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new StartFragment())
                    .commit();
        }


        Intent intent = getIntent();
        if(intent.getBooleanExtra("print_completion", false)) {
            Toast toast = Toast.makeText(this, "Ride ended Safely!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onResume() {


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (!sharedPreferences.getBoolean("SIGNUP_DONE", false)) {


            Intent intent = new Intent(this, Request_otp_activity.class);
            startActivity(intent);

        }

        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class StartFragment extends Fragment {

        public StartFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            Button startRide = (Button) rootView.findViewById(R.id.start_journey_button);


            startRide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    EditText aadharNum = (EditText) rootView.findViewById(R.id.aadhar_user_input_edittext);

                    String aNum = aadharNum.getText().toString();

                    if(aNum.length() != 12) {
                        Toast toast = Toast.makeText(getActivity(), "Please enter a valid 12-digit aadhar number", Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    } else {
                        Intent intent = new Intent(getActivity(), RideOnActivity.class);
                        intent.putExtra(Intent.EXTRA_TEXT, aNum);

                        startActivity(intent);
                    }
                }
            });
            return rootView;
        }
    }
}
