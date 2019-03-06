package com.appnroll.mvi.ui.base.mvi

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders

abstract class MviBaseFragment<
        ActionType: MviAction,
        ResultType: MviResult,
        ViewStateType: MviViewState<ResultType>,
        ViewModelType: MviViewModel<ActionType, ResultType, ViewStateType>>(
    private val viewModelClass: Class<ViewModelType>
): Fragment() {

    protected val mviController by lazy {
        MviController(
            ViewModelProviders.of(this),
            javaClass.name,
            lifecycle,
            ::render,
            ::initialAction
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mviController.initViewModel(viewModelClass)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mviController.initViewStatesObservable(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        mviController.saveLastViewState(outState)
        super.onSaveInstanceState(outState)
    }

    /**
     * Sends this action right after subscription to viewStates observer
     */
    open fun initialAction(lastViewState: ViewStateType?): ActionType? = null

    /**
     * Update UI based on ViewState
     */
    abstract fun render(viewState: ViewStateType)
}