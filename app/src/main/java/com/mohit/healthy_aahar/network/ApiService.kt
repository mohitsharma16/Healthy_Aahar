package com.mohit.healthy_aahar.network

import com.mohit.healthy_aahar.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("register_user")
    suspend fun registerUser(@Body user: User): Response<Map<String, Any>>

    @GET("generate_meal_plan/{user_name}")
    suspend fun generateMealPlan(@Path("user_name") userName: String): Response<MealPlanResponse>

    @PUT("swap_meal/{user_name}")
    suspend fun swapMeal(
        @Path("user_name") userName: String,
        @Body request: SwapMealRequest
    ): Response<Map<String, Any>>

    @GET("get_user/{uid}")
    suspend fun getUser(@Path("uid") uid: String): Response<UserDetails>

    @POST("generate_recipe_by_ingredients")
    suspend fun generateRecipeByIngredients(
        @Body request: IngredientRecipeRequest
    ): Response<Meal>

    @POST("log_meal")
    suspend fun logMeal(@Body request: LogMealRequest): Response<Map<String, Any>>

    @GET("daily_nutrition/{uid}/{date}")
    suspend fun getDailyNutrition(
        @Path("uid") uid: String,
        @Path("date") date: String
    ): Response<DailyNutrition>

    @POST("log_custom_meal")
    suspend fun logCustomMeal(@Body request: CustomMealRequest): Response<CustomMeal>

}

