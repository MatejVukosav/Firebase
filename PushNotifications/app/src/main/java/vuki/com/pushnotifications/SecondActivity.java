package vuki.com.pushnotifications;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_second );

        FirebaseMessaging.getInstance().subscribeToTopic( AppFirebaseMessagingService.SUBSCRIPTION_LIVE );
    }
}
