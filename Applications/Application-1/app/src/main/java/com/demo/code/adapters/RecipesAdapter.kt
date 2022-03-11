package com.demo.code.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.demo.code.R
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
                binding.recipeImageView.load(this.image){crossfade(true).crossfade(400).error(R.drawable.ic_error_placeholder)}
                binding.titleTextView.text = this.title
                binding.descriptionTextView.text = this.summary
                binding.heartTextView.text = this.aggregateLikes.toString()
                binding.clockTextView.text = this.readyInMinutes.toString()
                applyVeganColor(binding.leafImageView,this.vegan)
                applyVeganColor(binding.leafTextView,this.vegan)
            }
        }
    }

    override fun getItemCount(): Int {
        return recipieList.size
    }

    private fun applyVeganColor(view: View, vegan: Boolean) {
        if(vegan){
            when(view){
                is TextView -> {
                    view.setTextColor(
                        ContextCompat.getColor(
                            view.context,
                            R.color.green
                        )
                    )
                }
                is ImageView -> {
                    view.setColorFilter(
                        ContextCompat.getColor(
                            view.context,
                            R.color.green
                        )
                    )
                }
            }
        }
    }

    fun setData(newData: FoodRecipe){
        val recipesDiffUtil = RecipesDiffUtil(recipieList, newData.results)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        recipieList = newData.results
        diffUtilResult.dispatchUpdatesTo(this)
    }
}