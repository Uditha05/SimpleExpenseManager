package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;


import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.ToDatabaseAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.ToDatabaseTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;



public class PersistentExpenseManager extends ExpenseManager{
    public PersistentExpenseManager(Context context) {

        setup(context);
    }

    @Override
    public void setup(Context context) {

        TransactionDAO toDatabaseTransactionDAO = new ToDatabaseTransactionDAO(context);
        setTransactionsDAO(toDatabaseTransactionDAO);

        AccountDAO toDatabaseAccountDAO = new ToDatabaseAccountDAO(context);
        setAccountsDAO(toDatabaseAccountDAO);

    }

}
