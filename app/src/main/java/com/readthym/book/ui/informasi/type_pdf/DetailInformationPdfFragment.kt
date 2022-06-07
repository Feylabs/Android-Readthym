package com.readthym.book.ui.informasi.type_pdf

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.henrylabs.qumparan.utils.base.BaseFragment
import com.readthym.book.databinding.FragmentInformationTypePdfBinding
import org.koin.android.viewmodel.ext.android.viewModel
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.readthym.book.utils.FileUtils
import java.io.File


class DetailInformationPdfFragment : BaseFragment() {


    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentInformationTypePdfBinding? = null
    private val binding get() = _binding!!


    companion object {
        const val NEXT_DESTINATION = "NEXT_DESTINATION"
    }


    override fun initUI() {
        showLoadingArticle(true)
        hideActionBar()
        val next_dest = arguments?.getInt(NEXT_DESTINATION)

        if (next_dest == null || next_dest == 0) {

        } else {
            requireActivity().onBackPressedDispatcher.addCallback(
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        findNavController().navigate(next_dest)
                    }
                }
            )
        }

        binding.btnOpenBrowser.setOnClickListener {
            val url = arguments?.getString("pdf_url") ?: ""

            openWebsite(url)

        }

    }

    override fun initObserver() {
    }


    private fun showLoadingArticle(b: Boolean) {
        if (b) {
            binding.screenLoading.root.makeVisible()
        } else {
            binding.screenLoading.root.makeGone()
        }
    }

    override fun initAction() {
        val url = arguments?.getString("pdf_url") ?: ""

        val author = arguments?.getString("author") ?: ""
        val title = arguments?.getString("title") ?: ""

        binding.tvBookAuthor.text = author
        binding.tvBookTitle.text = title

        downloadPdfFromInternet(
            url = url,
            dirPath = FileUtils.getRootDirPath(requireContext()),
            fileName = title
        )
    }

    override fun initData() {
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInformationTypePdfBinding.inflate(inflater)
        return binding.root
    }

    private fun downloadPdfFromInternet(url: String, dirPath: String, fileName: String) {
        PRDownloader.download(
            url,
            dirPath,
            fileName
        ).build()
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    val downloadedFile = File(dirPath, fileName)
                    showPdfFromFile(downloadedFile)
                    showLoadingArticle(false)
                }

                override fun onError(error: com.downloader.Error?) {
                    showToast("PDF Error : $error")
                    showLoadingArticle(false)
                }
            })
    }

    private fun openWebsite(mUrl: String) {
        var url = mUrl
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    private fun showPdfFromFile(file: File) {
        binding.idPDFView.fromFile(file)
            .password(null)
            .defaultPage(0)
            .enableSwipe(true)
            .enableDoubletap(true)
            .onPageError { page, _ ->
                showToast("Error on Page $page")
            }
            .load()
    }

}

