package com.corey.ole;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/* Adapter for Properties in Landlord's Home */

public class PropertyAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<PropertyView> mPropertyView;
    private OnItemClickListener mListener;

    public PropertyAdapter(Context context, ArrayList<PropertyView> propertyView) {
        mContext = context;
        mPropertyView = propertyView;
    }

    public interface  OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.property_cell, viewGroup, false);
        return new PropertyViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        PropertyView property = mPropertyView.get(i);
        ((PropertyViewHolder) viewHolder).bind(property);
    }

    @Override
    public int getItemCount() {
        return mPropertyView.size();
    }

    /* PropertyViewHolder sets the image and info of the property on the UI */
    class PropertyViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mPropertyLayout;
        private TextView mTitle;
        private TextView mAddress;
        private TextView mTenants;
        private ImageView mImage;


        public PropertyViewHolder(View itemView, final PropertyAdapter.OnItemClickListener listener) {
            super(itemView);
            mPropertyLayout = itemView.findViewById(R.id.property_cell);
            mTitle = mPropertyLayout.findViewById(R.id.title);
            mAddress = mPropertyLayout.findViewById(R.id.address);
            mTenants = mPropertyLayout.findViewById(R.id.tenants);
            mImage = mPropertyLayout.findViewById(R.id.image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            PropertyView p = mPropertyView.get(position);
                            listener.onItemClick(position);

                        }
                    }
                }
            });
        }

        void bind(PropertyView property) {
            String t = Integer.toString(property.getTenants()) + " Tenants";
            mTitle.setText(property.getTitle());
            mAddress.setText(property.getAddress());
            mTenants.setText(t);
            mImage.setImageBitmap(property.getImage());
        }

    }
}