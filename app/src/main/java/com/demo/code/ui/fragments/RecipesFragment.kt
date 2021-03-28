package com.demo.code.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.demo.code.adapters.RecipesAdapter
import com.demo.code.databinding.FragmentRecipesBinding
import dagger.hilt.android.AndroidEntryPoint
import org.intellij.lang.annotations.Language

@AndroidEntryPoint
class RecipesFragment  : Fragment() {


    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    private lateinit var rvAdapter: RecipesAdapter
    private lateinit var recepiesList : List<Language>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        //recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }




}