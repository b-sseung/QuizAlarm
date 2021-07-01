package org.techtown.quizalram.setting;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.techtown.quizalram.R;

import java.util.ArrayList;

public class Setting_Adapter extends BaseAdapter {

    ArrayList<Set_List> items = new ArrayList<>();
    Context context;

    boolean value = false;

    public void addItem(Set_List item){
        items.add(item);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Set_List getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void resetAdapter(){
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        context = parent.getContext();

        final Set_List item = items.get(position);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_setting_color, parent, false);
        }

        final TextView text = convertView.findViewById(R.id.set_color_text);

        text.setBackgroundColor(Color.parseColor(item.getStringText()));

        return convertView;
    }
}
