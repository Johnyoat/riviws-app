package com.hipromarketing.riviws.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;

import com.hipromarketing.riviws.R;
import com.hipromarketing.riviws.models.Category;
import com.hipromarketing.riviws.ui.CategoryDetails;
import com.hipromarketing.riviws.utils.UICreator;

import java.util.ArrayList;
import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> implements Filterable {

    private AppCompatActivity activity;
    private List<Category> categories;
    private List<Category> filteredCategory;
    private Context context;


    public CategoriesAdapter(List<Category> categories, Context context, AppCompatActivity activity) {
        this.activity = activity;
        this.categories = categories;
        this.context = context;
        this.filteredCategory = categories;
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CategoriesViewHolder(LayoutInflater.from(context).inflate(R.layout.category_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoriesViewHolder holder, int i) {
        final Category category = filteredCategory.get(i);
        holder.image.setBackground(activity.getResources().getDrawable(category.getImgUrl()));
        holder.category.setText(category.getCategory());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UICreator.getInstance(activity).createDialog(CategoryDetails.newInstance(String.valueOf(category.getImgUrl()), category.getCategory()),"categoryDetails");
            }
        });


    }

    @Override
    public int getItemCount() {
        return filteredCategory.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String query = charSequence.toString().toLowerCase().trim();
                FilterResults filterResults = new FilterResults();
                List<Category> cats = new ArrayList<>();

                if (query.isEmpty()){
                    filteredCategory = categories;
                }else {

                }
                return null;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            }
        };
    }

    class CategoriesViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView category;
        RelativeLayout image;

        public CategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.category);
            image = itemView.findViewById(R.id.image);

        }
    }
}
