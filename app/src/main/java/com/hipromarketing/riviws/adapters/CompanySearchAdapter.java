package com.hipromarketing.riviws.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.hipromarketing.riviws.R;
import com.hipromarketing.riviws.models.Company;
import com.hipromarketing.riviws.ui.CategoryDetails;
import com.hipromarketing.riviws.utils.UICreator;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

public class CompanySearchAdapter extends RecyclerView.Adapter<CompanySearchAdapter.CMSearchViewHolder> implements Filterable {

    private AppCompatActivity activity;
    private Context context;
    private List<Company> filteredCompanies;
    private List<Company> companies;


    public CompanySearchAdapter(AppCompatActivity activity, Context context, List<Company> companies) {
        this.activity = activity;
        this.context = context;
        this.filteredCompanies = companies;
        this.companies = companies;
    }

    @NonNull
    @Override
    public CMSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CMSearchViewHolder(LayoutInflater.from(context).inflate(R.layout.company_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CMSearchViewHolder holder, int position) {
        final Company company = filteredCompanies.get(position);

        holder.companyName.setText(company.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UICreator.getInstance(activity).createDialog(CategoryDetails.newInstance(company),"comp");
            }
        });

    }

    @Override
    public int getItemCount() {
        return filteredCompanies.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString().toLowerCase().trim();
                List<Company> sorted = new ArrayList<>();

                if (query.isEmpty()){
                    filteredCompanies = companies;
                }else {
                    for (Company company : companies){
                        if (company.getName().toLowerCase().contains(query)){
                            sorted.add(company);
                        }
                    }
                    filteredCompanies = sorted;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredCompanies;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                            filteredCompanies = (List<Company>) results.values;
                            notifyDataSetChanged();
            }
        };
    }

    class CMSearchViewHolder extends RecyclerView.ViewHolder{
        AppCompatTextView companyName;

        public CMSearchViewHolder(@NonNull View itemView) {
            super(itemView);

            companyName = itemView.findViewById(R.id.companyName);

        }
    }
}
