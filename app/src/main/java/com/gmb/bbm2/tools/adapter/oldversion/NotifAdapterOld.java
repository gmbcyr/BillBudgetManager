package com.gmb.bbm2.tools.adapter.oldversion;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmb.bbm2.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;


/**
 * RecyclerView adapter for a list of Restaurants.
 */
public class NotifAdapterOld extends MyFBadapterOld<NotifAdapterOld.ViewHolder> {

    public interface OnNotifSelectedListener {

        void onNotifSelected(DocumentSnapshot restaurant);

    }

    private OnNotifSelectedListener mListener;

    public NotifAdapterOld(Query query, OnNotifSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {


        TextView nameView;


        TextView priceView;



        public ViewHolder(View itemView) {
            super(itemView);
            //ButterKnife.bind(this, itemView);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnNotifSelectedListener listener) {


            /*Restaurant restaurant = snapshot.toObject(Restaurant.class);
            Resources resources = itemView.getResources();

            // Load image
            Glide.with(imageView.getContext())
                    .load(restaurant.getPhoto())
                    .into(imageView);

            nameView.setText(restaurant.getName());
            ratingBar.setRating((float) restaurant.getAvgRating());
            cityView.setText(restaurant.getCity());
            categoryView.setText(restaurant.getCategory());
            numRatingsView.setText(resources.getString(R.string.fmt_num_ratings,
                    restaurant.getNumRatings()));
            priceView.setText(RestaurantUtil.getPriceString(restaurant));*/

            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onNotifSelected(snapshot);
                    }
                }
            });
        }

    }
}
