package sg.edu.rp.c346.smsapp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btn, btn2;
    EditText etTo, etCon;
    BroadcastReceiver br = new MessageReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        this.registerReceiver(br, filter);

        checkPermission();

        btn = findViewById(R.id.button);
        btn2 = findViewById(R.id.buttonMsg);
        etTo = findViewById(R.id.editTextTo);
        etCon = findViewById(R.id.editTextContent);

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                SmsManager smsManager = SmsManager.getDefault();

                String numbers[] = etTo.getText().toString().split(",");

                for (String number : numbers ){
                        smsManager.sendTextMessage(number, null, etCon.getText().toString(), null, null);
                }
                Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();
                etCon.setText("");

            }
        });

        btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String textnum = etTo.getText().toString();
                Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.setData(Uri.parse("sms:" + textnum));
                smsIntent.putExtra("sms_body", etCon.getText().toString());
                startActivity(smsIntent);
            }
        });
    }

    private void checkPermission() {
        int permissionSendSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int permissionRecvSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS);
        if (permissionSendSMS != PackageManager.PERMISSION_GRANTED &&
                permissionRecvSMS != PackageManager.PERMISSION_GRANTED) {
            String[] permissionNeeded = new String[]{Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS};
            ActivityCompat.requestPermissions(this, permissionNeeded, 1);
        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        this.unregisterReceiver(br);
    }
}

