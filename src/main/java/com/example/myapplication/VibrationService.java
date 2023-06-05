package com.example.myapplication;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import androidx.core.app.NotificationCompat;

public class VibrationService extends Service {


    private static final String CHANNEL_ID = "VibrationChannel";
    private static final int NOTIFICATION_ID = 1;
    private static final int VIBRATION_REQUEST_CODE = 1;
    public static final String ACTION_START_VIBRATION = "ACTION_START_VIBRATION";
    public static final String ACTION_STOP_VIBRATION = "ACTION_STOP_VIBRATION";
    public static final String EXTRA_VIBRATION_TIME = "EXTRA_VIBRATION_TIME";

    private Vibrator vibrator;

    @Override
    public void onCreate() {
        super.onCreate();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case ACTION_START_VIBRATION:
                        long vibrationTime = intent.getLongExtra(EXTRA_VIBRATION_TIME, 0);
                        startVibration(vibrationTime);
                        break;
                    case ACTION_STOP_VIBRATION:
                        stopVibration();
                        break;
                }
            }
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startVibration(long milliseconds) {
        if (vibrator != null && vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                VibrationEffect vibrationEffect = VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE);
                vibrator.vibrate(vibrationEffect);
            } else {
                // Deprecated in API 26 (Android 8.0 Oreo), but still works
                vibrator.vibrate(milliseconds);
            }
            showNotification();
        }
    }

    private void stopVibration() {
        if (vibrator != null) {
            vibrator.cancel();
            stopForeground(true);
            stopSelf();
        }
    }

    private void showNotification() {
        Intent stopIntent = new Intent(this, VibrationService.class);
        stopIntent.setAction(ACTION_STOP_VIBRATION);
        PendingIntent stopPendingIntent = PendingIntent.getService(this, VIBRATION_REQUEST_CODE, stopIntent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Vibration Service")
                .setContentText("Vibrating...")
                //.setSmallIcon(R.drawable.new_post)
                .setContentIntent(stopPendingIntent)
                .build();

        startForeground(NOTIFICATION_ID, notification);
    }
}
