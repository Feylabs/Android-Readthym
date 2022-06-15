package com.readthym.book.ui.rythm_home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.henrylabs.qumparan.data.remote.QumparanResource
import com.henrylabs.qumparan.utils.base.BaseFragment
import com.readthym.book.R
import com.readthym.book.data.local.MyPreference
import com.readthym.book.data.remote.reqres.BookData
import org.koin.android.viewmodel.ext.android.viewModel
import com.readthym.book.data.remote.reqres.ReadthymAllBookResponse
import com.readthym.book.databinding.FragmentHomeReadthymBinding

class RythmHomeFragment : BaseFragment() {

    val homeViewModel: RythmHomeViewModel by viewModel()
    private val mainBookAdapter by lazy { MainBookAdapter() }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var _binding: FragmentHomeReadthymBinding? = null

    override fun initUI() {
        initAdapter()

        MyPreference(requireContext()).apply {
            val name = getUserName()
            val id = getUserID()

            binding.welcomeText.text = "Welcome Back $name -$id"

            binding.containerUser.setOnClickListener {
                findNavController().navigate(R.id.manageBookFragment)
            }
            if(MyPreference(requireContext()).getUserRole()=="1"){
                binding.containerUser.makeVisible()
            }else{
                binding.containerUser.makeGone()
            }
        }
    }

    private fun initAdapter() {
        binding.rvBookMain.adapter = mainBookAdapter
        binding.rvBookMain.setHasFixedSize(true)
        binding.rvBookMain.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)

        mainBookAdapter.setupAdapterInterface(object : MainBookAdapter.ItemInterface {
            override fun onclick(model: BookData) {
                findNavController().navigate(
                    R.id.detailBookFragment, bundleOf(
                        "book_id" to model.id.toString(),
                        "title" to model.title.toString(),
                        "author" to model.authorName.toString()
                    )
                )
            }
        })
    }

    override fun initObserver() {
        homeViewModel.booksLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Default -> {}
                is QumparanResource.Error -> {}
                is QumparanResource.Loading -> {}
                is QumparanResource.Success -> {
                    it.data?.let { data ->
                        setupMainBook(data)
                    }
                }
            }

        }
    }

    private fun setupMainBook(data: ReadthymAllBookResponse) {
        mainBookAdapter.setWithNewData(data.resData.toMutableList())
        mainBookAdapter.notifyDataSetChanged()
    }


    override fun initAction() {

        binding.logout.setOnClickListener {
            MyPreference(requireContext()).clearPreferences()
            findNavController().navigate(R.id.readthymLoginFragment)
        }

        binding.btnMenuFav.setOnClickListener {
            findNavController().navigate(R.id.favoriteBookFragment)
        }

        binding.searchBox.root.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }
    }

    override fun initData() {
        MyPreference(requireContext()).apply {
            val id  = getUserID()
            if(id.isNullOrEmpty()){
                showToast("Please Login First")
                findNavController().navigate(R.id.readthymLoginFragment)
            }
        }
        homeViewModel.getBooks()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeReadthymBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}