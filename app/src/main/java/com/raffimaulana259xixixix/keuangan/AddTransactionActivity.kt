package com.raffimaulana259xixixix.keuangan

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.raffimaulana259xixixix.keuangan.database.Transaction
import com.raffimaulana259xixixix.keuangan.databinding.ActivityAddTransactionBinding
import com.raffimaulana259xixixix.keuangan.viewmodel.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * Activity untuk menambah transaksi baru
 * Menggunakan View Binding untuk akses view
 */
class AddTransactionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTransactionBinding
    private lateinit var viewModel: TransactionViewModel
    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup ActionBar
        supportActionBar?.title = getString(R.string.tambah_transaksi)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Inisialisasi ViewModel
        viewModel = ViewModelProvider(this)[TransactionViewModel::class.java]

        // Setup listeners
        setupClickListeners()
    }

    /**
     * Setup semua click listeners
     */
    private fun setupClickListeners() {
        // Tombol Pilih Tanggal
        binding.btnPilihTanggal.setOnClickListener {
            showDatePicker()
        }

        // Tombol Simpan
        binding.btnSimpan.setOnClickListener {
            saveTransaction()
        }

        // Tombol Batal
        binding.btnBatal.setOnClickListener {
            finish()
        }
    }

    /**
     * Menampilkan DatePicker dialog
     */
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Format tanggal yang dipilih
                val date = Calendar.getInstance()
                date.set(selectedYear, selectedMonth, selectedDay)

                val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
                selectedDate = dateFormat.format(date.time)

                // Tampilkan tanggal yang dipilih
                binding.tvTanggalTerpilih.text = selectedDate
                binding.tvTanggalTerpilih.visibility = View.VISIBLE
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    /**
     * Validasi input dan simpan transaksi
     */
    private fun saveTransaction() {
        val nominalText = binding.etNominal.text.toString().trim()
        val keterangan = binding.etKeterangan.text.toString().trim()

        // Validasi input
        if (nominalText.isEmpty()) {
            binding.etNominal.error = getString(R.string.error_nominal_kosong)
            binding.etNominal.requestFocus()
            return
        }

        if (keterangan.isEmpty()) {
            binding.etKeterangan.error = getString(R.string.error_keterangan_kosong)
            binding.etKeterangan.requestFocus()
            return
        }

        if (selectedDate.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_tanggal_kosong), Toast.LENGTH_SHORT).show()
            return
        }

        // Ambil jenis transaksi dari RadioButton
        val jenis = if (binding.rbPemasukan.isChecked) {
            "Pemasukan"
        } else {
            "Pengeluaran"
        }

        // Buat objek Transaction
        val transaction = Transaction(
            nominal = nominalText.toInt(),
            jenis = jenis,
            keterangan = keterangan,
            tanggal = selectedDate
        )

        // Simpan ke database melalui ViewModel
        viewModel.insert(transaction)

        // Tampilkan pesan sukses
        Toast.makeText(this, getString(R.string.transaksi_berhasil), Toast.LENGTH_SHORT).show()

        // Kembali ke MainActivity
        finish()
    }

    /**
     * Handle tombol back di ActionBar
     */
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}

