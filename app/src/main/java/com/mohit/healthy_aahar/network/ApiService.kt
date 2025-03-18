package com.mohit.healthy_aahar.network

import com.mohit.healthy_aahar.model.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("register_user")
    fun registerUser(@Body user: UserProfile): Call<Map<String, Any>>

    @GET("generate_meal_plan/{user_name}")
    fun getMealPlan(@Path("user_name") userName: String): Call<MealPlanResponse>

    @PUT("swap_meal/{user_name}")
    fun swapMeal(@Path("user_name") userName: String, @Query("meal_index") mealIndex: Int): Call<Map<String, Any>>

    @POST("recommend")
    fun recommendRecipes(@Body request: RecipeRequest): Call<List<Meal>>
}
