package sqlite.keruzam.pl.sqlite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by fuks on 2016-09-30.
 *
 * Car screen
 */

public class CarActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        Intent activityThatCalled = getIntent();
        String prevActivity = activityThatCalled.getExtras().getString("callingActivity");
        TextView callingActivityMessage = (TextView) findViewById(R.id.calling_activity_info_text_view);

        callingActivityMessage.append(" " + prevActivity);
    }

    public void onSaveCar(View view) {
        EditText carModelEditText = (EditText) findViewById(R.id.carModelEditText);
        String carModel = String.valueOf(carModelEditText.getText());
        Intent goingBack = new Intent();
        goingBack.putExtra("carModel", carModel);
        setResult(RESULT_OK, goingBack);
        finish();
    }
}
