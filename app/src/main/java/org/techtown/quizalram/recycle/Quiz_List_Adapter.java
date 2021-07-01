package org.techtown.quizalram.recycle;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.quizalram.R;
import org.techtown.quizalram.main.MainActivity;
import org.techtown.quizalram.main.Main_Quiz_Title;
import org.techtown.quizalram.swipelayout.Swipe_Layout;
import org.techtown.quizalram.swipelayout.ViewBinderListener;


import java.util.ArrayList;

public class Quiz_List_Adapter extends RecyclerView.Adapter<Quiz_List_Adapter.ViewHolder> {

    static ArrayList<Quiz_List> items = new ArrayList<>();
    private final ViewBinderListener viewBinderHelper = new ViewBinderListener();
    public static ArrayList<ViewHolder> holders = new ArrayList<>();
    public ArrayList<Boolean> holders_value = new ArrayList<>();
    boolean value = false;
    public static boolean swipe_value = true;
    boolean checkSelect, quiz_color_value;

    @NonNull
    @Override
    public Quiz_List_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.list_quiz_layout, parent, false);

        ViewHolder holder = new ViewHolder(itemView);

        Log.d("1818", "holder 생성");

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final Quiz_List item = items.get(position);
        holder.setItem(item);

        viewBinderHelper.setOpenOnlyOne (true);
        viewBinderHelper.bind(holder.swipeRevealLayout, String.valueOf(position));
        viewBinderHelper.closeLayout (String.valueOf (position));

        holders.add(holder);
        holders_value.add(false);

        if (value){
            if (item.getGroup().equals("수학")){
                holder.delete_check.setVisibility(View.INVISIBLE);
            } else {
                holder.delete_check.setVisibility(View.VISIBLE);
            }
            viewBinderHelper.lockSwipe(String.valueOf(position));
            setAnswerLayout();
        } else {
            holder.delete_check.setVisibility(View.INVISIBLE);
            viewBinderHelper.unlockSwipe(String.valueOf(position));

        }

        if (checkSelect){
            holder.delete_check.setChecked(true);
        } else {
            holder.delete_check.setChecked(false);
        }

        if (quiz_color_value){
            TextView[] quiz_list_views = new TextView[]{holder.group, holder.question, holder.answer, holder.text_color_9};
            if (!MainActivity.main_base_textcolor.equals("")){
                if (quiz_list_views.length != 0){
                    for (int i = 0; i < quiz_list_views.length; i++){
                        quiz_list_views[i].setTextColor(Color.parseColor(MainActivity.main_base_textcolor));
                    }
                }
            }
        }

        if (item.getGroup().equals("수학")){
            viewBinderHelper.lockSwipe(String.valueOf(position));
        }

        Log.d("1818", "holders 길이 : " + holders.size() + ", holders_value : " + holders_value.size());

        holder.delete_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                holders_value.set(holder.getAdapterPosition(), isChecked);

            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("1818", "값 : " + swipe_value);
                Log.d("1818", "값2 : " + value);
                Log.d("1818", "값3 : " + holder.answer_layout.getVisibility());
                if (value) {
                    if (holder.delete_check.isChecked()){
                        holder.delete_check.setChecked(false);
                    } else {
                        holder.delete_check.setChecked(true);
                    }
                } else {
                    if (swipe_value){
                        if (holder.answer_layout.getVisibility() == View.VISIBLE){
                            setAnswerVisible(true, holder.getAdapterPosition(), "클릭");
                        } else {
                            setAnswerVisible(false, holder.getAdapterPosition(), "클릭");
                        }
                    }
                }
            }
        });

        holder.delete_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!items.get(holder.getAdapterPosition()).getGroup().equals("수학")){
                    Main_Quiz_Title.deleteQuiz(items.get(position).getId());
                    items.remove(position);
                    notifyDataSetChanged();
                    Main_Quiz_Title.title_adapter.notifyDataSetChanged();
                } else {
                    viewBinderHelper.closeLayout(String.valueOf(holder.getAdapterPosition()));
                }

                notifyDataSetChanged();
            }
        });

        holder.modify_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewBinderHelper.closeLayout(String.valueOf(holder.getAdapterPosition()));
                Log.d("1818", "수정");
            }
        });

    }

    public static void setAnswerLayout(){
        for (int i = 0; i < holders.size(); i++){
            if (holders.get(i).answer_layout.getVisibility() == View.VISIBLE){
                setAnswerVisible(true, i, "스와이프");
            }
        }
    }

    public void setCheckSelect(boolean checkSelect){
        this.checkSelect = checkSelect;
        holders = new ArrayList<>();
        holders_value = new ArrayList<>();
        notifyDataSetChanged();
    }

    public static void setAnswerVisible(boolean bool, int position, String str){

        Log.d("1818", str + ", " + position + "로 호출됨" + ", " + bool);
        if (bool){

            for (int i = 0; i < holders.size(); i++){
                if (holders.get(i).getAdapterPosition() == position){
                    ViewHolder holder = holders.get(i);
                    holder.answer_layout.setVisibility(View.INVISIBLE);

                    LinearLayout.LayoutParams params_invisible = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                    LinearLayout.LayoutParams params_text = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    holder.answer_layout.setLayoutParams(params_invisible);
                    holder.text.setLayoutParams(params_text);
                }
            }


        } else {

            for (int i = 0; i < holders.size(); i++){
                if (holders.get(i).getAdapterPosition() == position){
                    ViewHolder holder = holders.get(i);
                    holder.answer_layout.setVisibility(View.VISIBLE);

                    LinearLayout.LayoutParams params_invisible = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    LinearLayout.LayoutParams params_text = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);

                    holder.answer_layout.setLayoutParams(params_invisible);
                    holder.text.setLayoutParams(params_text);
                }
            }
        }
    }


    public void setVisibleLayout(boolean value){
        this.value = value;
        holders = new ArrayList<>();
        holders_value = new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Quiz_List item){
        items.add(item);
    }

    public void setItems(ArrayList<Quiz_List> items){
        this.items = items;
    }

    public Quiz_List getItem(int position){
        return items.get(position);
    }

    public void setQuizTextColor(){
        this.quiz_color_value = true;
        notifyDataSetChanged();
    }


//    @Override
//    public void setVisibleLayout(RecyclerView.ViewHolder viewHolder, int position) {
//        LinearLayout.LayoutParams params_invisible = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        Log.d("1818", "호출");
//        viewHolder.itemView.findViewById(R.id.quiz_delete_button_layout).setLayoutParams(params_invisible);
//    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        Swipe_Layout swipeRevealLayout;
        FrameLayout modify_click;
        FrameLayout delete_click;

        TextView group, question, answer, text, text_color_9;

        CardView cardView;

        CheckBox delete_check;
        LinearLayout answer_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            swipeRevealLayout = itemView.findViewById(R.id.swipeLayout);
            modify_click = itemView.findViewById(R.id.quiz_modify_click);
            delete_click = itemView.findViewById(R.id.quiz_delete_click);

            group = itemView.findViewById(R.id.group_text);
            question = itemView.findViewById(R.id.quiz_text);
            answer = itemView.findViewById(R.id.answer_text);
            text = itemView.findViewById(R.id.text);
            text_color_9 = itemView.findViewById(R.id.text_color_9);

            delete_check = itemView.findViewById(R.id.list_check);
            answer_layout = itemView.findViewById(R.id.answer_layout);
            cardView = itemView.findViewById(R.id.cardview);

        }

        public void setItem(Quiz_List item){
            group.setText(item.getGroup());
            question.setText(item.getQuestion());
            answer.setText(item.getAnswer());
        }
    }
}
