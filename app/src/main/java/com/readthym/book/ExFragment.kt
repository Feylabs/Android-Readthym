package com.readthym.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.henrylabs.qumparan.utils.base.BaseFragment
import com.readthym.book.databinding.FragmentExBinding


class ExFragment : BaseFragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentExBinding? = null

    override fun initUI() {
    }

    override fun initObserver() {
    }

    override fun initAction() {
    }

    override fun initData() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentExBinding.inflate(inflater)
        return binding.root
    }

}