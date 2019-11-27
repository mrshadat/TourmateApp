package com.mrshadat.tourmateapp.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mrshadat.tourmateapp.pojos.EventExpense;
import com.mrshadat.tourmateapp.repos.ExpenseRepository;

import java.util.List;

public class ExpenseViewModel extends ViewModel {
    private ExpenseRepository repository;
    public MutableLiveData<List<EventExpense>> expenseListLD = new MutableLiveData<>();

    public ExpenseViewModel(){
        repository = new ExpenseRepository();
    }

    public void saveExpense(EventExpense expense){
        repository.addNewExpenseToRTDB(expense);
    }

    public void getAllExpenses(String eventId){
        expenseListLD = repository.getAllExpensesByEventId(eventId);
    }
}
