package com.example.android.pets;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.pets.data.PetContract;
import com.example.android.pets.data.PetContract.PetEntry;
import com.example.android.pets.data.PetDbHelper;
import com.example.android.pets.data.PetProvider;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    private void insertPet() {

        ContentValues values= new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, "Toto");
        values.put(PetEntry.COLUMN_PET_BREED, "Terrier");
        values.put(PetEntry.COLUMN_PET_GENDER, PetEntry.GENDER_MALE);
        values.put(PetEntry.COLUMN_PET_WEIGHT, 7);

        Uri newUri = getContentResolver().insert(PetEntry.CONTENT_URI, values);
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displayDatabaseInfo() {

        String[] projection = {
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_GENDER,
                PetEntry.COLUMN_PET_WEIGHT
        };

       Cursor cursor = getContentResolver().query(
               PetEntry.CONTENT_URI,
               projection,
               null,
               null,
               null,
               null);
        try {
            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).
            TextView displayView = (TextView) findViewById(R.id.text_view_pet);
            String countText = "The pets table contains " + cursor.getCount() + " pets.\n\n";
            String header = PetEntry._ID + " - " + PetEntry.COLUMN_PET_NAME + " - "
                    + PetEntry.COLUMN_PET_BREED + " - " + PetEntry.COLUMN_PET_GENDER + " - "
                    + PetEntry.COLUMN_PET_WEIGHT + "\n";
            displayView.setText(countText);
            displayView.append(header);

            int idColIndex = cursor.getColumnIndex(PetEntry._ID);
            int nameColIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME);
            int breedColIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED);
            int genderColIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_GENDER);
            int weightColIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT);

            while (cursor.moveToNext()) {
                int currentId = cursor.getInt(idColIndex);
                String currentName = cursor.getString(nameColIndex);
                String currentBreed = cursor.getString(breedColIndex);
                int currentGender = cursor.getInt(genderColIndex);
                int currentWeight = cursor.getInt(weightColIndex);

                String rowVals = "\n" + currentId + " - " + currentName + " - " + currentBreed
                        + " - " + currentGender + " - " + currentWeight;
                displayView.append(rowVals);
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertPet();
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}