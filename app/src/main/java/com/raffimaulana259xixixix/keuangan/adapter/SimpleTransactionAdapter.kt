package com.raffimaulana259xixixix.keuangan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.raffimaulana259xixixix.keuangan.R
import com.raffimaulana259xixixix.keuangan.database.Transaction
import com.raffimaulana259xixixix.keuangan.utils.CurrencyUtils

/**
 * Adapter sederhana untuk RecyclerView tampilan Home
 * Menampilkan list transaksi dengan tampilan yang lebih ringkas
 */
class SimpleTransactionAdapter(
    private var transactions: List<Transaction>
) : RecyclerView.Adapter<SimpleTransactionAdapter.SimpleViewHolder>() {

    inner class SimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvKeterangan: TextView = itemView.findViewById(R.id.tvKeterangan)
        private val tvTanggal: TextView = itemView.findViewById(R.id.tvTanggal)
        private val tvNominal: TextView = itemView.findViewById(R.id.tvNominal)
        private val viewColorIndicator: View = itemView.findViewById(R.id.viewColorIndicator)

        fun bind(transaction: Transaction) {
            tvKeterangan.text = transaction.keterangan
            tvTanggal.text = transaction.tanggal
            tvNominal.text = CurrencyUtils.formatToRupiah(transaction.nominal)

            // Set warna berdasarkan jenis transaksi
            val color = if (transaction.jenis == "Pemasukan") {
                itemView.context.getColor(R.color.income_color)
            } else {
                itemView.context.getColor(R.color.expense_color)
            }

            tvNominal.setTextColor(color)
            viewColorIndicator.setBackgroundColor(color)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction_simple, parent, false)
        return SimpleViewHolder(view)
    }

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    override fun getItemCount(): Int = transactions.size.coerceAtMost(5) // Max 5 items untuk Home

    fun updateTransactions(newTransactions: List<Transaction>) {
        transactions = newTransactions
        notifyDataSetChanged()
    }
}

