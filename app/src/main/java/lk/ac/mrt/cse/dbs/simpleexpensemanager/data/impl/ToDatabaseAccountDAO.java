package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;



public class ToDatabaseAccountDAO extends SQLiteOpenHelper implements AccountDAO{
    //private final Map<String, Account> accounts;

    public ToDatabaseAccountDAO(@Nullable Context context) {

        super(context, "SystemDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create TABLE Account(accountNo TEXT PRIMARY KEY, bankName TEXT NOT NULL, accountHolderName TEXT NOT NULL, balance REAL NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    //get all accountNo  from Account Table
    @Override
    public List<String> getAccountNumbersList() {
        List<String> returnList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from Account",null);
        if(cursor.moveToFirst()){
            do {
                String accountNo = cursor.getString(0);

                returnList.add(accountNo);

            }while (cursor.moveToNext());
        }
        return  returnList;
    }

    //get all account objects from Account Table
    @Override
    public List<Account> getAccountsList() {

        List<Account> returnList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from Account",null);
        if(cursor.moveToFirst()){
            do {
                String accountNo = cursor.getString(0);
                String bankName = cursor.getString(1);
                String accountHolderName = cursor.getString(2);
                double balance = cursor.getDouble(3);

                Account account = new Account(accountNo,bankName,accountHolderName,balance);
                returnList.add(account);

            }while (cursor.moveToNext());
        }
        return  returnList;
    }

    //get one account object from Account Table
    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Account account;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor  = db.rawQuery("select * from Account where accountNo = ?",new String[] {accountNo});
        if(cursor.getCount() > 0){

                String bankName = cursor.getString(1);
                String accountHolderName = cursor.getString(2);
                double balance = cursor.getDouble(3);

                account = new Account(accountNo,bankName,accountHolderName,balance);
                return account;
        }else {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }

    }

    //insert account object to Account Table
    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("accountNo",account.getAccountNo());
        cv.put("bankName",account.getBankName());
        cv.put("accountHolderName",account.getAccountHolderName());
        cv.put("balance",account.getBalance());

        long result  = db.insert("Account",null,cv);

    }

    // remove account from Account Table
    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor  = db.rawQuery("select * from Account where accountNo = ?",new String[] {accountNo});
        if(cursor.getCount() > 0){
            long result = db.delete("Account","accountNo=?",new String[] {accountNo});
        }else {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
    }

    //Update account value from account table
    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor  = db.rawQuery("select * from Account where accountNo = ?",new String[] {accountNo});

        if(cursor.getCount()>0){
            String bankName = cursor.getString(1);
            String accountHolderName = cursor.getString(2);
            double balance = cursor.getDouble(3);

            Account account = new Account(accountNo,bankName,accountHolderName,balance);

            switch (expenseType) {
                case EXPENSE:
                    account.setBalance(account.getBalance() - amount);
                    break;
                case INCOME:
                    account.setBalance(account.getBalance() + amount);
                    break;
            }
            ContentValues cv = new ContentValues();
            cv.put("bankName",account.getBankName());
            cv.put("accountHolderName",account.getAccountHolderName());
            cv.put("balance",account.getBalance());

            long result = db.update("Account",cv,"accountNo=?",new String[] {accountNo});

        }else{
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
    }


}
