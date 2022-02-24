package com.test.rbcaccountpoc.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.rbc.rbcaccountlibrary.Account
import com.rbc.rbcaccountlibrary.AccountType
import com.test.rbcaccountpoc.R
import com.test.rbcaccountpoc.databinding.ListItemAccountsBinding
import com.test.rbcaccountpoc.utils.Constants.Companion.ACCOUNT_NAME
import com.test.rbcaccountpoc.utils.Constants.Companion.ACCOUNT_NUMBER
import com.test.rbcaccountpoc.utils.Constants.Companion.BALANCE
import com.test.rbcaccountpoc.utils.Constants.Companion.IS_CREDIT_CARD
import com.test.rbcaccountpoc.utils.Currency

class AccountsListAdapter(private val accountList: List<Account>) :  RecyclerView.Adapter<AccountsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemAccountsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.accountNumber.text = "Balance : ".plus(Currency.formatBalance(accountList[position].balance))
        val bundle = bundleOf(ACCOUNT_NUMBER to accountList[position].number,
            IS_CREDIT_CARD to (accountList[position].type == AccountType.CREDIT_CARD),
            ACCOUNT_NAME to accountList[position].name,
            BALANCE to accountList[position].balance)

        holder.binding.icon.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_view_account_list_fragment_to_account_detail_fragment, bundle)
        }

        holder.bind(accountList[position])
    }

    override fun getItemCount(): Int = accountList.size

    class ViewHolder(val binding: ListItemAccountsBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(item: Account){
            binding.apply {
                account = item
                executePendingBindings()
            }
        }
    }

}