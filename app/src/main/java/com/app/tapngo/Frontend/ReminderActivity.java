package com.app.tapngo.Frontend;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.tapngo.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReminderActivity extends AppCompatActivity{
    private Button btn_setreminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        btn_setreminder=findViewById(R.id.set_reminder);

        Button ext = findViewById(R.id.exit);

        ext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
        btn_setreminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTime();
            }
        });
    }

    private void selectTime() {
        try {

            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int mint = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minutes);

                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    String selectedTime = sdf.format(calendar.getTime());
                    String[] arr = selectedTime.split(":");

                    Toast.makeText(getApplicationContext(), selectedTime, Toast.LENGTH_SHORT).show();
                    int alarmHour = Integer.parseInt(arr[0]);
                    int alarmMinute = Integer.parseInt(arr[1]);

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());

                    calendar.set(Calendar.HOUR_OF_DAY, alarmHour);
                    calendar.set(Calendar.MINUTE, alarmMinute);
                    calendar.set(Calendar.SECOND, 0);

//                    // Check if the alarm time is in the past, if so, add one day to it
                    if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                        calendar.add(Calendar.DAY_OF_YEAR, 1);
                    }
//
                    Intent notificationIntent = new Intent(getApplicationContext(), EventLocalNotificationReceiver.class);
                    final int id = (int) System.currentTimeMillis();
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(ReminderActivity.this, id, notificationIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_MUTABLE);

                    long intervalMillis = AlarmManager.INTERVAL_DAY; // Repeat every day

                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    assert alarmManager != null;

                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intervalMillis, pendingIntent);

                    Toast.makeText(getApplicationContext(), "Daily alarm added at: "+selectedTime, Toast.LENGTH_SHORT).show();

                }
            }, hour, mint, true);
            timePicker.setTitle("Choose Time");
            timePicker.show();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}