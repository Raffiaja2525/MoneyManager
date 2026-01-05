package com.raffimaulana259xixixix.keuangan.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.raffimaulana259xixixix.keuangan.R
import com.raffimaulana259xixixix.keuangan.adapter.TransactionAdapter
import com.raffimaulana259xixixix.keuangan.database.Transaction
import com.raffimaulana259xixixix.keuangan.databinding.FragmentHistoryBinding
import com.raffimaulana259xixixix.keuangan.viewmodel.TransactionViewModel

/**
 * HistoryFragment - Menampilkan riwayat transaksi lengkap dengan fitur edit, hapus, dan multi-select
 */
class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: TransactionViewModel
    private lateinit var adapter: TransactionAdapter
    private var isSelectionMode = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi ViewModel
        viewModel = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]

        // Setup RecyclerView
        setupRecyclerView()

        // Setup action bar listeners
        setupActionBarListeners()

        // Observe data dari ViewModel
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = TransactionAdapter(
            transactions = emptyList(),
            onItemClick = { transaction ->
                if (!isSelectionMode) {
                    // Navigasi ke edit transaksi
                    showEditDialog(transaction)
                }
            },
            onItemLongClick = { transaction ->
                // Activate selection mode
                activateSelectionMode(transaction)
            }
        )

        // Set callback untuk update selection count
        adapter.onSelectionChanged = {
            updateSelectionCount()
        }

        binding.rvTransactions.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTransactions.adapter = adapter
    }

    private fun setupActionBarListeners() {
        // Close selection mode
        binding.btnCloseSelection.setOnClickListener {
            deactivateSelectionMode()
        }

        // Select all
        binding.btnSelectAll.setOnClickListener {
            if (adapter.isAllSelected()) {
                adapter.clearSelection()
                binding.btnSelectAll.text = getString(R.string.pilih_semua)
            } else {
                adapter.selectAll()
                binding.btnSelectAll.text = getString(R.string.batalkan_pilihan)
            }
            updateSelectionCount()
        }

        // Delete selected
        binding.btnDeleteSelected.setOnClickListener {
            showBulkDeleteDialog()
        }
    }

    private fun observeViewModel() {
        viewModel.allTransactions.observe(viewLifecycleOwner) { transactions ->
            if (transactions.isEmpty()) {
                binding.rvTransactions.visibility = View.GONE
                binding.tvEmptyState.visibility = View.VISIBLE
                deactivateSelectionMode()
            } else {
                binding.rvTransactions.visibility = View.VISIBLE
                binding.tvEmptyState.visibility = View.GONE
                adapter.updateTransactions(transactions)
            }
        }
    }

    private fun activateSelectionMode(transaction: Transaction) {
        isSelectionMode = true
        adapter.enableSelectionMode(transaction)
        binding.selectionActionBar.visibility = View.VISIBLE
        updateSelectionCount()
    }

    private fun deactivateSelectionMode() {
        isSelectionMode = false
        adapter.disableSelectionMode()
        binding.selectionActionBar.visibility = View.GONE
        binding.btnSelectAll.text = getString(R.string.pilih_semua)
    }

    private fun updateSelectionCount() {
        val count = adapter.getSelectedCount()
        binding.tvSelectionCount.text = getString(R.string.item_dipilih, count)

        // Update button text
        if (adapter.isAllSelected()) {
            binding.btnSelectAll.text = getString(R.string.batalkan_pilihan)
        } else {
            binding.btnSelectAll.text = getString(R.string.pilih_semua)
        }
    }

    private fun showEditDialog(transaction: Transaction) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.pilih_aksi))
            .setItems(arrayOf(getString(R.string.edit), getString(R.string.hapus))) { dialog, which ->
                when (which) {
                    0 -> {
                        // Edit - Navigasi ke EditTransactionFragment
                        val fragment = EditTransactionFragment.newInstance(transaction)
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, fragment)
                            .addToBackStack(null)
                            .commit()
                    }
                    1 -> {
                        // Hapus
                        showDeleteDialog(transaction)
                    }
                }
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun showDeleteDialog(transaction: Transaction) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.hapus_transaksi))
            .setMessage(getString(R.string.konfirmasi_hapus))
            .setPositiveButton(getString(R.string.ya)) { dialog, _ ->
                viewModel.delete(transaction)
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.tidak)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun showBulkDeleteDialog() {
        val selectedTransactions = adapter.getSelectedTransactions()
        val count = selectedTransactions.size

        if (count == 0) {
            return
        }

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.hapus_transaksi))
            .setMessage(getString(R.string.konfirmasi_hapus_banyak, count))
            .setPositiveButton(getString(R.string.ya)) { dialog, _ ->
                // Delete all selected transactions
                selectedTransactions.forEach { transaction ->
                    viewModel.delete(transaction)
                }
                deactivateSelectionMode()
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.tidak)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


