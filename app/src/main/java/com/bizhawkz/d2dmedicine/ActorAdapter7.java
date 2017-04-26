package com.bizhawkz.d2dmedicine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Heena on 3/31/2017.
 */
public class ActorAdapter7  extends ArrayAdapter<Chemist> {
    ArrayList<Chemist> actorList;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder;

    public ActorAdapter7(Context context, int resource, ArrayList<Chemist> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        actorList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // convert view = design
        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = vi.inflate(Resource, null);
        /*    quantity*/
            holder.imageview = (TextView) v.findViewById(R.id.address);
        /*    medicinename*/
            holder.tvName = (TextView) v.findViewById(R.id.quantity);
/*            store Name*/
            holder.tvDOB=(TextView)v.findViewById(R.id.medname);


            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        holder.imageview.setText(actorList.get(position).getQuantity());
        holder.tvName.setText(actorList.get(position).getMedicine());
        holder.tvDOB.setText(actorList.get(position).getMenufacuter());

        return v;

    }

    static class ViewHolder {
        public TextView imageview;
        public TextView tvName;
        public TextView tvDOB;
        public TextView tvHeight;
    }



    public void updateAdapter(ArrayList<Chemist> actorList){
        this.actorList=actorList;
    }
}

