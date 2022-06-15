package com.readthym.book.ui.manage_book

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.henrylabs.qumparan.data.remote.QumparanResource
import com.henrylabs.qumparan.utils.base.BaseFragment
import com.readthym.book.R
import com.readthym.book.data.local.MyPreference
import com.readthym.book.data.remote.reqres.BookData
import com.readthym.book.data.remote.reqres.ReadthymAllBookResponse
import com.readthym.book.databinding.FragmentManageBookBinding
import com.readthym.book.ui.book.DetailBookViewModel
import com.readthym.book.ui.rythm_home.RythmHomeViewModel
import com.readthym.book.utils.UtilSnackbar.showSnakbarError
import com.readthym.book.utils.UtilSnackbar.showSnakbarSuccess
import com.yarolegovich.lovelydialog.LovelyStandardDialog
import org.koin.android.viewmodel.ext.android.viewModel

class ManageBookFragment : BaseFragment() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentManageBookBinding? = null
    private val binding get() = _binding!!

    private val PERMISSION_CODE_CAMERA = 1000
    private val PERMISSION_CODE_STORAGE = 1001
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_GALLERY = 2

    val viewModel: RythmHomeViewModel by viewModel()
    val bookViewModel: DetailBookViewModel by viewModel()

    private val mAdapter: ManageBookAdapter by lazy { ManageBookAdapter() }

    override fun initUI() {
        hideActionBar()
        requestPemission()
        initRecyclerView()

        binding.icBackButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.addBookFragment)
        }
    }

    private fun initRecyclerView() {

        binding.rvFav.setHasFixedSize(true)
        binding.rvFav.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFav.adapter = mAdapter

        mAdapter.setupAdapterInterface(object : ManageBookAdapter.ItemInterface {
            override fun onclick(model: BookData, action: String) {
                when (action) {
                    "DELETE" -> {
                        handleDeleteAction(model)
                    }
                    "EDIT" -> {
                        handleEditBook(model)
                    }
                    else->{
                        findNavController().navigate(R.id.detailBookFragment, bundleOf(
                            "book_id" to model.id.toString()
                        ))
                    }
                }
            }
        })
    }

    private fun handleEditBook(model: BookData) {
        findNavController().navigate(R.id.editBookFragment, bundleOf("id" to model.id.toString()))
    }

    private fun handleDeleteAction(model: BookData) {
        LovelyStandardDialog(requireContext(), LovelyStandardDialog.ButtonLayout.VERTICAL)
            .setTopColorRes(R.color.red)
            .setButtonsColorRes(R.color.domain_blue)
            .setTopTitleColor(R.color.white)
            .setNegativeButtonColor(resources.getColor(R.color.black))
            .setTitle("Anda Yakin ?")
            .setMessage("Buku ini akan dihapus secara permanen")
            .setPositiveButton("Batal") { }
            .setNegativeButton(
                "Hapus"
            ) {
                deleteBook(model)
            }
            .show()
    }

    private fun deleteBook(model: BookData) {
        bookViewModel.deleteBook(model.id.toString())
    }

    override fun initObserver() {

        bookViewModel.deleteBookLiveDataLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Default -> {}
                is QumparanResource.Error -> {
                    showLoading(false)
                    showSnakbarError(
                        requireActivity(),
                        binding.root,
                        it.data.toString()
                    )
                }
                is QumparanResource.Loading -> {
                    showLoading(true)
                }
                is QumparanResource.Success -> {
                    showLoading(false)
                    showSnakbarSuccess(
                        requireActivity(),
                        binding.root,
                        "Berhasil Menghapus Buku"
                    )
                    viewModel.getBooks()
                    bookViewModel.resetErrorLiveData()
                }
            }
        }

        viewModel.booksLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Default -> {}
                is QumparanResource.Error -> {
                    showLoading(false)
                }
                is QumparanResource.Loading -> {
                    showLoading(true)
                }
                is QumparanResource.Success -> {
                    showLoading(false)
                    it.data?.let { data ->
                        setupMainBook(data)
                    }
                }
            }
        }
    }

    private fun showLoading(b: Boolean) {
        if (b) {
            binding.includeLoadingContent.base.makeVisible()
        } else {
            binding.includeLoadingContent.base.makeGone()
        }
    }

    private fun getUserId(): String {
        return MyPreference(requireContext()).getUserID() ?: ""
    }

    private fun getBookId(): String {
        return arguments?.getString("book_id") ?: ""
    }

    private fun setupMainBook(data: ReadthymAllBookResponse) {
        mAdapter.setWithNewData(data.resData.toMutableList())
        mAdapter.notifyDataSetChanged()
    }

    override fun initAction() {
        binding.icBackButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun initData() {
        viewModel.getBooks()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageBookBinding.inflate(inflater)
        return binding.root
    }

    private fun requestPemission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) ==
                PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) ==
                PackageManager.PERMISSION_DENIED
            ) {
                val permission = arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                requestPermissions(permission, PERMISSION_CODE_STORAGE)
            } else {

            }
        } else {
            // do nothing
        }
    }

}