package org.techtown.quizalram.recycle;

import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.quizalram.AlarmBase;
import org.techtown.quizalram.main.MainActivity;
import org.techtown.quizalram.R;
import org.techtown.quizalram.swipe.ItemTouchHelperListener;
import org.techtown.quizalram.swipelayout.Swipe_Layout;
import org.techtown.quizalram.swipelayout.ViewBinderListener;

import java.util.ArrayList;
import java.util.Calendar;

public class Alarm_List_Adapter extends RecyclerView.Adapter<Alarm_List_Adapter.ViewHolder> {

    ViewBinderListener listener = new ViewBinderListener();

    ArrayList<Alarm_List> items = new ArrayList<Alarm_List>();
    final Calendar calendar = Calendar.getInstance();
    View itemView;

    Alarm_List itemitem;

    boolean value, color_value;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        itemView = inflater.inflate(R.layout.list_alarm_layout, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Alarm_List item = items.get(position);
        holder.setItem(item);

        listener.setOpenOnlyOne(true);
        listener.bind(holder.swipe_layout, String.valueOf(position));
        listener.closeLayout(String.valueOf(position));


        if (value){
            holder.switch2.setVisibility(View.INVISIBLE);
            holder.clickLayout.setVisibility(View.VISIBLE);
            holder.minus.setVisibility(View.VISIBLE);
            listener.lockSwipe(String.valueOf(position));
        } else {
            holder.switch2.setVisibility(View.VISIBLE);
            holder.clickLayout.setVisibility(View.INVISIBLE);
            holder.minus.setVisibility(View.INVISIBLE);
            listener.unlockSwipe(String.valueOf(position));
        }

        if (color_value){
            TextView[] textViews = new TextView[]{holder.hour, holder.text_color_10, holder.minute, holder.repeateDay, holder.label};
            if (!MainActivity.main_base_textcolor.equals("")){
                if (textViews.length != 0){
                    for (int i = 0; i < textViews.length; i++){
                        textViews[i].setTextColor(Color.parseColor(MainActivity.main_base_textcolor));
                    }
                }
            }
        }

        holder.switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                itemitem = items.get(holder.getAdapterPosition());


                itemitem.setTurn(isChecked);
                int num = itemitem.getId();
                MainActivity.modityAlarm(num, isChecked);


                int hour = itemitem.getHour();
                int minute = itemitem.getMinute();

                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);

                if (isChecked){
                    AlarmBase.alarmtlqkf(num, calendar, itemitem.getLabel(), true, itemitem.getRepeateDay(), itemitem.getRingtone(), itemitem.getVibrator());
                } else {
                    AlarmBase.alarmtlqkf(num, calendar, itemitem.getLabel(),false, itemitem.getRepeateDay(), itemitem.getRingtone(), itemitem.getVibrator());
                }
            }
        });


        holder.clickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (value){
                    MainActivity.leftSwipe(position);
                    setVisibleDelete(false);
                }
            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.deleteAlarm(items.get(position).getId());
                items.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.deleteAlarm(items.get(position).getId());
                items.remove(position);
                notifyDataSetChanged();

                listener.closeLayout(String.valueOf(position));
            }
        });

        holder.modify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.leftSwipe(position);
                listener.closeLayout(String.valueOf(position));
            }
        });

    }

    public void setVisibleDelete(boolean value){
        this.value = value;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Alarm_List item){
        items.add(item);
    }

    public void setItems(ArrayList<Alarm_List> items){
        this.items = items;
    }

    public Alarm_List getItem(int position){
        return items.get(position);
    }

    public Alarm_List getItemToId(int id){
        Log.d("1818", String.valueOf(items));
        Alarm_List item = null;
        for (int i = 0; i < items.size(); i++){
            if (items.get(i).getId() == id){
                item = items.get(i);
            }
        }
        return item;
    }

    public void setTextViewColor(){
        this.color_value = true;
        notifyDataSetChanged();
    }

    public void setItem(int position, Alarm_List item){
        items.set(position, item);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView hour;
        TextView text_color_10;
        TextView minute;
        TextView label;
        TextView repeateDay;
        Switch switch2;
        ImageView minus;

        FrameLayout clickLayout;
        FrameLayout modify_button;
        FrameLayout delete_button;
        Swipe_Layout swipe_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            hour = itemView.findViewById(R.id.hour);
            text_color_10 = itemView.findViewById(R.id.text_color_10);
            minute = itemView.findViewById(R.id.minute);
            label = itemView.findViewById(R.id.label);
            repeateDay = itemView.findViewById(R.id.repeateDay);
            switch2 = itemView.findViewById(R.id.switch2);
            minus = itemView.findViewById(R.id.minus);

            clickLayout = itemView.findViewById(R.id.clickLayout);
            modify_button = itemView.findViewById(R.id.alarm_modify_click);
            delete_button = itemView.findViewById(R.id.alarm_delete_click);
            swipe_layout = itemView.findViewById(R.id.alarm_swipeLayout);
        }

        public void setItem(Alarm_List item){

            if (item.getHour() < 10){
                hour.setText("0" + item.getHour());
            } else {
                hour.setText(String.valueOf(item.getHour()));
            }

            if (item.getMinute() < 10){
                minute.setText("0" + item.getMinute());
            } else {
                minute.setText(String.valueOf(item.getMinute()));
            }
                label.setText(item.getLabel());
                repeateDay.setText(item.getRepeateDay());
                switch2.setChecked(item.getTurn());

        }

    }
}
