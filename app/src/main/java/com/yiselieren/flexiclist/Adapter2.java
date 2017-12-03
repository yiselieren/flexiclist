package com.yiselieren.flexiclist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class Adapter2 extends BaseAdapter {
    final ArrayList<String> prakim;
    final HashMap<String, Boolean> checked;
    final HashMap<String, ArrayList<String>> tohen;
    final Context cntx;

    public Adapter2(Context context,
                    ArrayList<String> p,
                    HashMap<String, ArrayList<String>> t,
                    HashMap<String, Boolean> c)
    {
        this.cntx = context;
        this.prakim = p;
        this.checked = c;
        this.tohen = t;
    }

    public int getCount() {
        return prakim.size();
    }

    public Object getItem(int pos) {
        return prakim.get(pos);
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) cntx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.topics_view_layout, null);
        }

        TextView tv = (TextView) v.findViewById(R.id.items_txt);
        tv.setText(prakim.get(position));
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(cntx.getResources().getDimension(R.dimen.items_fontsize));
        tv.setTextColor(cntx.getResources().getColor(R.color.items_fg));

        //boolean chk = true;
        //for (int j = 0; j < tohen.get(prakim.get(position)).size(); j++) {
        //    if (!checked.get(prakim.get(j))) {
        //        chk = false;
        //        break;
        //    }
        //}
        boolean chk = checked.get(prakim.get(position));
        if (chk)
            tv.setBackgroundColor(cntx.getResources().getColor(R.color.items_checked_bg));
        else
            tv.setBackgroundColor(cntx.getResources().getColor(R.color.items_unchecked_bg));
        tv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(cntx, Item.class);
                intent.putExtra("items_list", tohen.get(prakim.get(position)));
                intent.putExtra("perek", prakim.get(position));
                ((Activity)cntx).startActivityForResult(intent, 1);
            }
        });
        return v;
    }

    public void setChecked(String p, boolean c)
    {
        checked.put(p, c);
    }

}
