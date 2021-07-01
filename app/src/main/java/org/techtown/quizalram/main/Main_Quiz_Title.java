package org.techtown.quizalram.main;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.techtown.quizalram.about_quiz.Create_Quiz;
import org.techtown.quizalram.R;
import org.techtown.quizalram.db.AlarmDatabase;
import org.techtown.quizalram.recycle.Quiz_List;
import org.techtown.quizalram.recycle.Quiz_List_Adapter;
import org.techtown.quizalram.recycle.Quiz_Title_List;
import org.techtown.quizalram.recycle.Quiz_Title_List_Adapter;
import org.techtown.quizalram.setting.Main_Setting;

import java.util.ArrayList;

public class Main_Quiz_Title extends Fragment {

    static RecyclerView recycler;
    public static Quiz_List_Adapter adapter;

    public static ArrayList<String> title_strings = new ArrayList<>();
    public static ArrayList<Boolean> title_values = new ArrayList<>();
    public static ArrayList<String> check_strings = new ArrayList<>();
    public static ArrayList<Boolean> check_values = new ArrayList<>();

    RecyclerView title_recycler;
    public static Quiz_Title_List_Adapter title_adapter;

    public static Create_Quiz create_quiz = new Create_Quiz();

    TextView group_list_button, group_set_button, group_add_cancle_button, add_button, quiz_delete, quiz_cancle, text_color_23;

    static TextView delete_button, all_select;

    LinearLayout title_layout, quiz_base_layout, quiz_top_layout;
    public static CheckBox checkBox;

    public static boolean click_boolean = true;

    static FrameLayout layout1;
    static LinearLayout layout2;

    TextView[] quiz_top_views, quiz_base_views;

    static String string30, string28;

    ViewGroup rootView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         rootView = (ViewGroup) inflater.inflate(R.layout.quiz_main, container, false);

         string30 = getString(R.string.string30);
        string28 = getString(R.string.string28);

        title_layout = rootView.findViewById(R.id.title_layout);
        checkBox = rootView.findViewById(R.id.checkBox);

        quiz_base_layout = rootView.findViewById(R.id.quiz_base_layout);
        quiz_top_layout = rootView.findViewById(R.id.quiz_top_layout);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recycler = rootView.findViewById(R.id.quiz_recyclerview);
        recycler.setLayoutManager(layoutManager);
        adapter = new Quiz_List_Adapter();
        recycler.setAdapter(adapter);

        layout1 = rootView.findViewById(R.id.quiz_layout1);
        layout2 = rootView.findViewById(R.id.quiz_layout2);

        all_select = rootView.findViewById(R.id.quiz_all_select);
        quiz_delete = rootView.findViewById(R.id.quiz_delete);
        quiz_cancle = rootView.findViewById(R.id.quiz_cancle);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        title_recycler = rootView.findViewById(R.id.quiz_title_recyclerview);
        title_recycler.setLayoutManager(layoutManager2);
        title_adapter = new Quiz_Title_List_Adapter();
        title_recycler.setAdapter(title_adapter);

        add_button = rootView.findViewById(R.id.quiz_add_Button);
        delete_button = rootView.findViewById(R.id.quiz_delete_Button);
        group_list_button = rootView.findViewById(R.id.quiz_group_list_text);
        group_set_button = rootView.findViewById(R.id.quiz_group_set_text);
        group_add_cancle_button = rootView.findViewById(R.id.quiz_group_add_cancle_text);
        text_color_23 = rootView.findViewById(R.id.text_color_23);

        adapter.holders = new ArrayList<>();

        quiz_top_views = new TextView[]{delete_button, add_button, text_color_23};
        quiz_base_views = new TextView[]{group_list_button, group_add_cancle_button, group_set_button, all_select, quiz_delete, quiz_cancle};

        setQuizBackground();
        loadQuizListData();

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!click_boolean){
                    delete_button.setText(R.string.string25);
                    click_boolean = true;
                    adapter.setVisibleLayout(false);

                    all_select.setText(R.string.string27);
                    adapter.setCheckSelect(false);

                }

                getFragmentManager().beginTransaction().add(R.id.fragment_layout2, create_quiz).commit();
                getActivity().findViewById(R.id.fragment_layout2).setVisibility(View.VISIBLE);

            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (click_boolean){
                   setDelete_button(true);
                } else {
                    setDelete_button(false);
                }
            }
        });

        group_list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_strings = new ArrayList<>();
                check_values = new ArrayList<>();

                recycler.setVisibility(View.INVISIBLE);

                title_layout.setVisibility(View.VISIBLE);
                group_list_button.setVisibility(View.INVISIBLE);
                group_set_button.setVisibility(View.VISIBLE);
                group_add_cancle_button.setVisibility(View.VISIBLE);
                add_button.setVisibility(View.INVISIBLE);
                delete_button.setVisibility(View.INVISIBLE);
            }
        });

        group_set_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adapter.holders = new ArrayList<>();
                adapter.holders_value = new ArrayList<>();

                recycler.setVisibility(View.VISIBLE);

                title_layout.setVisibility(View.INVISIBLE);
                group_list_button.setVisibility(View.VISIBLE);
                group_set_button.setVisibility(View.INVISIBLE);
                group_add_cancle_button.setVisibility(View.INVISIBLE);
                add_button.setVisibility(View.VISIBLE);
                delete_button.setVisibility(View.VISIBLE);

                if (check_strings.size() != 0){
                    for (int i = 0; i < check_strings.size(); i++){
                        Main_Quiz_Title.modityQuizValue(check_strings.get(i), String.valueOf(check_values.get(i)));

                        for (int a = 0; a < title_strings.size(); a++){

                        }
                    }
                }

                loadQuizListData();
            }
        });

        group_add_cancle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title_layout.setVisibility(View.INVISIBLE);
                group_list_button.setVisibility(View.VISIBLE);
                group_set_button.setVisibility(View.INVISIBLE);
                group_add_cancle_button.setVisibility(View.INVISIBLE);
                add_button.setVisibility(View.VISIBLE);
                delete_button.setVisibility(View.VISIBLE);

                recycler.setVisibility(View.VISIBLE);

                loadQuizListData();
            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //클릭되면서 체크박스에 체크되니까
                if (checkBox.isChecked()){
                    checkBox.setChecked(true);
                    title_adapter.setChecking(true);
                } else {
                    checkBox.setChecked(false);
                    title_adapter.setChecking(false);
                }
            }
        });

        all_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (all_select.getText().equals(R.string.string27)){
                    all_select.setText(R.string.string61);

                    adapter.setCheckSelect(true);
                } else {
                    all_select.setText(R.string.string27);

                    adapter.setCheckSelect(false);
                }
            }
        });

        quiz_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("1818", String.valueOf(adapter.holders_value));

                for (int i = 0; i < adapter.holders_value.size(); i++){
                    if (adapter.holders_value.get(i)){
                        deleteQuiz(adapter.getItem(i).getId());

                    }
                }
                loadQuizListData();

                oneClick();
            }
        });

        quiz_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oneClick();
            }
        });

        return rootView;
    }

    public static void setDelete_button(boolean bool){
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);

        if (bool){
            layout1.setLayoutParams(params2);
            layout2.setLayoutParams(params1);

            delete_button.setText(R.string.string4);
            click_boolean = false;
            adapter.setVisibleLayout(true);
        } else {
            layout1.setLayoutParams(params1);
            layout2.setLayoutParams(params2);

            all_select.setText(R.string.string27);
            adapter.setCheckSelect(false);

            delete_button.setText(R.string.string25);
            click_boolean = true;
            adapter.setVisibleLayout(false);
        }
    }

    public void oneClick(){
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);

        layout1.setLayoutParams(params1);
        layout2.setLayoutParams(params2);

        all_select.setText(R.string.string27);
        adapter.setCheckSelect(false);

        delete_button.setText(R.string.string25);
        click_boolean = true;
        adapter.setVisibleLayout(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("1818", "소멸함");
    }

    public static int loadQuizListData(){

        title_strings = new ArrayList<>();
        title_values = new ArrayList<>();

        AlarmDatabase.println("loadQuizListData called.");

        String sql = "select _id, TITLE, QUESTION, ANSWER, VALUE from " + AlarmDatabase.TABLE_QUIZ + " order by _id desc"; //내림차순 desc, 오름차순 asc

        int recordCount = -1;
        AlarmDatabase database = AlarmDatabase.getInstance(MainActivity.context);


        if (database != null){
            Cursor outCursor = database.rawQuery(sql);

            if (outCursor.getCount() == 0){
                setQuiz();
                return 0;
            }

            recordCount = outCursor.getCount();
            AlarmDatabase.println("quiz record count : " + recordCount + "\n");

            ArrayList<String> strings = new ArrayList<>();
            ArrayList<Quiz_List> items = new ArrayList<>();
            ArrayList<Quiz_Title_List> items2 = new ArrayList<>();
            boolean real_value;
            ArrayList<Boolean> values = new ArrayList<>();

            for (int i = 0; i < recordCount; i++){
                outCursor.moveToNext();

                int _id = outCursor.getInt(0);
                String title = outCursor.getString(1);
                String question = outCursor.getString(2);
                String answer = outCursor.getString(3);
                String value = outCursor.getString(4);

                if (value.equals("true")){
                    real_value = true;
                } else {
                    real_value = false;
                }

                if (title.equals("수학")){
                    if (!title.equals(string28)){
                        modityQuizTitle(title);
                        return 0;
                    }
                } else if (title.equals("数学")){
                    if (!title.equals(string28)){
                        modityQuizTitle(title);
                        return 0;
                    }
                }

                Log.d("1818", string28 + ", " + string30);
                if (real_value){
                    if (title.equals(string28)){
                        items.add(new Quiz_List(_id, title, question, string30));
                    } else {
                        items.add(new Quiz_List(_id, title, question, answer));
                    }
                }

                strings.add(title);
                values.add(real_value);
            }

            outCursor.close();

            adapter.setItems(items);
            adapter.notifyDataSetChanged();

            for (int i = strings.size() - 1; i > -1; i--){
                boolean value1 = true;
                for (int a = 0; a < title_strings.size(); a++){
                    if (strings.get(i).equals(title_strings.get(a))){
                        value1 = false;
                    }
                }
                if (value1){
                    title_strings.add(strings.get(i));
                    title_values.add(values.get(i));
                    items2.add(new Quiz_Title_List(strings.get(i), values.get(i)));
                }
            }

            if (items2.size() != 0){
                title_adapter.setItems(items2);
                title_adapter.notifyDataSetChanged();
            }
        }

        return recordCount;
    }

    public static void saveQuiz(String title, String question, String answer, String value){

        String sql = "insert into " + AlarmDatabase.TABLE_QUIZ +
                "(TITLE, QUESTION, ANSWER, VALUE) values("
                + "'" + title + "', " + "'" + question + "', " + "'" + answer + "', " + "'" + value + "')";

        AlarmDatabase.println("saveQuiz : " + sql);

        AlarmDatabase database = AlarmDatabase.getInstance(MainActivity.context);
        database.execSQL(sql);

        loadQuizListData();

    }

    public static void modityQuizValue(String title, String value){
        String sql = "update " + AlarmDatabase.TABLE_QUIZ + " set "
                + " VALUE = '" + value + "'"
                + " where " + " TITLE = " + "'" + title + "'";

        Log.d("1818", sql);

        AlarmDatabase database = AlarmDatabase.getInstance(MainActivity.context);
        database.rawQuery(sql);

        loadQuizListData();
    }

    public static void modityQuizTitle(String title){
        String sql = "update " + AlarmDatabase.TABLE_QUIZ + " set "
                + " TITLE = '" + string28 + "'"
                + " where " + " TITLE = " + "'" + title + "'";

        AlarmDatabase database = AlarmDatabase.getInstance(MainActivity.context);
        database.rawQuery(sql);

        loadQuizListData();
    }

    public static void deleteQuiz(int _id){
        AlarmDatabase.println("deleteAlarm called.");

        String sql = "delete from " + AlarmDatabase.TABLE_QUIZ + " where " + " _id = " + _id ;

        AlarmDatabase database = AlarmDatabase.getInstance(MainActivity.context);
        database.execSQL(sql);
    }

    public static void setQuiz(){

        String[] questions = new String[]{"2+2 = ", "4+4 = ", "38-19-12 = ", "96-27-69 = ", "70-23+39 = ", "32-17+99 = ", "56+29 = ",
                "5×8+7 = ", "9×5+13 = ", "6+8-5 = ", "17+57-29 = ", "1+6 = ", "3+8 = ", "2+0 = ", "112+119 = ", "12×8 = ", "12×8-6 = ",
                "9×22 = ", "11+27 = ", "20×9+22 = ", "1+9+9+7 = ", "9×15 = ", "9+15 = ", "19+97 = ", "24×10-9 = ", "3×6×4 = ", "52-16-36 = ",
                "44÷11-4 = ", "369÷9 = ", "10÷2×5 = "};
        String[] answers = new String[]{"4", "8", "7", "0", "86", "114", "85", "47", "58", "9", "45", "7", "11", "2", "231", "96", "90",
                "198", "38", "202", "26", "135", "24", "116", "231", "72", "0", "0", "41", "25"};

        for (int i = 0; i < questions.length; i++){
            String sql = "insert into " + AlarmDatabase.TABLE_QUIZ +
                    "(TITLE, QUESTION, ANSWER, VALUE) values("
                    + "'" + string28 + "', " + "'" + questions[i] + "', " + "'" + answers[i] + "', " + "'" + "true" + "')";

            AlarmDatabase.println("saveQuestion : " + sql);

            AlarmDatabase database = AlarmDatabase.getInstance(MainActivity.context);
            database.execSQL(sql);
        }
        loadQuizListData();
    }

    public void setQuizBackground(){
        if (!MainActivity.main_base_background.equals("")){
            quiz_base_layout.setBackgroundColor(Color.parseColor(MainActivity.main_base_background));
        } else {
            if (MainActivity.main_base_bitmap != null){
                BitmapDrawable drawable = new BitmapDrawable(getResources(), Main_Setting.getBitmapToByte(MainActivity.main_base_bitmap));
                quiz_base_layout.setBackgroundDrawable(drawable);
            }
        }

        if (!MainActivity.main_top_background.equals("")){
            quiz_top_layout.setBackgroundColor(Color.parseColor(MainActivity.main_top_background));
        }

        if (!MainActivity.main_top_textcolor.equals("")){
            if (quiz_top_views.length != 0){
                for (int i = 0; i < quiz_top_views.length; i++){
                    quiz_top_views[i].setTextColor(Color.parseColor(MainActivity.main_top_textcolor));
                }
            }
        }

        if (!MainActivity.main_base_textcolor.equals("")){
            if (quiz_base_views.length != 0){
                for (int i = 0; i < quiz_base_views.length; i++){
                    quiz_base_views[i].setTextColor(Color.parseColor(MainActivity.main_base_textcolor));
                }
                checkBox.setTextColor(Color.parseColor(MainActivity.main_base_textcolor));
            }
        }
        title_adapter.setQuizTitleColor();
        adapter.setQuizTextColor();
    }

}
