package me.krithiyer.todoapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // declaring objects
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;


    @Override
    // initializes variables and delete from list capabilities
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // reference to listView created in layout
        lvItems = (ListView) findViewById(R.id.lvItems);
        // initializing items list
        readItems();
        // initialize adapter using items list
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        // wiring (connecting data with list layout) adapter to view
        lvItems.setAdapter(itemsAdapter);

        // delete function
        setupListViewListener();
    }

    // adds items to the list
    public void onAddItem(View v) {
        // reference to EditText in layout
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        // grab input text as String
        String itemText = etNewItem.getText().toString();
        // use adapter to add String to list
        itemsAdapter.add(itemText);
        // clear editText to prepare for next input
        etNewItem.setText("");
        // store updated list
        writeItems();
        // user notification of update
        Toast.makeText(getApplicationContext(), "Item added to list", Toast.LENGTH_SHORT).show();
    }

    // foundation to delete items based on long clicks
    private void setupListViewListener() {
        // setting response to long click
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // remove specified item
                items.remove(position);
                // updating adapter that data has been modified
                itemsAdapter.notifyDataSetChanged();
                // store updated list
                writeItems();
                // debugging
                Log.i("MainActivity", "Removed item " + position);
                // return true to indicate long click consumed
                return true;
            }
        });
    }

    // returns file of data stored
    private File getDataFile() {
        return new File(getFilesDir(), "todo.txt");
    }

    // read items from file
    private void readItems() {
        try {
            // new array from file content
            items = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            // prints error
            e.printStackTrace();
            // loads empty list
            items = new ArrayList<>();
        }
    }

    // write items to filesystem
    private void writeItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("Main Activity", "Error writing file", e);
        }
    }
}
