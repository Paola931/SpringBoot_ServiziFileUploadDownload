package com.example.UploadDownloadFile.controllers;

import com.example.UploadDownloadFile.services.FileStorageService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/download")
    public @ResponseBody byte[] download(@RequestParam String fileName, HttpServletResponse response) throws IOException {
        System.out.println("Downloading "+ fileName);
        String extension = FilenameUtils.getExtension(fileName);
        //lo switch in questo caso fa parte del controller
        //permette il riconoscimento e consecutivamente il download del file
        switch (extension){
            case "gif":
                response.setContentType(MediaType.IMAGE_GIF_VALUE);
                break;
            case "jpg":
            case "jpeg":
                response.setContentType(MediaType.IMAGE_JPEG_VALUE);
                break;
            case "png":
                response.setContentType(MediaType.IMAGE_PNG_VALUE);
                break;
        }
        response.setHeader("Content-Disposition","attachment; filename=\"" + fileName + "\"");
        return fileStorageService.download(fileName);
    }

    //anche se questo metodo prende in considerazione solo un file,
    // Java lo tratta come un array di un singolo elemento, basta aggiungere: []
    @PostMapping("/uploadSingleFile")
    public String upload(@RequestParam MultipartFile file) {
       return fileStorageService.upload(file);
    }

    @PostMapping("/uploadFiles")
    public ArrayList<String> uploadMoreFile(@RequestParam MultipartFile[] files) {
       ArrayList<String> fileNames = new ArrayList<>();
        for(MultipartFile file : files){
            String singleUploadedFileName = fileStorageService.upload(file);
            fileNames.add(singleUploadedFileName);
        }
        return fileNames;
    }
}
