package kandz.me.shipenginehourslog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MaintenanceRoutinesAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<MaintanceRoutinesClass> routinesList;

    public MaintenanceRoutinesAdapter(Context context, ArrayList<MaintanceRoutinesClass> routinesList) {
        this.context = context;
        this.routinesList = routinesList;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return routinesList.size();
    }

    @Override
    public Object getItem(int position) {
        return routinesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(R.layout.list_item_maintenance_routines,parent,false);

        TextView maintenanceName = (TextView)view.findViewById(R.id.list_item_maintenance_routines_name);
        TextView maintenanceDesc= (TextView)view.findViewById(R.id.list_item_maintenance_routines_desc);
        TextView maintenanceHours = (TextView)view.findViewById(R.id.list_item_maintenance_routines_hours);

        MaintanceRoutinesClass tmpRoutine = routinesList.get(position);

        maintenanceDesc.setText(tmpRoutine.getmDesc());
        maintenanceHours.setText(String.valueOf(tmpRoutine.getmHours()));
        maintenanceName.setText(tmpRoutine.getmName());

        return view;
    }
}
