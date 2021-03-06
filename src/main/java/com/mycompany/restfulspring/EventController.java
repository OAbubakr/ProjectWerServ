/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.restfulspring;

import bean.EventId;
import dto.Response;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import second.DaoInstance;
import second.EventDAO;

/**
 *
 * @author Rana Gamal
 */

@RestController
public class EventController {
   
    @RequestMapping(value= "/getEvents", method = RequestMethod.GET, headers = "Accept=application/json")
    public Response getEvents(){
        Response response = new Response();
        EventDAO dao = DaoInstance.getInstance().getEventDAO();
        ArrayList<EventId> eventIds = dao.getEvents();
        return response.createResponse(eventIds);
    }
}
