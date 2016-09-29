package vuki.com.pushnotifications;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import vuki.com.pushnotifications.helpers.NotesHelper;

/**
 * Created by mvukosav on 5.8.2016..
 */
public class AppFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private final String TAG = getClass().getSimpleName();

    @Override
    public void onTokenRefresh() {
        //Get updated InstanceId token
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        NotesHelper.log( TAG, "Refresh token: " + refreshToken );
        /*If you want to send messages to this application or manage this app subscriptions on the server side,
          send the InstanceId token to app server.
        */

        sendRegistrationToServer( refreshToken );
    }

    /*
    Send registration token to server
     */
    private void sendRegistrationToServer( String refreshToken ) {

    }
}
