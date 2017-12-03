package com.yiselieren.flexiclist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Editor extends Activity implements TextWatcher {

    EditText editTxt;
	Button saveBtn;
	Button cancelBtn;
	String textBuffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.editor_layout);
        editTxt = (EditText) findViewById(R.id.edit_txt);

        // Read parameters
        final Intent data = getIntent();
        if (data.getExtras() == null)
            finish();

        final String fname = data.getStringExtra("filename");
        if (fname == null) {
            setResult(Activity.RESULT_CANCELED);
            finish();
        }
        final String title = data.getStringExtra("title");
        if (title == null) {
            setResult(Activity.RESULT_CANCELED);
            finish();
        }

        // Set title
        TextView tv = (TextView) findViewById(R.id.edit_title);
        tv.setText(title);

        // Prepare text
        BufferedReader br = null;
        try {
            File dir = getFilesDir();
            br = new BufferedReader(new FileReader(new File(dir, fname)));
        } catch (FileNotFoundException e) {
            finish();
            return;
        }
        String readLine;
        StringBuilder buf = new StringBuilder();
        try {
            while ((readLine = br.readLine()) != null) {
                buf.append(readLine);
                buf.append("\n");
            }
        } catch (IOException e) {
            finish();
            return;
        }
        try {
            br.close();
        } catch (IOException e) {
        }

        // Set text
        textBuffer = buf.toString();
        editTxt.setText(textBuffer);
        editTxt.addTextChangedListener(this);

        // Set "SAVE" button
        saveBtn = (Button) findViewById(R.id.edit_save);
        saveBtn.setEnabled(false);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String newBuf = editTxt.getText().toString();
                if (newBuf.equals(textBuffer)) {
                    // No changes
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                } else if (saveChanges(newBuf, fname)) {
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            }
        });

        // Set "CANCEL" button
        cancelBtn = (Button) findViewById(R.id.edit_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
    }

    public void onTextChanged(CharSequence s, int start, int before, int count)
    {

    }
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
    }
    public void afterTextChanged(Editable s) {
        final String newBuf = editTxt.getText().toString();
        if (newBuf.equals(textBuffer)) {
            saveBtn.setEnabled(false);
        } else {
            saveBtn.setEnabled(true);
        }
    }

    private boolean saveChanges(String newBuf, final String fname) {
        File dir = getFilesDir();

        // Open
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(new File(dir, fname)));
        } catch (IOException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Editor.this);
            builder.setTitle(getResources().getString(
                    R.string.editor_openerr_title));
            builder.setMessage(getResources().getString(R.string.editor_openerr_txt1)
                    + fname
                    + getResources().getString(R.string.editor_openerr_txt2));
            builder.setPositiveButton(
                    getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            setResult(Activity.RESULT_CANCELED);
                            finish();
                        }
                    });
            builder.show();
            return false;
        }

        // Write
        try {
            bw.write(newBuf, 0, newBuf.length());
        } catch (IOException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Editor.this);
            builder.setTitle(getResources().getString(R.string.editor_writeerr_title));
            builder.setMessage(getResources().getString(R.string.editor_writeerr_txt) + fname);
            builder.setPositiveButton(
                    getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            setResult(Activity.RESULT_OK);
                            finish();
                        }
                    });
            builder.show();
            return false;
        }

        // Close
        try {
            bw.close();
        } catch (IOException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Editor.this);
            builder.setTitle(getResources().getString(R.string.editor_closeerr_title));
            builder.setMessage(getResources().getString(R.string.editor_closeerr_txt) + fname);
            builder.setPositiveButton(
                    getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            setResult(Activity.RESULT_OK);
                            finish();
                        }
                    });
            builder.show();
            return false;
        }
        return true;
    }
}
