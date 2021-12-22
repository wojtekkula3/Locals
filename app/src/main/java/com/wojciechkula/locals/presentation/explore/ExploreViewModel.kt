package com.wojciechkula.locals.presentation.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import com.wojciechkula.locals.domain.interactor.GetGroupsByDistanceAndHobbiesInteractor
import com.wojciechkula.locals.domain.interactor.GetHobbiesInteractor
import com.wojciechkula.locals.domain.interactor.JoinToGroupInteractor
import com.wojciechkula.locals.domain.model.HobbyModel
import com.wojciechkula.locals.extension.newBuilder
import com.wojciechkula.locals.presentation.explore.list.ExploreItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val getGroupsByDistanceAndHobbies: GetGroupsByDistanceAndHobbiesInteractor,
    private val getHobbiesInteractor: GetHobbiesInteractor,
    private val joinToGroupInteractor: JoinToGroupInteractor,
) : ViewModel() {

    private var _viewState = MutableLiveData<ExploreViewState>()
    val viewState: LiveData<ExploreViewState>
        get() = _viewState

    private var _viewEvent = LiveEvent<ExploreViewEvent>()
    val viewEvent: LiveData<ExploreViewEvent>
        get() = _viewEvent

    init {
        _viewState.value = ExploreViewState(arrayListOf(), arrayListOf(), arrayListOf(), 10)
    }

    fun setInitialGroups(initialGroups: ArrayList<ExploreItem>?) {
        _viewState.value = ExploreViewState(initialGroups, arrayListOf(), arrayListOf(), 10, true)
    }

    fun getGroupsByDistanceAndHobbies() {
        viewModelScope.launch {
            val groups: ArrayList<ExploreItem> =
                getGroupsByDistanceAndHobbies(
                    distance = viewState.value!!.distance,
                    selectedHobbies = _viewState.value?.selectedHobbiesList
                ).map { groupModel ->
                    ExploreItem(
                        avatar = groupModel.avatar,
                        name = groupModel.name,
                        id = groupModel.id,
                        hobbies = groupModel.hobbies,
                        distance = groupModel.distance,
                        size = groupModel.members.size,
                        members = groupModel.members
                    )
                } as ArrayList<ExploreItem>
            _viewState.value = viewState.newBuilder { copy(groupsList = groups) }
        }
    }

    fun searchHobbies(searchText: String) {
        viewModelScope.launch {
            val hobbies = getHobbiesInteractor(searchText) as ArrayList<HobbyModel>
            _viewState.value = viewState.newBuilder { copy(searchHobbiesList = hobbies) }
            _viewEvent.postValue(ExploreViewEvent.ShowSearchHobbies(hobbies, _viewState.value?.selectedHobbiesList))
        }
    }

    fun addHobby(hobby: HobbyModel) {
        viewModelScope.launch {
            var selectedHobbies = _viewState.value?.selectedHobbiesList
            selectedHobbies?.add(hobby)
            if (!selectedHobbies.isNullOrEmpty()) {
                _viewState.value = viewState.newBuilder {
                    copy(
                        selectedHobbiesList = selectedHobbies,
                    )
                }
            }
            _viewEvent.postValue(ExploreViewEvent.ShowGroups)
        }
    }

    fun removeHobby(hobby: HobbyModel) {
        viewModelScope.launch {
            val selectedHobbies = _viewState.value?.selectedHobbiesList
            selectedHobbies?.remove(hobby)
            if (!selectedHobbies.isNullOrEmpty()) {
                _viewState.value = viewState.newBuilder {
                    copy(
                        selectedHobbiesList = selectedHobbies,
                    )
                }
            }
            _viewEvent.postValue(
                ExploreViewEvent.ShowSearchHobbies(
                    _viewState.value?.searchHobbiesList,
                    selectedHobbies
                )
            )
            _viewEvent.postValue(ExploreViewEvent.ShowGroups)
        }
    }

    fun joinTheGroup(groupId: String) {
        viewModelScope.launch {
            joinToGroupInteractor(groupId)
            _viewEvent.postValue(ExploreViewEvent.HideJoinedGroup)
        }
    }

    fun onDistanceChange(value: Int) {
        _viewState.value = viewState.newBuilder { copy(distance = value) }
        _viewEvent.postValue(ExploreViewEvent.ChangeDistance)

    }
}