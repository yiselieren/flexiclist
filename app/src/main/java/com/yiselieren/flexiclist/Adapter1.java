package com.yiselieren.flexiclist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Adapter1 extends BaseAdapter {

    final ArrayList<String> list;
    final ArrayList<String> names;
    final Context cntx;
    final HashMap<String, String> titles;

    public Adapter1(Context context,
                    ArrayList<String> items,
                    ArrayList<String> names,
                    HashMap<String, String> t)
    {
        this.cntx = context;
        this.list = items;
        this.names = names;
        this.titles = t;
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int pos) {
        return list.get(pos);
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) cntx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.cl_view_layout, null);
        }

        TextView tv = (TextView) v.findViewById(R.id.cl_text);
        //tv.setText(list.get(position));
        tv.setText(titles.get(list.get(position)));
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(cntx.getResources().getDimension(R.dimen.main_cl_fontsize));
        tv.setTextColor(cntx.getResources().getColor(R.color.main_cl_fg));
        tv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(cntx, Items.class);
                intent.putExtra("filename", names.get(position));
                cntx.startActivity(intent);
            }
        });

        TextView tv1 = (TextView) v.findViewById(R.id.cl_note);
        tv1.setText(names.get(position));
        tv1.setGravity(Gravity.RIGHT);
        tv1.setTextSize(cntx.getResources().getDimension(R.dimen.main_cl_notesize));
        tv1.setTextColor(cntx.getResources().getColor(R.color.main_cl_note_fg));
        tv1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(cntx, Items.class);
                intent.putExtra("filename", names.get(position));
                cntx.startActivity(intent);
            }
        });

        ImageButton rb = (ImageButton) v.findViewById(R.id.cl_remove);
        rb.setOnClickListener(new View.OnClickListener() {
                                  public void onClick(View v) {
                                      //Utils.Debug("REMOVE");
                                      AlertDialog.Builder b = new AlertDialog.Builder(cntx);
                                      b.setTitle(cntx.getResources().getString(R.string.remove_cl));
                                      b.setMessage(
                                              cntx.getResources().getString(R.string.remove_cl1)
                                                      + list.get(position)
                                                      + cntx.getResources().getString(R.string.remove_cl2)
                                                      + names.get(position)
                                                      + cntx.getResources().getString(R.string.remove_cl3));
                                      b.setPositiveButton(cntx.getResources().getString(R.string.yes),
                                              new DialogInterface.OnClickListener() {
                                                  public void onClick(DialogInterface dialog, int whichButton) {
                                                      File f = new File(cntx.getFilesDir(),names.get(position));
                                                      f.delete();
                                                      names.remove(position);
                                                      list.remove(position);
                                                      notifyDataSetChanged();
                                                  }
                                              });
                                      b.setNegativeButton(cntx.getResources().getString(R.string.no),
                                              new DialogInterface.OnClickListener() {
                                                  public void onClick(DialogInterface dialog, int whichButton) {
                                                  }
                                              });
                                      b.show();
                                  }
                              });
        ImageButton eb = (ImageButton) v.findViewById(R.id.cl_edit);
        eb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(cntx, Editor.class);
                intent.putExtra("filename", names.get(position));
                intent.putExtra("title", titles.get(list.get(position)));
                ((Activity)cntx).startActivityForResult(intent, MainActivity.EDIT_FILE);
            }
        });
        return v;
    }
}
