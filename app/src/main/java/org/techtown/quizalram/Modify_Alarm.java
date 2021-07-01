package org.techtown.quizalram;

import android.database.Cursor;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.techtown.quizalram.main.MainActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class Modify_Alarm extends Fragment {

    static TimePicker alarm_timepicker;
    static Calendar calendar;

    TextView add_Text, modify_Text, cancel_Text;

    EditText edit_label;

    Switch switch1;
    LinearLayout dayLayout;

    TextView allday, weekday, weekend;

    TextView sun, mon, tue, wed, thur, fri, satur;
    int sun_num = 0;
    int mon_num = 0;
    int tue_num = 0;
    int wed_num = 0;
    int thur_num = 0;
    int fri_num = 0;
    int satur_num = 0;


    ArrayList<String> main_days = new ArrayList<String>();
    ArrayList<String> cancle_days = new ArrayList<String>();

    int id, hour, minute;
    String label, repeate, ring, vibrator, string62, string63;
    boolean turn;

    ViewGroup container;

    Spinner alarm_spinner;
    ArrayList<String> ring_titles = new ArrayList<>();
    ArrayList<Integer> ring_id = new ArrayList<>();
    RingtoneManager ring_manager;

    CheckBox vibrator_on, vibrator_off;

    private InterstitialAd mInterstitialAd;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.alarm_modify, container, false);

        //광고
        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-8631957304793435/4408515737");
//        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");  //테스트
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        add_Text = rootView.findViewById(R.id.add_Button);
        modify_Text = rootView.findViewById(R.id.modify_Button);
        cancel_Text = rootView.findViewById(R.id.cancel_Button);

        edit_label = rootView.findViewById(R.id.alarm_label);
        dayLayout = rootView.findViewById(R.id.dayLayout);

        alarm_spinner = rootView.findViewById(R.id.alarm_spinner);

        ring_manager = new RingtoneManager(getContext());

        Cursor ring_cursor = ring_manager.getCursor();

        string62 = getString(R.string.string62);
        string63 = getString(R.string.string63);

        ring_titles.add(string62);
        ring_titles.add(string63);

        ring_id.add(-1);
        ring_id.add(null);

        for (int i = 0; i < ring_cursor.getCount(); i++){
            ring_cursor.moveToNext();
            ring_id.add(ring_cursor.getInt(0));
            ring_titles.add(ring_cursor.getString(1));

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, ring_titles);
        alarm_spinner.setAdapter(adapter);
        alarm_spinner.setSelection(0);

        switch1 = rootView.findViewById(R.id.switch1);

        MainActivity.value = true;
        this.container = container;

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dayLayout.setLayoutParams(params1);
                    for (int i = 0; i < 7; i++){
                        main_days.add(null);
                        cancle_days.add(null);
                    }
                } else {
                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
                    dayLayout.setLayoutParams(params2);
                    main_days.clear();
                }
            }
        });

        sun = rootView.findViewById(R.id.sun_day);
        mon = rootView.findViewById(R.id.mon_day);
        tue = rootView.findViewById(R.id.tue_day);
        wed = rootView.findViewById(R.id.wed_day);
        thur = rootView.findViewById(R.id.thur_day);
        fri = rootView.findViewById(R.id.fri_day);
        satur = rootView.findViewById(R.id.satur_day);

        sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sun_num == 0){
                    sun_num = 1;
                    sun.setBackgroundResource(R.drawable.stroke2);
                    sun.setTextColor(Color.parseColor("#FFFFFF"));
                    main_days.set(0, "일");
                } else if (sun_num == 1){
                    sun_num = 0;
                    sun.setBackgroundResource(R.drawable.stroke);
                    sun.setTextColor(Color.parseColor("#000000"));
                    main_days.set(0, null);
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
                    main_days.set(1, "월");
                } else if (mon_num == 1){
                    mon_num = 0;
                    mon.setBackgroundResource(R.drawable.stroke);
                    mon.setTextColor(Color.parseColor("#000000"));
                    main_days.set(1, null);
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
                    main_days.set(2, "화");
                } else if (tue_num == 1){
                    tue_num = 0;
                    tue.setBackgroundResource(R.drawable.stroke);
                    tue.setTextColor(Color.parseColor("#000000"));
                    main_days.set(2, null);
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
                    main_days.set(3, "수");
                } else if (wed_num == 1){
                    wed_num = 0;
                    wed.setBackgroundResource(R.drawable.stroke);
                    wed.setTextColor(Color.parseColor("#000000"));
                    main_days.set(3, null);
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
                    main_days.set(4, "목");
                } else if (thur_num == 1){
                    thur_num = 0;
                    thur.setBackgroundResource(R.drawable.stroke);
                    thur.setTextColor(Color.parseColor("#000000"));
                    main_days.set(4, null);
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
                    main_days.set(5, "금");
                } else if (fri_num == 1){
                    fri_num = 0;
                    fri.setBackgroundResource(R.drawable.stroke);
                    fri.setTextColor(Color.parseColor("#000000"));
                    main_days.set(5, null);
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
                    main_days.set(6, "토");
                } else if (satur_num == 1){
                    satur_num = 0;
                    satur.setBackgroundResource(R.drawable.stroke);
                    satur.setTextColor(Color.parseColor("#000000"));
                    main_days.set(6, null);
                }
            }
        });

        allday = rootView.findViewById(R.id.all_text);
        weekday = rootView.findViewById(R.id.noweekend_text);
        weekend = rootView.findViewById(R.id.weekend_text);

        allday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sun_num = 1;
                sun.setBackgroundResource(R.drawable.stroke2);
                sun.setTextColor(Color.parseColor("#FFFFFF"));
                main_days.set(0, "일");
                mon_num = 1;
                mon.setBackgroundResource(R.drawable.stroke2);
                mon.setTextColor(Color.parseColor("#FFFFFF"));
                main_days.set(1, "월");
                tue_num = 1;
                tue.setBackgroundResource(R.drawable.stroke2);
                tue.setTextColor(Color.parseColor("#FFFFFF"));
                main_days.set(2, "화");
                wed_num = 1;
                wed.setBackgroundResource(R.drawable.stroke2);
                wed.setTextColor(Color.parseColor("#FFFFFF"));
                main_days.set(3, "수");
                thur_num = 1;
                thur.setBackgroundResource(R.drawable.stroke2);
                thur.setTextColor(Color.parseColor("#FFFFFF"));
                main_days.set(4, "목");
                fri_num = 1;
                fri.setBackgroundResource(R.drawable.stroke2);
                fri.setTextColor(Color.parseColor("#FFFFFF"));
                main_days.set(5, "금");
                satur_num = 1;
                satur.setBackgroundResource(R.drawable.stroke2);
                satur.setTextColor(Color.parseColor("#FFFFFF"));
                main_days.set(6, "토");
            }
        });
        weekday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mon_num = 1;
                mon.setBackgroundResource(R.drawable.stroke2);
                mon.setTextColor(Color.parseColor("#FFFFFF"));
                main_days.set(1, "월");
                tue_num = 1;
                tue.setBackgroundResource(R.drawable.stroke2);
                tue.setTextColor(Color.parseColor("#FFFFFF"));
                main_days.set(2, "화");
                wed_num = 1;
                wed.setBackgroundResource(R.drawable.stroke2);
                wed.setTextColor(Color.parseColor("#FFFFFF"));
                main_days.set(3, "수");
                thur_num = 1;
                thur.setBackgroundResource(R.drawable.stroke2);
                thur.setTextColor(Color.parseColor("#FFFFFF"));
                main_days.set(4, "목");
                fri_num = 1;
                fri.setBackgroundResource(R.drawable.stroke2);
                fri.setTextColor(Color.parseColor("#FFFFFF"));
                main_days.set(5, "금");
                satur_num = 0;
                satur.setBackgroundResource(R.drawable.stroke);
                satur.setTextColor(Color.parseColor("#000000"));
                main_days.set(6, null);
                sun_num = 0;
                sun.setBackgroundResource(R.drawable.stroke);
                sun.setTextColor(Color.parseColor("#000000"));
                main_days.set(0, null);
            }
        });
        weekend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sun_num = 1;
                sun.setBackgroundResource(R.drawable.stroke2);
                sun.setTextColor(Color.parseColor("#FFFFFF"));
                main_days.set(0, "일");
                mon_num = 0;
                mon.setBackgroundResource(R.drawable.stroke);
                mon.setTextColor(Color.parseColor("#000000"));
                main_days.set(1, null);
                tue_num = 0;
                tue.setBackgroundResource(R.drawable.stroke);
                tue.setTextColor(Color.parseColor("#000000"));
                main_days.set(2, null);
                wed_num = 0;
                wed.setBackgroundResource(R.drawable.stroke);
                wed.setTextColor(Color.parseColor("#000000"));
                main_days.set(3, null);
                thur_num = 0;
                thur.setBackgroundResource(R.drawable.stroke);
                thur.setTextColor(Color.parseColor("#000000"));
                main_days.set(4, null);
                fri_num = 0;
                fri.setBackgroundResource(R.drawable.stroke);
                fri.setTextColor(Color.parseColor("#000000"));
                main_days.set(5, null);
                satur_num = 1;
                satur.setBackgroundResource(R.drawable.stroke2);
                satur.setTextColor(Color.parseColor("#FFFFFF"));
                main_days.set(6, "토");
            }
        });

        alarm_timepicker = rootView.findViewById(R.id.time_picker);

        calendar = Calendar.getInstance();

        alarm_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    ring = "content://settings/system/ringtone";
                } else if (position == 1){
                    ring = null;
                } else {
                    ring = "content://media/internal/audio/media" + ring_id.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        vibrator_on = rootView.findViewById(R.id.vibrator_on);
        vibrator_off = rootView.findViewById(R.id.vibrator_off);

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

        add_Text.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                hour = alarm_timepicker.getHour();
                minute = alarm_timepicker.getMinute();
                label = edit_label.getText().toString();
                String day = MainActivity.ArrayToString(main_days);
                Toast.makeText(getContext(),"Alarm 추가 " + hour + "시 " + minute + "분",Toast.LENGTH_SHORT).show();

                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);

                MainActivity.saveAlarm(hour, minute, label, day, ring, vibrator);
                AlarmBase.alarmtlqkf(id, calendar, label,true, day, ring, vibrator);
                MainActivity.loadAlarmListData();

                getFragmentManager().beginTransaction().remove(MainActivity.modify_frag).commit();
                getActivity().findViewById(R.id.fragment_layout1).setVisibility(View.INVISIBLE);

                if (MainActivity.adapter.getItemCount() % 1 == 0) {
                    mInterstitialAd.show();
                }

            }
        });



        modify_Text.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);

                AlarmBase.alarmtlqkf(id, calendar, label, false, MainActivity.ArrayToString(cancle_days), ring, vibrator);

                hour = alarm_timepicker.getHour();
                minute = alarm_timepicker.getMinute();
                label = edit_label.getText().toString();
                String day = MainActivity.ArrayToString(main_days);
                Toast.makeText(getContext(),"Alarm 수정 " + hour + "시 " + minute + "분",Toast.LENGTH_SHORT).show();

                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);

                MainActivity.modityAlarm(id, hour, minute, label, day, ring, vibrator);
                AlarmBase.alarmtlqkf(id, calendar, label,true, day, ring, vibrator);


                getFragmentManager().beginTransaction().remove(MainActivity.modify_frag).commit();
                getActivity().findViewById(R.id.fragment_layout1).setVisibility(View.INVISIBLE);

            }
        });

        cancel_Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFragmentManager().beginTransaction().remove(MainActivity.modify_frag).commit();
                getActivity().findViewById(R.id.fragment_layout1).setVisibility(View.INVISIBLE);

            }
        });

        return rootView;

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            checkBundle(getArguments());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkBundle(Bundle bundle){
        if (bundle != null){
            id = bundle.getInt("id");
            hour = bundle.getInt("hour");
            minute = bundle.getInt("minute");
            label = bundle.getString("label");
            repeate = bundle.getString("repeate");
            ring = bundle.getString("ring");
            vibrator = bundle.getString("vibrator");
            turn = bundle.getBoolean("turn");


            alarm_timepicker.setHour(hour);
            alarm_timepicker.setMinute(minute);
            edit_label.setText(label);

            ArrayList<String> days = new ArrayList<String>();

            if (ring.equals("content://settings/system/ringtone")){
                alarm_spinner.setSelection(0);
            } else if (ring.equals(null)){
                alarm_spinner.setSelection(1);
            } else {
                for (int i = 0; i < ring_id.size(); i++){
                    if (ring.equals("content://media/internal/audio/media" + ring_id.get(i))){
                        alarm_spinner.setSelection(i);
                    }
                }
            }

            if (vibrator.equals("on")){
                vibrator_on.setChecked(true);
            } else {
                vibrator_off.setChecked(true);
            }

            days = AlarmBase.returnRepeateDay(repeate);

            int setDays = 0;
            for (int i = 0; i < days.size(); i++){
                if (days.get(i) != null){
                    setDays = 1;
                }
            }

            if (setDays == 1){
                switch1.setChecked(turn);

                for (int i = 0; i < days.size(); i++){
                    switch (days.get(i)){
                        case "월" :
                            mon_num = 1;
                            mon.setBackgroundResource(R.drawable.stroke2);
                            mon.setTextColor(Color.parseColor("#FFFFFF"));
                            main_days.set(1, "월");
                            cancle_days.set(1, "월");
                            break;
                        case "화" :
                            tue_num = 1;
                            tue.setBackgroundResource(R.drawable.stroke2);
                            tue.setTextColor(Color.parseColor("#FFFFFF"));
                            main_days.set(2, "화");
                            cancle_days.set(2, "화");
                            break;
                        case "수" :
                            wed_num = 1;
                            wed.setBackgroundResource(R.drawable.stroke2);
                            wed.setTextColor(Color.parseColor("#FFFFFF"));
                            main_days.set(3, "수");
                            cancle_days.set(3, "수");
                            break;
                        case "목" :
                            thur_num = 1;
                            thur.setBackgroundResource(R.drawable.stroke2);
                            thur.setTextColor(Color.parseColor("#FFFFFF"));
                            main_days.set(4, "목");
                            cancle_days.set(4, "목");
                            break;
                        case "금" :
                            fri_num = 1;
                            fri.setBackgroundResource(R.drawable.stroke2);
                            fri.setTextColor(Color.parseColor("#FFFFFF"));
                            main_days.set(5, "금");
                            cancle_days.set(5, "금");
                            break;
                        case "토" :
                            satur_num = 1;
                            satur.setBackgroundResource(R.drawable.stroke2);
                            satur.setTextColor(Color.parseColor("#FFFFFF"));
                            main_days.set(6, "토");
                            cancle_days.set(6, "토");
                            break;
                        case "일" :
                            sun_num = 1;
                            sun.setBackgroundResource(R.drawable.stroke2);
                            sun.setTextColor(Color.parseColor("#FFFFFF"));
                            main_days.set(0, "일");
                            cancle_days.set(0, "일");
                            break;
                    }
                    Log.d("1818", "무슨 요일? : " + days.get(i));
                }
            }
        }
    }
}
