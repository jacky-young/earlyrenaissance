package com.young.jacky.earlyrenaissance.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.young.jacky.earlyrenaissance.R;
import com.young.jacky.earlyrenaissance.db.DBManager;
import com.young.jacky.earlyrenaissance.db.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jack on 11/11/15.
 */
public class SQLiteDBDemo extends BaseActivity {
    @Bind(R.id.add_btn)
    Button addBtn;
    @Bind(R.id.update_btn)
    Button updateBtn;
    @Bind(R.id.delete_btn)
    Button deleteBtn;
    @Bind(R.id.query_btn)
    Button queryBtn;
    @Bind(R.id.database_list)
    ListView databaseList;

    private DBManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_sqlite_db_layout);
        ButterKnife.bind(this);
        manager = new DBManager(context);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.closeDB();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.add_btn)
    public void add() {
        ArrayList<Person> persons = new ArrayList<Person>();
        Person person1 = new Person("Ella", 23, "lovely girl");
        Person person2 = new Person("Bob", 25, "hah girl");
        Person person3 = new Person("Jack", 20, "tt girl");
        Person person4 = new Person("Tom", 27, "good girl");
        Person person5 = new Person("Jimmy", 31, "ugly girl");
        Person person6 = new Person("Mike", 33, "nice girl");
        persons.add(person1);
        persons.add(person2);
        persons.add(person3);
        persons.add(person4);
        persons.add(person5);
        persons.add(person6);
        manager.add(persons);
    }

    @OnClick(R.id.update_btn)
    public void update() {
        Person person = new Person();
        person.name = "Bob";
        person.age = 35;
        manager.updateAge(person);
    }

    @OnClick(R.id.delete_btn)
    public void delete() {
        Person person = new Person();
        person.age = 30;
        manager.deleteOldPerson(person);
    }

    @OnClick(R.id.query_btn)
    public void query() {
        List<Person> persons = manager.query();
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (Person person : persons) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", person.name);
            map.put("info", person.age + " years old, " + person.info);
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, list, android.R.layout.simple_list_item_2,
            new String[]{"name", "info"}, new int[]{android.R.id.text1, android.R.id.text2});
        databaseList.setAdapter(adapter);
    }
}
