package com.readthym.book.ui.manage_book

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.henrylabs.qumparan.data.remote.QumparanResource
import com.henrylabs.qumparan.utils.base.BaseFragment
import com.readthym.book.customview.PhotoBottomSheet
import com.readthym.book.customview.UploadCallback
import com.readthym.book.data.remote.reqres.AuthorResponse
import com.readthym.book.data.remote.reqres.BookData
import com.readthym.book.databinding.FragmentEditBookBinding
import com.readthym.book.ui.book.DetailBookViewModel
import com.readthym.book.utils.UIHelper.loadImageFromURL
import com.readthym.book.utils.UtilSnackbar.showSnakbarError
import com.readthym.book.utils.UtilSnackbar.showSnakbarSuccess
import me.rosuh.filepicker.config.FilePickerManager
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File


class EditBookFragment : BaseFragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentEditBookBinding? = null

    val viewModel: DetailBookViewModel by viewModel()
    private val authorMap = mutableMapOf<String, Int>()

    override fun initUI() {
        setupFormListener()
    }

    private fun setupFormListener() {

        binding.tvTakePhoto.setOnClickListener {
            PhotoBottomSheet(object : UploadCallback {
                override fun upload(file: File) {
                    val someFilepath = file.absolutePath.toString()
                    binding.tvTakePhoto.text = file.absolutePath.toString()
                }
            }).show(
                (context as FragmentActivity).supportFragmentManager,
                "photoBottomSheet"
            )
        }

        binding.tvTakeBook.setOnClickListener {
            FilePickerManager
                .from(this)
                .forResult(FilePickerManager.REQUEST_CODE)
        }
    }


    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    override fun initObserver() {
        viewModel.authorLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Success -> {
                    it.data?.resData?.let {
                        populateSpinner(it)
                    }
                }
            }
        }

        viewModel.bookDetailLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Default -> {
                    showLoading(false)
                }
                is QumparanResource.Error -> {
                    showLoading(false)
                    showSnakbarError(requireActivity(), binding.root, it.message.toString())
                }
                is QumparanResource.Loading -> {
                    showLoading(true)
                }
                is QumparanResource.Success -> {
                    showLoading(false)
                    it.data?.resData?.let {
                        setupDetailBookData(it)
                    }
                }
            }
        }

        viewModel.updateBookLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Default -> {
                    showLoading(false)
                }
                is QumparanResource.Error -> {
                    showLoading(false)
                    showSnakbarError(requireActivity(), binding.root, it.message.toString())
                }
                is QumparanResource.Loading -> {
                    showLoading(true)
                }
                is QumparanResource.Success -> {
                    showLoading(false)
                    findNavController().popBackStack()
                    showSnakbarSuccess(requireActivity(), binding.root, "Berhasil Mengedit Data")
                }
            }
        }

    }

    private fun setupDetailBookData(it: BookData) {
        binding.textFieldTitle.setText(it.title)
        binding.textFieldCategory.setText(it.category)
        binding.textFieldOverview.setText(it.overview)
        binding.tvPenulis.text = it.authorName
        binding.idBook.text = it.id.toString()
        binding.idAuthor.text=it.idAuthor.toString()
        binding.thumbnails.loadImageFromURL(requireContext(), it.photoPathFull)

    }

    private fun populateSpinner(data: List<AuthorResponse.ResData>) {
        val spinnerArray: MutableList<String> = mutableListOf()
        authorMap.put("Pilih Penulis", -99)
        spinnerArray.add("Pilih Penulis")
        data.forEachIndexed { index, data ->
            authorMap.put(data.name, data.id)
            spinnerArray.add(data.name)
        }

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(), android.R.layout.simple_spinner_item, spinnerArray
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerWritter.adapter = adapter
    }


    private fun showLoading(b: Boolean) {
        if (b) {
            binding.btnSave.makeGone()
            binding.loadingLogin.makeVisible()
        } else {
            binding.btnSave.makeVisible()
            binding.loadingLogin.makeGone()
        }
    }

    override fun initAction() {
        binding.btnSave.setOnClickListener {
            var authorId : String? = null
            var bookPhoto: File? = null
            var bookPdf: File? = null

            val bookFilePath =  binding.tvTakeBook.text.toString()
            val photoFilePath = binding.tvTakePhoto.text.toString()

            val mselectedAuthors = binding.spinnerWritter.selectedItem.toString()
            var mauthorId = authorMap[mselectedAuthors]
            val mbookTitle = binding.textFieldTitle.editText?.text.toString()
            val mbookCategory = binding.textFieldCategory.editText?.text.toString()
            val mbookOverview = binding.textFieldOverview.editText?.text.toString()

            mauthorId = if(mauthorId==-99) binding.idAuthor.text.toString().toIntOrNull() else mauthorId
//            if(photoFilePath.contains("/")) showToast("Foto Ada")
//            if(bookFilePath.contains("/")) showToast("Buku Aada")

            if(photoFilePath.contains("/")) bookPhoto=File(photoFilePath)
            if(bookFilePath.contains("/")) bookPdf=File(bookFilePath)

            showToast(mauthorId.toString())
            viewModel.updateBook(
                id = binding.idBook.text.toString(),
                title = mbookTitle,
                category = mbookCategory,
                photo = bookPhoto,
                book = bookPdf,
                description = mbookOverview,
                idAuthor = mauthorId,
                overview = mbookOverview,
            )
        }
    }


    private fun getBookId(): String {
        val id = arguments?.getString("id") ?: ""
        return id
    }

    override fun initData() {
        viewModel.fetchDetailBook(getBookId())
        viewModel.fetchAuthors()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditBookBinding.inflate(inflater)
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            FilePickerManager.REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val list = FilePickerManager.obtainData()
                    binding.tvTakeBook.text = list.get(0)
                } else {
                    showToast("Choose Something")
                }
            }
        }
    }

    private fun TextInputLayout.setText(s: String) {
        this.editText?.setText(s)
    }

}


