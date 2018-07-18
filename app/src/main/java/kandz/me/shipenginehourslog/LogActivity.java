package kandz.me.shipenginehourslog;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class LogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<HourClass>>{

    public static final int LOADER_ID = 999;
    public static final String LOADER_ENGINE_ID ="engineID";

    private Spinner enginesSpinner;
    private TextView logTitleTxt;

    private ArrayList<EngineClass> engines;
    private ArrayList<HourClass> hours;
    private EngineClass tmpClass;
    private EngineDBhelper mDB;

    private LogAdapterRecycler logAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerViewLog;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        mDB = new EngineDBhelper(this);

        enginesSpinner = (Spinner) findViewById(R.id.engineSpinnerLog);
        logTitleTxt=(TextView)findViewById(R.id.logTitleTxt);
        recyclerViewLog = (RecyclerView) findViewById(R.id.recyclerViewLog);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        engines = mDB.getAllEngines();
        final SpinnerArrayAdapter adapter = new SpinnerArrayAdapter(this,R.layout.spinner_item_engines,engines);
        enginesSpinner.setAdapter(adapter);

        enginesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                progressBar.setVisibility(View.VISIBLE);
                tmpClass=engines.get(position);
                logTitleTxt.setText("Log for "+tmpClass.geteName().toString());

                loadLogListView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //getSupportLoaderManager().initLoader(LOADER_ID,null,this);
    }

    private void loadLogListView() {

        //hours = mDB.getAllLogsForEngine(tmpClass.geteId());
        Bundle query = new Bundle();
        query.putInt(LOADER_ENGINE_ID,tmpClass.geteId());
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> loader = loaderManager.getLoader(LOADER_ID);
        if (loader == null)
            loaderManager.initLoader(LOADER_ID,query,this);
        else
            loaderManager.restartLoader(LOADER_ID,query,this);

        recyclerViewLog.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewLog.setLayoutManager(layoutManager);

    }

    @NonNull
    @Override
    public Loader<ArrayList<HourClass>> onCreateLoader(int id, @Nullable final Bundle args) {
        return new AsyncTaskLoader<ArrayList<HourClass>>(this) {
            @Nullable
            @Override
            public ArrayList<HourClass> loadInBackground() {
                ArrayList<HourClass> hoursTmp = mDB.getAllLogsForEngine(args.getInt(LOADER_ENGINE_ID));
                return hoursTmp;
            }

            @Override
            protected void onStartLoading() {
                forceLoad();
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<HourClass>> loader, ArrayList<HourClass> data) {
        logAdapter = new LogAdapterRecycler(data);
        recyclerViewLog.setAdapter(logAdapter);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<HourClass>> loader) {

    }
}
