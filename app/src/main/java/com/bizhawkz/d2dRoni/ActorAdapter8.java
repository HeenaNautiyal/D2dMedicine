package com.bizhawkz.d2dRoni;

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
public class ActorAdapter8 extends ArrayAdapter<Chemist> {
    ArrayList<Chemist> actorList;
    LayoutInflater vi;
    int Resource;
    boolean[] checkBoxState;
    ViewHolder holder; String name;


    public ActorAdapter8(Context context, int resource, ArrayList<Chemist> objects) {
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

            holder.tvName = (TextView) v.findViewById(R.id.medname);
            holder.tvDOB = (TextView) v.findViewById(R.id.quantity);

            v.setTag(holder);


        } else {
            holder = (ViewHolder) v.getTag();
        }
        Chemist actors = actorList.get(position);
        holder.tvName.setText(actorList.get(position).getMedicine());
        holder.tvDOB.setText(actorList.get(position).getStock());


        return v;
    }

    static class ViewHolder {
        // public ImageView imageview;
        public TextView tvName;
        public TextView tvDOB;

    }


    ArrayList<Chemist> getBox() {
        ArrayList<Chemist> box = new ArrayList<Chemist>();
        for (Chemist p : actorList) {
            if (p.box)
                box.add(p);
        }
        return box;
    }
    public void updateAdapter(ArrayList<Chemist> actorList){
        this.actorList=actorList;
    }
}
