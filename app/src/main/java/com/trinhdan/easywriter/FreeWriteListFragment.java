package com.trinhdan.easywriter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class FreeWriteListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout of this fragment
        View view = inflater.inflate(R.layout.fragment_freewrite_list, container, false);
        LinearLayout layout = (LinearLayout) view;

        // Create the buttons using the band names and ids from BandDatabase
        List<FreeWrite> freeWritesList = FreeWriteDAO.getInstance(getContext()).fetchAllFreeWrites();

        // Skip the for loop, go straight to the implementation of a recycler view.

        return view;
    }

//    private final View.OnClickListener buttonClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            // Start DetailsActivity
//            Intent intent = new Intent(getActivity(), DetailsActivity.class);
//            startActivity(intent);
//        }
//    };

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