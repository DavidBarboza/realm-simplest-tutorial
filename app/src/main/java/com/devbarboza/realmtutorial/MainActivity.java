package com.devbarboza.realmtutorial;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.devbarboza.realmtutorial.model.Student;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    EditText name, age;
    TextView display;
    Button btnSave, btnAll;

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = (EditText) findViewById(R.id.name);
        age = (EditText) findViewById(R.id.age);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnAll = (Button) findViewById(R.id.btnAll);
        display = (TextView) findViewById(R.id.display);

        Log.d(TAG, "onCreate View Initialization done");

        realm = Realm.getDefaultInstance();

        /*realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();*/

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });

        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readData();
            }
        });
    }

    private void saveData(){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Student student = bgRealm.createObject(Student.class);
                student.setName(name.getText().toString().trim());
                student.setAge(Integer.parseInt(age.getText().toString().trim()));
            }
        }, new Realm.Transaction.OnSuccess(){
            @Override
            public void onSuccess(){
                Log.d(TAG, "onSuccess: Data Written Succesfully");
            }
        }, new Realm.Transaction.OnError(){
            @Override
            public void onError(Throwable error){
                Log.d(TAG, "onError: Error Occured");
            }
        });
    }

    private void readData(){
        RealmResults<Student> students = realm.where(Student.class).findAll();
        display.setText("");
        String data = "";

        for (Student student:students){
            try {
                Log.d(TAG, "readData: Reading Data");
                data = data + "\n" + student.toString();
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }

        display.setText(data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
