package kandz.me.shipenginehourslog;

import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import me.kandz.kz.KZActivity;

public class MainActivity extends AppCompatActivity {

    private EngineDBhelper mDB;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // test banner ad id ca-app-pub-3940256099942544/6300978111

        MobileAds.initialize(this, "ca-app-pub-0717744179319214/6502631736");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);

        mDB=new EngineDBhelper(this);

        //delete DB
        // mDB.deleteDB();
    }

    public void engineSettingsClick(View view) {
        startActivity(new Intent(this, Engines.class));
    }

    public void routinesClick(View view) {
        startActivity(new Intent(this,Maintance_routines.class));
    }

    public void hoursClick(View view) {
        startActivity(new Intent(this,Hours.class));
    }

    public void logClick(View view) {
        startActivity(new Intent(this,LogActivity.class));
    }

    public void maintanceSummaryClick(View view) {
        startActivity(new Intent(this,EngineSummary.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.helpMainMenu:
                AlertDialog.Builder builder = new AlertDialog.Builder(this,android.R.style.Theme_Material_Dialog_Alert);
                builder.setTitle("Important")
                        .setMessage("1. Add engines \n2. Add Maintenance routines and \n3. Add working hours to engine, \n\n It is important to add all engines and maintenance routines before you add working hours")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                return true;
            case R.id.exitMainMenu:
                System.exit(0);
                return true;
            case R.id.kandzMainMenu:
                startActivity(new Intent(this, KZActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
