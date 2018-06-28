package me.krithiyer.todoapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import static me.krithiyer.todoapplication.MainActivity.ITEM_POSITION;
import static me.krithiyer.todoapplication.MainActivity.ITEM_TEXT;

public class EditItemActivity extends AppCompatActivity {

    // keep track of edits
    EditText etItemText;
    // original position
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        //resolve edit text from layout
        etItemText = (EditText) findViewById(R.id.etItemText);
        etItemText.setText(getIntent().getStringExtra(ITEM_TEXT));
        position = getIntent().getIntExtra(ITEM_POSITION, 0);
        getSupportActionBar().setTitle("Edit item");
    }

    public void onSaveItem(View v) {
        // Prepare intent to pass back to MainActivity
        Intent data = new Intent();
        // Pass updated item text and original position
        data.putExtra(ITEM_TEXT, etItemText.getText().toString());
        data.putExtra(ITEM_POSITION, position); // ints work too
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); // closes the edit activity, passes intent back to main
    }

}
