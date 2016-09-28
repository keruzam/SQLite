package sqlite.keruzam.pl.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

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
        addCarButton = (Button) findViewById(R.id.addCarButton);
        deleteCarButton = (Button) findViewById(R.id.deleteCarButton);
        getCarButton = (Button) findViewById(R.id.getCarButton);
        createDBButton = (Button) findViewById(R.id.createDBButton);
        dropDBButton = (Button) findViewById(R.id.dropDBButton);
        //--------------------------------------
        markEditText = (EditText) findViewById(R.id.markEditText);
        modelEditText = (EditText) findViewById(R.id.modelEditText);
        engineEditText = (EditText) findViewById(R.id.engineEditText);
        idEditText = (EditText) findViewById(R.id.idEditText);
        carListEditText = (EditText) findViewById(R.id.carListEditText);
    }

    public void createDatabase(View view) {
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

    private void showButtons() {
        getCarButton.setVisibility(View.VISIBLE);
        dropDBButton.setVisibility(View.VISIBLE);
        deleteCarButton.setVisibility(View.VISIBLE);
        addCarButton.setVisibility(View.VISIBLE);
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

    private void cleanEditTextFields() {
        markEditText.setText("");
        modelEditText.setText("");
        engineEditText.setText("");
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
            if(cursor != null && (cursor.getCount() > 0)) {
                do {
                    String id = cursor.getString(idCol);
                    String model = cursor.getString(modelCol);
                    String mark = cursor.getString(markCol);
                    String engine = cursor.getString(engineCol);
                    carList = carList + id + ": " + mark + " " + model + " " + engine + "\n";
                } while(cursor.moveToNext());

                carListEditText.setText(carList);

            } else {
                showToast("NO RESULTS");
                carListEditText.setText("");
            }
        } catch (Exception e) {
            showToast(e.getMessage());
        }

    }
}
