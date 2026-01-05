package com.raffimaulana259xixixix.keuangan.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.raffimaulana259xixixix.keuangan.database.AppDatabase
import com.raffimaulana259xixixix.keuangan.database.Transaction
import com.raffimaulana259xixixix.keuangan.repository.TransactionRepository
import kotlinx.coroutines.launch

/**
 * ViewModel untuk mengelola data transaksi
 * Menggunakan AndroidViewModel untuk mendapatkan Application context
 */
class TransactionViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TransactionRepository

    // LiveData yang akan di-observe oleh UI
    val allTransactions: LiveData<List<Transaction>>
    val totalPemasukan: LiveData<Int?>
    val totalPengeluaran: LiveData<Int?>

    init {
        // Inisialisasi database dan repository
        val transactionDao = AppDatabase.getDatabase(application).transactionDao()
        repository = TransactionRepository(transactionDao)

        allTransactions = repository.allTransactions
        totalPemasukan = repository.totalPemasukan
        totalPengeluaran = repository.totalPengeluaran
    }

    /**
     * Menambahkan transaksi baru
     * Menggunakan viewModelScope untuk coroutine yang lifecycle-aware
     */
    fun insert(transaction: Transaction) = viewModelScope.launch {
        repository.insert(transaction)
    }

    /**
     * Menghapus transaksi
     */
    fun delete(transaction: Transaction) = viewModelScope.launch {
        repository.delete(transaction)
    }

    /**
     * Update transaksi
     */
    fun update(transaction: Transaction) = viewModelScope.launch {
        repository.update(transaction)
    }
}

