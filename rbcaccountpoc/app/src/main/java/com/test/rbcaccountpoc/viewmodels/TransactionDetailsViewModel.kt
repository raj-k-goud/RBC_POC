package com.test.rbcaccountpoc.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rbc.rbcaccountlibrary.AccountProvider
import com.rbc.rbcaccountlibrary.Transaction
import com.test.rbcaccountpoc.utils.Logger
import kotlinx.coroutines.*
import com.test.rbcaccountpoc.utils.Resource
import kotlinx.coroutines.Dispatchers.IO
import kotlin.system.measureTimeMillis

class TransactionDetailsViewModel : ViewModel() {

    private lateinit var accountNumber : String
    private var isCreditCard: Boolean = false
    private val transactions = MutableLiveData<Resource<List<Transaction>>>()

    fun setCurrentAccount(accountNumber : String, isCreditCard: Boolean){
        if(accountNumber.isNotEmpty()){
        this.accountNumber = accountNumber
        this.isCreditCard = isCreditCard
        fetchAllTransactions()
        }
    }

    private fun fetchAllTransactions() {
    viewModelScope.launch (IO) {
        transactions.postValue(Resource.loading(null))
        try {
            coroutineScope{
                val executionTime = measureTimeMillis {
                val allTransactions = mutableListOf<Transaction>()
                if(isCreditCard){
                    val additionalTransactionCall = async { AccountProvider.getAdditionalCreditCardTransactions(accountNumber) }
                    val additionalTransactions = additionalTransactionCall.await()
                    allTransactions.addAll(additionalTransactions)
                }

                val transactionCall = async { AccountProvider.getTransactions(accountNumber) }
                val transaction = transactionCall.await()

                allTransactions.addAll(transaction)
                allTransactions.sortByDescending {it.date }
                transactions.postValue(Resource.success(allTransactions))
            }
                Logger.debug("Time to complete the task: $executionTime ms.")
        }
        }catch (e: Exception){
            Logger.debug(e.localizedMessage)
            transactions.postValue(Resource.error("Something Went Wrong, Please Try Again! \n\n Error: $e.localizedMessage", null))
        }
    }
    }

    fun getAllTransactions(): LiveData<Resource<List<Transaction>>> {
        return transactions
    }

}