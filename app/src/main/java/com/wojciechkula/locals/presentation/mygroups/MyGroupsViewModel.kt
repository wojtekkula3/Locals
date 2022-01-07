package com.wojciechkula.locals.presentation.mygroups

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import com.wojciechkula.locals.data.exception.FirestoreException
import com.wojciechkula.locals.domain.interactor.GetUserGroupsInteractor
import com.wojciechkula.locals.domain.model.GroupModel
import com.wojciechkula.locals.domain.model.UserModel
import com.wojciechkula.locals.presentation.mygroups.list.MyGroupsItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MyGroupsViewModel @Inject constructor(
    private val getUserGroupsInteractor: GetUserGroupsInteractor,
) : ViewModel() {

    private var _viewState = MutableStateFlow(MyGroupsViewState())
    val viewState: StateFlow<MyGroupsViewState>
        get() = _viewState

    private var _viewEvent = LiveEvent<MyGroupsViewEvent>()
    val viewEvent: LiveData<MyGroupsViewEvent>
        get() = _viewEvent

    private var _showLoading = MutableLiveData(true)
    val showLoading: LiveData<Boolean>
        get() = _showLoading

    init {
        viewModelScope.launch {
            getUserGroupsInteractor()
                .catch { exception -> handleError(exception) }
                .collect(::getGroups)
        }
    }

    private fun handleError(throwable: Throwable) {
        when (throwable) {
            is FirestoreException -> Timber.e("Firestore exception: $throwable")
            else -> Timber.e("Other exception: $throwable")
        }
    }

    private fun getGroups(groups: List<GroupModel>) {
        viewModelScope.launch {
            val myGroupsItem: ArrayList<MyGroupsItem> = arrayListOf()
            for (group in groups) {
                val authorName =
                    if (viewState.value.user.id == group.latestMessage.authorId) "Ty" else group.latestMessage.authorName
                val myGroupItem = MyGroupsItem(
                    id = group.id,
                    avatar = group.avatar,
                    name = group.name,
                    author = authorName,
                    message = group.latestMessage.message,
                    sentAt = group.latestMessage.sentAt.toDate()
                )
                myGroupsItem.add(myGroupItem)
            }
            _viewState.value = _viewState.value.copy(groups = myGroupsItem)
            _showLoading.postValue(false)
        }
    }

    fun searchGroups(searchText: String?) {
        val groups = _viewState.value.groups
        val searchGroups = arrayListOf<MyGroupsItem>()
        for (group in groups) {
            if (group.name.lowercase().contains(searchText?.lowercase().toString())) {
                searchGroups.add(group)
            }
        }
        _viewEvent.postValue(MyGroupsViewEvent.SearchGroups(searchGroups))

    }

    fun openGroup(selectedGroup: MyGroupsItem) {
        val userId = viewState.value.user.id
        _viewEvent.postValue(MyGroupsViewEvent.OpenGroup(group = selectedGroup, userId = userId))
    }

    fun setUser(user: UserModel) {
        _viewState.value = _viewState.value.copy(user = user)
    }

}