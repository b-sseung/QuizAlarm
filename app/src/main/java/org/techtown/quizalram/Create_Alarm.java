package org.techtown.quizalram;

import android.app.AlarmManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.Calendar;

public class Create_Alarm extends AppCompatActivity {

    TimePicker alarm_timepicker;
    static Calendar calendar;

    TextView add_Text;
    TextView cancel_Text;

    EditText edit_label;

    Switch switch1;
    LinearLayout dayLayout;

    TextView allday;
    TextView weekday;
    TextView weekend;

    TextView sun;
    int sun_num = 0;
    TextView mon;
    int mon_num = 0;
    TextView tue;
    int tue_num = 0;
    TextView wed;
    int wed_num = 0;
    TextView thur;
    int thur_num = 0;
    TextView fri;
    int fri_num = 0;
    TextView satur;
    int satur_num = 0;

    ArrayList<String> days = new ArrayList<String>();

    Spinner alarm_spinner;
    ArrayList<String> ring_titles = new ArrayList<>();
    ArrayList<Integer> ring_id = new ArrayList<>();
    RingtoneManager ring_manager;
    String ring, string62, string63;

    CheckBox vibrator_on;
    CheckBox vibrator_off;
    String vibrator;

    private View 	decorView;
    private int	uiOption;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_create);

        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
//            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        string62 = getString(R.string.string62);
        string63 = getString(R.string.string63);

        add_Text = findViewById(R.id.add_Button);
        cancel_Text = findViewById(R.id.cancel_Button);

        edit_label = findViewById(R.id.alarm_label);
        dayLayout = findViewById(R.id.dayLayout);

        switch1 = findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        dayLayout.setLayoutParams(params1);
                        for (int i = 0; i < 7; i++){
                            days.add(null);
                        }
                } else {
                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
                    dayLayout.setLayoutParams(params2);
                    days.clear();
                }
            }
        });

        alarm_spinner = findViewById(R.id.alarm_spinner);

        ring_manager = new RingtoneManager(getApplicationContext());
        Cursor ring_cursor = ring_manager.getCursor();

        ring_titles.add(string62);
        ring_titles.add(string63);

        ring_id.add(-1);
        ring_id.add(null);


        for (int i = 0; i < ring_cursor.getCount(); i++){
            ring_cursor.moveToNext();
            ring_id.add(ring_cursor.getInt(0));
            ring_titles.add(ring_cursor.getString(1));

//            Log.d("1818", ring_cursor.getInt(0) + ", " + ring_cursor.getString(1)
//                    + ", " + ring_cursor.getString(2) + ", " + ring_cursor.getString(3));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, ring_titles);
        alarm_spinner.setAdapter(adapter);
        alarm_spinner.setSelection(0);

        vibrator_on = findViewById(R.id.vibrator_on);
        vibrator_off = findViewById(R.id.vibrator_off);

        vibrator_off.setChecked(true);

        vibrator_on.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    vibrator_off.setChecked(false);
                    vibrator = "on";
                }
            }
        });
        vibrator_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    vibrator = "off";
                    vibrator_on.setChecked(false);
                }
            }
        });

        sun = findViewById(R.id.sun_day);
        mon = findViewById(R.id.mon_day);
        tue = findViewById(R.id.tue_day);
        wed = findViewById(R.id.wed_day);
        thur = findViewById(R.id.thur_day);
        fri = findViewById(R.id.fri_day);
        satur = findViewById(R.id.satur_day);

        sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sun_num == 0){
                    sun_num = 1;
                    sun.setBackgroundResource(R.drawable.stroke2);
                    sun.setTextColor(Color.parseColor("#FFFFFF"));
                    days.set(0, "일");
                } else if (sun_num == 1){
                    sun_num = 0;
                    sun.setBackgroundResource(R.drawable.stroke);
                    sun.setTextColor(Color.parseColor("#000000"));
                    days.set(0, null);
                }
            }
        });
        mon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mon_num == 0){
                    mon_num = 1;
                    mon.setBackgroundResource(R.drawable.stroke2);
                    mon.setTextColor(Color.parseColor("#FFFFFF"));
                    days.set(1, "월");
                } else if (mon_num == 1){
                    mon_num = 0;
                    mon.setBackgroundResource(R.drawable.stroke);
                    mon.setTextColor(Color.parseColor("#000000"));
                    days.set(1, null);
                }
            }
        });
        tue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tue_num == 0){
                    tue_num = 1;
                    tue.setBackgroundResource(R.drawable.stroke2);
                    tue.setTextColor(Color.parseColor("#FFFFFF"));
                    days.set(2, "화");
                } else if (tue_num == 1){
                    tue_num = 0;
                    tue.setBackgroundResource(R.drawable.stroke);
                    tue.setTextColor(Color.parseColor("#000000"));
                    days.set(2, null);
                }
            }
        });
        wed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wed_num == 0){
                    wed_num = 1;
                    wed.setBackgroundResource(R.drawable.stroke2);
                    wed.setTextColor(Color.parseColor("#FFFFFF"));
                    days.set(3, "수");
                } else if (wed_num == 1){
                    wed_num = 0;
                    wed.setBackgroundResource(R.drawable.stroke);
                    wed.setTextColor(Color.parseColor("#000000"));
                    days.set(3, null);
                }
            }
        });
        thur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thur_num == 0){
                    thur_num = 1;
                    thur.setBackgroundResource(R.drawable.stroke2);
                    thur.setTextColor(Color.parseColor("#FFFFFF"));
                    days.set(4, "목");
                } else if (thur_num == 1){
                    thur_num = 0;
                    thur.setBackgroundResource(R.drawable.stroke);
                    thur.setTextColor(Color.parseColor("#000000"));
                    days.set(4, null);
                }
            }
        });
        fri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fri_num == 0){
                    fri_num = 1;
                    fri.setBackgroundResource(R.drawable.stroke2);
                    fri.setTextColor(Color.parseColor("#FFFFFF"));
                    days.set(5, "금");
                } else if (fri_num == 1){
                    fri_num = 0;
                    fri.setBackgroundResource(R.drawable.stroke);
                    fri.setTextColor(Color.parseColor("#000000"));
                    days.set(5, null);
                }
            }
        });
        satur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (satur_num == 0){
                    satur_num = 1;
                    satur.setBackgroundResource(R.drawable.stroke2);
                    satur.setTextColor(Color.parseColor("#FFFFFF"));
                    days.set(6, "토");
                } else if (satur_num == 1){
                    satur_num = 0;
                    satur.setBackgroundResource(R.drawable.stroke);
                    satur.setTextColor(Color.parseColor("#000000"));
                    days.set(6, null);
                }
            }
        });


        allday = findViewById(R.id.all_text);
        weekday = findViewById(R.id.noweekend_text);
        weekend = findViewById(R.id.weekend_text);

        allday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sun_num = 1;
                sun.setBackgroundResource(R.drawable.stroke2);
                sun.setTextColor(Color.parseColor("#FFFFFF"));
                days.set(0, "일");
                mon_num = 1;
                mon.setBackgroundResource(R.drawable.stroke2);
                mon.setTextColor(Color.parseColor("#FFFFFF"));
                days.set(1, "월");
                tue_num = 1;
                tue.setBackgroundResource(R.drawable.stroke2);
                tue.setTextColor(Color.parseColor("#FFFFFF"));
                days.set(2, "화");
                wed_num = 1;
                wed.setBackgroundResource(R.drawable.stroke2);
                wed.setTextColor(Color.parseColor("#FFFFFF"));
                days.set(3, "수");
                thur_num = 1;
                thur.setBackgroundResource(R.drawable.stroke2);
                thur.setTextColor(Color.parseColor("#FFFFFF"));
                days.set(4, "목");
                fri_num = 1;
                fri.setBackgroundResource(R.drawable.stroke2);
                fri.setTextColor(Color.parseColor("#FFFFFF"));
                days.set(5, "금");
                satur_num = 1;
                satur.setBackgroundResource(R.drawable.stroke2);
                satur.setTextColor(Color.parseColor("#FFFFFF"));
                days.set(6, "토");
            }
        });
        weekday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mon_num = 1;
                mon.setBackgroundResource(R.drawable.stroke2);
                mon.setTextColor(Color.parseColor("#FFFFFF"));
                days.set(1, "월");
                tue_num = 1;
                tue.setBackgroundResource(R.drawable.stroke2);
                tue.setTextColor(Color.parseColor("#FFFFFF"));
                days.set(2, "화");
                wed_num = 1;
                wed.setBackgroundResource(R.drawable.stroke2);
                wed.setTextColor(Color.parseColor("#FFFFFF"));
                days.set(3, "수");
                thur_num = 1;
                thur.setBackgroundResource(R.drawable.stroke2);
                thur.setTextColor(Color.parseColor("#FFFFFF"));
                days.set(4, "목");
                fri_num = 1;
                fri.setBackgroundResource(R.drawable.stroke2);
                fri.setTextColor(Color.parseColor("#FFFFFF"));
                days.set(5, "금");
                satur_num = 0;
                satur.setBackgroundResource(R.drawable.stroke);
                satur.setTextColor(Color.parseColor("#000000"));
                days.set(6, null);
                sun_num = 0;
                sun.setBackgroundResource(R.drawable.stroke);
                sun.setTextColor(Color.parseColor("#000000"));
                days.set(0, null);
            }
        });
        weekend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sun_num = 1;
                sun.setBackgroundResource(R.drawable.stroke2);
                sun.setTextColor(Color.parseColor("#FFFFFF"));
                days.set(0, "일");
                mon_num = 0;
                mon.setBackgroundResource(R.drawable.stroke);
                mon.setTextColor(Color.parseColor("#000000"));
                days.set(1, null);
                tue_num = 0;
                tue.setBackgroundResource(R.drawable.stroke);
                tue.setTextColor(Color.parseColor("#000000"));
                days.set(2, null);
                wed_num = 0;
                wed.setBackgroundResource(R.drawable.stroke);
                wed.setTextColor(Color.parseColor("#000000"));
                days.set(3, null);
                thur_num = 0;
                thur.setBackgroundResource(R.drawable.stroke);
                thur.setTextColor(Color.parseColor("#000000"));
                days.set(4, null);
                fri_num = 0;
                fri.setBackgroundResource(R.drawable.stroke);
                fri.setTextColor(Color.parseColor("#000000"));
                days.set(5, null);
                satur_num = 1;
                satur.setBackgroundResource(R.drawable.stroke2);
                satur.setTextColor(Color.parseColor("#FFFFFF"));
                days.set(6, "토");
            }
        });


        alarm_timepicker = findViewById(R.id.time_picker);

        calendar = Calendar.getInstance();

        add_Text.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                int hour = alarm_timepicker.getHour();
                int minute = alarm_timepicker.getMinute();
                Toast.makeText(getApplicationContext(),"Alarm 예정 " + hour + "시 " + minute + "분",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.putExtra("시간", hour);
                intent.putExtra("분", minute);
                intent.putExtra("요일", days);
                intent.putExtra("라벨", edit_label.getText().toString());
                intent.putExtra("벨", ring);
                intent.putExtra("진동", vibrator);
                setResult(101, intent);
                finish();



            }
        });

        cancel_Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        alarm_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0){
                    ring = "content://settings/system/ringtone";
//                    spinner_uri = Uri.parse("content://settings/system/ringtone");
                } else if (position == 1){
                    ring = null;
//                    spinner_uri = null;
                } else {
                    ring = "content://media/internal/audio/media/" + ring_id.get(position);
//                    spinner_uri = Uri.parse("content://media/internal/audio/media" + ring_id.get(position));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                alarm_spinner.setSelection(0);
                ring = "content://settings/system/ringtone";
//                spinner_uri = Uri.parse("content://settings/system/ringtone");
            }
        });

//        Button button = findViewById(R.id.button1);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
//                startActivityForResult(intent, 101);
//            }
//        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        // super.onWindowFocusChanged(hasFocus);

        if( hasFocus ) {
            decorView.setSystemUiVisibility( uiOption );
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
//        Log.d("1818", "선택한 것 : " + uri);
//    }
}
