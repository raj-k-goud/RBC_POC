package com.test.rbcaccountpoc.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rbc.rbcaccountlibrary.Account
import com.rbc.rbcaccountlibrary.AccountProvider
import com.test.rbcaccountpoc.utils.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import com.test.rbcaccountpoc.utils.Resource
import java.lang.Exception

class AccountsViewModel : ViewModel() {

    private val accountsList = MutableLiveData<Resource<List<Account>>>()

   init {
       fetchAccountList()
   }

    private fun fetchAccountList(){
        viewModelScope.launch (Dispatchers.IO) {
            accountsList.postValue(Resource.loading(null))
            try {
                coroutineScope{
                    val allAccounts = async { AccountProvider.getAccountsList() }
                    val accounts = allAccounts.await()
                    if(allAccounts.isCompleted){
                        accountsList.postValue(Resource.success(accounts.sortedWith(compareBy { it.type })))
                    }
                }
            } catch (e: Exception)    {
                Logger.debug(e.localizedMessage)
                accountsList.postValue(Resource.error("Something Went Wrong, Please Try Again! \n Error: " +e.localizedMessage, null))
            }
        }
    }

    fun getAllAccounts(): LiveData<Resource<List<Account>>> {
        return accountsList
    }

}