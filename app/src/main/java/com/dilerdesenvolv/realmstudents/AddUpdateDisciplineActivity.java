package com.dilerdesenvolv.realmstudents;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dilerdesenvolv.realmstudents.bo.DisciplineBO;
import com.dilerdesenvolv.realmstudents.domain.Discipline;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class AddUpdateDisciplineActivity extends AppCompatActivity {

    private Realm realm;
    private RealmResults<Discipline> disciplines;

    private Discipline discipline;
    private EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_discipline);

        discipline = new Discipline();
        etName = (EditText) findViewById(R.id.et_name);
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        Button btAddUpdate = (Button) findViewById(R.id.bt_add_update);

        realm = Realm.getDefaultInstance();
        disciplines = realm.where(Discipline.class).findAll();

        if (getIntent() != null && getIntent().getLongExtra(Discipline.ID, 0) > 0) {
            discipline.setId(getIntent().getLongExtra(Discipline.ID, 0));

            discipline = disciplines.where().equalTo("id", discipline.getId()).findAll().get(0);
            etName.setText(discipline.getName());
            tvTitle.setText(R.string.atualiza_disci);
            btAddUpdate.setText(R.string.update);
        }
    }

    public void callAddUpdateDiscipline(View view) {
        int label = R.string.alterado_sucess;

        if (discipline.getId() == 0) {
            disciplines = disciplines.sort("id", Sort.DESCENDING);
            long id = disciplines.size() == 0 ? 1 : disciplines.get(0).getId() + 1;
            discipline.setId(id);
            label = R.string.cadastrado_sucess;
        }

        String test = null;
        String name = etName.getText().toString().trim();
        test = DisciplineBO.isName(name);
        if (test != "1") {
            etName.findFocus();
            etName.setError(test);
            return ;
        }

        try {
            realm.beginTransaction();
            discipline.setName(name);
            realm.copyToRealmOrUpdate(discipline);
            realm.commitTransaction();

            Toast.makeText(AddUpdateDisciplineActivity.this, label, Toast.LENGTH_LONG).show();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(AddUpdateDisciplineActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
