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
        holder.bind(mBooks.get(position));
    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }

    public void setBooks(ArrayList<CoinoneOrderbook.Book> books) {
        mBooks = books;
        notifyDataSetChanged();
    }

    public void setBook(CoinoneOrderbook.Book book, int position) {
        mBooks.set(position, book);
        notifyItemChanged(position);
    }

    public CoinoneOrderbook.Book getBook(int position) {
        return mBooks.get(position);
    }

    public enum BookType {
        ASK, BID
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private OrderbookItemBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void bind(CoinoneOrderbook.Book book) {
            binding.setBookType(mBookType);
            switch (mBookType) {
                case ASK: {
                    binding.setLeftValue(String.format("%,.4f", book.qty));
                    binding.setRightValue(String.format("%,d", book.price));
                }
                break;
                case BID: {
                    binding.setLeftValue(String.format("%,d", book.price));
                    binding.setRightValue(String.format("%,.4f", book.qty));
                }
                break;
            }
        }
    }
}
