package com.raffimaulana259xixixix.keuangan.database

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Data Access Object (DAO) untuk operasi database transaksi
 * Berisi query untuk insert, select, dan delete data
 */
@Dao
interface TransactionDao {

    /**
     * Menambahkan transaksi baru ke database
     */
    @Insert
    suspend fun insert(transaction: Transaction)

    /**
     * Mengambil semua transaksi, diurutkan berdasarkan ID descending (terbaru di atas)
     */
    @Query("SELECT * FROM transactions ORDER BY id DESC")
    fun getAllTransactions(): LiveData<List<Transaction>>

    /**
     * Menghitung total nominal semua pemasukan
     */
    @Query("SELECT SUM(nominal) FROM transactions WHERE jenis = 'Pemasukan'")
    fun getTotalPemasukan(): LiveData<Int?>

    /**
     * Menghitung total nominal semua pengeluaran
     */
    @Query("SELECT SUM(nominal) FROM transactions WHERE jenis = 'Pengeluaran'")
    fun getTotalPengeluaran(): LiveData<Int?>

    /**
     * Menghapus transaksi tertentu dari database
     */
    @Delete
    suspend fun delete(transaction: Transaction)

    /**
     * Update transaksi
     */
    @Update
    suspend fun update(transaction: Transaction)

    /**
     * Menghapus semua transaksi (untuk keperluan testing atau reset)
     */
    @Query("DELETE FROM transactions")
    suspend fun deleteAll()
}

