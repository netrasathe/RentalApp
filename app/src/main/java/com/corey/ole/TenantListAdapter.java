package com.corey.ole;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

// Adapter for the recycler view in the Property's tenant list.
public class TenantListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<TenantProfile> mTenants;

    public TenantListAdapter(Context context, ArrayList<TenantProfile> tenants) {
        mContext = context;
        mTenants = tenants;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tenant_cell, parent, false);
        return new TenantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        TenantProfile tenant = mTenants.get(position);
        ((TenantViewHolder) holder).bind(tenant);
    }

    @Override
    public int getItemCount() {
        return mTenants.size();
    }
}

class TenantViewHolder extends RecyclerView.ViewHolder {

    public RelativeLayout mCommentBubbleLayout;
    public TenantProfile mTenant;
    public TextView mNameTextView;
    public TextView mRoomTextView;
    public ImageView mPhotoTextView;



    public TenantViewHolder(View itemView) {
        super(itemView);
        mCommentBubbleLayout = itemView.findViewById(R.id.tenant_cell);
        mNameTextView = mCommentBubbleLayout.findViewById(R.id.name);
        mRoomTextView = mCommentBubbleLayout.findViewById(R.id.room);
        mPhotoTextView = mCommentBubbleLayout.findViewById(R.id.photo);


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToTenantListIntent = new Intent(view.getContext(), LandlordTenantProfileActivity.class);
                goToTenantListIntent.putExtra(TenantProfile.EXTRA_TENANT_ID, mTenant.getId());
                view.getContext().startActivity(goToTenantListIntent);

            }
    });

    }

    void bind(TenantProfile tenant) {
        mTenant = tenant;
        mNameTextView.setText(tenant.getFirstName() + " " + tenant.getLastName());
        mRoomTextView.setText(tenant.getRoom());
        //mPhotoTextView.setImageBitmap(tenant.getPhoto());
    }
}
