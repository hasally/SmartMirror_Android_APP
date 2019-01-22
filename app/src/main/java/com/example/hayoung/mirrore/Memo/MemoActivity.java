package com.example.hayoung.mirrore.Memo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hayoung.mirrore.Main.CustomCallingWeb;
import com.example.hayoung.mirrore.Main.CustomSettingTask;
import com.example.hayoung.mirrore.R;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import prefs.UserInfo;

public class MemoActivity extends AppCompatActivity {
    Toolbar toolbar;
    private static final String ACTIVITY_TAG = "MemoActivity";
    List<MyNote> mNoteTemp = new ArrayList<>();
    UserInfo userInfo;

    private com.zcw.togglebutton.ToggleButton memo_togglebtn;

    private ImageView add_memo;
    private TextView tv_noMore;  //더 이상 내용이없는 텍스트
    private long mExitTime = 0; //돌아 가기 버튼을 클릭 한 시간을 기록하십시오.

    //public:
    public static List<MyNote> mNote;   //note 리스트
    public static int notePosition; //리스트 추가시 위치
    public RecyclerView noteListView;   //노트 view
    public static int longClickPosition = 0;

    private String title;
    private String content;
    private String identifier;
    public MyDate createdDate;
    EditText noteTitle;
    EditText noteContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        //toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_noMore = findViewById(R.id.no_more);
        noteListView = findViewById(R.id.note_list);
        add_memo = (ImageView) findViewById(R.id.add_memo);
        memo_togglebtn = (com.zcw.togglebutton.ToggleButton) findViewById(R.id.memo_togglebtn);
        userInfo = new UserInfo(this);

        noteTitle = findViewById(R.id.title_editor);
        noteContent = findViewById(R.id.content_editor);

        createdDate = new MyDate();

        //toggle
        if (userInfo.getKeyMemo() == 0) {
            memo_togglebtn.setToggleOff();
            userInfo.setKeyMemo(0);
        } else {
            memo_togglebtn.setToggleOn();
            userInfo.setKeyMemo(1);
        }

        memo_togglebtn.setOnToggleChanged(new com.zcw.togglebutton.ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                CustomSettingTask customSettingTask = new CustomSettingTask(MemoActivity.this);
                if (on == true) {
                    try {
                        String updateSuccessStr = customSettingTask.execute("memo", "1", userInfo.getKeyId().toString()).get();
                        System.out.println("update 성공 여부:" + updateSuccessStr);
                        userInfo.setKeyMemo(1);
                        memo_togglebtn.setToggleOn();
//                        MyToast.makeText(MemoActivity.this, "성공적으로 저장되었습니다.", Toast.LENGTH_SHORT).show();
                        MemoActivity.notePosition = DataSupport.count(MyNote.class) - 1;
//                        finish();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        String updateSuccessStr = customSettingTask.execute("memo", "0", userInfo.getKeyId().toString()).get();
                        System.out.println("update 성공 여부:" + updateSuccessStr);
                        userInfo.setKeyMemo(0);
                        memo_togglebtn.setToggleOff();
//                        MyToast.makeText(MemoActivity.this, "성공적으로 저장되었습니다.", Toast.LENGTH_SHORT).show();
                        MemoActivity.notePosition = DataSupport.count(MyNote.class) - 1;
//                        finish();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //메모 추가
        add_memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent jumpToCreateNote = new Intent(MemoActivity.this, CreateNote.class);
                startActivity(jumpToCreateNote);
            }
        });

        //데이터베이스에서 기존 노트를 읽고 임시로 저장.
        mNoteTemp = DataSupport.findAll(MyNote.class);
        //데이터 베이스 확인
        if (mNoteTemp.size() != 0) {
            mNote = mNoteTemp;
            refreshNoteListView(noteListView);
        } else {
            mNote = new ArrayList<>();
            tv_noMore.setVisibility(View.VISIBLE);
        }
        mNoteTemp = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNote.size() != 0) {
            tv_noMore = findViewById(R.id.no_more);
            if (tv_noMore.getVisibility() == View.VISIBLE) {
                tv_noMore.setVisibility(View.GONE);
            }
            noteListView = findViewById(R.id.note_list);
            refreshNoteListView(noteListView);
            noteListView.scrollToPosition(notePosition == 0 ? mNote.size() : notePosition);
        }
    }

    public void refreshNoteListView(RecyclerView recyclerView) {
        if (recyclerView != null) {
//            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            LinearLayoutManager layoutManager = new LinearLayoutManager(this); //???
            layoutManager.setStackFromEnd(true);
            layoutManager.setReverseLayout(true);
            recyclerView.setLayoutManager(layoutManager);
            NoteAdapter2 adapter = new NoteAdapter2(mNote, MemoActivity.this);
            recyclerView.setAdapter(adapter);
            noteListView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    menu.add(0, 666, 0, "삭제");
                }
            });
            if (mNote.size() != 0) {
                if (tv_noMore.getVisibility() == View.VISIBLE) {
                    tv_noMore.setVisibility(View.GONE);
                }
            } else {
                if (tv_noMore.getVisibility() != View.VISIBLE) {
                    tv_noMore.setVisibility(View.VISIBLE);
                }
            }

        } else {
            Log.d(ACTIVITY_TAG, "refreshNoteListView: 비어있는 recyclerView 매개 변수가 전달되었습니다.");
            MyToast.makeText(this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    /*//toolbar
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.setting_main_menu, menu);
        return true;
    }*/

    //뒤로 버튼 동작 ( "프로그램을 끝내려면 다시 누르십시오")을 클릭하십시오.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // 사용자가 "뒤로 버튼"을 클릭했는지 확인합니다.
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


    public static AlertDialog.Builder buildAlertDialog(Context context, String alertTitle,
                                                       String alertMessage) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(alertTitle);
        dialog.setMessage(alertMessage);
        return dialog;
    }

    //메뉴를 길게 누르고있을 때해야 할 일
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 666:   //긴 보도 후 나타나는 삭제 버튼을 클릭하면
                try {
                    DeleteMemo deleteMemo = new DeleteMemo(getApplicationContext());//외부 데이터베이스에 저장
                    String successStr = deleteMemo.execute(mNote.get(longClickPosition).getIdentifier()).get();
                    MyToast.makeText(MemoActivity.this, "성공적으로 저장되었습니다.", Toast.LENGTH_SHORT).show();
                    MemoActivity.notePosition = DataSupport.count(MyNote.class) - 1;
                    finish();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mNote.get(longClickPosition).delete();
                mNote.remove(longClickPosition);
                refreshNoteListView(noteListView);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //backButton 처리
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

}

