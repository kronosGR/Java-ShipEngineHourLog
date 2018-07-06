package kandz.me.shipenginehourslog;

import android.icu.util.TimeZone;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Hours extends AppCompatActivity {

    private Spinner enginesSpinner;
    private TextView engineNameTxt;
    private EditText engineHours;
    private EditText engineMinutes;
    private AdView mAdView;

    private ArrayList<EngineClass> engines;
    EngineClass tmpClass;
    EngineDBhelper mDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hours);

        MobileAds.initialize(this, "ca-app-pub-0717744179319214/6502631736");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);


        mDB=new EngineDBhelper(this);

        enginesSpinner = (Spinner)findViewById(R.id.engineSpinner);
        engineNameTxt = (TextView)findViewById(R.id.engineNameTxt);
        engineHours = (EditText)findViewById(R.id.engineHoursEdt);
        engineMinutes = (EditText)findViewById(R.id.engineMinutesEdt);

        engines = mDB.getAllEngines();

        final SpinnerArrayAdapter adapter=new SpinnerArrayAdapter(this,R.layout.spinner_item_engines,engines);
        enginesSpinner.setAdapter(adapter);

        enginesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tmpClass = engines.get(position);
                engineNameTxt.setText(tmpClass.geteName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void addHoursToEngine(View view) {
        String regexStr = "^[0-9]*$";
        if (engineHours.length()<=0 || engineMinutes.length()<=0)
            return;
        if (Integer.parseInt(engineMinutes.getText().toString()) >59)
        {
            engineMinutes.setText("0");
            Toast.makeText(getApplicationContext(),"Minutes should have max 59 value.",Toast.LENGTH_SHORT).show();
            return;
        }
    // check if is number
        if((engineHours.getText().toString().trim().matches(regexStr)) && (engineMinutes.getText().toString().trim().matches(regexStr))) {
            int eId = tmpClass.geteId();
            HourClass tmpHour = new HourClass();
            tmpHour.sethEId(eId);
            String hourTmp =engineHours.getText().toString();
            hourTmp.replaceAll("\\s+","");  //remove all whitespaces
            tmpHour.sethAmount(Integer.parseInt(hourTmp));

            String minutesTmp = engineMinutes.getText().toString();
            minutesTmp.replaceAll("\\s+" ,"");
            tmpHour.sethAmountMinutes(Integer.parseInt(minutesTmp));

            Calendar calendar = Calendar.getInstance();
            java.util.TimeZone tz = calendar.getTimeZone();

            SimpleDateFormat smFormat = new SimpleDateFormat("yyyy/MM/dd");
            smFormat.setTimeZone(tz);
            String dateNow = smFormat.format(calendar.getTime());
            tmpHour.sethDate(dateNow);

            Date date = calendar.getTime();
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            String timeNow = dateFormat.format(calendar.getTime());
            tmpHour.sethTime(timeNow);

            mDB.addHoursToEngine(tmpHour);
            Toast.makeText(this,"Hours added to the engine",Toast.LENGTH_SHORT).show();
            engineHours.setText("0");
            engineMinutes.setText("0");
        }
        else{
            Toast.makeText(this,"Please enter an intenger!", Toast.LENGTH_SHORT).show();
        }



    }
}
