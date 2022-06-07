package com.readthym.book.ui.auth

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.henrylabs.qumparan.data.remote.QumparanResource
import com.henrylabs.qumparan.utils.base.BaseFragment
import com.readthym.book.R
import com.readthym.book.data.local.MyPreference
import com.readthym.book.data.remote.reqres.RegisterResponse
import com.readthym.book.databinding.FragmentReadtyhmRegisterBinding
import com.readthym.book.ui.NavdrawContainerActivity
import org.koin.android.viewmodel.ext.android.viewModel


class RegisterFragment : BaseFragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentReadtyhmRegisterBinding? = null

    val viewModel: AuthViewModel by viewModel()

    override fun initUI() {
        setupFormListener()
    }

    private fun setupFormListener() {
        binding.textFieldEmail.editText?.apply {
            doAfterTextChanged {
                if (text.toString().isNotEmpty()) {
                    binding.textFieldEmail.error = null
                }
            }
        }

        binding.textFieldPassword.editText?.apply {
            doAfterTextChanged {
                if (text.toString().isNotEmpty()) {
                    binding.textFieldPassword.error = null
                }
            }
        }
    }

    fun validateInput() {
        val email = binding.textFieldEmail.editText?.text
        val name = binding.textFieldName.editText?.text
        val password = binding.textFieldPassword.editText?.text
        var isError = false

        if (isValidEmail(email).not()) {
            isError = true
            binding.textFieldEmail.error = getString(R.string.email_format_error)
        }

        if (password != null) {
            if (password.length < 8) {
                isError = true
                binding.textFieldPassword.error = getString(R.string.minimal_8_karakter)
            }
        }

        if (password.isNullOrEmpty()) {
            isError = true
            binding.textFieldPassword.error = getString(R.string.column_required)
        }
        if (name.isNullOrEmpty()) {
            isError = true
            binding.textFieldName.error = getString(R.string.column_required)
        }

        if (!isError) {
            proceedLogin(
                name = name.toString(),
                email = email.toString(),
                password = password.toString()
            )
        }
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    override fun initObserver() {

        viewModel.registerLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Default -> {
                    showLoading(false)
                }
                is QumparanResource.Error -> {
                    showLoading(false)
                    showToast(it.message.toString())
                }
                is QumparanResource.Loading -> {
                    showLoading(true)
                }
                is QumparanResource.Success -> {
                    showLoading(false)
                    showToast(it.message.toString())
                    it.data?.let {
                        proceedRegisSuccess(it)
                    }
                }
            }
        }
    }

    private fun proceedRegisSuccess(it: RegisterResponse) {
        saveLoginData(it)
        proceedToNextActivity()
    }


    private fun proceedToNextActivity() {
        val nextActivity: Int = arguments?.getInt(NavdrawContainerActivity.NEXT_DESTINATION) ?: R.id.rythmHomeFragment
        findNavController().navigate(nextActivity)
    }

    private fun saveLoginData(it: RegisterResponse) {
        MyPreference(requireContext()).apply {
            val user = it.resData
            saveUserID(user.id.toString())
            saveUserEmail(user.email)
            saveUserName(user.name)
        }
    }

    private fun showLoading(b: Boolean) {
        if (b) {
            binding.btnLogin.makeGone()
            binding.loadingLogin.makeVisible()
        } else {
            binding.btnLogin.makeVisible()
            binding.loadingLogin.makeGone()
        }
    }

    override fun initAction() {
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.readthymLoginFragment)
        }

        binding.btnLogin.setOnClickListener {
            validateInput()
        }
    }

    private fun proceedLogin(email: String, password: String,name:String) {
        viewModel.register(email = email, password =  password,name = name)
    }

    override fun initData() {
        MyPreference(requireContext()).apply {
            val id = getUserID()
            val name = getUserName()
            if (id.isNullOrEmpty().not()){
                findNavController().navigate(R.id.rythmHomeFragment)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentReadtyhmRegisterBinding.inflate(inflater)
        return binding.root
    }

}