package com.mohit.healthy_aahar.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohit.healthy_aahar.model.CustomMeal
import com.mohit.healthy_aahar.model.CustomMealRequest
import com.mohit.healthy_aahar.model.DailyNutrition
import com.mohit.healthy_aahar.model.FeedbackRequest
import com.mohit.healthy_aahar.model.IngredientRecipeRequest
import com.mohit.healthy_aahar.model.LogMealRequest
import com.mohit.healthy_aahar.model.LoggedMeal
import com.mohit.healthy_aahar.model.Meal
import com.mohit.healthy_aahar.model.MealHistoryResponse
import com.mohit.healthy_aahar.model.MealPlanResponse
import com.mohit.healthy_aahar.model.NutritionReport
import com.mohit.healthy_aahar.model.RecipeDetails
import com.mohit.healthy_aahar.model.RecipeSearchResponse
import com.mohit.healthy_aahar.model.RecipeWithFeedback
import com.mohit.healthy_aahar.model.SwapMealRequest
import com.mohit.healthy_aahar.model.User
import com.mohit.healthy_aahar.model.UserDetails
import com.mohit.healthy_aahar.model.WeeklyReport
import com.mohit.healthy_aahar.network.RetrofitClient
import com.mohit.healthy_aahar.network.RetrofitClient.apiService
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val api = RetrofitClient.apiService

    private val _mealPlan = MutableLiveData<MealPlanResponse>()
    val mealPlan: LiveData<MealPlanResponse> get() = _mealPlan

    private val _mealHistoryResponse = MutableLiveData<List<MealHistoryResponse>?>()
    val mealHistoryResponse: LiveData<List<MealHistoryResponse>?> get() = _mealHistoryResponse


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

    private val _weeklyReport = MutableLiveData<WeeklyReport?>()
    val weeklyReport: LiveData<WeeklyReport?> get() = _weeklyReport

    private val _nutritionReport = MutableLiveData<NutritionReport?>()
    val nutritionReport: LiveData<NutritionReport?> get() = _nutritionReport

    private val _mealHistory = MutableLiveData<List<LoggedMeal>?>()
    val mealHistory: LiveData<List<LoggedMeal>?> get() = _mealHistory

    private val _recipeWithFeedback = MutableLiveData<RecipeWithFeedback?>()
    val recipeWithFeedback: LiveData<RecipeWithFeedback?> get() = _recipeWithFeedback

    private val _cuisineRecipes = MutableLiveData<List<RecipeDetails>?>()
    val cuisineRecipes: LiveData<List<RecipeDetails>?> get() = _cuisineRecipes

    private val _searchResults = MutableLiveData<RecipeSearchResponse?>()
    val searchResults: LiveData<RecipeSearchResponse?> get() = _searchResults

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
    fun fetchWeeklyReport(uid: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                Log.d("API", "Fetching weekly report for $uid")

                val response = api.getWeeklyReport(uid)
                if (response.isSuccessful) {
                    _weeklyReport.value = response.body()
                    Log.d("API", "Weekly report received: ${response.body()}")
                } else {
                    val errorText = response.errorBody()?.string()
                    _error.value = "Weekly report error: $errorText"
                    Log.e("API", "Error: $errorText")
                }
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("API", "Exception: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchNutritionReport(uid: String, startDate: String, endDate: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                Log.d("API", "Fetching nutrition report for $uid from $startDate to $endDate")

                val response = api.getNutritionReport(uid, startDate, endDate)
                if (response.isSuccessful) {
                    _nutritionReport.value = response.body()
                    Log.d("API", "Nutrition report received: ${response.body()}")
                } else {
                    val errorText = response.errorBody()?.string()
                    _error.value = "Nutrition report error: $errorText"
                    Log.e("API", "Error: $errorText")
                }
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("API", "Exception: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchMealHistory(uid: String, date: String? = null) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                Log.d("API", "Fetching meal history for $uid" + if (date != null) " on $date" else "")

                val response = api.getMealHistory(uid, date)
                if (response.isSuccessful) {
                    _mealHistoryResponse.value = response.body()
                    Log.d("API", "Meal history received: ${response.body()}")
                } else {
                    val errorText = response.errorBody()?.string()
                    _error.value = "Meal history error: $errorText"
                    Log.e("API", "Error: $errorText")
                }
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("API", "Exception: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchRecipeWithFeedback(recipeId: String) {
        viewModelScope.launch {
            try {
                _isLoadingRecipe.value = true
                _error.value = null
                Log.d("API", "Fetching recipe with feedback for $recipeId")

                val response = api.getRecipeWithFeedback(recipeId)
                if (response.isSuccessful) {
                    _recipeWithFeedback.value = response.body()
                    Log.d("API", "Recipe with feedback received: ${response.body()}")
                } else {
                    _error.value = "Failed to load recipe with feedback: ${response.message()}"
                    Log.e("API", "Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _error.value = "Error loading recipe with feedback: ${e.message}"
                Log.e("API", "Exception: ${e.message}")
            } finally {
                _isLoadingRecipe.value = false
            }
        }
    }

    fun fetchRecipesByCuisine(cuisine: String, limit: Int = 10) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                Log.d("API", "Fetching recipes for cuisine: $cuisine")

                val response = api.getRecipesByCuisine(cuisine, limit)
                if (response.isSuccessful) {
                    _cuisineRecipes.value = response.body()
                    Log.d("API", "Cuisine recipes received: ${response.body()}")
                } else {
                    val errorText = response.errorBody()?.string()
                    _error.value = "Cuisine recipes error: $errorText"
                    Log.e("API", "Error: $errorText")
                }
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("API", "Exception: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchRecipes(query: String, limit: Int = 10) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                Log.d("API", "Searching recipes for: $query")

                val response = api.searchRecipes(query, limit)
                if (response.isSuccessful) {
                    _searchResults.value = response.body()
                    Log.d("API", "Search results received: ${response.body()}")
                } else {
                    val errorText = response.errorBody()?.string()
                    _error.value = "Search error: $errorText"
                    Log.e("API", "Error: $errorText")
                }
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("API", "Exception: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun submitRecipeFeedback(uid: String, recipeId: String, rating: Int, comments: String) {
        viewModelScope.launch {
            try {
                Log.d("API", "Submitting feedback for recipe $recipeId")

                val feedback = FeedbackRequest(recipeId, rating, comments)
                val response = api.submitFeedback(uid, feedback)

                if (response.isSuccessful) {
                    _registerResponse.value = "Feedback submitted successfully"
                    Log.d("API", "Feedback submitted successfully")
                } else {
                    val errorText = response.errorBody()?.string()
                    _error.value = "Feedback error: $errorText"
                    Log.e("API", "Error: $errorText")
                }
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("API", "Exception: ${e.message}")
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
