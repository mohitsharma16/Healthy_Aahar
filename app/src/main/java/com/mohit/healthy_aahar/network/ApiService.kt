package com.mohit.healthy_aahar.network

import com.mohit.healthy_aahar.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("register_user")
    suspend fun registerUser(@Body user: User): Response<Map<String, Any>>

    @GET("generate_meal_plan/{uid}")
    suspend fun generateMealPlan(@Path("uid") uid: String): Response<MealPlanResponse>

    @PUT("swap_meal/{uid}")
    suspend fun swapMeal(
        @Path("uid") uid: String,
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

    @GET("get_meal_history")
    suspend fun getMealHistory(): Response<List<LoggedMeal>>

    @GET("weekly_report/{uid}")
    suspend fun getWeeklyReport(@Path("uid") uid: String): Response<WeeklyReport>

    @POST("recipe_feedback/{uid}")
    suspend fun submitFeedback(
        @Path("uid") uid: String,
        @Body feedback: FeedbackRequest
    ): Response<Map<String, Any>>

    // ==================== LATEST ENDPOINTS ====================

    @GET("get_recipe/{recipe_id}")
    suspend fun getRecipeDetails(
        @Path("recipe_id") recipeId: String
    ): Response<RecipeDetails>

    @GET("get_recipe_with_feedback/{recipe_id}")
    suspend fun getRecipeWithFeedback(
        @Path("recipe_id") recipeId: String
    ): Response<RecipeWithFeedback>

    @GET("get_recipes_by_cuisine/{cuisine}")
    suspend fun getRecipesByCuisine(
        @Path("cuisine") cuisine: String,
        @Query("limit") limit: Int = 10
    ): Response<List<RecipeDetails>>

    @GET("search_recipes")
    suspend fun searchRecipes(
        @Query("query") query: String,
        @Query("limit") limit: Int = 10
    ): Response<RecipeSearchResponse>
}

