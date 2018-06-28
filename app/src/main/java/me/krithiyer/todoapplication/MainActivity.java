package me.krithiyer.todoapplication;

import android.content.Intent;
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

    // numeric code to identify edit activity
    public static final int EDIT_REQUEST_CODE = 20;
    // keys for passing data between activities
    public static final String ITEM_TEXT = "itemText";
    public static final String ITEM_POSITION = "itemPosition";

    // declaring objects
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       super.onActivityResult(requestCode, resultCode, data);
       // EDIT_REQUEST_CODE defined with constants
       if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE) {
           // get updated item data
           String updatedItem = data.getExtras().getString(ITEM_TEXT);
           // position of updated item
           int position = data.getExtras().getInt(ITEM_POSITION, 0);
           // update the model with the new item text at the edited position
           items.set(position, updatedItem);
           // update adapter about data changes
           itemsAdapter.notifyDataSetChanged();
           // store updated data
           writeItems();
           // user notification of completion
           Toast.makeText(this, "Item updated", Toast.LENGTH_SHORT).show();
       }
   }


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

    // foundation for list manipulation based on type of click
    private void setupListViewListener() {
        Log.i("Main Activity", "Setting up listener on list view");
        // setting response to long click
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // remove specific item
                items.remove(position);
                // notifying that data has been updated
                itemsAdapter.notifyDataSetChanged();
                // store updated list
                writeItems();
                // debugging
                Log.i("MainActivity", "Removed item " + position);
                // return true for long click
                return true;
            }
        });
        // setting response to a click (not long click)
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // context and activity class
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                // extras accessible for edit activity
                i.putExtra(ITEM_TEXT, items.get(position));
                i.putExtra(ITEM_POSITION, position);
                // edit and result expectation
                startActivityForResult(i, EDIT_REQUEST_CODE);
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
