/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.util.ArrayList;

/**
 *
 * @author salma
 */
public class Course {
    
    private int courseId;
    private String courseName;
    ArrayList<TrackInstructor> trackInstructors = new ArrayList<>();

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public ArrayList<TrackInstructor> getTrackInstructors() {
        return trackInstructors;
    }

    public void setTrackInstructors(ArrayList<TrackInstructor> trackInstructors) {
        this.trackInstructors = trackInstructors;
    }
    
}
