package com.rex.common.widget.recyblerView;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rex.common.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

// Question: 1. @LayoutRes
//           2. Interface and implementation
//           3. inflater.inflate, why false
//           4. Collections.addAll
//           5. Abstract class and interface
public abstract class RecyclerViewAdapter<Data>
        extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder<Data>>
        implements View.OnClickListener, View.OnLongClickListener, AdapterCallBack<Data> {
    private final List<Data> mDataList;
    private AdapterListener<Data> mListener;

    public RecyclerViewAdapter() {
        this(null);
    }

    public RecyclerViewAdapter(List<Data> mDataList, AdapterListener<Data> mListener) {
        this.mDataList = mDataList;
        this.mListener = mListener;
    }

    public RecyclerViewAdapter(AdapterListener<Data> mListener) {
        this.mDataList = new ArrayList<Data>();
        this.mListener = mListener;
    }

    public void setListener(AdapterListener<Data> mListener) {
        this.mListener = mListener;
    }

    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, mDataList.get(position));
    }

    @LayoutRes
    protected abstract int getItemViewType(int i, Data data);

    /**
     * creat a view Holder fot the view
     * @param viewGroup view group
     * @param i the id of the layout
     * @return view holder
     */
    @NonNull
    @Override
    public ViewHolder<Data> onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View root = inflater.inflate(i, viewGroup, false);

        ViewHolder<Data> viewHolder= onCreateViewHolder(root, i);
        root.setTag(R.id.recyclerView_holder, viewHolder);

        viewHolder.mUnbinder = ButterKnife.bind(viewHolder, root);

        root.setOnClickListener(this);
        root.setOnLongClickListener(this);

        return viewHolder;
    }

    /**
     * get all data
     * @return data list
     */
    public List<Data> getItems(){
        return mDataList;
    }

    /**
     * create a specific view Holder
     * @return view holder
     */
    protected abstract ViewHolder<Data> onCreateViewHolder(View root, int viewType);

    /**
     * bind the viewHolder with the data
     * @param dataViewHolder viewHolder
     * @param i position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder<Data> dataViewHolder, int i) {
        Data data = mDataList.get(i);
        dataViewHolder.bind(data);
    }

    /**
     *  get the size of the data
     */
    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    //****************************below is the operations of data**********************
    /**
     * append one data
     * @param data data
     */
    public void add(Data data){
        mDataList.add(data);
        notifyItemChanged(mDataList.size() - 1);
    }

    /**
     * append some data into the dataList
     * @param dataList data list
     */
    public void add(Data... dataList){
        if(dataList == null || dataList.length <= 0){
            return;
        }
        int startPos = mDataList.size();
        Collections.addAll(mDataList, dataList);
        notifyItemRangeInserted(startPos, mDataList.size());
    }
    /**
     * append a list of data
     * @param dataList datalist
     */
    public void add(Collection<Data> dataList){
        if(dataList == null || dataList.size() <= 0){
            return;
        }
        int startPos = mDataList.size();
        mDataList.addAll(dataList);
        notifyItemRangeInserted(startPos, mDataList.size());
    }

    /**
     * clear datalist
     */
    public void clear(){
        mDataList.clear();
        notifyDataSetChanged();
    }


    public void replace(Collection<Data> dataList){
        mDataList.clear();
        if(dataList == null || dataList.size() == 0){
            return;
        }
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    //**************************** View Listener ***********************
    @Override
    public void onClick(View view){
        if(mListener != null){
            ViewHolder viewHolder = (ViewHolder) view.getTag(R.id.recyclerView_holder);
            int pos = viewHolder.getAdapterPosition();
            mListener.onClickListener(viewHolder, mDataList.get(pos));
        }
    }

    @Override
    public boolean onLongClick(View view){
        if(mListener != null){
            ViewHolder viewHolder = (ViewHolder) view.getTag(R.id.recyclerView_holder);
            int pos = viewHolder.getAdapterPosition();
            mListener.onLongClickListener(viewHolder, mDataList.get(pos));
            return true;
        }
        return false;
    }


    interface AdapterListener<Data> {
        void onClickListener(ViewHolder viewHolder, Data data);

        boolean onLongClickListener(ViewHolder viewHolder, Data data);
    }

    public static abstract class AdapterListenerImpl<Data> implements AdapterListener<Data> {
        @Override
        public void onClickListener(ViewHolder viewHolder, Data data) {

        }

        @Override
        public boolean onLongClickListener(ViewHolder viewHolder, Data data) {
            return false;
        }
    }

    @Override
    public void update(Data data, ViewHolder<Data> viewHolder) {
        int pos = viewHolder.getAdapterPosition();
        if(pos >= 0){
            // if changed, replace data in the dataList
            mDataList.remove(pos);
            mDataList.add(pos, data);
            // notify then
            notifyItemChanged(pos);
        }
    }

    //****************************View holder***************************

    public static abstract class ViewHolder<Data>
            extends RecyclerView.ViewHolder{
        Data mData;
        Unbinder mUnbinder;
        AdapterCallBack<Data> callback;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        void bind(Data data){
            mData = data;
            if(mData != null){
                onBind(data);
            }
        }

        /**
         * give the value to the specific widgets
         * @param data an object has all value;
         */
        protected abstract void onBind(Data data);

        // replace the original data
        protected void updateData(Data data){
            if(this.callback != null){
                this.callback.update(mData, this);
            }
        }
    }


}



