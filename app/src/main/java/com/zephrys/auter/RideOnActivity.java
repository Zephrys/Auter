package com.zephrys.auter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class RideOnActivity extends ActionBarActivity {

    Long mAadarNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_on);

        Intent intent = getIntent();
        if(intent.getBooleanExtra("print_safe", false)) {
            Toast toast = Toast.makeText(this, "I am safe!", Toast.LENGTH_SHORT);
            toast.show();
        }
        mAadarNum = Long.valueOf(intent.getStringExtra(Intent.EXTRA_TEXT));

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new RideOnFragment(mAadarNum))
                    .commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return false;
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
    public static class RideOnFragment extends Fragment {

        Long aadhar;

        public RideOnFragment(Long a) {
            aadhar = a;
        }

        @Override
        public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_ride_on, container, false);

            TextView aadhar_view = (TextView) rootView.findViewById(R.id.aadhar_view);
            aadhar_view.setVisibility(View.VISIBLE);
            aadhar_view.setText(aadhar.toString());

            final EditText dispInfo = (EditText) rootView.findViewById(R.id.info_driver_edittext);
            dispInfo.setText("");

            Toast toast = Toast.makeText(getActivity(), "Fetching Data!", Toast.LENGTH_SHORT);
            toast.show();

            String printed = "Some Network error! Try again!";

            // Fetching data from parse.com!!

            ParseQuery<ParseObject> getUser = ParseQuery.getQuery("Cabbies");
            getUser.whereEqualTo("ID", aadhar.toString());

            getUser.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {
                    if(e == null) {
                        try {
                            ParseObject first = parseObjects.get(0);
                            String temp1 = first.getString("Name");
                            temp1 += "\n" + first.getString("Address");
                            dispInfo.setText(temp1);
                        }
                        catch (Exception some) {
                            Toast toast = Toast.makeText(getActivity(), "Aadhar does not belong to a Registered Driver", Toast.LENGTH_SHORT);
                            toast.show();
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        Toast toast = Toast.makeText(getActivity(), "Network Error! Try again", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            });

            Button panic = (Button) rootView.findViewById(R.id.panic_button);

            panic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.panic)
                            .setMessage("Are you sure you want to invoke the Panic message, this would alert to the concerned authorities with your details?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getActivity(), Panic_Activity.class);
                                    intent.putExtra(Intent.EXTRA_TEXT, aadhar.toString());
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();


                }
            });

            Button safe = (Button) rootView.findViewById(R.id.safe_button);

            safe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Confirm Safe?")
                            .setMessage("Are you sure you want to mark yourself safe and end this ride?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    intent.putExtra("print_completion", true);
                                    startActivity(intent);

                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();

                }
            });


            return rootView;
        }
    }
}
