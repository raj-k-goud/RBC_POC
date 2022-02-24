package com.test.rbcaccountpoc.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rbc.rbcaccountlibrary.Transaction
import com.test.rbcaccountpoc.databinding.ListItemTransactionsBinding
import android.text.format.DateFormat
import com.test.rbcaccountpoc.utils.Constants.Companion.DATE_FORMAT
import com.test.rbcaccountpoc.utils.Currency.Companion.formatBalance

class TransactionListAdapter(private val transactionList: List<Transaction>) :  RecyclerView.Adapter<TransactionListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemTransactionsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.date.text = DateFormat.format(DATE_FORMAT, transactionList[position].date).toString()
        holder.binding.amount.text = formatBalance(transactionList[position].amount)
        holder.bind(transactionList[position])
    }

    override fun getItemCount(): Int = transactionList.size

    class ViewHolder(val binding: ListItemTransactionsBinding):
        RecyclerView.ViewHolder(binding.root){

        fun bind(item: Transaction){
            binding.apply {
                transactions = item
                executePendingBindings()
            }
        }
    }
}