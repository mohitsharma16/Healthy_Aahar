package com.mohit.healthy_aahar.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohit.healthy_aahar.model.CustomMeal
import com.mohit.healthy_aahar.model.CustomMealRequest
import com.mohit.healthy_aahar.model.DailyNutrition
import com.mohit.healthy_aahar.model.IngredientRecipeRequest
import com.mohit.healthy_aahar.model.LogMealRequest
import com.mohit.healthy_aahar.model.Meal
import com.mohit.healthy_aahar.model.MealPlanResponse
import com.mohit.healthy_aahar.model.RecipeDetails
import com.mohit.healthy_aahar.model.SwapMealRequest
import com.mohit.healthy_aahar.model.User
import com.mohit.healthy_aahar.model.UserDetails
import com.mohit.healthy_aahar.network.RetrofitClient
import com.mohit.healthy_aahar.network.RetrofitClient.apiService
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val api = RetrofitClient.apiService

    private val _mealPlan = MutableLiveData<MealPlanResponse>()
    val mealPlan: LiveData<MealPlanResponse> get() = _mealPlan

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _registerResponse = MutableLiveData<String?>()
    val registerResponse: LiveData<String?> get() = _registerResponse

    private val _userDetails = MutableLiveData<UserDetails?>()
    val userDetails: LiveData<UserDetails?> get() = _userDetails

    private val _generatedRecipe = MutableLiveData<Meal?>()
    val generatedRecipe: LiveData<Meal?> get() = _generatedRecipe

    private val _logMealResponse = MutableLiveData<String?>()
    val logMealResponse: LiveData<String?> get() = _logMealResponse

    private val _dailyNutrition = MutableLiveData<DailyNutrition?>()
    val dailyNutrition: LiveData<DailyNutrition?> get() = _dailyNutrition

    private val _customMeal = MutableLiveData<CustomMeal?>()
    val customMeal: LiveData<CustomMeal?> get() = _customMeal

    private val _recipeDetails = MutableLiveData<RecipeDetails?>()
    val recipeDetails: LiveData<RecipeDetails?> = _recipeDetails

    private val _isLoadingRecipe = MutableLiveData<Boolean>()
    val isLoadingRecipe: LiveData<Boolean> = _isLoadingRecipe

    fun registerUser(user: User) {
        viewModelScope.launch {
            try {
                Log.d("API", "Sending user: $user")
                val response = api.registerUser(user)
                Log.d("API", "Response code: ${response.code()}")

                if (response.isSuccessful) {
                    val message = response.body()?.get("message")?.toString()
                    _registerResponse.value = message ?: "Success!"
                    Log.d("API", "Register Success: $message")
                } else {
                    val errorText = response.errorBody()?.string()
                    _error.value = "Failed: $errorText"
                    Log.e("API", "Error: $errorText")
                }
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("API", "Exception: ${e.message}")
            }
        }
    }

    fun getMealPlan(uid: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                Log.d("API", "Fetching meal plan for $uid")
                val response = api.generateMealPlan(uid)
                Log.d("API", "Meal plan response code: ${response.code()}")

                if (response.isSuccessful) {
                    _mealPlan.value = response.body()
                    Log.d("API", "Meal plan received: ${response.body()}")
                } else {
                    val errorText = response.errorBody()?.string()
                    _error.value = "Meal plan error: $errorText"
                    Log.e("API", "Error: $errorText")
                }
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("API", "Exception: ${e.message}")
            }finally {
                // IMPORTANT: Always set loading to false when done
                _isLoading.value = false
            }
        }
    }

    fun swapMeal(userName: String, index: Int,onResult: (Boolean) -> Unit) {

        viewModelScope.launch {
            try {
                Log.d("API", "Swapping meal at index $index for $userName")
                val response = api.swapMeal(userName, SwapMealRequest(index))
                Log.d("API", "Swap response code: ${response.code()}")

                if (response.isSuccessful) {
                    val newMeal = response.body()?.get("new_meal")
                    Log.d("API", "Swapped Meal: $newMeal")
                    onResult(true)
                    _registerResponse.value = "Meal swapped successfully"
                } else {
                    val errorText = response.errorBody()?.string()
                    _error.value = "Swap failed: $errorText"
                    onResult(false)
                    Log.e("API", "Error: $errorText")
                }
            } catch (e: Exception) {
                _error.value = e.message
                onResult(false)
                Log.e("API", "Exception: ${e.message}")
            }
        }
    }

    fun fetchUserDetails(uid: String, onResult: (String?) -> Unit) {
        viewModelScope.launch {
            try {
                Log.d("API", "Fetching user by uid: $uid")
                val response = api.getUser(uid)
                if (response.isSuccessful) {
                    _userDetails.value = response.body()
                    val name = _userDetails.value?.name
                    onResult(name)
                    Log.d("API", "User details: ${response.body()}")
                } else {
                    _error.value = "Failed: ${response.errorBody()?.string()}"
                    onResult(null)
                }
            } catch (e: Exception) {
                _error.value = e.message
                onResult(null)
            }
        }
    }

    fun generateRecipeByIngredients(uid: String, ingredients: List<String>) {
        viewModelScope.launch {
            try {
                Log.d("API", "Generating recipe for UID: $uid, ingredients: $ingredients")
                val response = api.generateRecipeByIngredients(
                    IngredientRecipeRequest(uid, ingredients)
                )

                if (response.isSuccessful) {
                    _generatedRecipe.value = response.body()
                    Log.d("API", "Generated recipe: ${response.body()}")
                } else {
                    val err = response.errorBody()?.string()
                    _error.value = "Failed: $err"
                    Log.e("API", "Error: $err")
                }
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("API", "Exception: ${e.message}")
            }
        }
    }

    // Add this updated logMeal function to your MainViewModel

    fun logMeal(
        uid: String,
        mealId: String,
        date: String,
        mealType: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val request = LogMealRequest(
                    uid = uid,
                    meal_id = mealId,
                    date = date,
                    meal_type = mealType
                )

                val response = apiService.logMeal(request)

                if (response.isSuccessful) {
                    onResult(true)
                } else {
                    onResult(false)
                }
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }

    fun fetchDailyNutrition(uid: String, date: String) {
        viewModelScope.launch {
            try {
                Log.d("API", "Getting nutrition for $uid on $date")
                val response = api.getDailyNutrition(uid, date)
                if (response.isSuccessful) {
                    _dailyNutrition.value = response.body()
                    Log.d("API", "Nutrition summary: ${response.body()}")
                } else {
                    val err = response.errorBody()?.string()
                    _error.value = "Nutrition error: $err"
                    Log.e("API", "Error: $err")
                }
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("API", "Exception: ${e.message}")
            }
        }
    }

    fun logCustomMeal(uid: String, date: String, description: String) {
        viewModelScope.launch {
            try {
                Log.d("API", "Logging custom meal for $uid on $date: $description")
                val response = api.logCustomMeal(CustomMealRequest(uid, date, description))
                if (response.isSuccessful) {
                    _customMeal.value = response.body()
                    Log.d("API", "Logged custom meal: ${response.body()}")
                } else {
                    val err = response.errorBody()?.string()
                    _error.value = "Custom meal error: $err"
                    Log.e("API", "Error: $err")
                }
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("API", "Exception: ${e.message}")
            }
        }
    }

    fun getRecipeDetails(recipeId: String) {
        viewModelScope.launch {
            try {
                _isLoadingRecipe.value = true
                _error.value = null

                val response = apiService.getRecipeDetails(recipeId)
                if (response.isSuccessful) {
                    _recipeDetails.value = response.body()
                } else {
                    _error.value = "Failed to load recipe details: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Error loading recipe: ${e.message}"
            } finally {
                _isLoadingRecipe.value = false
            }
        }
    }

    // Clear recipe details when navigating away
    fun clearRecipeDetails() {
        _recipeDetails.value = null
    }

    fun clearMessages() {
        _registerResponse.value = null
        _error.value = null
    }
}
