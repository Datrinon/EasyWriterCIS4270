package com.trinhdan.easywriter;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class FreeWriteListFragment extends Fragment {

    private List<FreeWrite> freeWrites;

    // For the activity to implement
    public interface OnFreeWriteSelectedListener {
        void onFreeWriteSelected(int bandId);
    }

    // Reference to the activity
    private OnFreeWriteSelectedListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        freeWrites = FreeWriteDAO.getInstance(getContext()).fetchAllFreeWrites();


        if (freeWrites.size() > 0) {
            // Inflate the RecyclerView view*holder* (not a viewgroup, is a viewholder)
            View view = inflater.inflate(R.layout.fragment_freewrite_list, container, false);

            // RecyclerView -- three steps
            // 1. Reference the recycler view inside of the fragment's view
            // 2. Set a layout manager to manage how views should be recycled.
            // 3. Set an adapter to inflate data into views for the recycler view.

            // This is the recyclerview that is defined inside of the layout that was just inflated.
            RecyclerView recyclerView = view.findViewById(R.id.freewrite_recycler_view);
            // Set the layout manager the recycler view will use
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            // Set adapter for the recyclerview to use.
            FreeWriteAdapter adapter = new FreeWriteAdapter(FreeWriteDAO.getInstance(getContext()).fetchAllFreeWrites());
            recyclerView.setAdapter(adapter);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(dividerItemDecoration);

            return view;
        } else {
            LinearLayoutCompat ll = new LinearLayoutCompat(getContext());
            showEmptyScreen(ll);
            // Return this fragment to the user.
            return ll;
        }


    }

    private void showEmptyScreen(LinearLayoutCompat ll){
        ll.setOrientation(LinearLayoutCompat.VERTICAL);

        ImageView warningSign = new ImageView(getContext());
        LinearLayoutCompat.LayoutParams layoutParams = new LinearLayoutCompat.LayoutParams(250, 250);
        layoutParams.gravity=Gravity.CENTER;
        layoutParams.topMargin = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) ? 500 : 250;
        warningSign.setLayoutParams(layoutParams);

        warningSign.setImageResource(R.drawable.warning);
        warningSign.requestLayout();


        Log.d("DEBUG", "Tell the user there are no free writes");
        TextView noFreeWritesMsg = new TextView(getContext());
        noFreeWritesMsg.setText("No free writes found... yet. Get writing!");
        noFreeWritesMsg.setTextSize(20);
        noFreeWritesMsg.setGravity(Gravity.CENTER);

        ll.addView(warningSign);
        ll.addView(noFreeWritesMsg);
    }

    @Override
    public void onAttach(Context context) { // Context refers to the activity that started the fragment.
        super.onAttach(context);
        if (context instanceof OnFreeWriteSelectedListener) {
            listener = (OnFreeWriteSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFreeWriteSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


    private class FreeWriteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private FreeWrite freeWrite;

        private TextView titleTextView;

        public FreeWriteHolder(LayoutInflater inflater, ViewGroup parent) {
            // parent constructor takes in a View.
            // This view should be the view of an item in the collection.
            // Use inflater passed in to get it.
            // Parent specifies the parent of the inflated items.
            super(inflater.inflate(R.layout.list_item_freewrite, parent, false));
            itemView.setOnClickListener(this);
            titleTextView = itemView.findViewById(R.id.freeWriteTitle);
        }

        public void bind(FreeWrite fw) {
            freeWrite = fw;
            titleTextView.setText(fw.getTitle());
        }

        @Override
        public void onClick(View view) {
            // Tell ListActivity what band was clicked, so it can launch the detailActivity.
            //mListener.onFreeWriteSelected(FreeWrite.getId());
            // TODO when we implement the detailActivity.
            listener.onFreeWriteSelected(freeWrite.getId());

        }
    }

    private class FreeWriteAdapter extends RecyclerView.Adapter<FreeWriteHolder> {

        private List<FreeWrite> freeWrites;

        public FreeWriteAdapter(List<FreeWrite> freeWrites) {
            this.freeWrites = freeWrites;
        }

        @Override
        public FreeWriteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new FreeWriteHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(FreeWriteHolder holder, int position) {
            FreeWrite freeWrite = freeWrites.get(position);
            holder.bind(freeWrite);
        }

        @Override
        public int getItemCount() {
            return freeWrites.size();
        }
    }


}