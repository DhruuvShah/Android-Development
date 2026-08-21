package com.northq.learninghub;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "manifold_hub.db";
    private static final int DATABASE_VERSION = 3;

    private static final String TABLE_FEES = "fees";
    private static final String COL_ID = "_id";
    private static final String COL_TITLE = "title";
    private static final String COL_DATE = "date";
    private static final String COL_AMOUNT = "amount";
    private static final String COL_STATUS = "status";

    private static final String TABLE_COMPLAINTS = "complaints";
    private static final String COL_COMPLAINT_TITLE = "title";
    private static final String COL_COMPLAINT_DESC = "description";
    private static final String COL_COMPLAINT_STATUS = "status";

    public ExpenseDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_FEES + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " TEXT NOT NULL, " +
                COL_DATE + " TEXT, " +
                COL_AMOUNT + " REAL NOT NULL, " +
                COL_STATUS + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_COMPLAINTS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_COMPLAINT_TITLE + " TEXT NOT NULL, " +
                COL_COMPLAINT_DESC + " TEXT, " +
                COL_COMPLAINT_STATUS + " TEXT)");
                
        seedData(db);
    }

    private void seedData(SQLiteDatabase db) {
        addFee(db, "Term 3 School Fees", "Due on 15 Jul 2026", 15000, "Paid");
        addFee(db, "Tuition Fee - Quarter 4", "Due on 1 Aug 2026", 8500, "Due");
        addFee(db, "Transportation Fee", "Optional Service", 3200, "Due");
        addFee(db, "Optional Sports Program", "For term 3 & 4", 1100, "Due");
    }

    private void addFee(SQLiteDatabase db, String title, String date, double amount, String status) {
        ContentValues v = new ContentValues();
        v.put(COL_TITLE, title);
        v.put(COL_DATE, date);
        v.put(COL_AMOUNT, amount);
        v.put(COL_STATUS, status);
        db.insert(TABLE_FEES, null, v);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLAINTS);
        onCreate(db);
    }

    public List<Expense> getAllFees() {
        List<Expense> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_FEES, null, null, null, null, null, COL_ID + " DESC");
        while (c.moveToNext()) {
            list.add(new Expense(
                    c.getLong(c.getColumnIndexOrThrow(COL_ID)),
                    c.getString(c.getColumnIndexOrThrow(COL_TITLE)),
                    c.getString(c.getColumnIndexOrThrow(COL_DATE)),
                    c.getDouble(c.getColumnIndexOrThrow(COL_AMOUNT)),
                    c.getString(c.getColumnIndexOrThrow(COL_STATUS))
            ));
        }
        c.close();
        return list;
    }

    public int deleteFee(long id) {
        return getWritableDatabase().delete(TABLE_FEES, COL_ID + "=?", new String[]{String.valueOf(id)});
    }
}
