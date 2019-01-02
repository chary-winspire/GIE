package com.example.extarc.androidpushnotification.Models;

public class StudentNotifications {

    private int id;
    private String StudentorUserID;
    private String NotificationType;
    private String NotificationSID;
    private String Fcmtoken;
    private String IsRead;
    private String Remarks;
    private String Comment;
    private String CreatedBy;
    private String CreatedDate;
    private String LastModifiedBy;
    private String LastModifiedDate;
    private int AndroidVersion;
    private String DeviceID;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudentorUserID() {
        return StudentorUserID;
    }

    public void setStudentorUserID(String studentorUserID) {
        StudentorUserID = studentorUserID;
    }

    public String getNotificationType() {
        return NotificationType;
    }

    public void setNotificationType(String notificationType) {
        NotificationType = notificationType;
    }

    public String getNotificationSID() {
        return NotificationSID;
    }

    public void setNotificationSID(String notificationSID) {
        NotificationSID = notificationSID;
    }

    public String getFcmtoken() {
        return Fcmtoken;
    }

    public void setFcmtoken(String fcmtoken) {
        Fcmtoken = fcmtoken;
    }

    public String getIsRead() {
        return IsRead;
    }

    public void setIsRead(String isRead) {
        IsRead = isRead;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getLastModifiedBy() {
        return LastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        LastModifiedBy = lastModifiedBy;
    }

    public String getLastModifiedDate() {
        return LastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        LastModifiedDate = lastModifiedDate;
    }

    public int getAndroidVersion() {
        return AndroidVersion;
    }

    public void setAndroidVersion(int androidVersion) {
        AndroidVersion = androidVersion;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }
}
