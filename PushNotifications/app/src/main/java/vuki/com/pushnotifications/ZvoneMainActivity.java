package vuki.com.pushnotifications;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;

import vuki.com.pushnotifications.databinding.ActivityZvoneMainBinding;
import vuki.com.pushnotifications.helpers.NotesHelper;
import vuki.com.pushnotifications.models.NotificationModel;

public class ZvoneMainActivity extends AppCompatActivity {

    private static final String TAG = "ZvoneMainActivity";
    private final int NO_GOOGLE_PLAY_SERVICE_AVAILABLE = 3; //for activity on result
    FirebaseAnalytics firebaseAnalytics;
    ActivityZvoneMainBinding binding;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        binding = DataBindingUtil.setContentView( this, R.layout.activity_zvone_main );

        isGooglePlayServicesAvailable( this );
        firebaseAnalytics = FirebaseAnalytics.getInstance( this );
        final String token = FirebaseInstanceId.getInstance().getToken() != null ? FirebaseInstanceId.getInstance().getToken() : "";
        firebaseAnalytics.setUserId( token );
        NotesHelper.log( TAG, "Firebase token: " + token );
        binding.fcmToken.setText( token );

        getNotificationData();

    }

    private void getNotificationData() {
        Bundle bundle = getIntent().getExtras();
        StringBuilder stringBuilder = new StringBuilder();
        if( bundle != null ) {
            NotificationModel notification = (NotificationModel) bundle.getSerializable( "data" );

            if( notification != null ) {
                stringBuilder.append( "From: " + notification.getFrom() + System.getProperty( "line.separator" ) );
                stringBuilder.append( "Title: " + notification.getTitle() + System.getProperty( "line.separator" ) );
                stringBuilder.append( "Message: " + notification.getMessage() + System.getProperty( "line.separator" ) );
                stringBuilder.append( "DataName: " + notification.getName() + System.getProperty( "line.separator" ) );
                stringBuilder.append( "ActionName: " + notification.getActionName() + System.getProperty( "line.separator" ) );
                stringBuilder.append( "Ticker: " + notification.getTicker() + System.getProperty( "line.separator" ) );
                stringBuilder.append( "SendTime: " + notification.getSentTime() + System.getProperty( "line.separator" ) );
            }

//        if( bundle != null ) {
//            for( String key : bundle.keySet() ) {
//                String value = bundle.getString( key );
//                String data = "Key: " + key + " Value: " + value;
//                stringBuilder.append( data );
//            }
//        }
        }
        binding.fcmData.setText( stringBuilder );
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
