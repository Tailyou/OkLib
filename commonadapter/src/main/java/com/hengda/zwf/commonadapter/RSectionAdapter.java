package com.hengda.zwf.commonadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public abstract class RSectionAdapter<T> extends RMultiItemCommonAdapter<T> {

    private static final int TYPE_SECTION = 0;
    private RSectionSupport mRSectionSupport;
    private LinkedHashMap<String, Integer> mSections;
    final RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            findSections();
        }
    };
    private RMultiItemTypeSupport<T> headerItemTypeSupport;

    public RSectionAdapter(Context context, int layoutId, List<T> datas, RSectionSupport sectionSupport) {
        this(context, layoutId, null, datas, sectionSupport);
    }

    public RSectionAdapter(Context context, int layoutId, RMultiItemTypeSupport multiItemTypeSupport,
                           List<T> datas, RSectionSupport sectionSupport) {
        super(context, datas, null);
        mLayoutId = layoutId;
        initMulitiItemTypeSupport(layoutId, multiItemTypeSupport);
        mRMultiItemTypeSupport = headerItemTypeSupport;
        mRSectionSupport = sectionSupport;
        mSections = new LinkedHashMap<>();
        findSections();
        registerAdapterDataObserver(observer);
    }

    private void initMulitiItemTypeSupport(int layoutId, final RMultiItemTypeSupport multiItemTypeSupport) {
        if (layoutId != -1) {
            headerItemTypeSupport = new RMultiItemTypeSupport<T>() {
                @Override
                public int getLayoutId(int itemType) {
                    return itemType == TYPE_SECTION ? mRSectionSupport.sectionLayoutId() : mLayoutId;
                }

                @Override
                public int getItemViewType(int position, T o) {
                    return mSections.values().contains(position) ? TYPE_SECTION : 1;
                }
            };
        } else if (multiItemTypeSupport != null) {
            headerItemTypeSupport = new RMultiItemTypeSupport<T>() {
                @Override
                public int getLayoutId(int itemType) {
                    return itemType == TYPE_SECTION ? mRSectionSupport.sectionLayoutId() : multiItemTypeSupport.getLayoutId(itemType);
                }

                @Override
                public int getItemViewType(int position, T o) {
                    int positionVal = getIndexForPosition(position);
                    return mSections.values().contains(position) ? TYPE_SECTION : multiItemTypeSupport.getItemViewType(positionVal, o);
                }
            };
        } else {
            throw new RuntimeException("layoutId or MultiItemTypeSupport must set one.");
        }
    }

    public void findSections() {
        int n = mDatas.size();
        int nSections = 0;
        mSections.clear();
        for (int i = 0; i < n; i++) {
            String sectionName = mRSectionSupport.getTitle(mDatas.get(i));
            if (!mSections.containsKey(sectionName)) {
                mSections.put(sectionName, i + nSections);
                nSections++;
            }
        }
    }

    public int getIndexForPosition(int position) {
        int nSections = 0;
        Set<Map.Entry<String, Integer>> entrySet = mSections.entrySet();
        for (Map.Entry<String, Integer> entry : entrySet) {
            if (entry.getValue() < position) {
                nSections++;
            }
        }
        return position - nSections;
    }

    public RSectionAdapter(Context context, RMultiItemTypeSupport RMultiItemTypeSupport,
                           List<T> datas, RSectionSupport sectionSupport) {
        this(context, -1, RMultiItemTypeSupport, datas, sectionSupport);
    }

    @Override
    public int getItemViewType(int position) {
        return mRMultiItemTypeSupport.getItemViewType(position, null);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        unregisterAdapterDataObserver(observer);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        position = getIndexForPosition(position);
        if (holder.getItemViewType() == TYPE_SECTION) {
            mRSectionSupport.sectionConvert(holder, mDatas.get(position));
            return;
        }
        super.onBindViewHolder(holder, position);
    }

    @Override
    protected int getPosition(RecyclerView.ViewHolder viewHolder) {
        return getIndexForPosition(viewHolder.getAdapterPosition());
    }

    @Override
    protected boolean isEnabled(int viewType) {
        if (viewType == TYPE_SECTION)
            return false;
        return super.isEnabled(viewType);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + mSections.size();
    }

}
