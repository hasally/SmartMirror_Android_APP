package com.example.hayoung.mirrore.Memo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hayoung.mirrore.R;

import org.litepal.crud.DataSupport;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import prefs.UserInfo;

public class CreateNote extends AppCompatActivity {
    private String title;
    private String content;
    private String identifier;
    public MyDate createdDate;

    UserInfo userInfo = null;

    EditText noteTitle;
    EditText noteContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        Toolbar toolbar = findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        noteTitle = findViewById(R.id.title_editor);
        noteContent = findViewById(R.id.content_editor);
        userInfo = new UserInfo(getApplication());

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Confirmation();
        }
        return super.onKeyDown(keyCode, event);
    }

    //toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cre_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.finish:
                return save();
            case android.R.id.home:
                Confirmation();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean save() {
        createdDate = new MyDate();
        title = noteTitle.getText().toString();
        content = noteContent.getText().toString();

        if (title.trim().equals("") && content.trim().equals("")) {
            finish();
            return false;
        } else {
            //제목이 없으면 "제목 없음"으로 완성
            if (title.trim().equals("")) {
                title = "제목 없음";
            }
            //내용이 없으면 빈 메모가 완성
            if (content.trim().equals("")){
                content = "";
            }
            identifier = UUID.randomUUID().toString();  //UUID
            NoteUtils.INSTANCE.saveNote(title, content, identifier, createdDate);  //데이터베이스에 저장
            try {
                InsertMemoTask insertMemo = new InsertMemoTask(getApplicationContext());//외부 데이터베이스에 저장
                String successStr = insertMemo.execute(userInfo.getKeyId(), title, content, identifier).get();

                MyToast.makeText(CreateNote.this, "성공적으로 저장되었습니다.", Toast.LENGTH_SHORT).show();
                MemoActivity.notePosition = DataSupport.count(MyNote.class) - 1;
                finish();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            return true;
        }
    }

    public void Confirmation() {
        if ( !(noteTitle.getText().toString().trim().equals("") &&
                noteContent.getText().toString().equals("")) ) {
            AlertDialog.Builder backConfirmation = MemoActivity.buildAlertDialog(CreateNote.this,
                    "저장하시겠습니까?", null);
            backConfirmation.setPositiveButton("저장하고 반환하십시오.", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    noteTitle = findViewById(R.id.title_editor);
                    noteContent = findViewById(R.id.content_editor);
                    createdDate = new MyDate();
                    identifier = UUID.randomUUID().toString();
                    title = noteTitle.getText().toString().equals("") ? "제목 없음" : noteTitle.getText().toString();
                    content = noteContent.getText().toString();
                    NoteUtils.INSTANCE.saveNote(title, content, identifier, createdDate);
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
        } else {
            finish();
        }
    }
}
