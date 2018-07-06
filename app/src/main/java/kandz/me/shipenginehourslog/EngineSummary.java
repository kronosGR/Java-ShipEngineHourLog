package kandz.me.shipenginehourslog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class EngineSummary extends AppCompatActivity {

    private Spinner engineSumSpinner;
    private TextView sumTitleTxt;
    private TableLayout tableLayout;

    private ArrayList<EngineClass> engines;

    private EngineClass tmpEngine;
    private EngineDBhelper mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engine_summary);

        mDB = new EngineDBhelper(this);

        engineSumSpinner = (Spinner) findViewById(R.id.engineSpinnerSum);
        sumTitleTxt = (TextView) findViewById(R.id.sumTitleTxt);
        tableLayout = (TableLayout) findViewById(R.id.tableLayoutSum);

        engines = mDB.getAllEngines();
        SpinnerArrayAdapter adapter = new SpinnerArrayAdapter(this,R.layout.spinner_item_engines,engines);
        engineSumSpinner.setAdapter(adapter);

        engineSumSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tmpEngine = engines.get(position);
                sumTitleTxt.setText("Summary for "+tmpEngine.geteName());
                showSummary();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.summary_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.helpSummary:
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this,android.R.style.Theme_Material_Dialog_Alert);
                builder.setTitle("Info")
                        .setMessage("Update the records with the \"Update\" Button, only if you need to reset, or update a maintenance routine that you added afterwards ")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * show summary for the selected engine
     */
    private void showSummary(){
        tableLayout.removeAllViews();

        addTodaySummary();
        addMonthSummary();
        addAllTimeSummary();
        addRoutinesSummary();
    }


    private void addTodaySummary() {
        TableRow tr =new TableRow(this);

        TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT,
                0f
        );
        tr.setLayoutParams(params);

        TableRow.LayoutParams par1 = new TableRow.LayoutParams(
                0,
                TableLayout.LayoutParams.WRAP_CONTENT,
                1f
        );
        TextView txt1= new TextView(this);
        txt1.setText("Today");
        txt1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
        txt1.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        txt1.setLayoutParams(par1);
        tr.addView(txt1,0);

        TableRow.LayoutParams par2 = new TableRow.LayoutParams(
                0,
                TableLayout.LayoutParams.WRAP_CONTENT,
                1f
        );

        //Get total for one day
        Calendar calendar =Calendar.getInstance();
        TimeZone tz = calendar.getTimeZone();
        SimpleDateFormat smFormat =new SimpleDateFormat("yyyy/MM/dd");
        smFormat.setTimeZone(tz);
        String today = smFormat.format(calendar.getTime());

        int amountToday=0;
        int amountTodayMinutes=0;
        ArrayList<HourClass> logs = mDB.getAllLogWithDate(tmpEngine.geteId(),today);

        for(HourClass h :logs){
            amountToday+=h.gethAmount();
            amountTodayMinutes+=h.gethAmountMinutes();
        }

        amountToday= amountToday+ (amountTodayMinutes/60);
        int finalMinutes = amountTodayMinutes%60;

        TextView txt2= new TextView(this);
        txt2.setText(String.valueOf(amountToday)+" hours");
        txt2.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        txt2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        txt2.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        txt2.setLayoutParams(par2);
        tr.addView(txt2,1);

        TableRow.LayoutParams par3 = new TableRow.LayoutParams(
                0,
                TableLayout.LayoutParams.WRAP_CONTENT,
                1f
        );
        TextView txt3= new TextView(this);
        txt3.setText(finalMinutes+" minutes");
        txt3.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        txt3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        txt3.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        txt3.setLayoutParams(par3);
        tr.addView(txt3,2);


        tableLayout.addView(tr);
    }

    /**
     * shows today's summary for specific engine
     */
    private void addMonthSummary() {
        TableRow tr = new TableRow(this);

        TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT,
                0f
        );
        tr.setLayoutParams(params);

        TableRow.LayoutParams par1 = new TableRow.LayoutParams(
                0,
                TableLayout.LayoutParams.WRAP_CONTENT,
                1f
        );
        TextView txt1 = new TextView(this);
        txt1.setText("Month");
        txt1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
        txt1.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        txt1.setLayoutParams(par1);
        tr.addView(txt1,0);

        TableRow.LayoutParams par2 =new TableRow.LayoutParams(
                0,
                TableLayout.LayoutParams.WRAP_CONTENT,
                1f
        );

        Calendar calendar = Calendar.getInstance();
        TimeZone tz = calendar.getTimeZone();
        SimpleDateFormat smFormat = new SimpleDateFormat("yyyy/MM/dd");
        smFormat.setTimeZone(tz);
        String today =smFormat.format(calendar.getTime());
        String month = today.substring(0, today.lastIndexOf('/'));

        int amountToday=0;
        int amountTodayMinutes=0;
        ArrayList<HourClass> logs = mDB.getAllLogForMonth(tmpEngine.geteId(),month);

        for(HourClass h :logs){
            amountToday+=h.gethAmount();
            amountTodayMinutes+=h.gethAmountMinutes();
        }

        amountToday= amountToday+ (amountTodayMinutes/60);
        int finalMinutes = amountTodayMinutes%60;

        TextView txt2= new TextView(this);
        txt2.setText(String.valueOf(amountToday) + " hours");
        txt2.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        txt2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        txt2.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        txt2.setLayoutParams(par2);
        tr.addView(txt2,1);

        TableRow.LayoutParams par3 =new TableRow.LayoutParams(
                0,
                TableLayout.LayoutParams.WRAP_CONTENT,
                1f
        );
        TextView txt3= new TextView(this);
        txt3.setText(finalMinutes+" minutes");
        txt3.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        txt3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        txt3.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        txt3.setLayoutParams(par3);
        tr.addView(txt3,2);
        tableLayout.addView(tr);
    }

    /**
     * show the month's summary for a specific engine
     */
    private void addAllTimeSummary() {
        TableRow tr = new TableRow(this);

        TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT,
                0f
        );
        tr.setLayoutParams(params);

        TableRow.LayoutParams par1 = new TableRow.LayoutParams(
                0,
                TableLayout.LayoutParams.WRAP_CONTENT,
                1f
        );
        TextView txt1 = new TextView(this);
        txt1.setText("All time");
        txt1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
        txt1.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        txt1.setLayoutParams(par1);
        tr.addView(txt1,0);

        TableRow.LayoutParams par2 =new TableRow.LayoutParams(
                0,
                TableLayout.LayoutParams.WRAP_CONTENT,
                1f
        );

        int amountToday=0;
        int amountTodayMinutes=0;
        ArrayList<HourClass> logs = mDB.getAllLogsForEngine(tmpEngine.geteId());

        for(HourClass h :logs){
            amountToday+=h.gethAmount();
            amountTodayMinutes+=h.gethAmountMinutes();
        }

        amountToday= amountToday+ (amountTodayMinutes/60);
        int finalMinutes = amountTodayMinutes%60;

        TextView txt2= new TextView(this);
        txt2.setText(String.valueOf(amountToday) + " hours");
        txt2.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        txt2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        txt2.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        txt2.setLayoutParams(par2);
        tr.addView(txt2,1);

        TableRow.LayoutParams par3 =new TableRow.LayoutParams(
                0,
                TableLayout.LayoutParams.WRAP_CONTENT,
                1f
        );
        TextView txt3= new TextView(this);
        txt3.setText(finalMinutes+" minutes");
        txt3.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        txt3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        txt3.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        txt3.setLayoutParams(par3);
        tr.addView(txt3,2);
        tableLayout.addView(tr);
    }

    /**
     * Add the routines that are available for the specific engine
     */
    private void addRoutinesSummary() {

        ArrayList<MaintanceRoutinesClass> routines = mDB.getAllMaintenanceRoutinesAsc();
        for (MaintanceRoutinesClass mr : routines)
        {
            TotalRecordsClass totalRecord = mDB.getTotalRecordForEngine(mr.getMid(), tmpEngine.geteId());


            TableRow tr = new TableRow(this);
            TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    0f
            );
            tr.setLayoutParams(params);

            TableRow.LayoutParams par1 = new TableRow.LayoutParams(
                    0,
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    0.8f
            );
            TextView txt1 = new TextView(this);
            txt1.setText(mr.getmName());
            txt1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
            txt1.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            txt1.setLayoutParams(par1);
            tr.addView(txt1,0);

            TableRow.LayoutParams par2 =new TableRow.LayoutParams(
                    0,
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    0.8f
            );
            EditText txt2= new EditText(this);
            txt2.setGravity(Gravity.RIGHT);
            txt2.setText(String.valueOf(totalRecord.gettTotal()));
            txt2.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            txt2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            txt2.setMaxWidth(7);
            txt2.setTag(String.valueOf(totalRecord.gettMid()));
            txt2.setEms(7);
            txt2.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
            txt2.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
            txt2.setLayoutParams(par2);
            tr.addView(txt2,1);

            TableRow.LayoutParams par3 =new TableRow.LayoutParams(
                    0,
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    0.3f
            );
            EditText txt3= new EditText(this);
            txt3.setGravity(Gravity.RIGHT);
            txt3.setText(String.valueOf(totalRecord.gettTotalMinutes()));
            txt3.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            txt3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            txt3.setMaxWidth(7);
            txt3.setTag(String.valueOf(totalRecord.gettMid())+"|minutes");
            txt3.setEms(7);
            txt3.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
            txt3.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
            txt3.setLayoutParams(par3);
            tr.addView(txt3,2);



            TableRow.LayoutParams par4 =new TableRow.LayoutParams(
                    0,
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    1f
            );

            Button btn = new Button(this);
            btn.setText("Update");
            btn.setLayoutParams(par4);
            btn.setTag(totalRecord);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    TotalRecordsClass tmpTR= (TotalRecordsClass)v.getTag();

                    ArrayList<View> result = getViewsByTag((ViewGroup)findViewById(R.id.tableLayoutSum),String.valueOf(tmpTR.gettMid()));
                    EditText tmpEdit = (EditText)result.get(0);

                    ArrayList<View> resultMinutes= getViewsByTag((ViewGroup)findViewById(R.id.tableLayoutSum),String.valueOf(tmpTR.gettMid()+"|minutes"));
                    EditText tmpEditMinutes = (EditText)resultMinutes.get(0);

                    if (Integer.parseInt(tmpEditMinutes.getText().toString())>59 ){
                        Toast.makeText(getApplicationContext(),"Minutes should have max 59 value.",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Log.d("-----",tmpEditMinutes.getText().toString());
                    tmpTR.settTotal(Integer.parseInt(tmpEdit.getText().toString()));
                    tmpTR.settTotalMinutes(Integer.parseInt(tmpEditMinutes.getText().toString()));
                    mDB.updateTotalRecord(tmpTR);
                    Toast.makeText(getApplicationContext(),"Record Updated." ,Toast.LENGTH_SHORT).show();
                    showSummary();
                    /*
                    Log.d("----",String.valueOf(tmpTR.gettEid()));
                    Log.d("--M-",String.valueOf( tmpTR.gettMid()));
                    Log.d("----",String.valueOf( tmpTR.gettTotal()));
                    */
                }
            });

            tr.addView(btn,3);
            tableLayout.addView(tr);
        }


    }
    private static ArrayList<View> getViewsByTag(ViewGroup root, String tag){
        ArrayList<View> views = new ArrayList<View>();
        final int childCount = root.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = root.getChildAt(i);
            if (child instanceof ViewGroup) {
                views.addAll(getViewsByTag((ViewGroup) child, tag));
            }

            final Object tagObj = child.getTag();
            if (tagObj != null && tagObj.equals(tag)) {
                views.add(child);
            }

        }
        return views;
    }

    public static List<View> findViewWithTagRecursively(ViewGroup root, Object tag){
        List<View> allViews = new ArrayList<View>();

        final int childCount = root.getChildCount();
        for(int i=0; i<childCount; i++){
            final View childView = root.getChildAt(i);

            if(childView instanceof ViewGroup){
                allViews.addAll(findViewWithTagRecursively((ViewGroup)childView, tag));
            }
            else{
                final Object tagView = childView.getTag();
                if(tagView != null && tagView.equals(tag))
                    allViews.add(childView);
            }
        }

        return allViews;
    }
}
