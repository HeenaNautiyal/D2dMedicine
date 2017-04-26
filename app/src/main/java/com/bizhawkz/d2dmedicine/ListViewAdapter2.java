package com.bizhawkz.d2dmedicine;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Heena on 3/7/2017.
 */
public class ListViewAdapter2 extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    public ArrayList<Chemist> worldpopulationlist = null;
    ArrayList<Chemist> actorsList;
    int a=0;
    int abc;

    public ListViewAdapter2(Context context, ArrayList<Chemist> worldpopulationlist) {
        mContext = context;
        this.worldpopulationlist = worldpopulationlist;
        inflater = LayoutInflater.from(mContext);
        this.actorsList = new ArrayList<Chemist>();
        actorsList.addAll(worldpopulationlist);
    }

    public void updateAdapter(ArrayList<Chemist> actorsList) {
        this.actorsList=actorsList;
    }

    public void clearData() {
        worldpopulationlist.clear();
    }

    public class ViewHolder {
        TextView rank;
        TextView tvcount;
        ImageView btnsub;
        public CheckBox box;
        ImageView btnadd;
    }

    @Override
    public int getCount() {
        return worldpopulationlist.size();
    }

    @Override
    public Chemist getItem(int position) {
        return worldpopulationlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            final Chemist actors = worldpopulationlist.get(position);
            view = inflater.inflate(R.layout.listview_item, null);
            holder.tvcount=(TextView)view.findViewById(R.id.text);
            holder.rank = (TextView) view.findViewById(R.id.tvName);
            holder.btnadd=(ImageView)view.findViewById(R.id.bt_add);
            holder.btnsub=(ImageView)view.findViewById(R.id.bt_minus);
            holder.btnsub.setVisibility(View.INVISIBLE);
            holder.box=(CheckBox)view.findViewById(R.id.chk);
            holder.box.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v ;
                    Chemist country = (Chemist) cb.getTag();
                    Log.e("main1", (String) cb.getText());
                    country.setSelected(cb.isChecked());
                }
            });
            holder.btnadd.setTag(position);
            holder.btnsub.setTag(position);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final Chemist actors = worldpopulationlist.get(position);
        holder.rank.setText(worldpopulationlist.get(position).getMedicine());
        holder.tvcount.setText(worldpopulationlist.get(position).getQuantity());
        holder.box.setChecked(actors.isSelected());
        holder.btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Button clicked","Clicked");
                int position=(Integer)v.getTag();
                final Chemist Chem=worldpopulationlist.get(position);
                abc=Integer.parseInt(Chem.getQuantity())+1;
                Chem.setQuantity(String.valueOf(abc));
                worldpopulationlist.set(position,Chem);
                Log.e("Button Item",String.valueOf(abc));
                notifyDataSetChanged();
                holder.rank.setText(worldpopulationlist.get(position).getQuantity());
            }
        });
        holder.btnsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Button clicked","Clicked");
                int position=(Integer)v.getTag();
                final Chemist Chem=worldpopulationlist.get(position);
                if (Integer.parseInt(Chem.getQuantity())>1)
                 abc=Integer.parseInt(Chem.getQuantity())-1;
                Chem.setQuantity(String.valueOf(abc));
                worldpopulationlist.set(position,Chem);
                Log.e("Button Item",String.valueOf(abc));
                notifyDataSetChanged();
                holder.rank.setText(worldpopulationlist.get(position).getQuantity());
            }
        });
        holder.box.setTag(actors);

        return view;
    }

    public void filter(String charText) {
        if (actorsList.isEmpty())
        {
            actorsList.addAll(worldpopulationlist);
        }

        worldpopulationlist.clear();

        for (Chemist wp : actorsList)
        {
            if (wp.getMedicine().contains(charText))
            {
                worldpopulationlist.add(wp);
            }
        }
        notifyDataSetChanged();
    }
}
