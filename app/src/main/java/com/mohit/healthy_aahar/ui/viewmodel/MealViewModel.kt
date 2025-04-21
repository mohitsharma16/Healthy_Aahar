package com.mohit.healthy_aahar.ui.viewmodel

//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.mohit.healthy_aahar.model.*
//import com.mohit.healthy_aahar.repository.RecipeRepository
//import kotlinx.coroutines.launch
//
//class MealViewModel : ViewModel() {
//    private val repository = RecipeRepository()
//
//    private val _registrationResult = MutableLiveData<Result<Map<String, Any>>>()
//    val registrationResult: LiveData<Result<Map<String, Any>>> = _registrationResult
//
//    private val _mealPlanResult = MutableLiveData<Result<MealPlanResponse>>()
//    val mealPlanResult: LiveData<Result<MealPlanResponse>> = _mealPlanResult
//
//    private val _swapMealResult = MutableLiveData<Result<SwapMealResponse>>()
//    val swapMealResult: LiveData<Result<SwapMealResponse>> = _swapMealResult
//
//    fun registerUser(userProfile: UserProfile) {
//        viewModelScope.launch {
//            _registrationResult.value = repository.registerUser(userProfile)
//        }
//    }
//
//    fun generateMealPlan(userName: String) {
//        viewModelScope.launch {
//            _mealPlanResult.value = repository.generateMealPlan(userName)
//        }
//    }
//
//    fun swapMeal(userName: String, mealIndex: Int) {
//        viewModelScope.launch {
//            _swapMealResult.value = repository.swapMeal(userName, mealIndex)
//        }
//    }
//}