package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;


public class ToDatabaseTransactionDAO extends SQLiteOpenHelper implements TransactionDAO{
    //private final List<Transaction> transactions;

    public ToDatabaseTransactionDAO(@Nullable Context context) {
        super(context, "SystemDB", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create TABLE Transactions(date TEXT,accountNo TEXT, bankName TEXT NOT NULL, expenseType TEXT NOT NULL, amount REAL NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //insert transaction object to Transactions Table
    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("date",date.toString());
        cv.put("accountNo",accountNo);
        cv.put("expenseType",expenseType.toString());
        cv.put("amount",amount);

        long result  = db.insert("Transactions",null,cv);

    }

    //get all transaction object from Transaction Table
    @Override
    public List<Transaction> getAllTransactionLogs() {

        List<Transaction> returnList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from Transactions",null);
        if(cursor.moveToFirst()){
            do {
                Date date;
                ExpenseType expenseType;
                String dateString = cursor.getString(0);
                String accountNo = cursor.getString(1);
                String expenseTypeString = cursor.getString(2);
                double amount = cursor.getDouble(3);

                try {
                   date=new SimpleDateFormat("dd-MMM-yyyy").parse(dateString);
                   expenseType = ExpenseType.valueOf(expenseTypeString);
                }catch (Exception e){
                    date =new Date();
                    expenseType = ExpenseType.valueOf(expenseTypeString);
                }

                Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
                returnList.add(transaction);

            }while (cursor.moveToNext());
        }
        return  returnList;
    }

    //get all transaction object which size greater than limit from Transaction Table
    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        int size = this.getAllTransactionLogs().size();
        if (size <= limit) {
            return this.getAllTransactionLogs();
        }
        // return the last <code>limit</code> number of transaction logs
        return this.getAllTransactionLogs().subList(size - limit, size);
    }


}
