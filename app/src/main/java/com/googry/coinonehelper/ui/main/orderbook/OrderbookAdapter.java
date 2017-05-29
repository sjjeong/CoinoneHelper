package com.googry.coinonehelper.ui.main.orderbook;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.data.CoinoneOrderbook;
import com.googry.coinonehelper.databinding.OrderbookItemBinding;

import java.util.ArrayList;

/**
 * Created by seokjunjeong on 2017. 5. 28..
 */

public class OrderbookAdapter extends RecyclerView.Adapter<OrderbookAdapter.ViewHolder> {
    private ArrayList<CoinoneOrderbook.Book> mBooks;
    private Context mContext;
    public enum BookType{
        ASK,BID
    };

    private BookType mBookType;

    public OrderbookAdapter(Context mContext,
                            BookType bookType) {
        this.mContext = mContext;
        mBookType = bookType;
        mBooks = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.orderbook_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(mBooks.get(position));
    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }

    public void setBooks(ArrayList<CoinoneOrderbook.Book> books){
        mBooks = books;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        OrderbookItemBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
        public void setData(CoinoneOrderbook.Book book){
            switch (mBookType){
                case ASK:{
                    binding.setLeftValue(Double.toString(book.qty));
                    binding.setRightValue(Long.toString(book.price));
                }
                break;
                case BID:{
                    binding.setLeftValue(Long.toString(book.price));
                    binding.setRightValue(Double.toString(book.qty));
                }
                break;
            }
        }
    }
}
