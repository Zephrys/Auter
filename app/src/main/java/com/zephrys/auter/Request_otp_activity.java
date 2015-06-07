package com.zephrys.auter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;


public class Request_otp_activity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_request_otp_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_request_otp_activity, menu);
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
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_request_otp_activity, container, false);

            Button b1 = (Button) rootView.findViewById(R.id.send_otp_button);
            //sends the otp. updates text view to 'otp sent'
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String aadhar = ((EditText) getActivity().findViewById(R.id.aadhar_user_input_otp)).getText().toString();
                    String son1 = "{ \"aadhaar-id\": \""+aadhar+"\", \"channel\":\"SMS\", \"location\": { \"type\": \"gps\", \"latitude\": \"73.2\", \"longitude\": \"22.3\", \"altitude\": \"0\" } }";

                    String[] inputArr = new String[] {
                            aadhar,
                            son1,
                    };

                    // Use return Value TODO GYANI!!
                    TextView t1 = (TextView) rootView.findViewById(R.id.out);
                    t1.setText(("Sending OTP."));

                    String res[] = new String[0];
                    try {
                        res = new RequestOTPTask().execute(aadhar, son1).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }


                    if (res[0] == "true"){
                        t1.setText("Sent!");
                    }
                    else{
                        t1.setText("Check Connectivity.");
                    }
                }
            });

            //confirms otp. if true then sets textview to 'otp entered correctly', 'incorrect*' otherwise.
            //to do parse json output.
            Button b2 = (Button) rootView.findViewById(R.id.check_otp_button);
            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("I am here", "YE");
                    //get the various required fields
                    String otp = ((EditText) rootView.findViewById(R.id.enter_otp)).getText().toString();
                    String aadhar = ((EditText) rootView.findViewById(R.id.aadhar_user_input_otp)).getText().toString();
                    String son1 = "{\"consent\":\"Y\", \"auth-capture-request\":{\"aadhaar-id\": \""+aadhar+"\", \"modality\":\"otp\", \"otp\":\""+otp+"\", \"device-id\":\"MAC\", \"certificate-type\":\"preprod\",  \"location\": { \"type\": \"gps\", \"latitude\": \"73.2\", \"longitude\": \"22.3\", \"altitude\": \"0\" } } }";
                    TextView t1 = (TextView) rootView.findViewById(R.id.out);


                    String[] inputArr = new String[] {
                            aadhar,
                            son1,
                    };

                    // Use return Value TODO GYANI!!
                    t1.setText(("Checking Password"));

                    String res[] = new String[7];
                    try {
                        res = new CheckOTPTask().execute(aadhar, son1).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }


                    if (res[6] == "true"){
                        t1.setText("Logged IN!");

                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("SIGNUP_DONE", true);
                        editor.commit();


                        // TODO add parse user: db here!!!



                        // Welcome new user!
                        Toast toast = Toast.makeText(getActivity(), "Welcome " + res[4], Toast.LENGTH_SHORT);
                        toast.show();

                        // Activity calling!!

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }
                    else{
                        t1.setText("Try Again!");
                    }
                }
            });


            // TODO:: <<<GYANI>>> add the code to send the otp request here

            return rootView;
        }

        public static class RequestOTPTask extends AsyncTask<String, Void, String[]> {

            @Override
            protected String[] doInBackground(String... params) {
                Log.d("I am here", "YE");
                //get the various required fields

                String son1 = params[1];

                Boolean otp_sent = false;
                DefaultHttpClient client = new DefaultHttpClient();
                HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

                //adding ssl capabilities
                SchemeRegistry registry = new SchemeRegistry();
                SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
                socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
                registry.register(new Scheme("https", socketFactory, 443));
                SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);

                DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());


                HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10000); //Timeout Limit



                HttpResponse response;
                //JSONObject json = new JSONObject();
//                    Log.d("I am here", "YE");
                try {
                    HttpPost post = new HttpPost("https://ac.khoslalabs.com/hackgate/hackathon/otp");
                    StringEntity se = new StringEntity(son1);
                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    post.setEntity(se);
                    response = httpClient.execute(post);

                    /*Checking response */
                    if(response!=null){
                        Log.d("RESPONSE", "FOUND");
                        ResponseHandler<String> handler = new BasicResponseHandler();
                        String body = handler.handleResponse(response);
                        Log.d("stream", body);
                        if(body.indexOf("success\":true") > 0) {
                            otp_sent= true;
                        }
                        // insteas of this I will introduce android toast!
//                        t1.setText("Details are correct");
                        Log.d("AsyncTask", "done done Details Correct");
                    }

                } catch(Exception e) {
                    e.printStackTrace();
                }
                if(otp_sent==true) {
//                    t1.setText("OTP succesfully sent.");
                    Log.d("AsyncTask", "OTP succesfully sent");
                    String result[] = new String[1];
                    result[0] = "true";
                    return result;
                }

                String result[] = new String[1];
                result[0] = "false";
                return result;

            }

        }

        public static class CheckOTPTask extends AsyncTask<String, Void, String[]> {
            @Override
            protected String[] doInBackground(String... params) {

                String son1 = params[1];
                Boolean otp_sent = false;


                DefaultHttpClient client = new DefaultHttpClient();
                HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

                //adding ssl capabilities
                SchemeRegistry registry = new SchemeRegistry();
                SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
                socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
                registry.register(new Scheme("https", socketFactory, 443));
                SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);

                DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());


                HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10000); //Timeout Limit


                String[] output = new String[7];
                HttpResponse response;
                //JSONObject json = new JSONObject();
//                    Log.d("I am here", "YE");
                try {
                    HttpPost post = new HttpPost("https://ac.khoslalabs.com/hackgate/hackathon/kyc/raw");
                    StringEntity se = new StringEntity(son1);
                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    post.setEntity(se);
                    response = httpClient.execute(post);

                    /*Checking response */
                    if(response!=null){
                        Log.d("RESPONSE", "FOUND");
                        ResponseHandler<String> handler = new BasicResponseHandler();
                        String body = handler.handleResponse(response);
                        Log.d("stream", body); //this contains the fat json response


                        if(body.indexOf("photo") > 0) {
                            otp_sent= true;
                            String goon =  "aadhaar";
                            String p;
//                            p = "\"aadhaar-id\":\"(\\d*)\",\"photo\":\"(.*)\",\"poi\":\\{\"name\":\"([a-zA-Z\\s]*)\",\"dob\":\"([0-9\\-]*)\",\"gender\":\"(\\w)\"";
//                            Log.d("pattern", p);
//                            Pattern r = Pattern.compile(p);
//                            Matcher m = r.matcher(body);
//                            if(m.find()) {
//                                output[0] = "" + m.group(0);
//                                output[1] = "" + m.group(1);
//                                output[2] = "" + m.group(2);
//                                output[3] = "" + m.group(3);
//                                output[4] = "" + m.group(4);
////                                output[5] = "" + m.group(5);
//                            }
                            JSONObject object = new JSONObject(body);
                            String syncresponse = object.getString("kyc");
                            JSONObject object2 = new JSONObject(syncresponse);
                            output[0]= object2.getString("aadhaar-id");
                            output[1]= object2.getString("photo");
                            String poi= object2.getString("poi");
                            JSONObject object21 = new JSONObject(poi);
                            output[2] = object21.getString("gender");
                            output[4] = object21.getString("name");
                            output[3] = object21.getString("dob");
                            String poa = object2.getString("poa");
                            object21 = new JSONObject(poa);
                            output[5] = object21.getString("pc");

                            for(int i = 0 ; i<=4 ; i++){
                                Log.d("JSON", output[i]);
                            }

                        }
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
                if(otp_sent==true) {
//                    t1.setText("OTP entered correctly.");
                    Log.d("AsyncTask", "Otp Entered correctly");
                    output[6] = "true";
                }
                else{
//                    t1.setText("Incorrect OTP entered.");
                    Log.d("AsyncTask", "Otp Entered was wrong");
                    output[6] = "false" ;
                }

                return output;
            }
        }
    }
}