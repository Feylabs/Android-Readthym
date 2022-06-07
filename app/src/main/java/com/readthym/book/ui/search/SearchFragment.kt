package com.readthym.book.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.henrylabs.qumparan.data.remote.QumparanResource
import com.henrylabs.qumparan.utils.base.BaseFragment
import com.readthym.book.R
import com.readthym.book.data.remote.reqres.BookData
import com.readthym.book.databinding.FragmentSearchBinding
import com.readthym.book.ui.book.DetailBookViewModel
import com.readthym.book.utils.DialogUtils
import org.koin.android.viewmodel.ext.android.viewModel


class SearchFragment : BaseFragment() {

    val viewModel: DetailBookViewModel by viewModel()

    private val binding get() = _binding!!
    private var _binding: FragmentSearchBinding? = null

    private val adapter by lazy { SearchAdapter() }
    lateinit var mView: View

    override fun initUI() {
        mView = binding.root
        addSearchListener()
        showLoading(false)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.rvSearchResult.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun addSearchListener() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchBook(
                    query.toString(),
                )
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.data.clear()
                adapter.notifyDataSetChanged()
                return false
            }
        })
    }


    override fun initObserver() {


        viewModel.searchLiveData.observe(viewLifecycleOwner){
            when(it){
                is QumparanResource.Default -> {

                }
                is QumparanResource.Error -> {
                    showLoading(false)
                    DialogUtils.showCustomDialog(
                        context = requireContext(),
                        title = getString(R.string.NoResult),
                        message = getString(R.string.SearchNoResult),
                        positiveAction = Pair("OK", {}),
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                }
                is QumparanResource.Loading -> {
                    showLoading(true)
                }
                is QumparanResource.Success -> {
                    showLoading(false)
                    it.data?.resData?.let {bookData->
                        setupUI(bookData.toMutableList())
                    }
                }
            }
        }
    }

    private fun setupUI(bookList:MutableList<BookData>) {

        adapter.setWithNewData(bookList)
        adapter.notifyDataSetChanged()


        adapter.setupAdapterInterface(object : SearchAdapter.SearchItemInterface {
            override fun onclick(model: BookData) {
                findNavController().navigate(
                    R.id.detailBookFragment, bundleOf(
                        "book_id" to model.id.toString(),
                        "pdf_url" to model.photoPathFull,
                        "title" to model.title,
                        "author" to model.authorName
                    )
                )
            }
        })
    }

    private fun showLoading(b: Boolean) {
        if (b) {
            binding.rvSearchResult.makeGone()
            binding.includeLoading.root.makeVisible()
        } else {
            binding.rvSearchResult.makeVisible()
            binding.includeLoading.root.makeGone()
        }
    }

    override fun initAction() {
    }

    override fun initData() {
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

}
