package com.example.tarun.unmevents;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Tarun on 2/20/2016.
 * Class for Node
 */
public class Event implements Parcelable {
    private String id;
    private Date createTime;
    private Date startTime;
    private Date endTime;
    private String summary;
    private String description;
    private String category;
    private String eventClass;
    private String location;
    private String organizer;
    public Event(){}
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEventClass() {
        return eventClass;
    }

    public void setEventClass(String eventClass) {
        this.eventClass = eventClass;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    protected Event(Parcel in) {
        id = in.readString();
        long tmpCreateTime = in.readLong();
        createTime = tmpCreateTime != -1 ? new Date(tmpCreateTime) : null;
        long tmpStartTime = in.readLong();
        startTime = tmpStartTime != -1 ? new Date(tmpStartTime) : null;
        long tmpEndTime = in.readLong();
        endTime = tmpEndTime != -1 ? new Date(tmpEndTime) : null;
        summary = in.readString();
        description = in.readString();
        category = in.readString();
        eventClass = in.readString();
        location = in.readString();
        organizer = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeLong(createTime != null ? createTime.getTime() : -1L);
        dest.writeLong(startTime != null ? startTime.getTime() : -1L);
        dest.writeLong(endTime != null ? endTime.getTime() : -1L);
        dest.writeString(summary);
        dest.writeString(description);
        dest.writeString(category);
        dest.writeString(eventClass);
        dest.writeString(location);
        dest.writeString(organizer);
    }
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}
