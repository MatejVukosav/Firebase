package vuki.com.pushnotifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import vuki.com.pushnotifications.helpers.NotesHelper;
import vuki.com.pushnotifications.models.NotificationModel;

/**
 * Created by mvukosav on 5.8.2016..
 */
public class AppFirebaseMessagingService extends FirebaseMessagingService {
    private final String TAG = getClass().getSimpleName();
    private static final int REQUEST_CODE = 2;
    private static final int NOTIFICATION_ID = 2;

    public static final String SUBSCRIPTION_LIVE = "live";
    public static final String SUBSCRIPTION_COOL = "cool";

    @Override
    public void onMessageReceived( RemoteMessage remoteMessage ) {
        NotificationModel notificationModel = new NotificationModel();

        //Handle FCM messages here:
        NotesHelper.log( TAG, "From: " + remoteMessage.getFrom() );
        notificationModel.setFrom( remoteMessage.getFrom() );
        notificationModel.setSentTime( remoteMessage.getSentTime() );

        //notificationModel.setMessageType( remoteMessage.getMessageType() );

        //Check if message contains a data payload
        if( !remoteMessage.getData().isEmpty() ) {
            NotesHelper.log( TAG, "Message data payload: " + remoteMessage.getData() );

            notificationModel.setName( remoteMessage.getData().get( "name" ) );
            notificationModel.setCity( remoteMessage.getData().get( "city" ) );
            notificationModel.setTicker( remoteMessage.getData().get( "ticker" ) );
        }
        //Check if message contains a notification payload.
        if( remoteMessage.getNotification() != null ) {
            NotesHelper.log( TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody() );
            notificationModel.setMessage( remoteMessage.getNotification().getBody() );
            notificationModel.setTitle( remoteMessage.getNotification().getTitle() );
            notificationModel.setActionName( remoteMessage.getNotification().getClickAction() );
        }

        sendNotification( notificationModel );
    }

    private void sendNotification( NotificationModel notificationModel ) {
        Intent launchingIntent;
        //za zvonu
        launchingIntent = new Intent( this, ZvoneMainActivity.class );
        launchingIntent.putExtra( "data",notificationModel );

//        if( TextUtils.isEmpty( notificationModel.getActionName() ) ) {
//            launchingIntent = new Intent( this, ZvoneMainActivity.class );
//        } else {
//            launchingIntent = new Intent();
//            launchingIntent.setComponent( new ComponentName( this.getPackageName(), notificationModel.getActionName() ) );
//        }
        /** If set, and the activity being launched is already running in the current task,
         * then instead of launching a new instance of that activity,
         * all of the other activities on top of it will be closed and
         * this Intent will be delivered to the (now on top) old activity as a new Intent.
         * */
        launchingIntent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );

        /* PendingIntent.FLAG_ONE_SHOT - Flag indicating that this PendingIntent can be used only once.  */
        PendingIntent pendingIntent = PendingIntent.getActivity( this, REQUEST_CODE, launchingIntent, PendingIntent.FLAG_ONE_SHOT );

        int notificationColor;
        if( notificationModel.isPriorityHigh() ) {
            notificationColor = ContextCompat.getColor( this, R.color.colorAccent );
        } else {
            notificationColor = ContextCompat.getColor( this, R.color.colorPrimaryDark );
        }

//        // The stack builder object will contain an artificial back stack for the
//        // started Activity.
//        // This ensures that navigating backward from the Activity leads out of
//        // your application to the Home screen.
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create( this );
//        // Adds the back stack for the Intent (but not the Intent itself)
//        stackBuilder.addParentStack( MainActivity.class );
//        // Adds the Intent that starts the Activity to the top of the stack
//        stackBuilder.addNextIntent( launchingIntent );
//        PendingIntent resultPendingIntent =
//                stackBuilder.getPendingIntent(
//                        0,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );

        Uri notificationSoundUri = RingtoneManager.getDefaultUri( RingtoneManager.TYPE_NOTIFICATION );
        Notification notification = new NotificationCompat.Builder( this )
                .setSmallIcon( R.drawable.common_plus_signin_btn_icon_light )
                .setContentTitle( notificationModel.getTitle() )
                /** Setting this flag will make it so the notification is automatically
                 * canceled when the user clicks it in the panel.  The PendingIntent
                 * set with {@link #setDeleteIntent} will be broadcast when the notification
                 * is canceled. */
                .setContentText( notificationModel.getMessage() )
                .setAutoCancel( true )
                .setSound( notificationSoundUri )
                .setColor( notificationColor )
                /**Set the text that is displayed in the status bar when the notification first
                 * arrives.*/
                .setTicker( notificationModel.getTitle() )
                .setContentIntent( pendingIntent )
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
        notificationManager.notify( NOTIFICATION_ID, notification );
    }

}
