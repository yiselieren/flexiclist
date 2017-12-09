package com.yiselieren.flexiclist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.content.Context;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    public static Context GlobalContext = null;
    private static final int FILE_SELECT_CODE = 56;
    private static final int EDIT_FILE = 57;
    private ListView lv;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalContext = this;
        Utils.Debug("****  onCreate: " + savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lv = (ListView) findViewById(R.id.main_lv);
        tv = (TextView) findViewById(R.id.main_tv);

        File dir = getFilesDir();
        File[] subFiles = dir.listFiles();

        // If empty - add predefined file // TODO: Remove that
        if (subFiles.length < 1) {
            Utils.importInternal(this, R.raw.c172_hebrew, dir, "c172_hebrew.txt");
            subFiles = dir.listFiles();
        }

        if (subFiles.length > 0)
                refreshList();
        else
        {
            // No checklist found
            ((ViewGroup) lv.getParent()).removeView(lv);
            tv.setGravity(Gravity.CENTER);
            tv.setText(getResources().getString(R.string.no_checklist));
            tv.setBackgroundColor(getResources().getColor(R.color.main_no_cl_bg));
            tv.setTextColor(getResources().getColor(R.color.main_no_cl_fg));
            tv.setTextSize(getResources().getDimension(R.dimen.main_nocl_fontsize));
        }
    }

    private void refreshList()
    {
        File dir = getFilesDir();
        File[] subFiles = dir.listFiles();

        // Checklists found
        ArrayList<String> clists = new ArrayList<String>();
        ArrayList<String> origs = new ArrayList<String>();
        HashMap<String, String> titles = new HashMap<String, String>();

        for (int i = 0; i < subFiles.length; i++) {
            String name = subFiles[i].getName();
            origs.add(name);
            int pos = name.lastIndexOf(".");
            if (pos > 0) {
                name = name.substring(0, pos);
            }
            clists.add(name);
            titles.put(name, Utils.getTitle(dir, origs.get(origs.size() - 1), name));
            //Utils.Debug("TITLES[" + name + "] = \"" + Utils.getTitle(dir, origs.get(origs.size() - 1), name) + "\"");
        }

        Adapter1 ad = new Adapter1(this, clists, origs, titles);
        lv.setAdapter(ad);
        lv.setBackgroundColor(getResources().getColor(R.color.main_cl_bg));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            // ************
            // *** Settings
            // ************
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setTitle(getResources().getString(R.string.nothing_to_set_title));
            b.setMessage(getResources().getString(R.string.nothing_to_set_text));
            b.setPositiveButton(getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });
            b.show();
            return true;
        } else if (id == R.id.action_create) {
            //  **************
            //  *** Create new
            //  **************
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            final Context cntx = this;
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            builder.setTitle(R.string.create_new_title);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final File dir = getFilesDir();
                    final File[] subFiles = dir.listFiles();
                    final String new_file = input.getText().toString();
                    ArrayList<String> names = new ArrayList<String>();
                    for (int i = 0; i < subFiles.length; i++)
                        names.add(subFiles[i].getName());
                    if (names.contains(new_file)) {
                        AlertDialog.Builder b = new AlertDialog.Builder(cntx);
                        b.setTitle(getResources().getString(R.string.already_exists_title));
                        b.setMessage(getResources().getString(R.string.already_exists_text));
                        b.setPositiveButton(getResources().getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                    }
                                });
                        b.show();
                    } else {
                        Utils.importInternal(cntx, R.raw.template, dir, new_file);
                        Intent intent = new Intent(cntx, Editor.class);
                        intent.putExtra("filename", new_file);
                        intent.putExtra("title", Utils.getTitle(dir, new_file, new_file));
                        startActivityForResult(intent, EDIT_FILE);
                    }
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
            return true;
        } else if (id == R.id.action_import_url) {
            // *******************
            // *** Import from URL
            // *******************
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setTitle(getResources().getString(R.string.not_implemented_title));
            b.setMessage(R.string.not_implemented_url);
            b.setPositiveButton(R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            ;
                        }
                    });
            b.show();
            return true;
        } else if (id == R.id.action_import_file) {
            // ********************
            // *** Import from file
            // ********************
            Intent getContentIntent = FileUtils.createGetContentIntent();
            Intent intent = Intent.createChooser(getContentIntent, "Select a file");
            try {
                startActivityForResult(intent, FILE_SELECT_CODE);
            } catch (android.content.ActivityNotFoundException e) {
                Toast.makeText(this, "Please install some File Manager.",
                        Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (id == R.id.action_export_file) {
            // ******************
            // *** Export to file
            // ******************
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setTitle(getResources().getString(R.string.not_implemented_title));
            b.setMessage(R.string.not_implemented_export);
            b.setPositiveButton(R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            ;
                        }
                    });
            b.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    String path = FileUtils.getPath(this, uri);
                    Utils.Debug("File path " + path);
                }
                break;
            case EDIT_FILE:
                refreshList();
                break;
        }
    }
}