package project.oqha.wowo;

/**
 * Created by oqha on 7/14/15.
 */
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ForegroundService extends Service {
    private static final String LOG_TAG = "OqhaJect";


    public static final int SOURCE_PORT = 2222;
    public static final String DESTINATION_HOST = "10.19.19.19";
    public static final int DESTINATION_PORT = 8080;

    public void oject(String zz){

    Thread thread = new Thread(){
        @Override
        public void run() {
            ServerSocket serverSocket =
                    null;
            try {
                serverSocket = new ServerSocket(SOURCE_PORT);

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    ClientThread clientThread = new ClientThread(clientSocket);
                    clientThread.start();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            }
    };
        if (zz.equals("mulai")){
            thread.start();
        }
        else {
            thread.stop();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Engine On");
            Intent notificationIntent = new Intent(this, MainActivity.class);
            notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);


            Bitmap icon = BitmapFactory.decodeResource(getResources(),
                    R.drawable.ic_launcher);

            Notification notification = new NotificationCompat.Builder(this)
                    .setContentTitle("OJect - Indosat")
                    .setTicker("OJect - Indosat Started")
                    .setContentText("berjalan mengarungi 200 ok")
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setLargeIcon(
                            Bitmap.createScaledBitmap(icon, 128, 128, false))
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .build();
            startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                    notification);


            oject("mulai");

        } else if (intent.getAction().equals(
                Constants.ACTION.STOPFOREGROUND_ACTION)) {

            ClientThread.interrupted();
            stopForeground(true);
            stopSelf();
            
            System.exit(0);

        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "service di tutup");
    }

    @Override
    public IBinder onBind(Intent intent) {
// Used only in case of bound services.
        return null;
    }
}