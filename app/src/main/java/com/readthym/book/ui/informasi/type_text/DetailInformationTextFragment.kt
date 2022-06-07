package com.readthym.book.ui.informasi.type_text

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.henrylabs.qumparan.utils.base.BaseFragment
import com.readthym.book.R
import com.readthym.book.data.remote.reqres.CommonContent
import com.readthym.book.data.remote.reqres.CommonTitle
import com.readthym.book.databinding.*
import kotlinx.parcelize.Parcelize
import org.koin.android.viewmodel.ext.android.viewModel
import android.content.Intent
import android.net.Uri


class DetailInformationTextFragment : BaseFragment() {


    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentInformationTypeTextBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val IS_FROM_DOC_ANSWER_DETAIL = "isFromDocumentAnswerDetail"
        const val IS_RAW_HTML = "isRawHTML"
        const val CONTENT_RAW = "isFroSDSDSmDocumentAnswerDetail"
        const val TITLE_RAW = "TITLE_RAW"
        const val NEXT_DEST = "NEXT_DEST"
        const val IS_WEB_VIEW = "IS_WEB_VIEW"
    }

    override fun initUI() {
        hideActionBar()

        val next_dest =
            arguments?.getParcelable<NextDestinationEnum>(NEXT_DEST) ?: NextDestinationEnum.HOME

        binding.btnBack.setOnClickListener {
            if (next_dest.destination != null)
                findNavController().navigate(next_dest.destination)
            else
                findNavController().popBackStack()
        }

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            @Override
            override fun handleOnBackPressed() {
                if (next_dest.destination != null)
                    findNavController().navigate(next_dest.destination)
                else
                    findNavController().popBackStack()
            }
        })

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

    @SuppressLint("SetJavaScriptEnabled")
    override fun initAction() {
        arguments?.apply {

            val content =
                getParcelable<CommonContent>("text_content")
            val title = getParcelable<CommonTitle>("text_title")
            val isWebview = getBoolean(IS_WEB_VIEW) ?: false

            // for document answer detail
            val isFromDocumentAnswerDetail = getBoolean(IS_FROM_DOC_ANSWER_DETAIL) ?: false
            val isRawHTML = getBoolean(IS_RAW_HTML) ?: false
            val content_raw = getString(CONTENT_RAW)
            val title_raw = getString(TITLE_RAW)

            binding.labelPageTitleTopbar.text = title?.id.toString()

            val webView = binding.webView
            val webSettings = webView.settings
            webView.getSettings().pluginState = WebSettings.PluginState.ON
            webView.settings.domStorageEnabled = true
            webSettings.javaScriptEnabled = true
            webSettings.allowFileAccess = true
            webSettings.allowContentAccess = true
            webView.clearCache(true)

            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    return if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                        view.context.startActivity(
                            Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        )
                        true
                    } else {
                        false
                    }
                }
            }

            val header = """<!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta http-equiv="X-UA-Compatible" content="IE=edge">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Document</title>
            
            <style>
            @font-face {
               font-family: 'myface';
               src: url('file:///android_asset/fonts/poppins_regular.ttf'); 
            } 
        
            body { 
            font-family: 'myface', serif;
            </style>
            </head>
            <body>"""

            val footer = """</body></html>"""

            if (isWebview) {
                binding.containerToolbar.makeGone()
                webView.loadUrl(content.toString())
            } else if (!isWebview && !isFromDocumentAnswerDetail) {
                val mContent = header + content?.id.toString() + footer
                webView.loadDataWithBaseURL(null, mContent, "text/html", "UTF-8", null);
//                webView.loadData(
//                    mContent,
//                    "text/html; charset=utf-8",
//                    "UTF-8"
//                )
            }

            if (isFromDocumentAnswerDetail || isRawHTML) {
                binding.containerSomething.makeGone()
                binding.labelPageTitleTopbar.text = title_raw
                binding.btnBack.setOnClickListener {
                    findNavController().popBackStack()
                }
                val htmlContent = header + content_raw.toString().normalize() + footer

                webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null);
                binding.webView.loadData(
                    htmlContent,
                    "text/html; charset=utf-8",
                    "UTF-8"
                )
            }


            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    showLoadingArticle(false)
                    webView.visibility = View.VISIBLE
                }

                override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    showLoadingArticle(true)
                }

                override fun onReceivedError(
                    view: WebView,
                    errorCod: Int,
                    description: String,
                    failingUrl: String
                ) {
                    showLoadingArticle(false)
                    showToast("PDF Gagal Dimuat")
                }
            }
        }
    }

    private fun String.normalize(): String {
        return this.replace("&quot;", "\"")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
    }

    override fun initData() {
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInformationTypeTextBinding.inflate(inflater)
        return binding.root
    }

}

@Parcelize
enum class NextDestinationEnum(val destination: Int?) : Parcelable {
    HOME(R.id.nav_home),
    BACK(null)
}

