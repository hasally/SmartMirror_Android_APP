package com.example.hayoung.mirrore.Memo;

import java.util.List;

enum NoteUtils {
    INSTANCE;

    public void saveNote(String title, String content, String identifier, MyDate createdDate) {
        MyNote note = new MyNote(title, content, identifier, createdDate.toString());
        note.setLastEdited(createdDate.toString());
        note.setLastEdited(createdDate.toString());
        MemoActivity.mNote.add(note);
        note.save();
    }
    public void updateNote(MyNote note, String title, String content, MyDate editedDate) {
        note.setTitle(title);
        note.setContent(content);
        note.setLastEdited(editedDate.toString());
        note.save();
    }

    public void removeNote(List<MyNote> mNote, int position) {
        mNote.remove(position);
    }
}
