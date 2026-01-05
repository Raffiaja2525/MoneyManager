package com.raffimaulana259xixixix.keuangan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.raffimaulana259xixixix.keuangan.R
import com.raffimaulana259xixixix.keuangan.database.Transaction
import com.raffimaulana259xixixix.keuangan.utils.CurrencyUtils

/**
 * Adapter untuk RecyclerView daftar transaksi
 * Menampilkan list transaksi dengan ViewHolder pattern dan mendukung multi-select
 */
class TransactionAdapter(
    private var transactions: List<Transaction>,
    private val onItemClick: (Transaction) -> Unit,  // Callback untuk click (edit)
    private val onItemLongClick: (Transaction) -> Unit  // Callback untuk long click (activate selection)
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    private var isSelectionMode = false
    private val selectedItems = mutableSetOf<Int>() // Store transaction IDs
    var onSelectionChanged: (() -> Unit)? = null // Callback untuk perubahan selection

    /**
     * ViewHolder untuk menyimpan referensi view
     */
    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvKeterangan: TextView = itemView.findViewById(R.id.tvKeterangan)
        private val tvTanggal: TextView = itemView.findViewById(R.id.tvTanggal)
        private val tvJenis: TextView = itemView.findViewById(R.id.tvJenis)
        private val tvNominal: TextView = itemView.findViewById(R.id.tvNominal)
        private val viewColorIndicator: View = itemView.findViewById(R.id.viewColorIndicator)
        private val checkboxSelect: CheckBox = itemView.findViewById(R.id.checkboxSelect)

        /**
         * Bind data transaksi ke view
         */
        fun bind(transaction: Transaction) {
            tvKeterangan.text = transaction.keterangan
            tvTanggal.text = transaction.tanggal
            tvJenis.text = transaction.jenis
            tvNominal.text = CurrencyUtils.formatToRupiah(transaction.nominal)

            // Set warna berdasarkan jenis transaksi
            val color = if (transaction.jenis == "Pemasukan") {
                itemView.context.getColor(R.color.income_color)  // Hijau untuk pemasukan
            } else {
                itemView.context.getColor(R.color.expense_color)  // Merah untuk pengeluaran
            }

            tvNominal.setTextColor(color)
            viewColorIndicator.setBackgroundColor(color)

            // Set background badge jenis transaksi
            tvJenis.setBackgroundColor(color)

            // Handle selection mode
            checkboxSelect.visibility = if (isSelectionMode) View.VISIBLE else View.GONE
            checkboxSelect.isChecked = selectedItems.contains(transaction.id)

            // Click listener
            itemView.setOnClickListener {
                if (isSelectionMode) {
                    toggleSelection(transaction.id)
                } else {
                    onItemClick(transaction)
                }
            }

            // Long click untuk activate selection mode
            itemView.setOnLongClickListener {
                if (!isSelectionMode) {
                    onItemLongClick(transaction)
                }
                true
            }

            // Checkbox click listener
            checkboxSelect.setOnClickListener {
                toggleSelection(transaction.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    override fun getItemCount(): Int = transactions.size

    /**
     * Update data transaksi dan refresh RecyclerView
     */
    fun updateTransactions(newTransactions: List<Transaction>) {
        transactions = newTransactions
        notifyDataSetChanged()
    }

    /**
     * Toggle selection untuk item dengan ID tertentu
     */
    private fun toggleSelection(transactionId: Int) {
        if (selectedItems.contains(transactionId)) {
            selectedItems.remove(transactionId)
        } else {
            selectedItems.add(transactionId)
        }
        notifyDataSetChanged()
        onSelectionChanged?.invoke()
    }

    /**
     * Aktifkan selection mode
     */
    fun enableSelectionMode(transaction: Transaction) {
        isSelectionMode = true
        selectedItems.clear()
        selectedItems.add(transaction.id)
        notifyDataSetChanged()
    }

    /**
     * Matikan selection mode
     */
    fun disableSelectionMode() {
        isSelectionMode = false
        selectedItems.clear()
        notifyDataSetChanged()
    }

    /**
     * Pilih semua item
     */
    fun selectAll() {
        selectedItems.clear()
        transactions.forEach { transaction ->
            selectedItems.add(transaction.id)
        }
        notifyDataSetChanged()
        onSelectionChanged?.invoke()
    }

    /**
     * Hapus semua pilihan
     */
    fun clearSelection() {
        selectedItems.clear()
        notifyDataSetChanged()
        onSelectionChanged?.invoke()
    }

    /**
     * Dapatkan item yang dipilih
     */
    fun getSelectedTransactions(): List<Transaction> {
        return transactions.filter { selectedItems.contains(it.id) }
    }

    /**
     * Dapatkan jumlah item yang dipilih
     */
    fun getSelectedCount(): Int = selectedItems.size

    /**
     * Cek apakah semua item dipilih
     */
    fun isAllSelected(): Boolean = selectedItems.size == transactions.size
}


