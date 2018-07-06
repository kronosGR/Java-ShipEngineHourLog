package kandz.me.shipenginehourslog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class LogActivity extends AppCompatActivity {

    private Spinner enginesSpinner;
    private TextView logTitleTxt;
    private ListView listViewLog;

    private ArrayList<EngineClass> engines;
    private ArrayList<HourClass> hours;
    private EngineClass tmpClass;
    private EngineDBhelper mDB;

    private LogAdapter logAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        mDB = new EngineDBhelper(this);

        enginesSpinner = (Spinner) findViewById(R.id.engineSpinnerLog);
        logTitleTxt=(TextView)findViewById(R.id.logTitleTxt);
        listViewLog = (ListView)findViewById(R.id.listViewLog);

        engines = mDB.getAllEngines();
        final SpinnerArrayAdapter adapter = new SpinnerArrayAdapter(this,R.layout.spinner_item_engines,engines);
        enginesSpinner.setAdapter(adapter);

        enginesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tmpClass=engines.get(position);
                logTitleTxt.setText("Log for "+tmpClass.geteName().toString());

                loadLogListView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadLogListView() {
        hours = mDB.getAllLogsForEngine(tmpClass.geteId());
        logAdapter = new LogAdapter(this,hours);
        listViewLog.setAdapter(logAdapter);
    }
}
