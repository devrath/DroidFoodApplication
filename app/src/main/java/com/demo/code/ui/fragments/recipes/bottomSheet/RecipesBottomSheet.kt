package com.demo.code.ui.fragments.recipes.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.demo.code.databinding.RecipesBottomSheetFragmentBinding

class RecipesBottomSheet : Fragment() {

    companion object {
        fun newInstance() = RecipesBottomSheet()
    }

    private var _binding: RecipesBottomSheetFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: RecipesBottomSheetViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = RecipesBottomSheetFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RecipesBottomSheetViewModel::class.java)

    }

}