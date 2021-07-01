package org.techtown.quizalram.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import org.techtown.quizalram.AlarmBase;
import org.techtown.quizalram.Create_Alarm;
import org.techtown.quizalram.Modify_Alarm;
import org.techtown.quizalram.R;
import org.techtown.quizalram.db.AlarmDatabase;
import org.techtown.quizalram.recycle.Alarm_List;
import org.techtown.quizalram.recycle.Alarm_List_Adapter;
import org.techtown.quizalram.setting.Main_Setting;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements AutoPermissionsListener {

    public static RecyclerView recyclerView;
    TextView main_add_button;
    public static TextView main_cancle_button;

    BottomNavigationView navigation;

    public static Alarm_List_Adapter adapter;

    public static Context context;
    public static AlarmManager alarm_manager;

    public static FrameLayout fragment_layout1;
    public static FragmentManager fragmentManager;
    public static Modify_Alarm modify_frag = new Modify_Alarm();

    public static boolean value = true;

    public static View 	decorView;
    public static int	uiOption;

    Main_Quiz_Title main_quiz_fragment = new Main_Quiz_Title();
    Main_Setting main_set_fragment = new Main_Setting();
    FrameLayout main_quiz_frag_layout, main_set_frag_layout;


    public static FrameLayout main_base_layout;
    public static LinearLayout main_top_layout;
    public static String main_base_background, main_top_background, main_top_textcolor, main_base_textcolor;
    public static byte[] main_base_bitmap;

    TextView text_color_22;

    TextView[] main_top_views;
    TextView[] main_base_views;

    private AdView mAdView;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //하단 홈버튼 표시할지 말지
        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
//            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

       //SDK 초기화
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        //하단배너 광고 로드
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //전면광고 객체 만들기 + 로드
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-8631957304793435/4001892440");
//        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");  //테스트
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        this.context = getApplicationContext();


        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Alarm_List_Adapter();

        recyclerView.setAdapter(adapter);

        fragmentManager = getSupportFragmentManager();
        fragment_layout1 = findViewById(R.id.fragment_layout1);
        main_quiz_frag_layout = findViewById(R.id.main_quiz_fragment);
        main_set_frag_layout = findViewById(R.id.main_setting_fragment);

        main_base_layout = findViewById(R.id.main_base_layout);
        main_top_layout = findViewById(R.id.main_top_layout);

        main_add_button = findViewById(R.id.main_add_Button);
        main_cancle_button = findViewById(R.id.main_motify_Button);

        text_color_22 = findViewById(R.id.text_color_22);

        main_top_views = new TextView[]{main_add_button, main_cancle_button, text_color_22};
        main_base_views = new TextView[]{};

        Main_Setting.firstSetBackground();
        setMainBackground();
        loadAlarmListData();


        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bottom_main :
                        setMainBackground();
                        try{
                            if (main_quiz_frag_layout.getVisibility() == View.VISIBLE){
                                fragmentManager.beginTransaction().remove(main_quiz_fragment).commit();
                                main_quiz_frag_layout.setVisibility(View.INVISIBLE);
                                setMain_cancle_button(false);
                            } else if (main_set_frag_layout.getVisibility() == View.VISIBLE){
                                fragmentManager.beginTransaction().remove(main_set_fragment).commit();
                                main_set_frag_layout.setVisibility(View.INVISIBLE);
                                setMain_cancle_button(false);
                            } else {
                                setMain_cancle_button(false);
                            }
                        } catch (Exception ex){
                            Log.d("1818", "오류 : " + ex);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
//                            main_quiz_frag_layout.setVisibility(View.INVISIBLE);
//                            main_set_frag_layout.setVisibility(View.INVISIBLE);
//                            setMain_cancle_button(false);
                        }

                        return true;

                    case R.id.bottom_quiz :
                        try {
                            if (main_quiz_frag_layout.getVisibility() == View.INVISIBLE){
                                fragmentManager.beginTransaction().replace(R.id.main_quiz_fragment, main_quiz_fragment).commit();
                                main_quiz_frag_layout.setVisibility(View.VISIBLE);
                                if (main_set_frag_layout.getVisibility() == View.VISIBLE){
                                    main_set_frag_layout.setVisibility(View.INVISIBLE);
                                    fragmentManager.beginTransaction().remove(main_set_fragment).commit();
                                }
                            } else {
                                Main_Quiz_Title.setDelete_button(false);
                            }
                        } catch (Exception ex){
                            Log.d("1818", "오류 : " + ex);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("시발이게 뭐람", "퀴즈");
                            startActivity(intent);
                        }


                        return true;

                    case R.id.bottom_setting :
                        try{
                            if (main_set_frag_layout.getVisibility() == View.INVISIBLE) {
                                fragmentManager.beginTransaction().replace(R.id.main_setting_fragment, main_set_fragment).commit();
                                main_set_frag_layout.setVisibility(View.VISIBLE);
                                if (main_quiz_frag_layout.getVisibility() == View.VISIBLE){
                                    main_quiz_frag_layout.setVisibility(View.INVISIBLE);
                                    fragmentManager.beginTransaction().remove(main_quiz_fragment).commit();
                                }
                            } else {

                            }
                        } catch (Exception ex){
                            Log.d("1818", "오류 : " + ex);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("시발이게 뭐람", "셋팅");
                            startActivity(intent);
                        }


                        return true;
                }
                return false;
            }
        });


        main_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Create_Alarm.class);
                intent.putExtra("size", adapter.getItemCount() + 1);
                startActivityForResult(intent, 101);


            }
        });

        main_cancle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (value){
                    setMain_cancle_button(true);
                } else {
                    setMain_cancle_button(false);
                }
            }
        });

        if (getIntent().getIntExtra("alarm_num", -1) != -1){
            Log.d("1818", "알람 호출");

            adapter.getItemToId(getIntent().getIntExtra("alarm_num", -1)).setTurn(false);
            loadAlarmListData();
            finish();
        }

        if (getIntent().getStringExtra("시발이게 뭐람") != null){
            if (getIntent().getStringExtra("시발이게 뭐람").equals("퀴즈")){
                if (main_quiz_frag_layout.getVisibility() == View.INVISIBLE) {
                    fragmentManager.beginTransaction().replace(R.id.main_quiz_fragment, main_quiz_fragment).commit();
                    main_quiz_frag_layout.setVisibility(View.VISIBLE);
                    if (main_set_frag_layout.getVisibility() == View.VISIBLE) {
                        main_set_frag_layout.setVisibility(View.INVISIBLE);
                        fragmentManager.beginTransaction().remove(main_set_fragment).commit();
                    }
                }
            } else if (getIntent().getStringExtra("시발이게 뭐람").equals("셋팅")){
                if (main_set_frag_layout.getVisibility() == View.INVISIBLE) {
                    fragmentManager.beginTransaction().replace(R.id.main_setting_fragment, main_set_fragment).commit();
                    main_set_frag_layout.setVisibility(View.VISIBLE);
                    if (main_quiz_frag_layout.getVisibility() == View.VISIBLE){
                        main_quiz_frag_layout.setVisibility(View.INVISIBLE);
                        fragmentManager.beginTransaction().remove(main_quiz_fragment).commit();
                    }
                }
            }
        }

        if (getIntent().getStringExtra("족같다") != null){

        }

        AutoPermissions.Companion.loadAllPermissions(this, 111);
    }

    public void setMain_cancle_button(boolean bool){
        if (bool) {
            main_cancle_button.setText(R.string.string4);
            value = false;
            adapter.setVisibleDelete(true);
        } else {
            main_cancle_button.setText(R.string.string2);
            value = true;
            adapter.setVisibleDelete(false);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101){
            if (resultCode == 101){
                int hour = data.getIntExtra("시간", -1);
                int minute = data.getIntExtra("분", -1);

                ArrayList<String> days = data.getStringArrayListExtra("요일");

                String day = ArrayToString(days);

                String label = data.getStringExtra("라벨");
                String ring = data.getStringExtra("벨");
                String vibrator = data.getStringExtra("진동");

                saveAlarm(hour, minute, label, day, ring, vibrator);

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);

                String check_day = MainActivity.ArrayToString(days);

                int alarm_num = readLastAlarm();
                Log.d("1818", "readLastAlarm_num : " + alarm_num);
                AlarmBase.alarmtlqkf(alarm_num, calendar, label,true, check_day, ring, vibrator);

                loadAlarmListData();

                if (adapter.getItemCount() % 1 == 0) {
                    mInterstitialAd.show();
                }
            }
        }

    }

    public static String ArrayToString(ArrayList<String> days){
        StringBuilder day_bul = new StringBuilder();
        String day;

        if (days.size() != 0){
             for (int i= 0; i < days.size(); i++){
                if (days.get(i) != null){
                    day_bul.append(days.get(i) + " ");
                }
            }
        }

        if (day_bul.toString().equals("월 화 수 목 금 ")){
            day = "월 ~ 금";
        } else if (day_bul.toString().equals("월 화 수 목 금 토 ")){
            day = "월 ~ 토";
        } else if (day_bul.toString().equals("일 월 화 수 목 금 토 ")){
            day = "매일";
        } else if (day_bul.toString().equals("일 토 ")){
            day = "주말만";
        } else {
            day = day_bul.toString();
        }

        return day;
    }

    public static int loadAlarmListData(){
        AlarmDatabase.println("loadAlarmListData called.");

        String sql = "select _id, HOUR, MINUTE, LABEL, REPEATEDAY, RINGTONE, VIBRATOR, TURN from " + AlarmDatabase.TABLE_ALARM + " order by HOUR asc, MINUTE asc"; //내림차순 desc

        int recordCount = -1;
        AlarmDatabase database = AlarmDatabase.getInstance(context);


        if (database != null){
            Cursor outCursor = database.rawQuery(sql);

            recordCount = outCursor.getCount();
            AlarmDatabase.println("record count : " + recordCount + "\n");

            ArrayList<Alarm_List> items = new ArrayList<Alarm_List>();

            for (int i = 0; i < recordCount; i++){
                outCursor.moveToNext();

                int _id = outCursor.getInt(0);
                int hour = outCursor.getInt(1);
                int minute = outCursor.getInt(2);
                String label = outCursor.getString(3);
                String repeateDay = outCursor.getString(4);
                String ringtone = outCursor.getString(5);
                String vibrator = outCursor.getString(6);
                boolean turn;
                if (outCursor.getString(7).equals("true")) {
                    turn = true;
                } else {
                    turn = false;
                }

                Log.d("1818", "로딩? : " + _id + ", " + hour + ", " + minute + ", " + label + ", " + repeateDay + ", " + ringtone + ", " + vibrator + ", " + turn);

                items.add(new Alarm_List(_id, hour, minute, label, repeateDay, ringtone, vibrator, turn));
            }

            outCursor.close();

            adapter.setItems(items);
            adapter.notifyDataSetChanged();
        }

        return recordCount;
    }

    public static void saveAlarm(int hour, int minute, String label, String day, String ring, String vibrator){

        String sql = "insert into " + AlarmDatabase.TABLE_ALARM +
                "(HOUR, MINUTE, LABEL, REPEATEDAY, RINGTONE, VIBRATOR, TURN) values("
                + "'" + hour + "', " + "'" + minute + "', " + "'" + label + "', " + "'" + day + "', " + "'" + ring + "', " + "'" + vibrator + "', " + "'" + "true" + "')";

        AlarmDatabase.println("saveAlarm : " + sql);

        AlarmDatabase database = AlarmDatabase.getInstance(context);
        database.execSQL(sql);

    }

    public static void modityAlarm(int id, boolean isCheck){
        Cursor outCursor = readAlarm(id);
        int recordCount = outCursor.getCount();

        for (int i = 0; i < recordCount; i++){
            outCursor.moveToNext();
            int hour = outCursor.getInt(1);
            int minute = outCursor.getInt(2);
            String label = outCursor.getString(3);
            String repeateDay = outCursor.getString(4);
            String ringtone = outCursor.getString(5);
            String vibrator = outCursor.getString(6);

            String sql = "update " + AlarmDatabase.TABLE_ALARM + " set "
                    + " HOUR = '" + hour + "'"
                    + " ,MINUTE = '" + minute + "'"
                    + " ,LABEL = '" + label + "'"
                    + " ,REPEATEDAY = '" + repeateDay + "'"
                    + " ,RINGTONE = '" + ringtone + "'"
                    + " ,VIBRATOR = '" + vibrator + "'"
                    + " ,TURN = '" + isCheck + "'"
                    + " where " + " _id = " + id;

            Log.d("1818", sql);

            AlarmDatabase database = AlarmDatabase.getInstance(context);
            database.rawQuery(sql);
        }
    }

    public static void modityAlarm(int id, int hour, int minute, String label, String repeateDay, String ring, String vibrator){
        String sql = "update " + AlarmDatabase.TABLE_ALARM + " set "
                + " HOUR = '" + hour + "'"
                + " ,MINUTE = '" + minute + "'"
                + " ,LABEL = '" + label + "'"
                + " ,REPEATEDAY = '" + repeateDay + "'"
                + " ,RINGTONE = '" + ring + "'"
                + " ,VIBRATOR = '" + vibrator + "'"
                + " ,TURN = '" + "true" + "'"
                + " where " + " _id = " + id;

        Log.d("1818", sql);

        AlarmDatabase database = AlarmDatabase.getInstance(context);
        database.rawQuery(sql);

        loadAlarmListData();
    }

    public static Cursor readAlarm(int id){
        Context context = MainActivity.context;
        AlarmDatabase database = AlarmDatabase.getInstance(context);

        String sql = "select _id, HOUR, MINUTE, LABEL, REPEATEDAY, RINGTONE, VIBRATOR, TURN " + "from " + AlarmDatabase.TABLE_ALARM + " where _id = " + id;

        Cursor cursor = database.rawQuery(sql);

        return cursor;
    }

    public int readLastAlarm(){
        Context context = MainActivity.context;
        AlarmDatabase database = AlarmDatabase.getInstance(context);

        int _id = 0;

        String sql = "select _id, HOUR, MINUTE, LABEL, REPEATEDAY, RINGTONE, VIBRATOR, TURN " + "from " + AlarmDatabase.TABLE_ALARM;

        Cursor cursor = database.rawQuery(sql);

        int recordCount = cursor.getCount();
        for (int i = 0; i < recordCount; i++){
            cursor.moveToNext();

            _id = cursor.getInt(0);
        }

        return _id;
    }

    public static void deleteAlarm(int _id){
        AlarmDatabase.println("deleteAlarm called.");

        String sql = "delete from " + AlarmDatabase.TABLE_ALARM + " where " + " _id = " + _id ;

        AlarmDatabase database = AlarmDatabase.getInstance(context);
        database.execSQL(sql);
    }

    public static void leftSwipe(int position){
        Alarm_List item = adapter.getItem(position);

        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putInt("hour", item.getHour());
        bundle.putInt("minute", item.getMinute());
        bundle.putString("label", item.getLabel());
        bundle.putString("repeate", item.getRepeateDay());
        bundle.putString("ring", item.getRingtone());
        bundle.putString("vibrator", item.getVibrator());
        bundle.putBoolean("turn", item.getTurn());

        modify_frag.setArguments(bundle);
        Log.d("1818", "번들" + bundle);
        fragmentManager.beginTransaction().add(R.id.fragment_layout1, modify_frag).commit();

        fragment_layout1.setVisibility(View.VISIBLE);
        Animation trans = AnimationUtils.loadAnimation(context, R.anim.translate_up_fragment);
        fragment_layout1.startAnimation(trans);

    }

    public void setMainBackground(){
        if (!main_base_background.equals("")){
            main_base_layout.setBackgroundColor(Color.parseColor(main_base_background));
        } else {
            if (MainActivity.main_base_bitmap != null){
                BitmapDrawable drawable = new BitmapDrawable(getResources(), Main_Setting.getBitmapToByte(main_base_bitmap));
                main_base_layout.setBackgroundDrawable(drawable);
            }
        }

        if (!main_top_background.equals("")){
            main_top_layout.setBackgroundColor(Color.parseColor(main_top_background));
        }

//        글씨 바꾸는 것
        if (!main_top_textcolor.equals("")){
            if (main_top_views.length != 0){
                for (int i = 0; i < main_top_views.length; i++){
                    main_top_views[i].setTextColor(Color.parseColor(main_top_textcolor));
                }
            }

        }

        if (!main_base_textcolor.equals("")){
            if (main_base_views.length != 0){
                for (int i = 0; i < main_base_views.length; i++){
                    main_base_views[i].setTextColor(Color.parseColor(main_base_textcolor));
                }
            }
            adapter.setTextViewColor();
        }
    }

    @Override
    public void onBackPressed() {
        if (fragment_layout1.getVisibility() == View.VISIBLE){
            getSupportFragmentManager().beginTransaction().remove(modify_frag).commit();
            findViewById(R.id.fragment_layout1).setVisibility(View.INVISIBLE);
        }
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
    }

    @Override
    public void onDenied(int i, String[] strings) {
        Log.d("1818", "onDenied");
    }

    @Override
    public void onGranted(int i, String[] strings) {
        Log.d("1818", "onGranted");
    }
}