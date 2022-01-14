package com.wojciechkula.locals.presentation.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import com.wojciechkula.locals.domain.interactor.GetFirestoreUserFlowInteractor
import com.wojciechkula.locals.domain.interactor.GetGroupsByDistanceAndHobbiesInteractor
import com.wojciechkula.locals.domain.model.UserModel
import com.wojciechkula.locals.extension.newBuilder
import com.wojciechkula.locals.presentation.explore.list.ExploreItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val getgroupsByDistanceAndHobbiesInteractor: GetGroupsByDistanceAndHobbiesInteractor,
    private val getFirestoreUserFlowInteractor: GetFirestoreUserFlowInteractor,
) : ViewModel() {

    private var _viewState = MutableLiveData<SharedViewState>()
    val viewState: LiveData<SharedViewState>
        get() = _viewState

    private var _viewEvent = LiveEvent<SharedViewEvent>()
    val viewEvent: LiveData<SharedViewEvent>
        get() = _viewEvent

    init {
        _viewState.postValue(SharedViewState(user = UserModel(), exploreGroups = null))
    }

    fun getUser() {
        viewModelScope.launch {
            getFirestoreUserFlowInteractor()
                .catch { exception -> Timber.e("Exception while getting user flow", exception) }
                .collect { user ->
                    _viewState.value = viewState.newBuilder { copy(user = user) }
                    Timber.d("user: $user")
                }
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
}
