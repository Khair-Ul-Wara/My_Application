//package com.example.myapplication;
//
//import android.app.AlarmManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import java.util.Date;
//
//public class NotificationHelper {
//    public static void scheduleTaskNotification(Context context, String taskId, String taskTitle, Date dueDate) {
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(context, TaskNotificationReceiver.class);
//        intent.putExtra("task_id", taskId);
//        intent.putExtra("task_title", taskTitle);
//
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(
//                context,
//                taskId.hashCode(),
//                intent,
//                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
//        );
//
//        alarmManager.setExact(
//                AlarmManager.RTC_WAKEUP,
//                dueDate.getTime(),
//                pendingIntent
//        );
//    }
//
//    public static void cancelTaskNotification(Context context, String taskId) {
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(context, TaskNotificationReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(
//                context,
//                taskId.hashCode(),
//                intent,
//                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
//        );
//        alarmManager.cancel(pendingIntent);
//    }
//}
package com.example.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import java.util.Date;

public class NotificationHelper {
    public static void scheduleTaskNotification(Context context, String taskId, String taskTitle, Date dueDate) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, TaskNotificationReceiver.class);
        intent.putExtra("task_id", taskId);
        intent.putExtra("task_title", taskTitle);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                taskId.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    dueDate.getTime(),
                    pendingIntent
            );
        } else {
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    dueDate.getTime(),
                    pendingIntent
            );
        }
    }

    public static void cancelTaskNotification(Context context, String taskId) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, TaskNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                taskId.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        alarmManager.cancel(pendingIntent);
    }
}