package com.mohit.healthy_aahar.repository

//import com.mohit.healthy_aahar.network.RetrofitClient
//import com.mohit.healthy_aahar.model.*
//
//class RecipeRepository {
//    private val api = RetrofitClient.apiService
//
//    suspend fun registerUser(userProfile: UserProfile): Result<Map<String, Any>> {
//        return try {
//            val response = api.registerUser(userProfile)
//            if (response.isSuccessful) {
//                Result.success(response.body() ?: mapOf("message" to "User registered successfully"))
//            } else {
//                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
//            }
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//
//    suspend fun generateMealPlan(userName: String): Result<MealPlanResponse> {
//        return try {
//            val response = api.generateMealPlan(userName)
//            if (response.isSuccessful) {
//                response.body()?.let {
//                    Result.success(it)
//                } ?: Result.failure(Exception("Empty response"))
//            } else {
//                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
//            }
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//
//    suspend fun swapMeal(userName: String, mealIndex: Int): Result<SwapMealResponse> {
//        return try {
//            val request = SwapMealRequest(mealIndex)
//            val response = api.swapMeal(userName, request)
//            if (response.isSuccessful) {
//                response.body()?.let {
//                    Result.success(it)
//                } ?: Result.failure(Exception("Empty response"))
//            } else {
//                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
//            }
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//}