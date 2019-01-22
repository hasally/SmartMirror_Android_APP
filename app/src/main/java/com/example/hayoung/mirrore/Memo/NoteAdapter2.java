package com.example.hayoung.mirrore.Memo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hayoung.mirrore.R;

import java.util.List;

public class NoteAdapter2 extends RecyclerView.Adapter<NoteAdapter2.NotesViewHolder> {

    private List<MyNote> myNotes;
    private Context context;

    public NoteAdapter2(List<MyNote> myNotes, Context context) {
        this.myNotes = myNotes;
        this.context = context;
    }

    static class NotesViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView noteTitle;
        TextView noteContent;
        TextView editedDate;

        public NotesViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            noteTitle = itemView.findViewById(R.id.note_title2);
            noteContent = itemView.findViewById(R.id.note_content2);
            editedDate = itemView.findViewById(R.id.note_edited_date2);
            noteTitle.setBackgroundColor(Color.argb(20, 0, 0, 0));
        }
    }

    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.note_card_item, viewGroup, false);
        final NotesViewHolder notesViewHolder = new NotesViewHolder(view);
//        final NoteAdapter.ViewHolder holder = new NoteAdapter.ViewHolder(view);

        notesViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = notesViewHolder.getAdapterPosition();
                MemoActivity.notePosition = position;
                Intent jumpToEditNote = new Intent(view.getContext(), EditNote.class);
                jumpToEditNote.putExtra("note_position", position);
                view.getContext().startActivity(jumpToEditNote);
            }
        });

        notesViewHolder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                MemoActivity.longClickPosition = notesViewHolder.getAdapterPosition();
                return false;
            }
        });

        return notesViewHolder;
    }

    @Override
    public void onBindViewHolder(NotesViewHolder holder, int position) {
        MyNote note = myNotes.get(position);
        holder.noteTitle.setText(note.getTitle());
        holder.noteContent.setText(note.getContent());
        holder.editedDate.setText(note.getLastEdited());
    }

    @Override
    public int getItemCount() {
        return myNotes.size();
    }


}
