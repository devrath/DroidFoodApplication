package com.demo.code.data

import com.demo.code.data.network.FoodRecipesApi
import com.demo.code.models.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

/** Here we have injected remote data source with food recipe api **/
class RemoteDataSource  @Inject constructor(
    private val foodRecipesApi: FoodRecipesApi
) {

    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipe> {
        return foodRecipesApi.getRecipes(queries)
    }

}
