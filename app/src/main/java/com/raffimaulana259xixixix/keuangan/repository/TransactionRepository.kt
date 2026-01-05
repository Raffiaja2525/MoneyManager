package com.raffimaulana259xixixix.keuangan.repository

import androidx.lifecycle.LiveData
import com.raffimaulana259xixixix.keuangan.database.Transaction
import com.raffimaulana259xixixix.keuangan.database.TransactionDao

/**
 * Repository class untuk mengelola data transaksi
 * Bertindak sebagai perantara antara ViewModel dan Database
 */
class TransactionRepository(private val transactionDao: TransactionDao) {

    /**
     * LiveData dari semua transaksi
     */
    val allTransactions: LiveData<List<Transaction>> = transactionDao.getAllTransactions()

    /**
     * LiveData dari total pemasukan
     */
    val totalPemasukan: LiveData<Int?> = transactionDao.getTotalPemasukan()

    /**
     * LiveData dari total pengeluaran
     */
    val totalPengeluaran: LiveData<Int?> = transactionDao.getTotalPengeluaran()

    /**
     * Menambahkan transaksi baru
     * Suspend function untuk operasi asynchronous
     */
    suspend fun insert(transaction: Transaction) {
        transactionDao.insert(transaction)
    }

    /**
     * Menghapus transaksi
     */
    suspend fun delete(transaction: Transaction) {
        transactionDao.delete(transaction)
    }

    /**
     * Update transaksi
     */
    suspend fun update(transaction: Transaction) {
        transactionDao.update(transaction)
    }
}

