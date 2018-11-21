package com.rex.common.widget.recyblerView;

public interface AdapterCallBack<Data>{
    void update(Data data, RecyclerViewAdapter.ViewHolder<Data> viewHolder);
}
