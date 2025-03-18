package com.mohit.healthy_aahar.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohit.healthy_aahar.model.*
import com.mohit.healthy_aahar.network.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel : ViewModel() {
    private val apiService = RetrofitClient.apiService

    fun registerUser(user: UserProfile, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            apiService.registerUser(user).enqueue(object : Callback<Map<String, Any>> {
                override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                    if (response.isSuccessful) {
                        onResult(true, "User Registered")
                    } else {
                        onResult(false, "Failed to Register")
                    }
                }

                override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                    onResult(false, t.message ?: "Unknown Error")
                }
            })
        }
    }

    fun getMealPlan(userName: String, onResult: (Boolean, MealPlanResponse?) -> Unit) {
        viewModelScope.launch {
            apiService.getMealPlan(userName).enqueue(object : Callback<MealPlanResponse> {
                override fun onResponse(call: Call<MealPlanResponse>, response: Response<MealPlanResponse>) {
                    if (response.isSuccessful) {
                        onResult(true, response.body())
                    } else {
                        onResult(false, null)
                    }
                }

                override fun onFailure(call: Call<MealPlanResponse>, t: Throwable) {
                    onResult(false, null)
                }
            })
        }
    }

    fun swapMeal(userName: String, mealIndex: Int, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            apiService.swapMeal(userName, mealIndex).enqueue(object : Callback<Map<String, Any>> {
                override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                    if (response.isSuccessful) {
                        onResult(true, "Meal Swapped Successfully")
                    } else {
                        onResult(false, "Failed to Swap Meal")
                    }
                }

                override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                    onResult(false, t.message ?: "Unknown Error")
                }
            })
        }
    }

    fun recommendRecipes(ingredients: String, onResult: (Boolean, List<Meal>?) -> Unit) {
        viewModelScope.launch {
            apiService.recommendRecipes(RecipeRequest(ingredients)).enqueue(object : Callback<List<Meal>> {
                override fun onResponse(call: Call<List<Meal>>, response: Response<List<Meal>>) {
                    if (response.isSuccessful) {
                        onResult(true, response.body())
                    } else {
                        onResult(false, null)
                    }
                }

                override fun onFailure(call: Call<List<Meal>>, t: Throwable) {
                    onResult(false, null)
                }
            })
        }
    }
}
