package com.readthym.book.ui.book

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.henrylabs.qumparan.data.remote.QumparanResource
import com.henrylabs.qumparan.utils.base.BaseFragment
import com.readthym.book.R
import com.readthym.book.data.local.MyPreference
import com.readthym.book.data.remote.reqres.ReadthymDetailBookResponse
import com.readthym.book.databinding.FragmentDetailBookBinding
import com.readthym.book.utils.UIHelper.loadImageFromURL
import com.yarolegovich.lovelydialog.LovelyStandardDialog
import org.koin.android.viewmodel.ext.android.viewModel

class DetailBookFragment : BaseFragment() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentDetailBookBinding? = null
    private val binding get() = _binding!!

    val viewModel: DetailBookViewModel by viewModel()

    override fun initUI() {
        hideActionBar()
    }

    override fun initObserver() {
        viewModel.addToFavoriteLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Default -> {
                    binding.btnAddToFav.isEnabled = true
                }
                is QumparanResource.Error -> {
                    binding.btnAddToFav.isEnabled = true
                    checkIfFavorite()
                }
                is QumparanResource.Loading -> {
                    showLoading(true)
                }
                is QumparanResource.Success -> {
                    binding.btnAddToFav.isEnabled = true
                    checkIfFavorite()
                    showToast("Successfully added to favorite")
                }
            }
        }

        viewModel.deleteFromFavoriteLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Default -> {
                    binding.btnAddToFav.isEnabled = true
                }
                is QumparanResource.Error -> {
                    binding.btnAddToFav.isEnabled = true
                    checkIfFavorite()
                    showToast(it.message.toString())
                }
                is QumparanResource.Loading -> {
                    showLoading(true)
                }
                is QumparanResource.Success -> {
                    val apiCode = it.data?.apiCode ?: 0
                    if (apiCode != 3) {
                        binding.btnAddToFav.isEnabled = true
                        checkIfFavorite()
                        showToast("Successfully removed from favorite")
                    } else {
                    }
                }
            }
        }

        viewModel.favoriteCheckLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Success -> {
                    binding.btnAddToFav.isEnabled = true
                    showLoading(false)
                    val code = it.data?.statusCode ?: 0
                    if (code == 3) {
                        setFavButtonUi(isFavorite = false)
                    } else {
                        setFavButtonUi(isFavorite = true)
                    }
                }
                is QumparanResource.Loading -> {
                    binding.btnAddToFav.isEnabled = false
                    showLoading(true)
                }
                else -> {
                    binding.btnAddToFav.isEnabled = true
                    showLoading(false)
                }
            }
        }


        viewModel.bookDetailLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Error -> {
                    showLoading(false)
                    showToast(it.message.toString())
                }
                is QumparanResource.Loading -> {
                    showLoading(true)
                    showToast("Memuat Data Buku")
                }
                is QumparanResource.Success -> {
                    showLoading(false)
                    it.data?.let { data ->
                        setupUiDetailFromNetwork(data)
                    }
                }
            }

        }
    }

    private fun showLoading(b: Boolean) {
        if (b) {
            binding.loadingContent.root.makeVisible()
        } else {
            binding.loadingContent.root.makeGone()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setFavButtonUi(isFavorite: Boolean) {
        if (isFavorite) {
            binding.btnAddToFav.text = "Delete From Favorite"
            binding.btnAddToFav.setOnClickListener {
                binding.btnAddToFav.isEnabled = false

                LovelyStandardDialog(requireContext(), LovelyStandardDialog.ButtonLayout.VERTICAL)
                    .setTopColorRes(R.color.red)
                    .setButtonsColorRes(R.color.domain_blue)
                    .setTopTitleColor(R.color.white)
                    .setNegativeButtonColor(resources.getColor(R.color.black))
                    .setTitle("Anda Yakin ?")
                    .setMessage("Buku ini akan dihapus dari favorit anda")
                    .setPositiveButton("Batal") { }
                    .setNegativeButton(
                        "Hapus"
                    ) {
                        viewModel.deleteFromFavorite(
                            bookId = getBookId(), userId = getUserId()
                        )
                    }
                    .show()
            }
        } else {
            binding.btnAddToFav.text = "Add To Favorite"
            binding.btnAddToFav.setOnClickListener {
                binding.btnAddToFav.isEnabled = false
                viewModel.saveFavorite(
                    bookId = getBookId(), userId = getUserId()
                )
            }
        }
    }

    private fun getUserId(): String {
        return MyPreference(requireContext()).getUserID() ?: ""
    }

    private fun getBookId(): String {
        return arguments?.getString("book_id") ?: ""
    }

    private fun setupUiDetailFromNetwork(rawData: ReadthymDetailBookResponse) {
        val bookData = rawData.resData
        binding.thumbnails.loadImageFromURL(requireContext(), bookData.photoPathFull)
        binding.tvBookTitle.text = bookData.title
        binding.tvOverview.text = bookData.overview
        binding.tvAboutAuthor.text = bookData.authorDesc
        binding.tvBookAuthor.text = bookData.authorName

        binding.btnReadBook.setOnClickListener {
            findNavController().navigate(
                R.id.detailInformationPdfFragment, bundleOf(
                    "pdf_url" to bookData.pdfPathFull,
                    "title" to bookData.title,
                    "author" to bookData.authorName
                )
            )
        }
    }


    override fun initAction() {
        binding.icBackButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun initData() {
        checkIfFavorite()
        viewModel.fetchDetailBook(getBookId())
    }

    private fun checkIfFavorite() {
        val book_id = getBookId()
        val userId = getUserId()
        viewModel.fetchIfFavorite(bookId = book_id, userId = userId.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBookBinding.inflate(inflater)
        return binding.root
    }

}