package kandz.me.shipenginehourslog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class EngineDBhelper extends SQLiteOpenHelper {

    private final static String TAG = EngineDBhelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "engineMaintanceLogDB.db";

    private static final String TABLE_ENGINE = "engines";
    private static final String TABLE_ENGINE_KEY_EID = "eid";
    private static final String TABLE_ENGINE_KEY_ENAME = "ename";
    private static final String TABLE_ENGINE_KEY_EDESC = "edesc";
    private static final String TABLE_ENGINE_KEY_ELASTMAINTANCEID = "elastmaintanceid";

    private static final String TABLE_MAINTANCE = "maintances";
    private static final String TABLE_MAINTANCE_MID = "mid";
    private static final String TABLE_MAINTANCE_MNAME = "mname";
    private static final String TABLE_MAINTANCE_MDESC = "mdesc";
    private static final String TABLE_MAINTANCE_MHOURS = "mhours";

    private static final String TABLE_HOURS = "hours";
    private static final String TABLE_HOURS_HID = "hid";
    private static final String TABLE_HOURS_HDATE = "hdate";
    private static final String TABLE_HOURS_HTIME = "htime";
    private static final String TABLE_HOURS_HEID = "heid";
    private static final String TABLE_HOURS_HAMOUNT = "hamount";
    private static final String TABLE_HOURS_HAMOUNT_MINUTES = "hamountminutes"; //Added on version 2

    private static final String TABLE_TOTALRECORDS = "totalrecords";
    private static final String TABLE_TOTALRECORDS_TMID = "tmid";
    private static final String TABLE_TOTALRECORDS_TEID = "teid";
    private static final String TABLE_TOTALRECORDS_TTOTAL = "ttotal";
    private static final String TABLE_TOTALRECORDS_TTOTALMINUtES = "ttotalminutes";  //Added on version 2

    private SQLiteDatabase mWritableDB;
    private SQLiteDatabase mRreadableDB;
    private Cursor cursor;
    private Context context;

    private static final String CREATE_TABLE_ENGINE = "create table if not exists " + TABLE_ENGINE +
            "(" + TABLE_ENGINE_KEY_EID + " integer primary key autoincrement, " +
            TABLE_ENGINE_KEY_ENAME + " text, " +
            TABLE_ENGINE_KEY_EDESC + " text, " +
            TABLE_ENGINE_KEY_ELASTMAINTANCEID + " integer) ";

    private static final String CREATE_TABLE_MAINTANCE = " create table if not exists " + TABLE_MAINTANCE +
            "(" + TABLE_MAINTANCE_MID + " integer primary key autoincrement, " +
            TABLE_MAINTANCE_MNAME + " text, " +
            TABLE_MAINTANCE_MDESC + " text, " +
            TABLE_MAINTANCE_MHOURS + " integer)";

    private static final String CREATE_TABLE_HOURS = "create table if not exists " + TABLE_HOURS +
            "(" + TABLE_HOURS_HID + " integer primary key autoincrement, " +
            TABLE_HOURS_HDATE + " text, " +
            TABLE_HOURS_HTIME + " text, " +
            TABLE_HOURS_HEID + " integer, " +
            TABLE_HOURS_HAMOUNT + " integer, "+
            TABLE_HOURS_HAMOUNT_MINUTES +" integer)";  // changed on version 2

    private static final String CREATE_TABLE_TOTALRECORDS = "create table if not exists " + TABLE_TOTALRECORDS +
            "(" + TABLE_TOTALRECORDS_TMID + " integer," +
            TABLE_TOTALRECORDS_TEID + " integer," +
            TABLE_TOTALRECORDS_TTOTAL + " integer," +
            TABLE_TOTALRECORDS_TTOTALMINUtES + " integer)"; //changed on version 2

    public EngineDBhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mWritableDB = getWritableDatabase(); //needs to be called on constructor, it will create the dbs from onCreate
        mRreadableDB = getReadableDatabase();
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ENGINE);
        db.execSQL(CREATE_TABLE_HOURS);
        db.execSQL(CREATE_TABLE_MAINTANCE);
        db.execSQL(CREATE_TABLE_TOTALRECORDS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion<2){
            String updateTableTotalRecords ="alter table "+TABLE_TOTALRECORDS+" add column "+ TABLE_TOTALRECORDS_TTOTALMINUtES+ " integer default 0";
            db.execSQL(updateTableTotalRecords);
            String updateTableHours = "alter table "+ TABLE_HOURS+ " add column "+TABLE_HOURS_HAMOUNT_MINUTES+" integer default 0";
            db.execSQL(updateTableHours);
        }
    }


    /**
     * Add or update an engine
     *
     * @param engine engineClass object
     */
    public void addOrReplaceEngine(EngineClass engine) {
        if (mWritableDB == null) mWritableDB = getWritableDatabase();

        if (engine.geteId() == 0) {
            try {
                String insertSql = "insert into " + TABLE_ENGINE + " (" + TABLE_ENGINE_KEY_ENAME + "," + TABLE_ENGINE_KEY_EDESC + "," + TABLE_ENGINE_KEY_ELASTMAINTANCEID + ") values('" +
                        engine.geteName() + "','" + engine.geteDesc() + "','0')";
                mWritableDB.execSQL(insertSql);
            } catch (Exception e) {
                Log.d(TAG, "Error: " + e.toString());
            }
        } else {
            ContentValues values = new ContentValues();
            values.put(TABLE_ENGINE_KEY_ENAME, engine.geteName());
            values.put(TABLE_ENGINE_KEY_EDESC, engine.geteDesc());
            mWritableDB.update(TABLE_ENGINE, values, TABLE_ENGINE_KEY_EID + "=" + engine.geteId(), null);
        }
    }

    /**
     * Add or update a maintenance routine
     *
     * @param routine maintenance routine class
     */
    public void addOrReplaceMaintenanceRoutine(MaintanceRoutinesClass routine) {
        if (mWritableDB == null) mWritableDB = getWritableDatabase();
        try {

            ContentValues values = new ContentValues();
            values.put(TABLE_MAINTANCE_MNAME, routine.getmName());
            values.put(TABLE_MAINTANCE_MDESC, routine.getmDesc());
            values.put(TABLE_MAINTANCE_MHOURS, routine.getmHours());

            if (routine.getMid() == 0) {
                long newRowId = mWritableDB.insert(TABLE_MAINTANCE, null, values);
            } else {
                mWritableDB.update(TABLE_MAINTANCE, values, TABLE_MAINTANCE_MID + "=" + routine.getMid(), null);
            }
        } catch (Exception e) {
            Log.d(TAG, "Error :" + e.toString());
        }
    }

    /**
     * Add ours to specific engine.
     *
     * @param hour
     */
    public void addHoursToEngine(HourClass hour) {
        if (mWritableDB == null) mWritableDB = getWritableDatabase();

        ArrayList<MaintanceRoutinesClass> routines = this.getAllMaintenanceRoutines();
        ArrayList<TotalRecordsClass> totalRecordsList = this.getAllRecordsForEngine(hour.gethEId());

        if (totalRecordsList.size() == 0) {
            //create all records to total records with all routines for this engine
            for (MaintanceRoutinesClass m : routines) {
                TotalRecordsClass tmpTot = new TotalRecordsClass(m.getMid(), hour.gethEId(), hour.gethAmount(), hour.gethAmountMinutes());
                this.insertTotalRecord(tmpTot);
            }
        } else {
            // iterate and update the records
            for (MaintanceRoutinesClass m : routines) {
                TotalRecordsClass tmpTotRec = this.getTotalRecordForEngine(m.getMid(), hour.gethEId());
                int oldtotal = tmpTotRec.gettTotal();
                int oldtotalMinutes = tmpTotRec.gettTotalMinutes();

                //calculate extra minutes and hours
                int totalMinutes = oldtotalMinutes+hour.gethAmountMinutes();
                int newHours = oldtotal+hour.gethAmount()+ (totalMinutes/60);
                int newMinutes = totalMinutes%60;

                tmpTotRec.settTotal(newHours);
                tmpTotRec.settTotalMinutes(newMinutes);
                if (tmpTotRec.gettMid()==0) tmpTotRec.settMid(m.getMid());
                if (tmpTotRec.gettEid()==0) tmpTotRec.settEid(hour.gethEId());

                if (!this.updateTotalRecord(tmpTotRec)) {
                    this.insertTotalRecord(tmpTotRec);
                }
            }

        }

        try {
            ContentValues values = new ContentValues();
            values.put(TABLE_HOURS_HDATE, hour.gethDate());
            values.put(TABLE_HOURS_HTIME, hour.gethTime());
            values.put(TABLE_HOURS_HAMOUNT, hour.gethAmount());
            values.put(TABLE_HOURS_HEID, hour.gethEId());
            values.put(TABLE_HOURS_HAMOUNT_MINUTES, hour.gethAmountMinutes());

            long newRowId = mWritableDB.insert(TABLE_HOURS, null, values);

        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }

    }

    /**
     * insert record to totalRecord for engine and routine
     *
     * @param totRec totalrecord class
     */
    public void insertTotalRecord(TotalRecordsClass totRec) {

        ContentValues value = new ContentValues();
        value.put(TABLE_TOTALRECORDS_TMID, totRec.gettMid());
        value.put(TABLE_TOTALRECORDS_TEID, totRec.gettEid());
        value.put(TABLE_TOTALRECORDS_TTOTAL, totRec.gettTotal());
        value.put(TABLE_TOTALRECORDS_TTOTALMINUtES,totRec.gettTotalMinutes());

        mWritableDB.insert(TABLE_TOTALRECORDS, null, value);
    }

    /**
     * Get all Engines from the DB
     *
     * @return Arraylist with the engineClass objects
     */
    public ArrayList<EngineClass> getAllEngines() {
        ArrayList<EngineClass> engines = new ArrayList<EngineClass>();

        String selectQuery = "select * from " + TABLE_ENGINE + " order by " + TABLE_ENGINE_KEY_EID;

        cursor = mRreadableDB.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                EngineClass tmpEngine = new EngineClass();
                tmpEngine.seteId(cursor.getInt(0));
                tmpEngine.seteName(cursor.getString(1));
                tmpEngine.seteDesc(cursor.getString(2));
                tmpEngine.seteLastMaintanceId(cursor.getInt(3));
                engines.add(tmpEngine);
            } while (cursor.moveToNext());
        }
        return engines;
    }

    /**
     * Get all maintenance routines from the DB
     *
     * @return Arraylist with maintenanceClass objects
     */
    public ArrayList<MaintanceRoutinesClass> getAllMaintenanceRoutines() {
        ArrayList<MaintanceRoutinesClass> routines = new ArrayList<MaintanceRoutinesClass>();

        String selectQuery = "select * from " + TABLE_MAINTANCE + " order by " + TABLE_MAINTANCE_MID;
        cursor = mRreadableDB.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                MaintanceRoutinesClass tmpRoutine = new MaintanceRoutinesClass();
                tmpRoutine.setMid(cursor.getInt(0));
                tmpRoutine.setmName(cursor.getString(1));
                tmpRoutine.setmDesc(cursor.getString(2));
                tmpRoutine.setmHours(cursor.getInt(3));
                routines.add(tmpRoutine);
            } while (cursor.moveToNext());
        }
        return routines;
    }

    /**
     * Get all maintenance routines from the DB orderby Amount Asc
     *
     * @return Arraylist with maintenanceClass objects
     */
    public ArrayList<MaintanceRoutinesClass> getAllMaintenanceRoutinesAsc() {
        ArrayList<MaintanceRoutinesClass> routines = new ArrayList<MaintanceRoutinesClass>();

        String selectQuery = "select * from " + TABLE_MAINTANCE + " order by " + TABLE_MAINTANCE_MHOURS;
        cursor = mRreadableDB.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                MaintanceRoutinesClass tmpRoutine = new MaintanceRoutinesClass();
                tmpRoutine.setMid(cursor.getInt(0));
                tmpRoutine.setmName(cursor.getString(1));
                tmpRoutine.setmDesc(cursor.getString(2));
                tmpRoutine.setmHours(cursor.getInt(3));
                routines.add(tmpRoutine);
            } while (cursor.moveToNext());
        }
        return routines;
    }

    /**
     * get all the hour logs for specific engine
     *
     * @param eId the Engine id
     * @return return the list for the logs
     */
    public ArrayList<HourClass> getAllLogsForEngine(int eId) {
        ArrayList<HourClass> hours = new ArrayList<HourClass>();

        String searchQuery = "select * from " + TABLE_HOURS + " where " + TABLE_HOURS_HEID + "='" + String.valueOf(eId) + "' order by " + TABLE_HOURS_HID + " desc";
        cursor = mRreadableDB.rawQuery(searchQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HourClass tmpHour = new HourClass();
                tmpHour.sethId(cursor.getInt(0));
                tmpHour.sethDate(cursor.getString(1));
                tmpHour.sethTime(cursor.getString(2));
                tmpHour.sethEId(cursor.getInt(3));
                tmpHour.sethAmount(cursor.getInt(4));
                tmpHour.sethAmountMinutes(cursor.getInt(5));
                hours.add(tmpHour);
            } while (cursor.moveToNext());
        }
        return hours;
    }

    /**
     * get all totalrecords for specific engine
     *
     * @param eId the engine id
     * @return total routine records for the engine
     */
    public ArrayList<TotalRecordsClass> getAllRecordsForEngine(int eId) {
        ArrayList<TotalRecordsClass> totRec = new ArrayList<TotalRecordsClass>();

        try {
            String searchQuery = "select * from " + TABLE_TOTALRECORDS + " where " + TABLE_TOTALRECORDS_TEID + "='" + String.valueOf(eId) + "'";
            cursor = mRreadableDB.rawQuery(searchQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    TotalRecordsClass tmpTotRec = new TotalRecordsClass();
                    tmpTotRec.settMid(cursor.getInt(0));
                    tmpTotRec.settEid(cursor.getInt(1));
                    tmpTotRec.settTotal(cursor.getInt(2));
                    totRec.add(tmpTotRec);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
        return totRec;
    }

    /**
     * get total Record class for specifig routine and engine
     *
     * @param mId routine
     * @param eId engine
     * @return returns a TotalRecordClass object
     */
    public TotalRecordsClass getTotalRecordForEngine(int mId, int eId) {
        TotalRecordsClass tmpTotalRecord = new TotalRecordsClass();

        try {

            String searchQuery = "select * from " + TABLE_TOTALRECORDS + " where " + TABLE_TOTALRECORDS_TMID + " ='" + String.valueOf(mId) +
                    "' and " + TABLE_TOTALRECORDS_TEID + "='" + String.valueOf(eId)+"'";

            cursor = mRreadableDB.rawQuery(searchQuery, null);
            if (cursor.moveToFirst()) {
                tmpTotalRecord.settMid(cursor.getInt(0));
                tmpTotalRecord.settEid(cursor.getInt(1));
                tmpTotalRecord.settTotal(cursor.getInt(2));
                tmpTotalRecord.settTotalMinutes(cursor.getInt(3));
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
        return tmpTotalRecord;
    }

    /**
     * get all log for an engine for specific date
     * @param eId engine
     * @param date date
     * @return list of hourclass objects
     */
     public ArrayList<HourClass> getAllLogWithDate(int eId, String date){
        ArrayList<HourClass> logs = new ArrayList<HourClass>();

        try {
            String searchQuery = "select * from " +TABLE_HOURS+ " where " + TABLE_HOURS_HEID + " ='" + String.valueOf(eId) +
                    "' and " + TABLE_HOURS_HDATE+ "='" + date+"'";

            cursor = mRreadableDB.rawQuery(searchQuery,null);
            if (cursor.moveToFirst()){
                do{
                    HourClass tmpHour = new HourClass();
                    tmpHour.sethId(cursor.getInt(0));
                    tmpHour.sethDate(cursor.getString(1));
                    tmpHour.sethTime(cursor.getString(2));
                    tmpHour.sethEId(cursor.getInt(3));
                    tmpHour.sethAmount(cursor.getInt(4));
                    tmpHour.sethAmountMinutes(cursor.getInt(5));
                    logs.add(tmpHour);
                } while (cursor.moveToNext());
            }
        } catch (Exception e){
            Log.d(TAG,e.toString());
        }
                return logs;
     }

    /**
     * get all log for specific month
     * @param eId engine
     * @param date date /month
     * @return list of hourClass
     */
    public ArrayList<HourClass> getAllLogForMonth(int eId, String date){
        ArrayList<HourClass> logs = new ArrayList<HourClass>();

        try {
            String searchQuery = "select * from " +TABLE_HOURS+ " where " + TABLE_HOURS_HEID + " ='" + String.valueOf(eId) +
                    "' and " + TABLE_HOURS_HDATE+ " like '" + date+"%'";

            cursor = mRreadableDB.rawQuery(searchQuery,null);
            if (cursor.moveToFirst()){
                do{
                    HourClass tmpHour = new HourClass();
                    tmpHour.sethId(cursor.getInt(0));
                    tmpHour.sethDate(cursor.getString(1));
                    tmpHour.sethTime(cursor.getString(2));
                    tmpHour.sethEId(cursor.getInt(3));
                    tmpHour.sethAmount(cursor.getInt(4));
                    tmpHour.sethAmountMinutes(cursor.getInt(5));
                    logs.add(tmpHour);
                } while (cursor.moveToNext());
            }
        } catch (Exception e){
            Log.d(TAG,e.toString());
        }
        return logs;
    }

    /**
     * update specific totalRecord record for specific engine and routine
     *
     * @param totRec totalRecordClass object
     * @return true if updated otherwise false
     */
    public boolean updateTotalRecord(TotalRecordsClass totRec) {
        boolean updated = false;

        try {
            ContentValues values = new ContentValues();
            values.put(TABLE_TOTALRECORDS_TMID, totRec.gettMid());
            values.put(TABLE_TOTALRECORDS_TEID, totRec.gettEid());
            values.put(TABLE_TOTALRECORDS_TTOTAL, totRec.gettTotal());
            values.put(TABLE_TOTALRECORDS_TTOTALMINUtES,totRec.gettTotalMinutes());

            int updateRows = mWritableDB.update(
                    TABLE_TOTALRECORDS,
                    values,
                    TABLE_TOTALRECORDS_TMID + " = ? AND " + TABLE_TOTALRECORDS_TEID + " = ?",
                    new String[]{String.valueOf(totRec.gettMid()), String.valueOf(totRec.gettEid())});

            if (updateRows <= 0) {
                updated = false;
            } else {
                updated = true;
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
        return updated;
    }

    /**
     * Delete an engine from the engines Table
     *
     * @param engine the engine you want to delete
     * @return returns true if succesful
     */
    public boolean deleteEngine(EngineClass engine) {
        return mWritableDB.delete(TABLE_ENGINE, TABLE_ENGINE_KEY_EID + "=" + String.valueOf(engine.geteId()), null) > 0;
    }

    /**
     * Delete a routine from the maintenance table
     *
     * @param routine the routine class
     * @return true if succesful
     */
    public boolean deleteMaintenanceRoutine(MaintanceRoutinesClass routine) {
        return mWritableDB.delete(TABLE_MAINTANCE, TABLE_MAINTANCE_MID + "=" + String.valueOf(routine.getMid()), null) > 0;
    }

    public void deleteDB() {
        context.deleteDatabase(DATABASE_NAME);
    }
}
