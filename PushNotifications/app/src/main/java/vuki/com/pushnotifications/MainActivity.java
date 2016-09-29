package vuki.com.pushnotifications;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import vuki.com.pushnotifications.databinding.ActivityMainBinding;
import vuki.com.pushnotifications.helpers.NotesHelper;

public class MainActivity extends AppCompatActivity {
    private final int NO_GOOGLE_PLAY_SERVICE_AVAILABLE = 3; //for activity on result
    ActivityMainBinding binding;
    private final String TAG = getClass().getSimpleName();

    FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        binding = DataBindingUtil.setContentView( this, R.layout.activity_main );
        isGooglePlayServicesAvailable( this );

        firebaseAnalytics = FirebaseAnalytics.getInstance( this );

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "3");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Orhideja");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        firebaseAnalytics.logEvent( FirebaseAnalytics.Event.LOGIN, bundle );

        final String token = FirebaseInstanceId.getInstance().getToken() != null ? FirebaseInstanceId.getInstance().getToken() : "";
        NotesHelper.log( TAG, "Firebase token: " + token );

        firebaseAnalytics.setUserId( token );

        binding.fcmRegToken.setText( token );
        binding.sendUpstreamMsg.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                FirebaseMessaging fm = FirebaseMessaging.getInstance();
                RemoteMessage remoteMessage = new RemoteMessage.Builder( token )
                        .setMessageType( "high" )
                        .addData( "city", "grad" )
                        .addData( "name", "android" )
                        .build();
                fm.send( remoteMessage );
            }
        } );

        binding.btnActivity2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                startActivity( new Intent( MainActivity.this, SecondActivity.class ) );
                firebaseAnalytics.setUserProperty( "activity_change", "Second Activity" );
            }
        } );
        binding.btnActivity3.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                startActivity( new Intent( MainActivity.this, ThirdActivity.class ) );
                firebaseAnalytics.setUserProperty( "activity_change", "Third Activity" );
            }
        } );

        getNotificationData();
    }

    private void getNotificationData() {
        Bundle bundle = getIntent().getExtras();
        if( bundle != null ) {
            for( String key : bundle.keySet() ) {
                String value = bundle.getString( key );
                NotesHelper.logMessage( "main", "Key: " + key + " Value: " + value );
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isGooglePlayServicesAvailable( this );
    }

    public boolean isGooglePlayServicesAvailable( Context context ) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable( context );
        if( resultCode != ConnectionResult.SUCCESS ) {
            //If user can resolve the problem
            if( apiAvailability.isUserResolvableError( resultCode ) ) {
                apiAvailability.getErrorDialog( this, resultCode, NO_GOOGLE_PLAY_SERVICE_AVAILABLE )
                        .show();
            } else {
                Toast.makeText( this, "This device is not supported", Toast.LENGTH_SHORT ).show();
                finish();
            }
            return false;
        }
        return true;
    }

}
