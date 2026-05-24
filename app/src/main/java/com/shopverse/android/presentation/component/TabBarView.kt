package com.shopverse.android.presentation.component

import android.annotation.SuppressLint
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.shopverse.android.core.color.AppColorProvider
import com.shopverse.android.core.extension.TAG
import com.shopverse.android.core.extension.readParcelableCompat
import com.shopverse.android.core.extension.setBackgroundColor
import com.shopverse.android.core.layout.AppLayout
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.parcelize.Parcelize
import java.util.Stack
import java.util.concurrent.TimeUnit


@SuppressLint("ViewConstructor")
class TabBarView private constructor(
    context: Context,
    private val builder: Builder
) : LinearLayout(context) {

    private var backStack: Stack<Tab> = builder.backStack

    /**
     *  DebounceDisposable should be passed to AutoDisposable in Fragment.
     *  it has a custom getter for checking if the value has been called once or not.
     *  in onClick process if the debounceAdded flag be false , DebounceDisposable will be disposed
     **/
    private var debounceAdded = false
    val debounceDisposable: Disposable
        get() {
            debounceAdded = true
            return field
        }
    private val clickedBehaviorSubject = BehaviorSubject.create<String>()
    private val clickedFlowable = clickedBehaviorSubject.toFlowable(BackpressureStrategy.LATEST)
    private val debounceDelay: Long = 30

    init {
        debounceDisposable = createDebounceDisposable()
        builder.list.forEach { itemView ->
            itemView.view.setOnClickListener {
                if (debounceAdded && debounceDisposable.isDisposed.not()) {
                    clickedBehaviorSubject.onNext(itemView.tag)
                } else {
                    if (debounceDisposable.isDisposed.not())
                        debounceDisposable.dispose()
                    select(itemView.tag)
                }
            }
            addView(itemView.view, AppLayout.Linear.fullScreen().apply { weight = 1F })
        }

        val isTagAvailable = builder.list.any { item -> builder.defaultTab == item.tag }
        if (isTagAvailable.not()) {
            throw IllegalArgumentException("DefaultTabTag not found in ItemView tags!")
        }
        if (backStack.empty()) {
            select(builder.defaultTab, true)
        } else {
            select(backStack.lastElement().tag, false)
        }

        setBackgroundColor(AppColorProvider.background)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)
        if (state is SavedState && state.state != null) {
            backStack = Stack<Tab>().apply {
                addAll(state.state.backStack)
            }
            select(backStack.lastElement().tag, false)
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        return SavedState(super.onSaveInstanceState(), SavedState.State(backStack.toList()))
    }

    internal class SavedState : BaseSavedState {
        val state: State?

        constructor(source: Parcel) : super(source) {
            this.state = source.readParcelableCompat(State::class)
        }

        constructor(superState: Parcelable?, state: State) : super(superState) {
            this.state = state
        }

        @Parcelize
        data class State(val backStack: List<Tab>) : Parcelable

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeParcelable(state, PARCELABLE_WRITE_RETURN_VALUE)
        }

        override fun toString(): String {
            return ("NZTabBarView.SavedState{"
                    + Integer.toHexString(System.identityHashCode(this))
                    + "selected state=" + state + "}")
        }

        companion object CREATOR : Creator<SavedState?> {
            override fun createFromParcel(`in`: Parcel): SavedState {
                return SavedState(`in`)
            }

            override fun newArray(size: Int): Array<SavedState?> {
                return Array(size) { null }
            }
        }
    }

    private fun createDebounceDisposable() = clickedFlowable
        .debounce(debounceDelay, TimeUnit.MILLISECONDS)
        .subscribeOn(AndroidSchedulers.mainThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
            Log.d(TAG, it)
            select(it)
        }

    @Suppress("Public")
    val current: Tab?
        get() {
            if (backStack.isNotEmpty()) {
                return backStack.peek()
            }
            return null
        }

    fun getBackStack(): Stack<Tab> = backStack

    // Return false if there is just one Tab (mDefaultTab) on the stack (or stack is empty just for safety).
    // Return true if stack has more than one item then the top item removed and the last item before that will be selected
    // but it shouldn't add to the stack again.
    @Suppress("Unused")
    fun back(): Boolean {
        if (backStack.isEmpty() || backStack.size == 1 || backStack.peek().tag == builder.defaultTab)
            return false
        backStack.pop()
        select(backStack.peek().tag, false)
        return true
    }

    @Suppress("Unused")
    fun select(tag: String) {
        select(tag, true)
    }

    // We have to overload select function due to have control on stack behavior only in the NZTabBar class
    // The user of this class shouldn't have access to control the stack behavior!
    private fun select(tag: String, addToStack: Boolean) {
        if (builder.onItemCanSelectListener?.onItemCanSelect(tag) == false) {
            return
        }
        // Checking reselection of a Tab. addToStack Condition is added due to have ability to back()
        // properly. Cause in back we don't add tabs to the stack!
        if (tag == current?.tag && addToStack) {
            builder.onItemReselectedListener?.onItemReselected(current!!)
            return
        }

        builder.list.forEachIndexed { index, item ->
            // deciding select of a view
            val shouldSelect = (tag == item.tag)
            // changing selected status of view
            item.view.isSelected = shouldSelect
            if (shouldSelect) {
                // Clearing Stack when user select tab with a position equal to DefaultTabPosition
                if (tag == builder.defaultTab) {
                    backStack.clear()
                    // Add default tab to stack again
                    if (addToStack.not()) backStack.push(Tab(item.tag, index))
                }
                // adding tab to stack
                if (addToStack) backStack.push(Tab(item.tag, index))
                // calling ItemSelectedListener callback
                builder.onItemSelectedListener?.onItemSelected(Tab(item.tag, index))
            }
        }
    }

    // If this interface implemented then before selecting each tab TabBar will check the result of
    // onItemCanSelect. If the result was true then selected tab goes selected
    interface OnItemCanSelectListener {
        fun onItemCanSelect(tag: String): Boolean
    }

    interface OnItemSelectedListener {
        fun onItemSelected(tab: Tab)
    }

    interface OnItemReselectedListener {
        fun onItemReselected(tab: Tab)
    }

    data class ItemView(val tag: String, val view: View)

    @Parcelize
    data class Tab(val tag: String, val position: Int) : Parcelable

    class Builder(private val context: Context, defaultTabTag: String) {

        var list: MutableList<ItemView> = mutableListOf()
            private set
        val backStack: Stack<Tab> = Stack()

        var defaultTab: String = defaultTabTag
            private set
        var onItemCanSelectListener: OnItemCanSelectListener? = null
            private set
        var onItemSelectedListener: OnItemSelectedListener? = null
            private set
        var onItemReselectedListener: OnItemReselectedListener? = null
            private set

        fun addItem(tag: String, view: View) = apply {
            list.add(ItemView(tag, view))
        }

        fun addBackStack(backStack: Stack<Tab>) {
            val clonedStack = Stack<Tab>()
            clonedStack.addAll(backStack)

            if (clonedStack.isEmpty())
                return
            this.backStack.clear()
            this.backStack.addAll(clonedStack)
        }

        fun setOnItemCanSelectListener(listener: OnItemCanSelectListener) = apply {
            onItemCanSelectListener = listener
        }

        fun setOnItemSelectedListener(listener: OnItemSelectedListener) = apply {
            onItemSelectedListener = listener
        }

        fun setOnItemReselectedListener(listener: OnItemReselectedListener) = apply {
            onItemReselectedListener = listener
        }

        fun build() = TabBarView(context, this)

    }
}