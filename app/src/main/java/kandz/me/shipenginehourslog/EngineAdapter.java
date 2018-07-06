package kandz.me.shipenginehourslog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class EngineAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<EngineClass> enginesArrayList;

    public EngineAdapter(Context context, ArrayList<EngineClass> enginesArrayList) {
        this.context = context;
        this.enginesArrayList = enginesArrayList;
        inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return enginesArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return enginesArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view =inflater.inflate(R.layout.list_item_engine,parent,false);

        TextView engineName = (TextView)view.findViewById(R.id.list_item_engine_name);
        TextView engineDesc = (TextView)view.findViewById(R.id.list_item_engine_desc);

        EngineClass tmpEngine = enginesArrayList.get(position);

        engineName.setText(tmpEngine.geteName());
        engineDesc.setText(tmpEngine.geteDesc());

        return view;
    }
}
