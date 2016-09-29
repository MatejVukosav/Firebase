package vuki.com.pushnotifications.network;

import android.os.SystemClock;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import vuki.com.pushnotifications.helpers.NotesHelper;

/**
 * Created by mvukosav on 27.4.2016..
 */
public class LoggingInterceptor2 implements Interceptor {

    private static final String TAG = "OkHttp";

    public enum LogLevel {
        NONE,
        BASIC,
        HEADERS,
        FULL
    }

    private final LogLevel logLevel;

    public LoggingInterceptor2( LogLevel logLevel ) {
        this.logLevel = logLevel;
    }

    @Override
    public Response intercept( Chain chain ) throws IOException {
        Request request = chain.request();

        Request.Builder requestBuilder = request.newBuilder().header( "Content-Type", "application/json" );
        request = requestBuilder.build();

        long startMs = SystemClock.currentThreadTimeMillis();
        if( logLevel.ordinal() >= LogLevel.BASIC.ordinal() ) {
            NotesHelper.logMessage( TAG, String.format( "---> %s %s", request.method(), request.url() ) );
        }
        if( logLevel.ordinal() >= LogLevel.HEADERS.ordinal() ) {
            NotesHelper.logMessage( TAG, prettyHeaders( request.headers() ) );
        }
        if( logLevel.ordinal() >= LogLevel.FULL.ordinal() && request.body() != null ) {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            try {
                copy.body().writeTo( buffer );
                final String bodyStr = buffer.readUtf8();
                NotesHelper.logMessage( TAG, bodyStr );
            } finally {
                buffer.close();
            }
        }

        Response response = chain.proceed( request );

        long tookMs = SystemClock.currentThreadTimeMillis() - startMs;
        if( logLevel.ordinal() >= LogLevel.BASIC.ordinal() ) {
            NotesHelper.logMessage( TAG, String.format( "<--- (%s) for %s %s in %sms", response.code(), request.method(), response.request().url(), tookMs ) );
        }
        if( logLevel.ordinal() >= LogLevel.HEADERS.ordinal() ) {
            NotesHelper.logMessage( TAG, prettyHeaders( response.headers() ) );
        }
        if( logLevel.ordinal() >= LogLevel.FULL.ordinal() && response.body() != null ) {
            final ResponseBody responseBody = response.body();
            final String responseBodyString = responseBody.string();
            NotesHelper.logMessage( TAG, responseBodyString );

            // response body was consumed, replace it with a copy
            final ResponseBody bodyCopy = ResponseBody.create( responseBody.contentType(), responseBodyString );
            response = response.newBuilder().body( bodyCopy ).build();
        }

        return response;
    }

    private String prettyHeaders( Headers headers ) {
        if( headers.size() == 0 ) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        builder.append( "  Headers:" );

        for( int i = 0; i < headers.size(); i++ ) {
            builder.append( "\n    " ).append( headers.name( i ) ).append( ": " ).append( headers.value( i ) );
        }

        return builder.toString();
    }

}
