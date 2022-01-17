package com.wojciechkula.locals.presentation.creategroup

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.hadilq.liveevent.LiveEvent
import com.wojciechkula.locals.common.validator.NotBlankValidator
import com.wojciechkula.locals.domain.interactor.*
import com.wojciechkula.locals.domain.model.*
import com.wojciechkula.locals.extension.newBuilder
import com.wojciechkula.locals.presentation.creategroup.list.HobbyItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CreateGroupViewModel @Inject constructor(
    private val getHobbiesInteractor: GetHobbiesInteractor,
    private val notBlankValidator: NotBlankValidator,
    private val createNewHobbiesInteractor: CreateNewHobbiesInteractor,
    private val createGroupInteractor: CreateGroupInteractor,
    private val getGroupInteractor: GetGroupInteractor,
    private val joinToGroupInteractor: JoinToGroupInteractor
) : ViewModel() {

    private val _showLoading = MutableLiveData(false)
    val showLoading: LiveData<Boolean>
        get() = _showLoading

    private var _viewState = MutableLiveData<CreateGroupViewState>()
    val viewState: LiveData<CreateGroupViewState>
        get() = _viewState

    private var _viewEvent = LiveEvent<CreateGroupViewEvent>()
    val viewEvent: LiveData<CreateGroupViewEvent>
        get() = _viewEvent

    init {
//        _viewState.postValue(CreateGroupViewState())
        _viewState.value = CreateGroupViewState()
    }

    fun onSearchChange(textInput: String) {
        viewModelScope.launch {
            var searchResultsHobbies = arrayListOf<HobbyItem>()
            if (textInput.isNotBlank()) {
                searchResultsHobbies =
                    getHobbiesInteractor(textInput).map { hobby -> HobbyItem(hobby.name) } as ArrayList<HobbyItem>
            }

            // Remove hobby form search list if it was already selected earlier
            val selectedHobbies = _viewState.value?.selectedHobbies
            val hobbiesToRemove = arrayListOf<HobbyItem>()
            if (selectedHobbies != null) {
                for (hobby in searchResultsHobbies) {
                    for (selectedHobby in selectedHobbies) {
                        if (hobby.name == selectedHobby.name) {
                            hobbiesToRemove.add(hobby)
                        }
                    }
                }
            }
            if (hobbiesToRemove.isNotEmpty()) {
                searchResultsHobbies.removeAll(hobbiesToRemove)
            }
            _viewState.value = viewState.newBuilder { copy(searchHobbies = searchResultsHobbies) }
        }
    }

    fun onNameChange(name: String, description: String, hobbiesSize: Int, location: LocationModel?) {
        val nameValid = notBlankValidator.validate(name)
        val descriptionValid = notBlankValidator.validate(description)
        val hobbiesValid = hobbiesSize in 1..10
        val locationValid = location != null
        Timber.d("Validatory $nameValid $descriptionValid $hobbiesValid $locationValid NAME ")

        _viewState.value = viewState.newBuilder {
            copy(
                nameValid = nameValid,
                nextActionEnabled = nameValid && descriptionValid && hobbiesValid && locationValid
            )
        }
    }

    fun onDescriptionChange(name: String, description: String, hobbiesSize: Int, location: LocationModel?) {
        val nameValid = notBlankValidator.validate(name)
        val descriptionValid = notBlankValidator.validate(description)
        val hobbiesValid = hobbiesSize in 1..10
        val locationValid = location != null
        Timber.d("Validatory $nameValid $descriptionValid $hobbiesValid $locationValid DESC")

        _viewState.value = viewState.newBuilder {
            copy(
                descriptionValid = descriptionValid,
                nextActionEnabled = nameValid && descriptionValid && hobbiesValid && locationValid
            )
        }
    }

    fun onHobbiesChange(name: String, description: String, hobbiesSize: Int, location: LocationModel?) {
        val nameValid = notBlankValidator.validate(name)
        val descriptionValid = notBlankValidator.validate(description)
        val hobbiesValid = hobbiesSize in 1..10
        val locationValid = location != null
        Timber.d("Validatory $nameValid $descriptionValid $hobbiesValid $locationValid HOBBIES ")

        _viewState.value = viewState.newBuilder {
            copy(
                hobbiesValid = hobbiesValid,
                nextActionEnabled = nameValid && descriptionValid && hobbiesValid && locationValid
            )
        }
    }

    fun onLocationChange(name: String, description: String, hobbiesSize: Int, location: LocationModel?) {
        val nameValid = notBlankValidator.validate(name)
        val descriptionValid = notBlankValidator.validate(description)
        val hobbiesValid = hobbiesSize in 1..10
        val locationValid = location != null
        Timber.d("Validatory $nameValid $descriptionValid $hobbiesValid $locationValid LOCATION ")
        _viewState.value = viewState.newBuilder {
            copy(
                locationValid = locationValid,
                nextActionEnabled = nameValid && descriptionValid && hobbiesValid && locationValid
            )
        }
    }

    fun selectHobby(hobbyName: String) {

        val hobby = HobbyItem(hobbyName)
        val selectedHobbies = _viewState.value?.selectedHobbies
        val searchHobbies = _viewState.value?.searchHobbies
        val newHobbies = _viewState.value?.newHobbies

        if (selectedHobbies?.contains(hobby) == false) {
            selectedHobbies.add(hobby)
            if (searchHobbies?.contains(hobby) == false) {
                newHobbies?.add(hobby)
                _viewState.value = viewState.newBuilder { copy(newHobbies = newHobbies) }
            }
            _viewState.value = viewState.newBuilder { copy(selectedHobbies = selectedHobbies) }
        }
        _viewEvent.postValue(CreateGroupViewEvent.ShowChipsWithSelectedHobbies(selectedHobbies))

    }

    fun removeHobby(hobbyName: String) {
        val hobby = HobbyItem(hobbyName)
        val selectedHobbies = _viewState.value?.selectedHobbies
        selectedHobbies?.remove(hobby)

        val newHobbies = _viewState.value?.newHobbies
        if (newHobbies?.contains(hobby) == true) {
            newHobbies.remove(hobby)
            _viewState.value = viewState.newBuilder { copy(newHobbies = newHobbies) }
        }
        _viewState.value = viewState.newBuilder { copy(selectedHobbies = selectedHobbies) }
        _viewEvent.postValue(CreateGroupViewEvent.ShowChipsWithSelectedHobbies(selectedHobbies))
    }

    fun showChipsWithHobbies() {
        val selectedHobbies = _viewState.value?.selectedHobbies
        _viewEvent.postValue(CreateGroupViewEvent.ShowChipsWithSelectedHobbies(selectedHobbies))
    }

    fun setLocation(selectedLocation: LocationModel?) {
        _viewEvent.postValue(CreateGroupViewEvent.SetLocation(selectedLocation))

    }

    fun onNextClick(
        avatarImage: Bitmap?,
        name: String,
        description: String,
        selectedLocation: LocationModel?,
        groupCreatedMessage: String
    ) {
        val newHobbies = _viewState.value?.newHobbies
        if (!newHobbies.isNullOrEmpty()) {
            _viewEvent.postValue(CreateGroupViewEvent.OpenDialog(newHobbies))
        } else {
            createGroup(avatarImage, name, description, selectedLocation, groupCreatedMessage)
        }
    }

    fun createGroup(
        image: Bitmap?,
        name: String,
        description: String,
        selectedLocation: LocationModel?,
        groupCreatedMessage: String
    ) {
        val newHobbies = viewState.value?.newHobbies?.map { hobbyItem -> HobbyModel(hobbyItem.name) }
        val selectedHobbies =
            viewState.value?.selectedHobbies?.map { hobbyItem -> hobbyItem.name } as ArrayList<String>
        var group = GroupModel()
        if (selectedLocation != null) {
            group =
                GroupModel(
                    name = name,
                    description = description,
                    location = selectedLocation,
                    hobbies = selectedHobbies,
                    latestMessage = LatestMessageModel(
                        authorId = _viewState.value!!.user.id,
                        authorName = _viewState.value!!.user.name,
                        message = groupCreatedMessage,
                        sentAt = Timestamp.now()
                    ),
                    members = arrayListOf(_viewState.value!!.user.id)
                )
        }
        viewModelScope.launch {
            _showLoading.postValue(true)

            if (!newHobbies.isNullOrEmpty()) {
                val isSuccess = createNewHobbiesInteractor(newHobbies)
            }
            val documentReference = createGroupInteractor(image, group)
            joinToGroupInteractor(documentReference.id)
            _showLoading.postValue(false)
            _viewEvent.postValue(CreateGroupViewEvent.OpenMyGroups)
        }
    }

    fun setUser(user: UserModel) {
        _viewState.value = viewState.newBuilder { copy(user = user) }
    }

}