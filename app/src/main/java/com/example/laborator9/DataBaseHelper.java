package com.example.laborator9;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase mReadableDB;
    public static final String STUDENT_TABLE = "STUDENT_TABLE";
    public static final String COLUMN_STUDENT_NAME = "STUDENT_NAME";
    public static final String COLUMN_STUDENT_AGE = "STUDENT_AGE";
    public static final String COLUMN_ACTIVE_STATUS = "ACTIVE_STATUS";
    public static final String COLUMN_ID = "ID";
    private static final String TAG = "DataBaseHelper";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "students.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement= "CREATE TABLE " + STUDENT_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_STUDENT_NAME + " TEXT, " + COLUMN_STUDENT_AGE + " INT, " + COLUMN_ACTIVE_STATUS + " BOOL)";
        db.execSQL(createTableStatement);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOne(StudentModel studentModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_STUDENT_NAME,studentModel.getName());
        cv.put(COLUMN_STUDENT_AGE,studentModel.getAge());
        cv.put(COLUMN_ACTIVE_STATUS,studentModel.isActive());

        long insert = db.insert(STUDENT_TABLE, null, cv);
        if(insert == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean deleteOne(StudentModel studentModel){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + STUDENT_TABLE + " WHERE " + COLUMN_ID + " = " + studentModel.getId();
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            return true;
        } else {
            return false;
        }

    }

    public List<StudentModel> getEveryone(){
        List<StudentModel> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " +STUDENT_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            do{
                int studentID= cursor.getInt(0);
                String studentNAME= cursor.getString(1);
                int studentAGE= cursor.getInt(2);
                boolean studentACTIVE= cursor.getInt(3) == 1 ? true: false;

                StudentModel studentModel=new StudentModel(studentID, studentNAME, studentAGE, studentACTIVE);
                returnList.add(studentModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return returnList;
    }


    public Cursor search (String searchString) {
        String[] columns = new String[]{COLUMN_STUDENT_NAME, COLUMN_STUDENT_AGE,COLUMN_ACTIVE_STATUS};
        searchString = "%" + searchString + "%";
        String where = COLUMN_STUDENT_NAME + " LIKE ?";
        String[]whereArgs = new String[]{searchString};

        Cursor cursor = null;

        try {
            if (mReadableDB == null) {mReadableDB = getReadableDatabase();}
            cursor = mReadableDB.query(STUDENT_TABLE, columns, where, whereArgs, null, null, null);
        } catch (Exception e) {
            Log.d(TAG, "SEARCH EXCEPTION! " + e);
        }

        return cursor;
    }
}