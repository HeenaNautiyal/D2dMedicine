package com.bizhawkz.d2dmedicine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Heena on 2/21/2017.
 */
public class ActorAdapter extends ArrayAdapter<Chemist> {
    ArrayList<Chemist> actorList;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder;

    public ActorAdapter(Context context, int resource, ArrayList<Chemist> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        actorList = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = vi.inflate(Resource, null);
            holder.tvName = (TextView) v.findViewById(R.id.tvName);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.tvName.setText(actorList.get(position).getName());

        return v;
    }

    static class ViewHolder {
        public TextView tvName;
    }

    public void updateAdapter(ArrayList<Chemist> actorList){
        this.actorList=actorList;
    }
}
