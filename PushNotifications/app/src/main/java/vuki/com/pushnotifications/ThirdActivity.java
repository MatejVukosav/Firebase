package vuki.com.pushnotifications;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.messaging.FirebaseMessaging;

public class ThirdActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_third );

        FirebaseMessaging.getInstance().subscribeToTopic( AppFirebaseMessagingService.SUBSCRIPTION_COOL );
    }
}
