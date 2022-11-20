package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteVH> implements Filterable {

    Context context;
    ArrayList<Note> notes;
    ArrayList<Note> notesFilter;
    Listener listener;

    public NoteAdapter(Listener listener, ArrayList<Note> notes,Context context) {
        this.listener = listener;
        this.notes = notes;
        this.notesFilter = notes;
        this.context = context;
    }

    @NonNull
    @Override
    public NoteVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_row, parent,false);
        return new NoteVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteVH holder, @SuppressLint("RecyclerView") int position) {
        Note note = notesFilter.get(position);
        holder.txName.setText(note.getTitle());
        holder.txDate.setText(note.getCreateDate());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnItemListener(position,note);
            }
        });


    }

    @Override
    public int getItemCount() {
        return notesFilter.size();
    }

    @Override
    public Filter getFilter() {
        return new NoteFilter();
    }

    class NoteVH extends RecyclerView.ViewHolder{
        TextView txName, txDate;

        public NoteVH(@NonNull View itemView) {
            super(itemView);
            txName = itemView.findViewById(R.id.txTitle);
            txDate = itemView.findViewById(R.id.txDate);

        }
    }

    class NoteFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            String charString = charSequence.toString();
            if (charString.isEmpty()) {
                notesFilter = notes;
            } else {
                List<Note> filteredList = new ArrayList<>();
                for (Note row : notes) {
                    if (row.getTitle().toLowerCase().contains(charString.toLowerCase()) || row.getCreateDate().contains(charSequence)) {
                        filteredList.add(row);
                    }
                }

                notesFilter = (ArrayList<Note>) filteredList;
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = notesFilter;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            notesFilter = (ArrayList<Note>) filterResults.values;
            notifyDataSetChanged();
        }
    }

    public void addNote(Note note){
        notesFilter.add(note);
        DBHandler dbHandler = new DBHandler(context);
        dbHandler.addNote(note);
        notifyDataSetChanged();
    }

    public void editNote(Note note, int pos){
        note.setId(notesFilter.get(pos).getId());
        notesFilter.set(pos, note);
        DBHandler dbHandler = new DBHandler(context);
        dbHandler.updateNote(note);
        notifyDataSetChanged();
    }

    public void deleteNote(int pos){
        DBHandler dbHandler = new DBHandler(context);
        dbHandler.deleteNote(notesFilter.get(pos).getId());
        notesFilter.remove(pos);
        notifyDataSetChanged();
    }

    interface Listener{
        void OnItemListener(int pos, Note note);
    }
}
