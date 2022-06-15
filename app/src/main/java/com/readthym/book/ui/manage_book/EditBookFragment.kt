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
import com.henrylabs.qumparan.data.remote.QumparanResource
import com.henrylabs.qumparan.utils.base.BaseFragment
import com.readthym.book.customview.PhotoBottomSheet
import com.readthym.book.customview.UploadCallback
import com.readthym.book.data.remote.reqres.AuthorResponse
import com.readthym.book.databinding.FragmentAddBookBinding
import com.readthym.book.ui.book.DetailBookViewModel
import com.readthym.book.utils.UtilSnackbar
import com.readthym.book.utils.UtilSnackbar.showSnakbarError
import com.readthym.book.utils.UtilSnackbar.showSnakbarSuccess
import me.rosuh.filepicker.config.FilePickerManager
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File


class AddBookFragment : BaseFragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentAddBookBinding? = null

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
                    val extension = someFilepath.substring(someFilepath.lastIndexOf("."))
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

        viewModel.storeBookLiveDataLiveData.observe(viewLifecycleOwner) {
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
                    showSnakbarSuccess(requireActivity(), binding.root, "Berhasil Menambahkan Data")
                }
            }
        }

    }

    private fun populateSpinner(data: List<AuthorResponse.ResData>) {
        val spinnerArray: MutableList<String> = mutableListOf()
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
            val selectedAuthors = binding.spinnerWritter.selectedItem.toString()
            val authorId = authorMap[selectedAuthors]
            val bookTitle  = binding.textFieldTitle.editText?.text.toString()
            val bookCategory = binding.textFieldCategory.editText?.text.toString()
            val bookOverview = binding.textFieldOverview.editText?.text.toString()
            val bookPhoto = File(binding.tvTakePhoto.text.toString())
            val bookPdf = File(binding.tvTakeBook.text.toString())

            viewModel.register(
                title = bookTitle,
                category = bookCategory,
                photo = bookPhoto,
                book = bookPdf,
                description = bookOverview,
                idAuthor = authorId.toString(),
                overview = bookOverview
            )

        }

    }


    override fun initData() {
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
        _binding = FragmentAddBookBinding.inflate(inflater)
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            FilePickerManager.REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val list = FilePickerManager.obtainData()
                    binding.tvTakeBook.text=list.get(0)
                } else {
                    showToast("Choose Something")
                }
            }
        }
    }

}