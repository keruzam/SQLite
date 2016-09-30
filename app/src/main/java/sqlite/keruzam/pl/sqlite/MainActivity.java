package sqlite.keruzam.pl.sqlite;

import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String DB_NAME = "MyCars";
    SQLiteDatabase db = null;

    Button addCarButton, deleteCarButton, getCarButton;
    Button createDBButton, dropDBButton;

    EditText markEditText, modelEditText, engineEditText, idEditText;
    EditText carListEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //-------------------------------------
        initButtons();
        initEditTextFields();
        createDatabase(this);
    }

    private void initEditTextFields() {
        markEditText = (EditText) findViewById(R.id.markEditText);
        modelEditText = (EditText) findViewById(R.id.modelEditText);
        engineEditText = (EditText) findViewById(R.id.engineEditText);
        idEditText = (EditText) findViewById(R.id.idEditText);
        carListEditText = (EditText) findViewById(R.id.carListEditText);
    }

    private void initButtons() {
        addCarButton = (Button) findViewById(R.id.addCarButton);
        deleteCarButton = (Button) findViewById(R.id.deleteCarButton);
        getCarButton = (Button) findViewById(R.id.getCarButton);
        createDBButton = (Button) findViewById(R.id.createDBButton);
        dropDBButton = (Button) findViewById(R.id.dropDBButton);
    }

    private void showButtons() {
        getCarButton.setVisibility(View.VISIBLE);
        dropDBButton.setVisibility(View.VISIBLE);
        deleteCarButton.setVisibility(View.VISIBLE);
        addCarButton.setVisibility(View.VISIBLE);
    }

    private void cleanEditTextFields() {
        markEditText.setText("");
        modelEditText.setText("");
        engineEditText.setText("");
    }

    private void createDatabase(MainActivity activity) {
        try {
            //DatabaseErrorHandler errorHandler = null;
            db = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS car " +
                    "(id integer primary key, mark VARCHAR, model VARCHAR, engine VARCHAR);");
            showToast("DB CREATED");
        } catch (Exception e) {
            Log.e("CAR ERROR", "Error creating database");
        }
        showButtons();
    }

    public void createDatabase(View view) {
        createDatabase(view);
    }


    public void dropDatabase(View view) {
        this.deleteDatabase(DB_NAME);
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }

    public void addCar(View view) {
        String mark = markEditText.getText().toString();
        String model = modelEditText.getText().toString();
        String engine = engineEditText.getText().toString();
        db.execSQL("INSERT INTO car (mark, model, engine) VALUES ('" +
        mark + "', '" + model + "', '" + engine + "');");
        cleanEditTextFields();
    }

    public void deleteCar(View view) {
        String id = idEditText.getText().toString();
        db.execSQL("DELETE FROM car WHERE id=" + id + ";");
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    public void getCarList(View view) {
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM car", null);
            int idCol = cursor.getColumnIndex("id");
            int markCol = cursor.getColumnIndex("mark");
            int modelCol = cursor.getColumnIndex("model");
            int engineCol = cursor.getColumnIndex("engine");
            cursor.moveToFirst();
            String carList = "";
            if(cursor.getCount() > 0) {
                do {
                    String id = cursor.getString(idCol);
                    String model = cursor.getString(modelCol);
                    String mark = cursor.getString(markCol);
                    String engine = cursor.getString(engineCol);
                    carList = carList + id + ": " + mark + " " + model + " " + engine + "\n";
                } while(cursor.moveToNext());

                carListEditText.setText(carList);
                cursor.close();
            } else {
                showToast("NO RESULTS");
                carListEditText.setText("");
            }
        } catch (Exception e) {
            showToast(e.getMessage());
        }
    }

    // This method creates the menu on the app
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // Called when a options menu item is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // We check what menu item was clicked and show a Toast
        if (id == R.id.action_settings) {

            // A DialogFragment is a Fragment you can place over top
            // the current Activity. A Fragment is like an interface
            // block that you can place into an Activity.
            // The FrgamentManager allows you to interact with the
            // Fragment
            DialogFragment myFragment = new MyDialogFragment();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                myFragment.show(getFragmentManager(), "dialog");
            }

            return true;
            // If exit was clicked close the app
        } else if (id == R.id.exit_the_app) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void goToCarView(View view) {
        Intent getNameScrenIntent = new Intent(this, CarActivity.class);
        final int result = 1;
        getNameScrenIntent.putExtra("callingActivity", "MainActivity");
        //startActivities(getNameScrenIntent);
        startActivityForResult(getNameScrenIntent,result);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String modelSentBack = data.getStringExtra("carModel");
        showToast("Car saved: " + modelSentBack);
    }
}
