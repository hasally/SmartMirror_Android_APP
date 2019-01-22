package com.example.hayoung.mirrore.Memo;

import org.litepal.crud.DataSupport;

public class MyNote extends DataSupport {
    private String title;
    private String content;
    private final String identifier;
    private String createdDate;
    private String lastEdited;

    //생성자는 제목, 내용, ID, 생성 날짜를 전달합니다. (마지막으로 편집 한 날짜는 별도로 설정됩니다.)
    MyNote(String title, String content, String identifier, String createdDate) {
        this.title = title;
        this.content = content;
        this.identifier = identifier;
        this.createdDate = createdDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getCreateDate() {
        return createdDate;
    }

    public String getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited(String lastEdited) {
        this.lastEdited = lastEdited;
    }
}
