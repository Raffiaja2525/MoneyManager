package com.raffimaulana259xixixix.keuangan.utils

import java.text.NumberFormat
import java.util.*

/**
 * Utility class untuk fungsi-fungsi helper
 */
object CurrencyUtils {

    /**
     * Format angka menjadi format mata uang Rupiah
     * Contoh: 50000 -> Rp 50.000
     */
    fun formatToRupiah(amount: Int): String {
        val localeID = Locale("in", "ID")
        val formatter = NumberFormat.getCurrencyInstance(localeID)
        return formatter.format(amount)
            .replace("Rp", "Rp ")  // Tambah spasi setelah Rp
            .replace(",00", "")     // Hilangkan desimal
    }
}

