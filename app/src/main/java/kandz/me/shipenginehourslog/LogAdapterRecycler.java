package kandz.me.shipenginehourslog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class LogAdapterRecycler  extends RecyclerView.Adapter<LogAdapterRecycler.ViewHolder>{

    private ArrayList<HourClass> hoursList;

    public LogAdapterRecycler(ArrayList<HourClass> hoursList){
        this.hoursList= hoursList;
    }

    //create new view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.list_item_log,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the view contents
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HourClass tmpHour = hoursList.get(position);

        holder.dateTxt.setText(tmpHour.gethDate());
        holder.timeTxt.setText(tmpHour.gethTime());
        holder.hoursTxt.setText(String.valueOf(tmpHour.gethAmount()));
        holder.minutesTxt.setText(String.valueOf(tmpHour.gethAmountMinutes()));
    }

    @Override
    public int getItemCount() {
        return hoursList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        public TextView dateTxt;
        public TextView timeTxt;
        public TextView hoursTxt;
        public TextView minutesTxt;
        public View layout;

        public ViewHolder(View v) {
            super(v);

            layout = v;
            dateTxt = (TextView)v.findViewById(R.id.list_item_log_date);
            timeTxt = (TextView)v.findViewById(R.id.list_item_log_time);
            hoursTxt = (TextView)v.findViewById(R.id.list_item_hours);
            minutesTxt = (TextView)v.findViewById(R.id.list_item_minutes);
        }
    }
}
