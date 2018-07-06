package kandz.me.shipenginehourslog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class LogAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HourClass> hoursList;
    private LayoutInflater inflater;

    public LogAdapter(Context context, ArrayList<HourClass> hoursList) {
        this.context = context;
        this.hoursList = hoursList;
        inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return hoursList.size();
    }

    @Override
    public Object getItem(int position) {
        return hoursList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(R.layout.list_item_log,parent,false);

        TextView dateTxt = (TextView)view.findViewById(R.id.list_item_log_date);
        TextView timeTxt = (TextView)view.findViewById(R.id.list_item_log_time);
        TextView hoursTxt  =(TextView)view.findViewById(R.id.list_item_hours);
        TextView minutesTxt = (TextView)view.findViewById(R.id.list_item_minutes);

        HourClass tmpHour=hoursList.get(position);
        dateTxt.setText(tmpHour.gethDate());
        timeTxt.setText(tmpHour.gethTime());
        hoursTxt.setText(String.valueOf(tmpHour.gethAmount()));
        minutesTxt.setText(String.valueOf(tmpHour.gethAmountMinutes()));

        return view;
    }
}
