package com.readthym.book.ui.fav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.henrylabs.qumparan.data.remote.QumparanResource
import com.henrylabs.qumparan.utils.base.BaseFragment
import com.readthym.book.R
import com.readthym.book.data.local.MyPreference
import com.readthym.book.data.remote.reqres.ListFavoriteBookResponse
import com.readthym.book.databinding.FragmentFavoriteBinding
import com.readthym.book.ui.book.DetailBookViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class FavoriteBookFragment : BaseFragment() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    val viewModel: DetailBookViewModel by viewModel()

    private val mAdapter: FavoriteBookAdapter by lazy { FavoriteBookAdapter() }

    override fun initUI() {
        hideActionBar()
        initRecyclerView()
    }

    private fun initRecyclerView() {

        binding.rvFav.setHasFixedSize(true)
        binding.rvFav.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFav.adapter=mAdapter

        mAdapter.setupAdapterInterface(object : FavoriteBookAdapter.ItemInterface {
            override fun onclick(model: ListFavoriteBookResponse.FavoriteData) {
                findNavController().navigate(
                    R.id.detailBookFragment, bundleOf(
                        "book_id" to model.book.id.toString(),
                        "pdf_url" to model.book.photoPathFull,
                        "title" to model.book.title,
                        "author" to model.book.authorName
                    )
                )
            }

            override fun onDelete(model: ListFavoriteBookResponse.FavoriteData) {
                viewModel.deleteFromFavorite(bookId = model.book.id.toString(), getUserId())
            }
        })
    }

    override fun initObserver() {
        viewModel.userFavoriteLiveData.observe(viewLifecycleOwner) {
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
                    it.data?.let { rawData ->
                        setupUiDetailFromNetwork(rawData)
                    }
                }
            }
        }

        viewModel.deleteFromFavoriteLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Default -> {
                }
                is QumparanResource.Error -> {
                    showLoading(false)
                    viewModel.fetchUserFavorite(getUserId())
                }
                is QumparanResource.Loading -> {
                    showLoading(true)
                }
                is QumparanResource.Success -> {
                    showLoading(false)
                    viewModel.fetchUserFavorite(getUserId())
                    val apiCode = it.data?.apiCode ?: 0
                    if (apiCode != 3) {
                        showToast("Successfully removed from favorite")
                    } else {
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

    private fun setupUiDetailFromNetwork(rawData: ListFavoriteBookResponse) {
        mAdapter.setWithNewData(rawData.resData.toMutableList())
        mAdapter.notifyDataSetChanged()

        if(rawData.resData.isEmpty()){
            showEmptyState(binding.root,true)
        }else{
            showEmptyState(binding.root,false)
        }
    }


    override fun initAction() {
        binding.icBackButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun initData() {
        viewModel.fetchUserFavorite(getUserId())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater)
        return binding.root
    }

}