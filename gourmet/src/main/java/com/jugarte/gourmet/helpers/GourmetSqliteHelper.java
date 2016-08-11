package com.jugarte.gourmet.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jugarte.gourmet.beans.Gourmet;
import com.jugarte.gourmet.beans.Operation;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by javiergon on 10/09/15.
 */
public class GourmetSqliteHelper extends SQLiteOpenHelper {

    private SQLiteDatabase database;

    private String sqlCreateTableOperations = "CREATE TABLE " + TABLE_OPERATIONS +
            " ( " + KEY_ID + "  VARCHAR, " +
            " " + KEY_NAME + " VARCHAR, " +
            " " + KEY_PRICE + " VARCHAR, " +
            " " + KEY_DATE + " VARCHAR," +
            " " + KEY_HOUR+ " VARCHAR )";

    private String sqlCreateTableGourmet = "CREATE TABLE " + TABLE_GOURMET +
            " ( " + KEY_ID + " INTEGER, " +
            " " + KEY_CURRENT_BALANCE + " VARCHAR, " +
            " " + KEY_MODIFICATION_DATE + " VARCHAR )";

    private String sqlInsertTableGourmet = "INSERT INTO " + TABLE_GOURMET + " VALUES (1, \"\", \"\")";

    private final static int VERSION = 9;
    private final static String SQLITE_NAME = "DB_GOURMET.db";

    public final static String TABLE_OPERATIONS = "operations";
    public final static String TABLE_GOURMET = "gourmet";
    public final static String KEY_CURRENT_BALANCE = "current_balance";
    public final static String KEY_MODIFICATION_DATE = "modification_date";
    public final static String KEY_ID = "_id";
    public final static String KEY_NAME = "name";
    public final static String KEY_PRICE = "price";
    public final static String KEY_HOUR = "hour";
    public final static String KEY_DATE = "date";

    public GourmetSqliteHelper(Context context) {
        super(context, SQLITE_NAME, null, VERSION);
        database = this.getWritableDatabase();
    }

    private boolean existsOperation(String operationId) {
        return getOperation(operationId) != null;
    }

    private String getOperationId(String date, String hour) {
        return date+hour;
    }

    private Operation getOperation(String operationId) {
        Cursor cursor = database.query(TABLE_OPERATIONS, new String[] {
                        KEY_NAME, KEY_PRICE, KEY_DATE, KEY_HOUR}, KEY_ID + " = \"" + operationId + "\"",
                null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        if (cursor.getCount() > 0) {
            Operation operation = new Operation();
            operation.setName(cursor.getString(0));
            operation.setPrice(cursor.getString(1));
            operation.setDate(cursor.getString(2));
            operation.setHour(cursor.getString(3));

            return operation;
        }
        return null;
    }

    private void updateGourmet(Gourmet gourmet) {
        ContentValues values = new ContentValues();
        values.put(KEY_CURRENT_BALANCE, gourmet.getCurrentBalance());
        values.put(KEY_MODIFICATION_DATE, gourmet.getModificationDate());
        database.update(TABLE_GOURMET, values, KEY_ID + " = 1", null);
    }

    private void insertOperation(Operation operation) {
        String operationId = getOperationId(operation.getDate(), operation.getHour());
        if (!existsOperation(operationId)) {
            ContentValues values = new ContentValues();
            values.put(KEY_ID, operationId);
            values.put(KEY_NAME, operation.getName());
            values.put(KEY_PRICE, operation.getPrice());
            values.put(KEY_HOUR, operation.getHour());
            values.put(KEY_DATE, operation.getDate());
            database.insert(TABLE_OPERATIONS, null, values);
        }
    }

    public String getCurrentData(String data) {
        String selectQuery = "SELECT " + data + " FROM " + TABLE_GOURMET;
        String returnData = null;

        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            if (cursor.getCount() > 0) {
                returnData = (cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return returnData;
    }

    public String getCurrentBalance() {
        return getCurrentData(KEY_CURRENT_BALANCE);
    }

    public String getModificationDate() {
        return getCurrentData(KEY_MODIFICATION_DATE);
    }

    public ArrayList<Operation> getOperations() {
        ArrayList<Operation> operations = null;
        String selectQuery = "SELECT name, price, date, hour FROM " + TABLE_OPERATIONS;

        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            if (cursor.getCount() > 0) {
                operations = new ArrayList<Operation>();
                do {
                    Operation operation = new Operation();
                    operation.setName(cursor.getString(0));
                    operation.setPrice(cursor.getString(1));
                    operation.setDate(cursor.getString(2));
                    operation.setHour(cursor.getString(3));
                    // Adding contact to list
                    operations.add(operation);
                } while (cursor.moveToNext());
            }
        }

        if (operations != null) {
            Collections.reverse(operations);
        }
        return operations;
    }

    public void updateElementsWithDatas(Gourmet gourmet) {
        if (gourmet == null){
            return;
        }
        updateGourmet(gourmet);
        if (gourmet.getOperations() != null) {
            Collections.reverse(gourmet.getOperations());
            for (Operation operation : gourmet.getOperations()) {
                insertOperation(operation);
            }
        }
    }

    public void resetTables() {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_GOURMET);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_OPERATIONS);
        database.execSQL(sqlCreateTableGourmet);
        database.execSQL(sqlCreateTableOperations);
        database.execSQL(sqlInsertTableGourmet);
    }

    public void open() {
        this.database = getWritableDatabase();
    }

    public void close() {
        this.database.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreateTableGourmet);
        db.execSQL(sqlInsertTableGourmet);
        db.execSQL(sqlCreateTableOperations);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            resetTables();
        }
    }
}
