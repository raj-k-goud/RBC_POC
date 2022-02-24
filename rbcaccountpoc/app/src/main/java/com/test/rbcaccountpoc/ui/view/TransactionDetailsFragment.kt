package com.test.rbcaccountpoc.ui.view

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rbc.rbcaccountlibrary.Transaction
import com.test.rbcaccountpoc.R
import com.test.rbcaccountpoc.adapter.TransactionListAdapter
import com.test.rbcaccountpoc.viewmodels.TransactionDetailsViewModel
import com.test.rbcaccountpoc.databinding.FragmentTransactionDetailsBinding
import com.test.rbcaccountpoc.utils.Constants.Companion.ACCOUNT_NAME
import com.test.rbcaccountpoc.utils.Constants.Companion.ACCOUNT_NUMBER
import com.test.rbcaccountpoc.utils.Constants.Companion.BALANCE
import com.test.rbcaccountpoc.utils.Constants.Companion.IS_CREDIT_CARD
import com.test.rbcaccountpoc.utils.Currency
import com.test.rbcaccountpoc.utils.Status

class TransactionDetailsFragment : Fragment() {

    private lateinit var viewModel: TransactionDetailsViewModel
    private lateinit var binding: FragmentTransactionDetailsBinding
    private var transactionList: ArrayList<Transaction> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         binding =  FragmentTransactionDetailsBinding.inflate(inflater, container, false)
        context ?: return binding.root
        val adapter = TransactionListAdapter(transactionList)
        binding.transactionListRv.adapter = adapter
        subscribeUi(adapter)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun subscribeUi(adapter: TransactionListAdapter) {
        viewModel = ViewModelProvider(this).get(TransactionDetailsViewModel::class.java)
        val accNo = arguments?.getString(ACCOUNT_NUMBER)?: ""
        val isCreditCard = arguments?.getBoolean(IS_CREDIT_CARD)?: false
        val accName =  arguments?.getString(ACCOUNT_NAME)?: ""
        val balance = arguments?.getString(BALANCE)?: ""
        viewModel.setCurrentAccount(accNo, isCreditCard)
        binding.accountName.text = "$accName ($accNo)"
        binding.accountNumber.text = resources.getString(R.string.balance).plus(Currency.formatBalance(balance))
        transactionList.clear()

        viewModel.getAllTransactions().observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    binding.message.visibility = View.GONE
                    it.data?.let { transactions ->
                        if(transactions.isEmpty()){
                            binding.message.visibility = View.VISIBLE
                            binding.message.text = String.format(resources.getString(R.string.no_transactions, accName))
                        }
                        transactionList.addAll(transactions)
                        adapter.notifyDataSetChanged()
                    }
                }
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    binding.message.text = it.message
                }
            }
        })
    }

}