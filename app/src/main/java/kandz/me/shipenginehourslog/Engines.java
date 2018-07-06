package kandz.me.shipenginehourslog;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Engines extends AppCompatActivity {

    private LinearLayout addPanel;
    private EditText engineNameEdt;
    private EditText engineDescEdt;
    private ListView engineListView;

    private EngineDBhelper mDB;
    private ArrayList<EngineClass> engines;
    private EngineAdapter adapter;
    private int tmpEID =0;

    private boolean addPanelOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engines);

        mDB = new EngineDBhelper(this);

        addPanel = (LinearLayout)findViewById(R.id.addPanel);
        engineNameEdt =(EditText)findViewById(R.id.engineNameEdt);
        engineDescEdt =  (EditText)findViewById(R.id.engineDescEdt);
        engineListView = (ListView)findViewById(R.id.listview_engines);

       loadListViewEngines();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.engines_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.addEnginesMenu:
                tmpEID=0;
                if (!addPanelOn){
                   showSavePanel();
                }
                else
                {
                    hideSavePanel();
                }
                return true;
            case R.id.helpEngineMenu:
                AlertDialog.Builder builder = new AlertDialog.Builder(this,android.R.style.Theme_Material_Dialog_Alert);
                builder.setTitle("Help")
                        .setMessage("+      Add new engine \n Touch      an engine to edit \n Long Touch         to delete an engine.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    /**
     * hide the panel
     */
    private void hideSavePanel() {
        float weight=0f;
        addPanelOn=false;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                weight
        );
        addPanel.setLayoutParams(params);
    }

    /**
     * show the save panel
     */
    private void showSavePanel(){
        float weight=4f;
        addPanelOn=true;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                weight
        );
        addPanel.setLayoutParams(params);
    }

    /**
     * Save update the panel. It is called from the save button
     * @param view
     */
    public void saveEngine(View view) {

        String engineName = engineNameEdt.getText().toString();
        String engineDesc = engineDescEdt.getText().toString();

        if (engineName.length()>0 && engineDesc.length()>0){
            EngineClass tmpEngine = new EngineClass(tmpEID,engineName,engineDesc,0);
            mDB.addOrReplaceEngine(tmpEngine);

            Toast.makeText(this,"Engine Saved.", Toast.LENGTH_SHORT).show();

            engineNameEdt.setText("");
            engineDescEdt.setText("");

            hideSavePanel();
            loadListViewEngines();


        }
        else {
            Toast.makeText(this,"Name and Info must not be empty", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Load all Engines and show them to the listview
     */
    private void loadListViewEngines(){
        engines = mDB.getAllEngines();
        adapter = new EngineAdapter(this, engines);
        engineListView.setAdapter(adapter);

        engineListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EngineClass tmpEngine = (EngineClass) adapter.getItem(position);
                tmpEID=tmpEngine.geteId();
                engineDescEdt.setText(tmpEngine.geteDesc());
                engineNameEdt.setText(tmpEngine.geteName());
                showSavePanel();
            }
        });

        engineListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                EngineClass tmpEngine = (EngineClass) adapter.getItem(position);
                if (mDB.deleteEngine(tmpEngine))
                {
                    Toast.makeText(view.getContext(),"Engine deleted",Toast.LENGTH_SHORT).show();
                    loadListViewEngines();
                }
                else
                    Toast.makeText(view.getContext(),"Problem!!! Nothing happened.",Toast.LENGTH_SHORT).show();

                return true;
            }
        });
    }
}
