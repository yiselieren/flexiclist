package com.yiselieren.flexiclist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Items extends Activity {

    ArrayList<String>        prakim;
    HashMap<String, Boolean> checked;
    HashMap<String, ArrayList<String>> tohen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topics_view_layout);
        setContentView(R.layout.items_layout);
        //setContentView(R.layout.cl_view_layout);

        if (savedInstanceState == null)
        {
            // Read parameters
            final Intent data = getIntent();
            if (data.getExtras() == null)
                finish();

            final String fname = data.getStringExtra("filename");
            if (fname == null) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }

            // ****
            // Parse input file
            // ****
            prakim = new ArrayList<String>();
            checked = new HashMap<String, Boolean>();
            tohen = new HashMap<String, ArrayList<String>>();

            BufferedReader br = null;
            try {
                File dir = getFilesDir();
                br = new BufferedReader(new FileReader(new File(dir, fname)));
            } catch (FileNotFoundException e) {
                finish();
                return;
            }
            String readLine;
            String current = null;
            try {
                while ((readLine = br.readLine()) != null) {
                    // Line processing
                    //Utils.Debug("Parse file, line='" + readLine + "'");
                    if (readLine.trim().isEmpty())
                        continue;
                    if (readLine.startsWith(" ")) {
                        String r = readLine.trim();
                        prakim.add(r);
                        current = r;
                        checked.put(r, false);
                        //Utils.Debug("Parse file, perek='" + r + "'");
                    }
                    else if (current != null && !current.isEmpty()) {
                        ArrayList<String> t = tohen.get(current);
                        if (t == null)
                            t = new ArrayList<String>();
                        t.add(readLine.trim().replaceFirst("^[0-9]+\\. ", ""));
                        tohen.put(current, t);
                        //Utils.Debug("Parse file, add line='" +
                        //        readLine.trim().replaceFirst("^[0-9]+\\. ", "") +
                        //        "' to '" + current + "'");
                    }
                }

                // Now remove all empty "prakim"
                ArrayList<String> prakim1 = new ArrayList<String>();
                for (int i=0; i<prakim.size(); i++) {
                    String p = prakim.get(i);
                    if (tohen.containsKey(p)) {
                        if (tohen.get(p).size() < 1)
                            tohen.remove(p);
                        else
                            prakim1.add(p);
                    }
                }
                prakim = prakim1;

            } catch (IOException e) {
                finish();
                return;
            }
            try {
                br.close();
            } catch (IOException e) { }

        }
        else
        {
            prakim = savedInstanceState.getStringArrayList("prakim");
            checked = new HashMap<String, Boolean>();
            tohen = new HashMap<String, ArrayList<String>>();
            for (String e : prakim) {
                final String key = "checked." + e;
                checked.put(e, savedInstanceState.getInt(key) != 0);
            }
            for (String e : prakim) {
                final String key = "tohen." + e;
                tohen.put(e, savedInstanceState.getStringArrayList(key));
            }
        }

        //// DEBUG print
        //Utils.Debug("PRAKIM:");
        //for (int i=0; i<prakim.size(); i++) {
        //    Utils.Debug("*** " + prakim.get(i));
        //    Utils.Debug("Checked: " + checked.get(prakim.get(i)));
        //    for (int j=0; j<tohen.get(prakim.get(i)).size(); j++) {
        //        Utils.Debug("    " + tohen.get(prakim.get(i)).get(j));
        //    }
        //}

        ListView lv = (ListView) findViewById(R.id.items_lv) ;
        Adapter2 ad = new Adapter2(this, prakim, tohen, checked);
        lv.setAdapter(ad);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putStringArrayList("prakim", prakim);
        for (Map.Entry<String, Boolean> entry : checked.entrySet())
            outState.putInt("checked." + entry.getKey(), entry.getValue() ? 1 : 0);
        for (Map.Entry<String, ArrayList<String>> entry : tohen.entrySet())
            outState.putStringArrayList("tohen." + entry.getKey(), entry.getValue());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            ListView lv = (ListView) findViewById(R.id.items_lv);
            Adapter2 ad = (Adapter2)lv.getAdapter();
            if (data.getExtras() == null)
                finish();

            final String perek = data.getStringExtra("perek");
            final boolean chk = data.getBooleanExtra("checked", false);
            ad.setChecked(perek, chk);
            ad.notifyDataSetChanged();
        }
    }
}
