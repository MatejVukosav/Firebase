package vuki.com.pushnotifications.models;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by mvukosav on 7.8.2016..
 */
public class NotificationModel implements Serializable {

    @SerializedName("title")
    String title;
    @SerializedName("msg")
    String message;
    @SerializedName("sent_time")
    long sentTime;
    @SerializedName("click_action")
    String actionName;

    @SerializedName("from")
    String from;

    @SerializedName("name")
    String name;
    @SerializedName("city")
    String city;
    @SerializedName("message_type")
    String messageType;
    @SerializedName("ticker")
    String ticker;

    boolean isPriorityHigh;

    public boolean isPriorityHigh() {
        if( !TextUtils.isEmpty( messageType ) && messageType.equals( "high" ) ) {
            isPriorityHigh = true;
        } else {
            isPriorityHigh = false;
        }
        return isPriorityHigh;
    }

    public String getCity() {
        return city;
    }

    public String getName() {
        return name;
    }

    public void setCity( String city ) {
        this.city = city;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom( String from ) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage( String message ) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public void setMessageType( String messageType ) {
        this.messageType = messageType;
    }

    public long getSentTime() {
        return sentTime;
    }

    public void setSentTime( long sentTime ) {
        this.sentTime = sentTime;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName( String actionName ) {
        this.actionName = actionName;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker( String ticker ) {
        this.ticker = ticker;
    }
}
