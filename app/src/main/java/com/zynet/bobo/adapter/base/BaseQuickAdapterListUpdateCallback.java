package com.zynet.bobo.adapter.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.ListUpdateCallback;

/**
 * @author Bobo
 * @date 2019/9/21
 * describe
 */
public final class BaseQuickAdapterListUpdateCallback implements ListUpdateCallback {

    @NonNull
    private final BaseRecyclerViewAdapter mAdapter;

    public BaseQuickAdapterListUpdateCallback(@NonNull BaseRecyclerViewAdapter adapter) {
        this.mAdapter = adapter;
    }

    public void onInserted(int position, int count) {
        this.mAdapter.notifyItemRangeInserted(position + mAdapter.getHeaderLayoutCount(), count);
    }

    public void onRemoved(int position, int count) {
        this.mAdapter.notifyItemRangeRemoved(position + mAdapter.getHeaderLayoutCount(), count);
    }

    public void onMoved(int fromPosition, int toPosition) {
        this.mAdapter.notifyItemMoved(fromPosition + mAdapter.getHeaderLayoutCount(), toPosition + mAdapter.getHeaderLayoutCount());
    }

    @Override
    public void onChanged(int position, int count, @Nullable Object payload) {
        this.mAdapter.notifyItemRangeChanged(position + mAdapter.getHeaderLayoutCount(), count, payload);
    }
}
