package com.example.ekaksha.Data;

public class Examination {
    private String classroomID;
    private String classroomName;
    private String description;
    private  String url;
    private long timeStart;
    private long timeEnd;
    private int maximumMarks;

    public void setMaximumMarks(int maximumMarks) {
        this.maximumMarks = maximumMarks;
    }

    public int getMaximumMarks() {
        return maximumMarks;
    }

    public String getClassroomID() {
        return classroomID;
    }

    public void setClassroomID(String classroomID) {
        this.classroomID = classroomID;
    }

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTimeEnd() {
        return timeEnd;
    }

    public long getTimeStart() {
        return timeStart;
    }

    public void setTimeEnd(long timeEnd) {
        this.timeEnd = timeEnd;
    }

    public void setTimeStart(long timeStart) {
        this.timeStart = timeStart;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }


}
