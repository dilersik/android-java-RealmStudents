package com.dilerdesenvolv.realmstudents;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dilerdesenvolv.realmstudents.adapter.DisciplineSpinnerAdapter;
import com.dilerdesenvolv.realmstudents.domain.Discipline;
import com.dilerdesenvolv.realmstudents.domain.Grade;
import com.dilerdesenvolv.realmstudents.domain.Student;
import com.dilerdesenvolv.realmstudents.util.ValidateU;

import java.util.LinkedList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by T-Gamer on 08/07/2016.
 */
public class AddUpdateStudentActivity extends AppCompatActivity {

    private Realm realm;
    private RealmResults<Student> students;
    private RealmResults<Discipline> disciplines;

    private Student student;
    private EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_student);

        student = new Student();
        etName = (EditText) findViewById(R.id.et_name);
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        Button btAddUpdate = (Button) findViewById(R.id.bt_add_update);

        realm = Realm.getDefaultInstance();
        students = realm.where(Student.class).findAll();

        if (getIntent() != null && getIntent().getLongExtra(Student.ID, 0) > 0) {
            student.setId(getIntent().getLongExtra(Student.ID, 0));

            student = students.where().equalTo("id", student.getId()).findAll().get(0);
            etName.setText(student.getName());
            tvTitle.setText(R.string.atualiza_disci);
            btAddUpdate.setText(R.string.update);
        }

        disciplines = realm.where(Discipline.class).findAll();

        // PRIMEIRO ELEMENTO
        Spinner spDiscipline = (Spinner) findViewById(R.id.sp_discipline);
        spDiscipline.setAdapter(new DisciplineSpinnerAdapter(this, disciplines));

        View btRemove = findViewById(R.id.bt_remove);
        btRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callRemoveGrade(v);
            }
        });

        // UPDATE
        if (student.getId() > 0) {
            for (Grade g : student.getGrades()) {
                createGradeForView(findViewById(R.id.bt_add), disciplines, g);
            }

            callRemoveGrade(btRemove);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // LISTENERS
    public void callAddUpdateStudent(View view) {
        int label = R.string.alterado_sucess;

        if (student.getId() == 0) {
            students = students.sort("id", Sort.DESCENDING);
            long id = students.size() == 0 ? 1 : students.get(0).getId() + 1;
            student.setId(id);
            label = R.string.cadastrado_sucess;
        }

        String name = etName.getText().toString().trim();
        if (!ValidateU.isName(name)) {
            etName.findFocus();
            etName.setError("Nome inválido");
            return ;
        }

        try {
            realm.beginTransaction();
            student.setName(name);
            realm.copyToRealmOrUpdate(student);
            realm.commitTransaction();

            student = realm.where(Student.class).equalTo("id", student.getId()).findFirst();

            realm.beginTransaction();
            student.getGrades().clear();
            student.getGrades().addAll(getGradesFromView(view, disciplines));
            realm.commitTransaction();

            Toast.makeText(AddUpdateStudentActivity.this, label, Toast.LENGTH_LONG).show();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(AddUpdateStudentActivity.this, R.string.falhou, Toast.LENGTH_LONG).show();
        }

    }

    public void callAddGrade(View view) {
        LinearLayout llParent = (LinearLayout) view.getParent();

        if (llParent.getChildCount() - 1 == disciplines.size()) {
            Toast.makeText(this, "Máximo de disciplinas atingido", Toast.LENGTH_LONG).show();
            return ;
        }

        createGradeForView(view, disciplines, null);
    }

    private void callRemoveGrade(View view) {
        LinearLayout llParent = (LinearLayout) view.getParent().getParent();
        // conta todos elementos select, input, etc
        if (llParent.getChildCount() > 2) {
            llParent.removeView((LinearLayout) view.getParent());
        }
    }

    private int getDisciplinePosition(RealmResults<Discipline> disciplines, Discipline discipline) {
        for (int i = 0; i < disciplines.size(); i ++) {
            if (disciplines.get(i).getId() == discipline.getId()) {
                return i;
            }
        }
        return 0;
    }

    // UTILS
    private void createGradeForView(View view, RealmResults<Discipline> disciplines, Grade grade) {
        LayoutInflater inflater = this.getLayoutInflater();
        LinearLayout llChild = (LinearLayout) inflater.inflate(R.layout.box_discipline_grade, null);

        Spinner spDiscipline = (Spinner) llChild.findViewById(R.id.sp_discipline);
        spDiscipline.setAdapter(new DisciplineSpinnerAdapter(this, disciplines));
        if (grade != null) {
            spDiscipline.setSelection(getDisciplinePosition(disciplines, grade.getDiscipline()));
        }

        EditText etGrade = (EditText) llChild.findViewById(R.id.et_grade);
        if (grade != null) {
            etGrade.setText(String.valueOf(grade.getGrade()));
        }

        View btRemove = llChild.findViewById(R.id.bt_remove);
        btRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callRemoveGrade(v);
            }
        });

        float scale = getResources().getDisplayMetrics().density;
        int margin = (int) (5 * scale + 0.5f);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(margin, margin, margin, margin);
        llChild.setLayoutParams(layoutParams);

        LinearLayout llParent = (LinearLayout) view.getParent();
        llParent.addView(llChild, llParent.getChildCount() - 1);
    }

    private List<Grade> getGradesFromView(View view, RealmResults<Discipline> disciplines) {
        List<Grade> list = new LinkedList<>();
        RelativeLayout rlParent = (RelativeLayout) view.getParent();

        for (int i = 0; i < rlParent.getChildCount(); i ++) {
            if (rlParent.getChildAt(i) instanceof ScrollView) {
                ScrollView scrollView = (ScrollView) rlParent.getChildAt(i);
                LinearLayout llChild = (LinearLayout) scrollView.getChildAt(0);

                for (int j = 0; j < llChild.getChildCount(); j ++) {
                    if (llChild.getChildAt(j) instanceof LinearLayout) {
                        Spinner spDiscipline = (Spinner) llChild.getChildAt(j).findViewById(R.id.sp_discipline);
                        EditText etGrade = (EditText) llChild.getChildAt(j).findViewById(R.id.et_grade);

                        Grade g = new Grade();
                        if (realm.where(Grade.class).findAll().size() > 0) {
                            g.setId(realm.where(Grade.class).findAllSorted("id", Sort.DESCENDING).get(0).getId() + 1 + j);
                        } else {
                            g.setId(1);
                        }

                        // deveria validar os dados
                        Discipline d = new Discipline();
                        d.setId(disciplines.get(spDiscipline.getSelectedItemPosition()).getId());
                        d.setName(disciplines.get(spDiscipline.getSelectedItemPosition()).getName());

                        g.setDiscipline(d);
                        g.setGrade(Double.parseDouble(etGrade.getText().toString()));

                        boolean existDisc = false;
                        for (Grade value : list) {
                            if (value.getDiscipline().getId() == d.getId()) {
                                existDisc = true;
                                break;
                            }
                        }

                        if (!existDisc) {
                            list.add(g);
                        }
                    }
                }
            }
        }

        return list;
    }

}
