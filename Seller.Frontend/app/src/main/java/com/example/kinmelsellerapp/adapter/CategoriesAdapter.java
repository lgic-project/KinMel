package com.example.kinmelsellerapp.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kinmelsellerapp.response.Category;
import com.example.kinmelsellerapp.utils.CategoriesBottomSheetDialogFragment;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {
    private final Category[] categories;
    private final CategoriesBottomSheetDialogFragment bottomSheetDialogFragment;

    public CategoriesAdapter(Category[] categories, CategoriesBottomSheetDialogFragment bottomSheetDialogFragment) {
        this.categories = categories;
        this.bottomSheetDialogFragment = bottomSheetDialogFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(categories[position].getName());
        holder.itemView.setOnClickListener(v -> {
       bottomSheetDialogFragment.dismiss();
            if (bottomSheetDialogFragment.listener != null) {
                bottomSheetDialogFragment.listener.onCategorySelected(categories[position]);
            }

        });
    }

    @Override
    public int getItemCount() {
        return categories.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }
}