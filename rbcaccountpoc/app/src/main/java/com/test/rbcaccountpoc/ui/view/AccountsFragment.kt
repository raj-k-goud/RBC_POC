package com.test.rbcaccountpoc.ui.view

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rbc.rbcaccountlibrary.Account
import com.test.rbcaccountpoc.R
import com.test.rbcaccountpoc.databinding.FragmentAccountListBinding
import com.test.rbcaccountpoc.adapter.AccountsListAdapter
import com.test.rbcaccountpoc.viewmodels.AccountsViewModel
import com.test.rbcaccountpoc.utils.Status

class AccountsFragment : Fragment() {

    private lateinit var viewModel: AccountsViewModel
    private var accountList: ArrayList<Account> = ArrayList()
    private lateinit var binding: FragmentAccountListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAccountListBinding.inflate(inflater, container, false)
        context ?: return binding.root
        val adapter = AccountsListAdapter(accountList)
        binding.accountList.adapter = adapter
        subscribeUi(adapter)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun subscribeUi(adapter: AccountsListAdapter) {
        viewModel = ViewModelProvider(this).get(AccountsViewModel::class.java)
        accountList.clear()
        viewModel.getAllAccounts().observe(viewLifecycleOwner,{
            when(it.status){
                Status.SUCCESS ->{
                    it.data?.let {accounts ->
                         accountList.addAll(accounts)
                         adapter.notifyDataSetChanged()
                    }
                }
                Status.ERROR ->{ binding.accountHeader.text = resources.getString(R.string.error) }
                Status.LOADING ->{ binding.accountHeader.text = resources.getString(R.string.loading)}
            }
        })
    }
}