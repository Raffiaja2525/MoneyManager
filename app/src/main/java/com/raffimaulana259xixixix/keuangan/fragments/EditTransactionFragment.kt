package com.raffimaulana259xixixix.keuangan.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.raffimaulana259xixixix.keuangan.R
import com.raffimaulana259xixixix.keuangan.database.Transaction
import com.raffimaulana259xixixix.keuangan.databinding.FragmentAddTransactionBinding
import com.raffimaulana259xixixix.keuangan.viewmodel.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * EditTransactionFragment - Fragment untuk mengedit transaksi yang sudah ada
 */
class EditTransactionFragment : Fragment() {

    private var _binding: FragmentAddTransactionBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: TransactionViewModel
    private var selectedDate: String = ""
    private var transactionToEdit: Transaction? = null

    companion object {
        private const val ARG_TRANSACTION = "transaction"

        fun newInstance(transaction: Transaction): EditTransactionFragment {
            val fragment = EditTransactionFragment()
            val args = Bundle()
            args.putSerializable(ARG_TRANSACTION, transaction as java.io.Serializable)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi ViewModel
        viewModel = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]

        // Ambil data transaksi dari arguments
        @Suppress("DEPRECATION")
        transactionToEdit = arguments?.getSerializable(ARG_TRANSACTION) as? Transaction

        // Populate data transaksi ke form
        populateData()

        // Setup listeners
        setupClickListeners()
    }

    private fun populateData() {
        transactionToEdit?.let { transaction ->
            binding.etNominal.setText(transaction.nominal.toString())
            binding.etKeterangan.setText(transaction.keterangan)
            selectedDate = transaction.tanggal
            binding.tvTanggalTerpilih.text = selectedDate
            binding.tvTanggalTerpilih.visibility = View.VISIBLE

            // Set jenis transaksi
            if (transaction.jenis == "Pemasukan") {
                binding.rbPemasukan.isChecked = true
            } else {
                binding.rbPengeluaran.isChecked = true
            }
        }
    }

    private fun setupClickListeners() {
        // Tombol Pilih Tanggal
        binding.btnPilihTanggal.setOnClickListener {
            showDatePicker()
        }

        // Tombol Simpan
        binding.btnSimpan.setOnClickListener {
            updateTransaction()
        }

        // Tombol Batal
        binding.btnBatal.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
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

    private fun updateTransaction() {
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
            Toast.makeText(requireContext(), getString(R.string.error_tanggal_kosong), Toast.LENGTH_SHORT).show()
            return
        }

        // Ambil jenis transaksi dari RadioButton
        val jenis = if (binding.rbPemasukan.isChecked) {
            "Pemasukan"
        } else {
            "Pengeluaran"
        }

        // Update objek Transaction dengan ID yang sama
        transactionToEdit?.let { oldTransaction ->
            val updatedTransaction = Transaction(
                id = oldTransaction.id,
                nominal = nominalText.toInt(),
                jenis = jenis,
                keterangan = keterangan,
                tanggal = selectedDate
            )

            // Update ke database melalui ViewModel
            viewModel.update(updatedTransaction)

            // Tampilkan pesan sukses
            Toast.makeText(requireContext(), getString(R.string.transaksi_diupdate), Toast.LENGTH_SHORT).show()

            // Kembali ke fragment sebelumnya
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

