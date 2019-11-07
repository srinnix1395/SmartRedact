package com.example.smartredact.common.widget.expandablerecyclerview

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference

/**
 * The common adapter helps you implement expandable recycler easier.
 * SH: The View Holder you want to use as the Header. Your header view holder must implement class CommonHeaderViewHolder
 * CH: The View Holder you want to use as the Child. Your child view holder must implement class CommonChildViewHolder
 */
abstract class CommonExpandableAdapter<SH : CommonHeaderViewHolder, CH : CommonChildViewHolder>() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val HEADER = 1001
        const val CHILD = 1002
        const val INVALID_CHILD_POSITION = -1
    }

    private var mHeaderItems = ArrayList<HeaderItems>()

    //Key is index of header in header list, Value is a index of header in all list
    private var mHeadersIndexes = mutableMapOf<Int, Int>()

    //Key is index of header in all list, Value is a index of header in header list
    private var mSwitchHeadersIndexes = mutableMapOf<Int, Int>()

    private var mHeadersViewType = HashSet<Int>()

    private var mExpandEnable: Boolean = true

    private var mIsOpenAtFirst: Boolean = true

    private var recyclerViewWeakReference: WeakReference<RecyclerView>? = null

    constructor(isOpenAtFirst: Boolean, expandEnable: Boolean) : this() {
        this.mIsOpenAtFirst = isOpenAtFirst
        this.mExpandEnable = expandEnable
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerViewWeakReference = WeakReference(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            in mHeadersViewType -> {
                getHeaderViewHolder(parent, viewType)
                    .setOnClickHeaderListener(this::onClickSection)
            }
            else -> { // CHILD
                getChildViewHolder(parent, viewType)
                    .setOnClickChildListener(this::onClickChild)
            }
        }
    }

    override fun getItemCount(): Int {
        val numberOfHeader = getHeaderCollapsibleCount()
        var currentItemCount = numberOfHeader
        (0 until numberOfHeader).forEach { header ->
            currentItemCount += getChildOfSectionCollapsibleCount(header)
        }
        return currentItemCount
    }

    override fun getItemViewType(position: Int): Int {
        val (headerIndex, childIndex) = getHeaderIndexAndChildIndex(position)

        return when (position) {
            in getAllHeaderPosition(switchKeyValue = true) -> {
                val headerType = getHeaderType(headerIndex)
                mHeadersViewType.add(headerType)
                headerType
            }
            else -> getChildTypeWithCheck(headerIndex, childIndex)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val (headerIndex, childIndex) = getHeaderIndexAndChildIndex(position)
        when (holder) {
            is CommonHeaderViewHolder -> {
                holder.headerIndex = headerIndex
                holder.isExpanding = mHeaderItems[headerIndex].isExpanded
                holder.onHeaderExpandedChanged(holder.isExpanding)
                onBindHeaderViewHolder(holder as SH, headerIndex, getHeaderType(headerIndex))
            }
            is CommonChildViewHolder -> {
                holder.headerIndex = headerIndex
                holder.childIndex = childIndex
                onBindChildViewHolder(holder as CH, headerIndex, childIndex, getChildTypeWithCheck(headerIndex, childIndex))
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
            return
        }

        val (headerIndex, childIndex) = getHeaderIndexAndChildIndex(position)
        when (holder) {
            is CommonHeaderViewHolder -> {
                onBindSectionViewHolderPayloads(holder as SH, headerIndex, payloads)
            }
            is CommonChildViewHolder -> {
                onBindChildViewHolderPayloads(holder as CH, headerIndex, childIndex, getChildTypeWithCheck(headerIndex, childIndex), payloads)
            }
        }
    }

    open fun onBindSectionViewHolderPayloads(headerHolder: SH, headerIndex: Int, payloads: MutableList<Any>) {
        // Lets sub-adapter implement
    }

    open fun onBindChildViewHolderPayloads(childHolder: CH, headerIndex: Int, childIndex: Int, childType: Int, payloads: MutableList<Any>) {
        // Lets sub-adapter implement
    }

    protected fun isExpanding(headerIndex: Int): Boolean {
        return mHeaderItems[headerIndex].isExpanded
    }

    private fun scrollToBottom() {
        recyclerViewWeakReference?.get()?.scrollToPosition(itemCount - 1)
    }

    protected fun getHeaderIndexAndChildIndex(position: Int): Pair<Int, Int> {
        val allHeaderPosition = getAllHeaderPosition(switchKeyValue = true)
        var realHeaderPosition = allHeaderPosition[position]
        return if (realHeaderPosition != null) {
            Pair(realHeaderPosition, -1)
        } else {
            val headerIndex = (position downTo 0).first { index ->
                allHeaderPosition[index] != null
            }
            realHeaderPosition = allHeaderPosition[headerIndex]!! // Use !! because Ive checked in above
            val childPosition = position - headerIndex - 1
            Pair(realHeaderPosition, childPosition)
        }
    }

    private fun getAllHeaderPosition(switchKeyValue: Boolean = false): Map<Int, Int> {
        if (mHeadersIndexes.isNotEmpty()) {
            return if (switchKeyValue) mSwitchHeadersIndexes else mHeadersIndexes
        }

        val numberOfHeader = getHeaderCollapsibleCount()
        if (numberOfHeader == 0) return mutableMapOf()

        mHeadersIndexes[0] = 0

        (1 until numberOfHeader).forEach { header ->
            mHeadersIndexes[header] = getChildCountBeforeSection(header) + header
        }

        mSwitchHeadersIndexes = mHeadersIndexes.switchKeyValue()

        return if (switchKeyValue) mSwitchHeadersIndexes else mHeadersIndexes
    }

    private fun getChildCountBeforeSection(headerIndex: Int): Int {
        var childCount = 0
        (0 until headerIndex).forEach { s ->
            childCount += getChildOfSectionCollapsibleCount(s)
        }
        return childCount
    }

    private fun onClickSection(holder: CommonHeaderViewHolder, position: Int) {
        if (position < 0) return
        val (headerIndex, _) = getHeaderIndexAndChildIndex(position)

        if (mExpandEnable && getChildCount(headerIndex) > 0) {

            val newState = !mHeaderItems[headerIndex].isExpanded
            mHeaderItems[headerIndex].isExpanded = newState
            holder.isExpanding = newState
            holder.onHeaderExpandedChanged(newState)
            expandOrCollapseSection(newState, headerIndex)

            if (newState && headerIndex == getHeaderCount() - 1) {
                scrollToBottom()
            }
        }
        holder.onHeaderClicked()
    }

    private fun onClickChild(holder: CommonChildViewHolder, position: Int) {
        if (position < 0) return
        val (headerIndex, childIndex) = getHeaderIndexAndChildIndex(position)
        holder.onChildClicked(headerIndex, childIndex)
    }

    private fun expandOrCollapseSection(expand: Boolean, headerIndex: Int) {
        mHeadersIndexes.clear()
        val headerPosition = getAllHeaderPosition()[headerIndex] ?: return
        if (expand) {
            notifyItemRangeInserted(headerPosition + 1, getChildCount(headerIndex))
        } else {
            notifyItemRangeRemoved(headerPosition + 1, getChildCount(headerIndex))
        }
    }

    private fun getChildOfSectionCollapsibleCount(header: Int): Int {
        return if (mHeaderItems[header].isExpanded) {
            getChildCount(header)
        } else {
            0
        }
    }

    private fun modifiedHeader(headerIndex: Int, isRemoving: Boolean) {
        if (isRemoving) {
            if (mHeaderItems.size > headerIndex) {
                mHeaderItems.removeAt(headerIndex)
            }
        } else {
            // Case adding : Implement later
        }
    }

    private fun getHeaderCollapsibleCount(): Int {
        val count = getHeaderCount()
        if (mHeaderItems.isEmpty() || mHeaderItems.size != count) {
            mHeaderItems.clear()
            for (i in 0 until count) {
                mHeaderItems.add(HeaderItems(i, mIsOpenAtFirst))
            }
        }
        return count
    }

    fun notifyItemInserted(headerIndex: Int, childIndex: Int) {
        notifyItemRangeInserted(headerIndex, childIndex, 1)
    }

    fun notifyChildItemChanged(headerIndex: Int, childIndex: Int, payloads: Any?) {
        notifyChildItemRangeChanged(headerIndex, childIndex, 1, payloads)
    }

    fun notifyHeaderItemChanged(headerIndex: Int) {
        val headerPositionChanged = getAllHeaderPosition()[headerIndex] ?: return
        notifyItemChanged(headerPositionChanged)
    }

    fun notifyHeaderItemChanged(headerIndex: Int, payloads: Any?) {
        val headerPositionChanged = getAllHeaderPosition()[headerIndex] ?: return
        notifyItemChanged(headerPositionChanged, payloads)
    }

    fun notifyItemRemoved(headerIndex: Int, childIndex: Int) {
        notifyItemRangeRemoved(headerIndex, childIndex, 1)
    }

    private fun notifyItemRangeInserted(headerIndex: Int, childStartIndex: Int, itemCount: Int) {
        if (mHeaderItems[headerIndex].isExpanded) {
            val headerPosition = getAllHeaderPosition()[headerIndex] ?: return
            notifyItemRangeInserted(headerPosition + childStartIndex + 1, itemCount)
        }
    }

    fun notifyChildItemRangeChanged(headerIndex: Int, childStartIndex: Int, itemCount: Int, payloads: Any?) {
        if (mHeaderItems[headerIndex].isExpanded) {
            val headerPosition = getAllHeaderPosition()[headerIndex] ?: return
            if (payloads != null) {
                notifyItemRangeChanged(headerPosition + childStartIndex + 1, itemCount, payloads)
            } else {
                notifyItemRangeChanged(headerPosition + childStartIndex + 1, itemCount)
            }
        }
    }

    private fun notifyItemRangeRemoved(headerIndex: Int, childStartIndex: Int, itemCount: Int) {
        val headerPosition = getAllHeaderPosition()[headerIndex] ?: return
        if (childStartIndex == INVALID_CHILD_POSITION) {
            notifyItemRangeRemoved(headerPosition, itemCount)
            modifiedHeader(headerIndex, true)

        } else {
            if (mHeaderItems[headerIndex].isExpanded) {
                notifyItemRangeRemoved(headerPosition + childStartIndex + 1, itemCount)
            }
        }
    }

    private fun getChildTypeWithCheck(headerIndex: Int, childIndex: Int): Int {
        val childType = getChildType(headerIndex, childIndex)
        if (childType == HEADER) throw IllegalStateException("Please don't use $childType as a type value of child view holder")

        return childType
    }

    fun setEnableExpand(enable: Boolean) {
        mExpandEnable = enable
    }

    /**
     * Return your child type view holder
     */
    open fun getChildType(headerIndex: Int, childIndex: Int): Int = CHILD

    /**
     * Return your header type view holder
     */
    open fun getHeaderType(headerIndex: Int): Int = HEADER

    /**
     * Create your header view holder and return it
     */
    abstract fun getHeaderViewHolder(parent: ViewGroup, headerType: Int): SH

    /**
     * Create your child view holder and return it
     */
    abstract fun getChildViewHolder(parent: ViewGroup, childType: Int): CH

    /**
     * Define how many header you want
     */
    abstract fun getHeaderCount(): Int

    /**
     * Define how many child in the header
     * @param header: the header index
     */
    abstract fun getChildCount(header: Int): Int

    /**
     * Bind data on your header view holder
     * @param headerIndex: The header index must be bind data
     */
    abstract fun onBindHeaderViewHolder(headerHolder: SH, headerIndex: Int, headerType: Int)

    /**
     * Bind data on your child view holder
     * @param headerIndex: The header index of child view holder
     * @param childIndex: The child index of header index must be bind data
     */
    abstract fun onBindChildViewHolder(childHolder: CH, headerIndex: Int, childIndex: Int, childType: Int)

    protected fun clearData() {
        this.mHeaderItems.clear()
        this.mSwitchHeadersIndexes.clear()
        this.mHeadersIndexes.clear()
    }

    data class HeaderItems(val headerIndex: Int, var isExpanded: Boolean)
}

fun <K, V> Map<K, V>.switchKeyValue(): MutableMap<V, K> {
    val switchMap = mutableMapOf<V, K>()
    forEach { (key, value) ->
        switchMap[value] = key
    }
    return switchMap
}
