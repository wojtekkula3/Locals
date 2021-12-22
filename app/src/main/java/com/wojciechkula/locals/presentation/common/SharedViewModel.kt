package com.wojciechkula.locals.presentation.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import com.wojciechkula.locals.domain.interactor.GetFirestoreUserInteractor
import com.wojciechkula.locals.domain.interactor.GetGroupsByDistanceAndHobbiesInteractor
import com.wojciechkula.locals.extension.newBuilder
import com.wojciechkula.locals.presentation.explore.list.ExploreItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val getgroupsByDistanceAndHobbiesInteractor: GetGroupsByDistanceAndHobbiesInteractor,
    private val getFirestoreUserInteractor: GetFirestoreUserInteractor,
) : ViewModel() {

    private var _viewState = MutableLiveData<SharedViewState>()
    val viewState: LiveData<SharedViewState>
        get() = _viewState

    private var _viewEvent = LiveEvent<SharedViewEvent>()
    val viewEvent: LiveData<SharedViewEvent>
        get() = _viewEvent

    private fun getUser() {
        viewModelScope.launch {
            val user = getFirestoreUserInteractor()
            _viewState.postValue(SharedViewState(user = user, exploreGroups = null))
        }
    }

    fun getGroupsForExplore() {
        viewModelScope.launch {
            getUser()
            val groups: ArrayList<ExploreItem> =
                getgroupsByDistanceAndHobbiesInteractor(10, arrayListOf()).map { groupModel ->
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

            _viewState.value = viewState.newBuilder { copy(exploreGroups = groups) }
            _viewEvent.postValue(SharedViewEvent.OpenDashboard)
        }
    }

    fun setGroupsForExplore() {
        _viewEvent.postValue(SharedViewEvent.SetGroupsForExplore(viewState.value?.exploreGroups))
    }

    fun setUserForProfile() {
        _viewEvent.postValue(SharedViewEvent.SetUserInformation(viewState.value?.user!!))
    }
}
