package com.raffimaulana259xixixix.keuangan.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.raffimaulana259xixixix.keuangan.adapter.SimpleTransactionAdapter
import com.raffimaulana259xixixix.keuangan.databinding.FragmentHomeBinding
import com.raffimaulana259xixixix.keuangan.utils.CurrencyUtils
import com.raffimaulana259xixixix.keuangan.viewmodel.TransactionViewModel

/**
 * HomeFragment - Menampilkan ringkasan keuangan dan riwayat transaksi terbaru
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: TransactionViewModel
    private lateinit var adapter: SimpleTransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi ViewModel
        viewModel = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]

        // Setup RecyclerView
        setupRecyclerView()

        // Observe data dari ViewModel
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = SimpleTransactionAdapter(emptyList())
        binding.rvRecentTransactions.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRecentTransactions.adapter = adapter
    }

    private fun observeViewModel() {
        // Observe daftar transaksi (hanya 5 terbaru)
        viewModel.allTransactions.observe(viewLifecycleOwner) { transactions ->
            if (transactions.isEmpty()) {
                binding.rvRecentTransactions.visibility = View.GONE
                binding.tvEmptyState.visibility = View.VISIBLE
            } else {
                binding.rvRecentTransactions.visibility = View.VISIBLE
                binding.tvEmptyState.visibility = View.GONE
                adapter.updateTransactions(transactions)
            }
        }

        // Observe total pemasukan
        viewModel.totalPemasukan.observe(viewLifecycleOwner) { total ->
            val totalPemasukan = total ?: 0
            binding.tvTotalPemasukan.text = CurrencyUtils.formatToRupiah(totalPemasukan)
            updateSaldo()
        }

        // Observe total pengeluaran
        viewModel.totalPengeluaran.observe(viewLifecycleOwner) { total ->
            val totalPengeluaran = total ?: 0
            binding.tvTotalPengeluaran.text = CurrencyUtils.formatToRupiah(totalPengeluaran)
            updateSaldo()
        }
    }

    private fun updateSaldo() {
        val pemasukan = viewModel.totalPemasukan.value ?: 0
        val pengeluaran = viewModel.totalPengeluaran.value ?: 0
        val saldo = pemasukan - pengeluaran

        binding.tvSaldo.text = CurrencyUtils.formatToRupiah(saldo)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

