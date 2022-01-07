package com.wojciechkula.locals.presentation.register.hobbies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import com.wojciechkula.locals.domain.interactor.GetHobbiesGeneralInteractor
import com.wojciechkula.locals.domain.interactor.GetHobbiesInteractor
import com.wojciechkula.locals.domain.interactor.RegisterUserDataInteractor
import com.wojciechkula.locals.domain.interactor.RegisterUserInteractor
import com.wojciechkula.locals.domain.model.HobbyModel
import com.wojciechkula.locals.domain.model.PersonalElementsVisibilityModel
import com.wojciechkula.locals.domain.model.UserModel
import com.wojciechkula.locals.extension.newBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RegisterHobbiesViewModel @Inject constructor(
    private val getHobbiesInteractor: GetHobbiesInteractor,
    private val getHobbiesGeneralInteractor: GetHobbiesGeneralInteractor,
    private val registerUserInteractor: RegisterUserInteractor,
    private val registerUserDataInteractor: RegisterUserDataInteractor
) : ViewModel() {

    private val _viewState = MutableLiveData<RegisterHobbiesViewState>()
    val viewState: LiveData<RegisterHobbiesViewState>
        get() = _viewState

    private val _viewEvent = LiveEvent<RegisterHobbiesViewEvent>()
    val viewEvent: LiveData<RegisterHobbiesViewEvent>
        get() = _viewEvent

    private val _showLoading = MutableLiveData(true)
    val showLoading: LiveData<Boolean>
        get() = _showLoading

    init {
        viewModelScope.launch {
            val list = getHobbiesGeneralInteractor() as ArrayList<HobbyModel>
            _viewState.postValue(RegisterHobbiesViewState(generalHobbiesList = list, customHobbiesList = list))
            _viewEvent.postValue(
                RegisterHobbiesViewEvent.GetGeneralHobbies(
                    generalHobbiesList = list,
                    selectedHobbiesList = arrayListOf()
                )
            )
            _showLoading.postValue(false)
        }
    }

    fun addHobby(hobby: HobbyModel) {
        viewModelScope.launch {
            var selectedHobbies = _viewState.value?.selectedHobbiesList
            selectedHobbies?.add(hobby)
            selectedHobbies?.sortBy { it.name }
            checkIfValidSizeAndUpdateSelectedHobbiesList(selectedHobbies)
        }
    }

    fun removeHobby(hobby: HobbyModel) {
        viewModelScope.launch {
            val selectedHobbies = _viewState.value?.selectedHobbiesList
            selectedHobbies?.remove(hobby)
            checkIfValidSizeAndUpdateSelectedHobbiesList(selectedHobbies)
            _viewEvent.value =
                RegisterHobbiesViewEvent.GetCustomHobbies(_viewState.value?.customHobbiesList, selectedHobbies)
        }
    }

    private fun checkIfValidSizeAndUpdateSelectedHobbiesList(selectedHobbies: ArrayList<HobbyModel>?) {
        if (selectedHobbies != null) {
            if (selectedHobbies.size in 3..20) {
                _viewState.value = viewState.newBuilder {
                    copy(
                        selectedHobbiesList = selectedHobbies,
                        registerActionEnabled = true
                    )
                }
            } else {
                Timber.d("$selectedHobbies")
                _viewState.value = viewState.newBuilder {
                    copy(
                        selectedHobbiesList = selectedHobbies,
                        registerActionEnabled = false
                    )
                }
            }
        }
    }

    fun onSearchChange(input: String) {
        viewModelScope.launch {
            val hobbiesList = getHobbiesInteractor(input) as ArrayList<HobbyModel>
            _viewState.value = viewState.newBuilder { copy(customHobbiesList = hobbiesList) }
            _viewEvent.value =
                RegisterHobbiesViewEvent.GetCustomHobbies(hobbiesList, _viewState.value?.selectedHobbiesList)
        }
    }

    fun onSearchChangeEmptyString() {
        _viewEvent.value = RegisterHobbiesViewEvent.GetGeneralHobbies(
            _viewState.value?.generalHobbiesList,
            _viewState.value?.selectedHobbiesList
        )
    }

    fun onNextClick(args: RegisterHobbiesFragmentArgs) {
        _showLoading.postValue(true)
        val selectedHobbiesSize = _viewState.value?.selectedHobbiesList?.size
        if (selectedHobbiesSize != null) {
            if (selectedHobbiesSize in 3..20) {
                viewModelScope.launch {
                    registerUserInteractor(args.userData.email, args.userData.password)
                        .addOnSuccessListener {
                            val user = UserModel(
                                name = args.userData.name,
                                surname = args.userData.surname,
                                email = args.userData.email,
                                avatar = null,
                                phoneNumber = args.userData.phoneNumber,
                                hobbies = _viewState.value?.selectedHobbiesList,
                                about = null,
                                elementsVisibility = PersonalElementsVisibilityModel(false, false, true)
                            )
                            viewModelScope.launch {
                                registerUserDataInteractor(user).addOnSuccessListener {
                                    Timber.d("Register completed!")
                                    _viewEvent.postValue(RegisterHobbiesViewEvent.GetGroupsForExplore)
                                }
                                    .addOnFailureListener { exception ->
                                        _showLoading.postValue(false)
                                        Timber.e(
                                            exception,
                                            "Register exception - Error occurred when adding new user data to firestore"
                                        )
                                    }
                            }
                        }
                        .addOnFailureListener { exception ->
                            Timber.e(exception, "Register exception - Error occurred when adding new user to firebase")
                        }
                }
            }
        }
    }
}