package com.demo.code.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.demo.code.databinding.RecipesRowLayoutBinding
import com.demo.code.models.FoodRecipe
import com.demo.code.models.Result
import com.demo.code.util.RecipesDiffUtil

class RecipesAdapter () : RecyclerView.Adapter<RecipesAdapter.ViewHolder>() {

    var recipieList: List<Result> = arrayListOf()

    inner class ViewHolder(val binding: RecipesRowLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecipesRowLayoutBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(recipieList[position]){
                binding.recipeImageView.load(this.image){crossfade(true).crossfade(400)}
                binding.titleTextView.text = this.title
                binding.descriptionTextView.text = this.summary
                binding.heartTextView.text = this.aggregateLikes.toString()
                binding.clockTextView.text = this.readyInMinutes.toString()
            }
        }
    }

    override fun getItemCount(): Int {
        return recipieList.size
    }

    fun setData(newData: FoodRecipe){
        val recipesDiffUtil = RecipesDiffUtil(recipieList, newData.results)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        recipieList = newData.results
        diffUtilResult.dispatchUpdatesTo(this)
    }
}