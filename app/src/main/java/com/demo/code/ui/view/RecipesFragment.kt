package com.demo.code.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.code.adapters.RecipesAdapter
import com.demo.code.databinding.FragmentRecipesBinding
import com.demo.code.ui.vm.MainViewModel
import com.demo.code.util.NetworkResult
import com.demo.code.util.observeOnce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment  : Fragment() {


    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel
    private val mAdapter by lazy { RecipesAdapter() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        readDatabase()
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    Log.d("RecipesFragment", "readDatabase called!")
                    mAdapter.setData(database[0].foodRecipe)
                    displayShimmerView(displayShimmerView = false)
                } else {
                    requestApiData()
                }
            }
        }
    }

    private fun requestApiData() {
        mainViewModel.apply {
            getRecipes(mainViewModel.applyQueries())
            recipesResponse.observe(viewLifecycleOwner, { response ->
                when(response){
                    is NetworkResult.Success -> {
                        displayShimmerView(displayShimmerView = false)
                        response.data?.let {
                            mAdapter.setData(it)
                            displayView()
                        }
                    }
                    is NetworkResult.Error -> {
                        displayShimmerView(displayShimmerView = false)
                        noConnectivityErrorView(response.message.toString())
                    }
                    is NetworkResult.Loading ->{
                        displayShimmerView(displayShimmerView = true)
                    }
                }
            })
        }
    }

    private fun setupRecyclerView() {
        binding.apply {
            recyclerview.adapter = mAdapter
            recyclerview.layoutManager = LinearLayoutManager(requireContext())
        }
        displayShimmerView(displayShimmerView = true)
    }

    private fun displayShimmerView(displayShimmerView :Boolean) = binding.apply {
        if (displayShimmerView) recyclerview.showShimmer() else recyclerview.hideShimmer()
    }

    private fun noConnectivityErrorView(message: String) = binding.apply {
        errorTextView.text = message
        errorTextView.visibility = View.VISIBLE
        errorImageView.visibility = View.VISIBLE
        recyclerview.visibility = View.GONE
    }

    private fun displayView() = binding.apply {
        errorTextView.visibility = View.GONE
        errorImageView.visibility = View.GONE
        recyclerview.visibility = View.VISIBLE
    }


}