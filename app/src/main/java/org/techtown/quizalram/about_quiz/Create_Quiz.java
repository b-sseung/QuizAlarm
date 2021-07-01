package org.techtown.quizalram.about_quiz;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.techtown.quizalram.R;
import org.techtown.quizalram.Toast_Activity_1;
import org.techtown.quizalram.main.MainActivity;
import org.techtown.quizalram.main.Main_Quiz_Title;

import java.util.ArrayList;

public class Create_Quiz extends Fragment {

    TextView cancle_Text;
    TextView add_Text;

    ArrayList<String> spinner_list;

    Spinner spinner;
    EditText question_edit;
    EditText answer_edit;
    String title, string28, string32, string33;

    boolean addTitle = false;

    private InterstitialAd mInterstitialAd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.quiz_set, container, false);

        question_edit = rootView.findViewById(R.id.set_quiz_edit1);
        answer_edit = rootView.findViewById(R.id.set_quiz_edit2);

        string28 = getString(R.string.string28);
        string32 = getString(R.string.string32);
        string33 = getString(R.string.string33);

        //광고
        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-8631957304793435/1375729104");
//        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");  //테스트
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        spinner_list = new ArrayList<>();

        if (spinner_list.size() == 0){
            spinner_list.add(string33);
            spinner_list.add(string32);
        }

        for (int i = 0; i < Main_Quiz_Title.title_strings.size(); i++){
            if (!Main_Quiz_Title.title_strings.get(i).equals(string28)){
                if (!Main_Quiz_Title.title_strings.get(i).equals(string33)){
                    if (!Main_Quiz_Title.title_strings.get(i).equals(string32)){
                        spinner_list.add(Main_Quiz_Title.title_strings.get(i));
                    }
                }
            }
        }

        spinner = rootView.findViewById(R.id.spinner);
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, spinner_list);
        spinner.setAdapter(spinner_adapter);
        spinner.setSelection(1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    Intent intent = new Intent(getContext(), Toast_Activity_1.class);
                    intent.putExtra("spinner_list", spinner_list);
                    startActivityForResult(intent, 101);
                } else {
                    title = spinner_list.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner.setSelection(1);
                title = spinner_list.get(1);

            }
        });

        add_Text = rootView.findViewById(R.id.add_Button);
        add_Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int title_value = -1;
                if (answer_edit.getText().toString().length() < 3){
                    Toast.makeText(getContext(), R.string.string37, Toast.LENGTH_SHORT).show();
                } else {
                    if (addTitle){
                        Main_Quiz_Title.saveQuiz(title, question_edit.getText().toString(), answer_edit.getText().toString(), "true");
                    } else {
                        for (int i = 0; i < Main_Quiz_Title.title_strings.size(); i++){
                            if (title.equals(Main_Quiz_Title.title_strings.get(i))){
                                title_value = i;
                            }
                        }
                        if (title_value != -1){
                            Main_Quiz_Title.saveQuiz(title, question_edit.getText().toString(),
                                    answer_edit.getText().toString(), Main_Quiz_Title.title_values.get(title_value).toString());
                        } else {
                            Main_Quiz_Title.saveQuiz(title, question_edit.getText().toString(), answer_edit.getText().toString(), "true");
                        }
                    }

                    question_edit.setText("");
                    answer_edit.setText("");
                    title = null;
                    addTitle = false;
                    spinner.setSelection(1);

                    getFragmentManager().beginTransaction().remove(Main_Quiz_Title.create_quiz).commit();
                    getActivity().findViewById(R.id.fragment_layout2).setVisibility(View.INVISIBLE);

                    MainActivity.decorView.setSystemUiVisibility( MainActivity.uiOption );

                    if (Main_Quiz_Title.adapter.getItemCount() % 1 == 0) {
                        mInterstitialAd.show();
                    }

                }
            }
        });

        cancle_Text = rootView.findViewById(R.id.cancle_Button);
        cancle_Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                question_edit.setText("");
                answer_edit.setText("");
                title = null;
                spinner.setSelection(1);

                getFragmentManager().beginTransaction().remove(Main_Quiz_Title.create_quiz).commit();
                getActivity().findViewById(R.id.fragment_layout2).setVisibility(View.INVISIBLE);

                MainActivity.decorView.setSystemUiVisibility( MainActivity.uiOption );

            }
        });

        return rootView;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101){
            if (resultCode == 101){
                int i = data.getIntExtra("value", -1);
                if (i != -1){
                    spinner.setSelection(i);
                } else {
                    addTitle = true;
                    spinner_list.add(data.getStringExtra("group"));
                    spinner.setSelection(spinner_list.size() - 1);
                }
            } else if (resultCode == 102){
                spinner.setSelection(1);
            }
        }
    }
}
