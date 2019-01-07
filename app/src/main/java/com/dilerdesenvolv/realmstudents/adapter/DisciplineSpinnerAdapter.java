package com.dilerdesenvolv.realmstudents.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dilerdesenvolv.realmstudents.AddUpdateDisciplineActivity;
import com.dilerdesenvolv.realmstudents.R;
import com.dilerdesenvolv.realmstudents.domain.Discipline;

import io.realm.Realm;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

public class DisciplineSpinnerAdapter extends RealmBaseAdapter<Discipline> implements ListAdapter {

    private RealmResults<Discipline> realmResults;

    public DisciplineSpinnerAdapter(Context context, RealmResults<Discipline> realmResults) {
        super(context, realmResults);
        this.realmResults = realmResults;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomViewHolder holder;

        if (convertView != null) {
            holder = (CustomViewHolder) convertView.getTag();

        } else {
            convertView = inflater.inflate(R.layout.item_discipline_spinner, parent, false);
            holder = new CustomViewHolder();
            convertView.setTag(holder);

            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
        }

        final Discipline d = realmResults.get(position);
        holder.tvName.setText(d.getName());

        return convertView;
    }

    private static class CustomViewHolder {
        TextView tvName;
    }

}
