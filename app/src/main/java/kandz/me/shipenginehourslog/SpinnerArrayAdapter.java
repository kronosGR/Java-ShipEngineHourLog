package kandz.me.shipenginehourslog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class SpinnerArrayAdapter extends ArrayAdapter<String>
{
    private final LayoutInflater inflater;
    private final Context context;
    private final List<EngineClass> items;
    private final int mResource;

    public SpinnerArrayAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);

        this.context=context;
        inflater=LayoutInflater.from(context);
        mResource=resource;
        items=objects;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position,convertView,parent);
    }

    private View createView(int position, View convertView, ViewGroup parent){
        final View view = inflater.inflate(mResource,parent,false);

        TextView engineName = (TextView)view.findViewById(R.id.spinner_engine_name);

        EngineClass tmpEngine= items.get(position);
        engineName.setText(tmpEngine.geteName());
        return view;
    }
}
