package com.example.UploadDownloadFile.services;

import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileStorageService {
    @Value("${fileRepositoryFolder}")
    public String fileRepositoryFolder;

    @SneakyThrows //throws new exception
    public String upload(MultipartFile file) {
        //bisogna preservare l'estensione del file
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        //serve per rinominare il file che viene caricato perché
        // se due persone dovessero caricare file con lo stesso nome,
        // il sistema andrebbe a sovrascrivere quella già esistente
        String newFileName = UUID.randomUUID().toString();
        String completeFileName = newFileName + "." + extension;

        File finalFolder = new File(fileRepositoryFolder);
        if (!finalFolder.exists()) throw new IOException("final folder does not exists");
        if (!finalFolder.isDirectory()) throw new IOException("final folder is not a directory");
        File finalDestination = new File(fileRepositoryFolder + "\\" + completeFileName);
        if (finalDestination.exists()) throw new IOException("file conflict");

        file.transferTo(finalDestination);
        return completeFileName;
    }

    public byte[] download(String fileName) throws IOException {
        File fileFromRepo = new File(fileRepositoryFolder + "\\" + fileName);
        if(!fileFromRepo.exists()) throw new IOException("file does not exists");
       return IOUtils.toByteArray(new FileInputStream(fileFromRepo));
    }

    @SneakyThrows
    public void remove(String fileName) {
        File fileFromRepo = new File(fileRepositoryFolder + "\\" + fileName);
        if(!fileFromRepo.exists()) return;
        boolean deleteResult = fileFromRepo.delete();
       if(deleteResult == false) throw new Exception("impossibile eliminare il file");
    }
}
