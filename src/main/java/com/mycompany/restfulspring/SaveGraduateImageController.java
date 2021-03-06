package com.mycompany.restfulspring;

import bean.FileInfo;
import bean.ReturnMessage;
import com.google.gson.Gson;
import dto.Response;
import java.io.File;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import second.DaoInstance;
import second.SaveGraduateImageDao;

@RestController
public class SaveGraduateImageController {

    @Autowired
    ServletContext context;

    /**
     *
     * @param inputFile
     * @param id
     * @return
     */
    @RequestMapping(value = "{id}/fileuploadgrad", headers = ("content-type=multipart/*"), method = RequestMethod.POST)
    public Response uploadAuthorized(Integer myid, HttpServletRequest request,
            HttpServletResponse response, @RequestParam("file") MultipartFile inputFile, @PathVariable int id) { //,@RequestParam("id") int id
        System.out.println("id is " + id);
        Gson gson = new Gson();
        ReturnMessage returnMessage = new ReturnMessage();
        FileInfo fileInfo = new FileInfo();
        HttpHeaders headers = new HttpHeaders();
        if (!inputFile.isEmpty()) {
            try {
                String originalFilename = inputFile.getOriginalFilename();
                File destinationFile = new File(request.getServletContext().getRealPath("/WEB-INF/uploaded")
                        + File.separator + originalFilename);
                inputFile.transferTo(destinationFile);
                fileInfo.setFileName(destinationFile.getPath());
                System.out.println(destinationFile.getPath());

                fileInfo.setFileSize(inputFile.getSize());
                headers.add("File Uploaded Successfully - ", id + originalFilename);
                SaveGraduateImageDao saveGraduateImageDao = DaoInstance.getInstance().getSaveGraduateImageDao();
                System.out.println("path is "+destinationFile.getPath());
                String path = destinationFile.getPath();
                String imageName = path.substring(path.lastIndexOf("\\") + 1);
                System.out.println("image name is ********** " + imageName);
                String s = saveGraduateImageDao.insertImage(id, imageName);

                returnMessage.setMessage(s);
                return new Response().createResponse(s);
            } catch (IOException | IllegalStateException e) {
                return new Response().createResponse(null);
            }
        }
        return new Response().createResponse(null);
    }
}
