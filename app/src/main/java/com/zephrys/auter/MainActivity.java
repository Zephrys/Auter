package com.zephrys.auter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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

        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

                    // Gyani: an intent basically like a message, conveys where is it coming from and which activity does it point to
                    Intent intent = new Intent(getActivity(), RideOnActivity.class);
                    // it may also bring some information along with it, we can use this aNum in the other activity, < please remove these comments once you are done and add something more legible to us later
                    intent.putExtra(Intent.EXTRA_TEXT, aNum);

                    startActivity(intent);
                }
            });


            // TODO: REMOVE THIS BUTTON FRMO HERE TO A NEW SIGNUP CLASS

            Button button = (Button) rootView.findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), SignUp.class);
                    startActivity(intent);
                }
            });

            // TODO: transfer someplace else
//
//            EditText aadharNum = (EditText) rootView.findViewById(R.id.aadhar_user_input_edittext);
//
//            final String aNum = aadharNum.getText().toString();


            Button button1 = (Button) rootView.findViewById(R.id.request_otp_button);
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // was first trying to use this value itself, now I have used a different edittext in the other screen!! :D, please don't remove this

                    /*if(aNum.length() != 12) {
                        Context context = getActivity().getApplicationContext();
                        Toast toast = Toast.makeText(context, "Put in proper 12 digit aadhar num", Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }*/



                    Intent intent = new Intent(getActivity(), Request_otp_activity.class);
//                    intent.putExtra(Intent.EXTRA_TEXT, aNum);
                    startActivity(intent);
                }
            });
            return rootView;
        }
    }
}
