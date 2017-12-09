package com.yiselieren.flexiclist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Item extends Activity {

    AutoResizeTextView   number;
    AutoResizeTextView   item;
    ArrayList<String>    list;
    String               perek;
    HashMap<String, Boolean> checked;
    int                  index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_layout);

        // Read parameters
        final Intent data = getIntent();
        if (data.getExtras() == null) {
            finish();
            return;
        }

        perek = data.getStringExtra("perek");
        if (perek == null){
            setResult(Activity.RESULT_CANCELED);
            finish();
            return;
        }
        list = data.getStringArrayListExtra("items_list");
        if (list == null) {
            setResult(Activity.RESULT_CANCELED);
            finish();
            return;
        }
        if (list.size() < 1) {
            setResult(Activity.RESULT_CANCELED);
            finish();
            return;
        }

        Button b1 = (Button) findViewById(R.id.item_back1);
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { back(); }});
        Button b2 = (Button) findViewById(R.id.item_back2);
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { back(); }});

        checked = new HashMap<String, Boolean>();
        for (int i=0; i<list.size(); i++)
            checked.put(list.get(i), false);
        number = (AutoResizeTextView) findViewById(R.id.item_number);
        number.setText("1/" + list.size());
        index = 1;
        item = (AutoResizeTextView) findViewById(R.id.item_txt);
        item.setText(list.get(0));
        checked.put(list.get(0), true);
        item.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String s = ((TextView)v).getText().toString();
                checked.put(s, true);
                next();
            }});
    }

    public void back()
    {
        if (index <= 1)
            endActivity();
        else
        {
            index -= 1;
            number.setText(index + "/" + list.size());
            item.setText(list.get(index-1));
        }
    }
    public void next()
    {
        if (index > list.size()-1)
            endActivity();
        else
        {
            index += 1;
            number.setText(index + "/" + list.size());
            item.setText(list.get(index-1));
        }
    }

    private void endActivity()
    {
        boolean c = true;
        for (Map.Entry<String, Boolean> entry : checked.entrySet())
        {
            if (! checked.get(entry.getKey())) {
                c = false;
                break;
            }
        }
        Intent returnIntent = new Intent();
        returnIntent.putExtra("checked", c);
        returnIntent.putExtra("perek", perek);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}