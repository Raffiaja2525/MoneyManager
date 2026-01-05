package com.raffimaulana259xixixix.keuangan.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * Entity class untuk tabel transaksi di database
 * Merepresentasikan satu baris data transaksi keuangan
 */
@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nominal: Int,           // Jumlah uang dalam transaksi
    val jenis: String,          // "Pemasukan" atau "Pengeluaran"
    val keterangan: String,     // Deskripsi transaksi
    val tanggal: String         // Tanggal transaksi dalam format string
) : Serializable

