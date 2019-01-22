package com.example.hayoung.mirrore.Memo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hayoung.mirrore.R;

import org.litepal.crud.ClusterQuery;
import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class EditNote extends AppCompatActivity {
    private String title;
    private String content;
    private String identifier;
    EditText noteTitle;
    EditText noteContent;
    TextView noteToolbar;
    public MyNote myNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        int notePosition = intent.getIntExtra("note_position", -1);

        if (notePosition != -1) {
            myNote = MemoActivity.mNote.get(notePosition);

            noteTitle = findViewById(R.id.title_editor);
            noteContent = findViewById(R.id.content_editor);
            noteToolbar = findViewById(R.id.toolbar_title);
            title = myNote.getTitle();
            content = myNote.getContent();
            identifier = myNote.getIdentifier();

            if (title.equals("제목 없음")) {
                noteTitle.setText("");
                noteToolbar.setText("");
            } else {
                noteTitle.setText(title);
                noteToolbar.setText(title);
            }
            noteContent.setText(myNote.getContent());

        } else {
            Toast.makeText(EditNote.this, "메모 위치를 가져 오지 못했습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = getIntent();
            int notePosition = intent.getIntExtra("note_position", -1);
            myNote = MemoActivity.mNote.get(notePosition);

            noteTitle = findViewById(R.id.title_editor);
            noteContent = findViewById(R.id.content_editor);

            title = myNote.getTitle();
            content = myNote.getContent();

            if (title.equals(noteTitle.getText().toString().equals("") ? "제목 없음" : noteTitle.getText().toString()) &&
                    content.equals(noteContent.getText().toString())) {
                finish();
            } else {
                AlertDialog.Builder backConfirmation = new AlertDialog.Builder(this);
                backConfirmation.setTitle("저장하시겠습니까?");
                backConfirmation.setCancelable(true);
                backConfirmation.setPositiveButton("저장 후 리턴", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final MyDate date = new MyDate();

                        title = noteTitle.getText().toString();
                        content = noteContent.getText().toString();

                        if (title.trim().equals("") && content.trim().equals("")) {
                            MyToast.makeText(EditNote.this, "제목과 내용은 비워 둘 수 없습니다.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (title.equals(myNote.getTitle()) && content.equals(myNote.getContent())) {
                            finish();
                        }
                        if (title.trim().equals("")) {
                            title = "제목 없음";
                        }
                        if (content.trim().equals("")){
                            content = "";
                        }
                        NoteUtils.INSTANCE.updateNote(myNote, title, content, date);
                        MyToast.makeText(EditNote.this, "성공적으로 저장되었습니다.", Toast.LENGTH_SHORT).show();
                        updateMemoDB();
                        finish();
                    }
                });
                backConfirmation.setNegativeButton("저장되지 않았습니다.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                backConfirmation.show();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cre_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final MyDate date = new MyDate();
        switch (item.getItemId()) {
            case R.id.finish:
                title = noteTitle.getText().toString();
                content = noteContent.getText().toString();

                if (title.trim().equals("") && content.trim().equals("")) {
                    MyToast.makeText(EditNote.this, "제목과 내용은 비워 둘 수 없습니다.",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                if ((title.equals(myNote.getTitle()) ||
                        (title.equals("") && myNote.getTitle().equals("제목 없음"))) &&
                        content.equals(myNote.getContent())) {
                    finish();
                    return true;
                }
                if (title.trim().equals("")) {
                    title = "제목 없음";
                }
                if (content.trim().equals("")) {
                    content = "";
                }
                NoteUtils.INSTANCE.updateNote(myNote, title, content, date);
                myNote.setLastEdited(date.toString());
                MyToast.makeText(EditNote.this, "성공적으로 저장되었습니다.", Toast.LENGTH_SHORT).show();
                updateMemoDB();

                finish();
                break;

            case android.R.id.home:
                Intent intent = getIntent();
                int notePosition = intent.getIntExtra("note_position", -1);
                myNote = MemoActivity.mNote.get(notePosition);

                noteTitle = findViewById(R.id.title_editor);
                noteContent = findViewById(R.id.content_editor);

                title = myNote.getTitle();
                content = myNote.getContent();

                if (title.equals(noteTitle.getText().toString().equals("") ? "제목 없음" : noteTitle.getText().toString()) &&
                        content.equals(noteContent.getText().toString())) {
                    finish();
                } else {
                    AlertDialog.Builder backConfirmation = new AlertDialog.Builder(this);
                    backConfirmation.setTitle("저장하시겠습니다.？");
                    backConfirmation.setCancelable(true);
                    backConfirmation.setPositiveButton("저장후 리턴", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final MyDate date = new MyDate();

                            title = noteTitle.getText().toString();
                            content = noteContent.getText().toString();

                            if (title.trim().equals("") && content.trim().equals("")) {
                                MyToast.makeText(EditNote.this, "제목과 내용은 비워 둘 수 없습니다.",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (title.equals(myNote.getTitle()) && content.equals(myNote.getContent())) {
                                finish();
                            }
                            if (title.trim().equals("")) {
                                title = "제목 없음";
                            }
                            if (content.trim().equals("")) {
                                content = "";
                            }
                            NoteUtils.INSTANCE.updateNote(myNote, title, content, date);
                            MyToast.makeText(EditNote.this, "성공적으로 저장되었습니다.", Toast.LENGTH_SHORT).show();
                            updateMemoDB();

                            finish();
                        }
                    });
                    backConfirmation.setNegativeButton("저장되지 않았습니다.", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    backConfirmation.show();
                }
                    break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void updateMemoDB(){
        try {
            UpdateMemo updateMemo = new UpdateMemo(getApplicationContext());//외부 데이터베이스에 저장
            String successStr = updateMemo.execute(title, content, identifier).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
