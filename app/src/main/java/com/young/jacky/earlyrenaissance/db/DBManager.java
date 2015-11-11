package com.young.jacky.earlyrenaissance.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jack on 11/11/15.
 */
public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    public void add(List<Person> persons) {
        db.beginTransaction();
        try {
            for (Person person : persons) {
                db.execSQL("INSERT INTO person VALUES(null, ?, ?, ?)",
                        new Object[]{person.name, person.age, person.info});
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void updateAge(Person person) {
        ContentValues cv = new ContentValues();
        cv.put("age", person.age);
        db.update("person", cv, "name = ?", new String[]{person.name});
    }

    public void deleteOldPerson(Person person) {
        db.delete("person", "age >= ?", new String[]{String.valueOf(person.age)});
    }

    public List<Person> query() {
        ArrayList<Person> persons = new ArrayList<Person>();
        Cursor c = queryTheCursor();
        while (c.moveToNext()) {
            Person person = new Person();
            person._id = c.getInt(c.getColumnIndex("_id"));
            person.name = c.getString(c.getColumnIndex("name"));
            person.age = c.getInt(c.getColumnIndex("age"));
            person.info = c.getString(c.getColumnIndex("info"));
            persons.add(person);
        }
        c.close();
        return persons;
    }

    public Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM person", null);
        return c;
    }

    public void closeDB() {
        db.close();
    }
}
