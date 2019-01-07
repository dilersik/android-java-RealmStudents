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
import com.dilerdesenvolv.realmstudents.domain.Student;

import io.realm.Realm;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

public class DisciplineAdapter extends RealmBaseAdapter<Discipline> implements ListAdapter {

    private Realm realm;
    private RealmResults<Discipline> realmResults;

    public DisciplineAdapter(Context context, Realm realm, RealmResults<Discipline> realmResults) {
        super(context, realmResults);
        this.realm = realm;
        this.realmResults = realmResults;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomViewHolder holder;

        if (convertView != null) {
            holder = (CustomViewHolder) convertView.getTag();

        } else {
            convertView = inflater.inflate(R.layout.item_discipline, parent, false);
            holder = new CustomViewHolder();
            convertView.setTag(holder);

            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.btUpdate = (Button) convertView.findViewById(R.id.bt_update);
            holder.btRemove = (Button) convertView.findViewById(R.id.bt_remove);
        }

        final Discipline d = realmResults.get(position);
        int amountStudents = realm.where(Student.class).equalTo("grades.discipline.id", d.getId()).findAll().size();

        holder.tvName.setText(d.getName() + "(" + amountStudents + " estudantes)");
        holder.btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, AddUpdateDisciplineActivity.class);
                it.putExtra(Discipline.ID, d.getId());
                context.startActivity(it);
            }
        });

        holder.btRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                d.deleteFromRealm();
                realm.commitTransaction();
                realm.close();
            }
        });

        return convertView;
    }

    private static class CustomViewHolder {
        TextView tvName;
        Button btUpdate;
        Button btRemove;
    }

}
