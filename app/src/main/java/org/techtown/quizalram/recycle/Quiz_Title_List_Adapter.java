package org.techtown.quizalram.recycle;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.quizalram.AlarmBase;
import org.techtown.quizalram.R;
import org.techtown.quizalram.main.MainActivity;
import org.techtown.quizalram.main.Main_Quiz_Title;

import java.util.ArrayList;
import java.util.Calendar;

public class Quiz_Title_List_Adapter extends RecyclerView.Adapter<Quiz_Title_List_Adapter.ViewHolder> {

    ArrayList<Quiz_Title_List> items = new ArrayList<>();

    Quiz_Title_List itemitem;

    boolean value, quiz_title_color_value;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.list_quiz_title_layout, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        Quiz_Title_List item = items.get(position);
        holder.setItem(item);

        if (value){
            item.setValue(true);
            holder.title_checkBox.setChecked(true);
        }
        if (quiz_title_color_value){
            if (!MainActivity.main_base_textcolor.equals("")){
                holder.title_checkBox.setTextColor(Color.parseColor(MainActivity.main_base_textcolor));
            }
        }

        holder.title_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                itemitem = items.get(holder.getAdapterPosition());

                itemitem.setValue(isChecked);

                int check_num = 0; //?????? ????????? for??? if ????????? ?????? ??????
                for (int i = 0; i < items.size(); i++){
                    if (!items.get(i).getValue()){
                        check_num = 1; //checkbox ?????? false?????? num = 1
                    }
                }

                if (check_num == 0){ //?????? items?????? checkbox??? ???????????? false??? ?????? ???????????? ?????? ??????
                    Main_Quiz_Title.checkBox.setChecked(true);
                } else {
                    Main_Quiz_Title.checkBox.setChecked(false);
                }

                int num = 0; //?????? ????????? for??? if ????????? ?????? ??????
                for (int i = 0; i < Main_Quiz_Title.check_strings.size(); i++){
                    if (Main_Quiz_Title.check_strings.get(i).equals(itemitem.getName())){
                        num = 1; //????????? check_string ??? ????????? ?????? ??????????????? ?????? ?????? ???????????? ????????? ?????????
                        Main_Quiz_Title.check_values.set(i, itemitem.getValue()); //check_values??? ?????? ??????????????? ????????? ????????? ???
                    }
                }

                if (num == 0) {
                    //?????? for????????? ??????????????? ????????? check_strings??? values??? ??????
                    Main_Quiz_Title.check_strings.add(itemitem.getName());
                    Main_Quiz_Title.check_values.add(itemitem.getValue());
                }

            }
        });


    }


    public void setChecking(boolean value){
        this.value = value;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Quiz_Title_List item){
        items.add(item);
    }

    public void setItems(ArrayList<Quiz_Title_List> items){
        this.items = items;
    }

    public Quiz_Title_List getItem(int position){
        return items.get(position);
    }

    public void setQuizTitleColor(){
        this.quiz_title_color_value = true;
        notifyDataSetChanged();
    }


    protected class ViewHolder extends RecyclerView.ViewHolder{

        CheckBox title_checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title_checkBox = itemView.findViewById(R.id.title_checkBox);

        }

        public void setItem(Quiz_Title_List item){
            title_checkBox.setText(item.getName());
            title_checkBox.setChecked(item.getValue());
        }

        public void setChange(boolean value){
            title_checkBox.setChecked(value);
        }
    }

}
