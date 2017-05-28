/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.restfulspring;

import bean.StudentDataByTrackID;
import java.util.ArrayList;
import javax.ws.rs.QueryParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import second.AllStudentByTrackDao;
import second.DaoInstance;

/**
 *
 * @author admin
 */
@RestController
public class StudentsDataByTrackIdController {
    
@RequestMapping(value="/getStudentsByTrackId", method = RequestMethod.GET ,headers ="Accept=application/json") 
public ArrayList<StudentDataByTrackID>getStudents(@QueryParam("id")int id){
    AllStudentByTrackDao dao = DaoInstance.getInstance().getAllStudentByTrackDao();
    return  dao.getAllStudentsDataByTrackId(id);
} 


   
    
    
}