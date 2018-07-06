package kandz.me.shipenginehourslog;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Maintance_routines extends AppCompatActivity {

    private LinearLayout addPanel;
    private EditText maintenanceRoutineNameEdt;
    private EditText maintenanceRoutineDescEdt;
    private EditText maintenanceRoutineHoursEdt;
    private ListView maintenanceListView;

    private EngineDBhelper mDB;
    private ArrayList<MaintanceRoutinesClass> maintenanceRoutines;
    private MaintenanceRoutinesAdapter adapter;
    private int tmpMID = 0;

    private boolean addPanelOn =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintance_routines);

        mDB = new EngineDBhelper(this);

        addPanel = (LinearLayout)findViewById(R.id.addPanelMaintanceRoutines);
        maintenanceRoutineNameEdt = (EditText)findViewById(R.id.maintanceRoutineNameEdt);
        maintenanceRoutineDescEdt = (EditText)findViewById(R.id.maintanceRoutineDescEdt);
        maintenanceRoutineHoursEdt = (EditText)findViewById(R.id.maintanceRoutineHoursEdt);
        maintenanceListView = (ListView)findViewById(R.id.listview_maintance_routines);

        loadListViewMaintenanceRoutines();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.maintenance_routines_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.addMaintenanceRoutinesMenu:
                tmpMID = 0;
                if (!addPanelOn)
                    showPanel();
                else
                    hidePanel();
                return  true;
            case R.id.helpMaintenanceRoutinesMenu:
                AlertDialog.Builder builder = new AlertDialog.Builder(this,android.R.style.Theme_Material_Dialog_Alert);
                builder.setTitle("Help")
                        .setMessage("+      Add new Maintenance routine  \n Touch      a Maintenance routine to edit \n Long Touch         to delete a Maintenance routine.")
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

    private void hidePanel() {
        float weight = 0f;
        addPanelOn=false;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                weight
        );
        addPanel.setLayoutParams(params);
    }

    private void showPanel() {
        float weight = 5f;
        addPanelOn=true;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                weight
        );
        addPanel.setLayoutParams(params);
    }

    public void saveMaintanceRoutine(View view) {
        try {
            String maintenanceRoutineName = maintenanceRoutineNameEdt.getText().toString();
            String maintenanceRoutineDesc = maintenanceRoutineDescEdt.getText().toString();
            int maintenanceRoutineHours = Integer.parseInt(maintenanceRoutineHoursEdt.getText().toString());

            if (maintenanceRoutineName.length() > 0 && maintenanceRoutineDesc.length() > 0 &&
                    maintenanceRoutineHours > 0 && maintenanceRoutineHours < 2000000) {
                MaintanceRoutinesClass tmpRoutine = new MaintanceRoutinesClass(
                        tmpMID, maintenanceRoutineName, maintenanceRoutineDesc, maintenanceRoutineHours
                );

                mDB.addOrReplaceMaintenanceRoutine(tmpRoutine);
                Toast.makeText(this, "Maintenance routine save.", Toast.LENGTH_SHORT).show();
                maintenanceRoutineDescEdt.setText("");
                maintenanceRoutineHoursEdt.setText("0");
                maintenanceRoutineNameEdt.setText("");
                hidePanel();

                loadListViewMaintenanceRoutines();

            } else {
                Toast.makeText(this, "Name and Info must not be empty", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){

        }
    }

    private void loadListViewMaintenanceRoutines() {
        maintenanceRoutines=mDB.getAllMaintenanceRoutines();
        adapter=new MaintenanceRoutinesAdapter(this,maintenanceRoutines);
        maintenanceListView.setAdapter(adapter);

        maintenanceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MaintanceRoutinesClass tmpRoutine = (MaintanceRoutinesClass)adapter.getItem(position);
                tmpMID=tmpRoutine.getMid();
                maintenanceRoutineNameEdt.setText(tmpRoutine.getmName());
                maintenanceRoutineDescEdt.setText(tmpRoutine.getmDesc());
                maintenanceRoutineHoursEdt.setText(String.valueOf(tmpRoutine.getmHours()));
                showPanel();
            }
        });

        maintenanceListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                MaintanceRoutinesClass tmpRoutine =(MaintanceRoutinesClass)adapter.getItem(position);
                if (mDB.deleteMaintenanceRoutine(tmpRoutine))
                {
                    Toast.makeText(view.getContext(),"Maintenance routine deleted",Toast.LENGTH_SHORT).show();
                    loadListViewMaintenanceRoutines();
                }
                else
                    Toast.makeText(view.getContext(),"Problem!!! Nothing happened.",Toast.LENGTH_SHORT).show();

                return true;
            }
        });
    }
}
