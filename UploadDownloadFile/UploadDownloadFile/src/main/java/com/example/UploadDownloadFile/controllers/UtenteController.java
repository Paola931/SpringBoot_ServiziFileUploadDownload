package com.example.UploadDownloadFile.controllers;

import com.example.UploadDownloadFile.dto.DownloadProfilePictureDTO;
import com.example.UploadDownloadFile.entities.Utente;
import com.example.UploadDownloadFile.repositories.UtenteRepository;
import com.example.UploadDownloadFile.services.UtenteService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/utenti")
public class UtenteController {
    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private UtenteService utenteService;

    @PostMapping("/create")
    public Utente create(@RequestBody Utente utente) {
        return utenteRepository.saveAndFlush(utente);
    }

    @SneakyThrows
    @PostMapping("/uploadProfilePicture/{id}")
    public Utente uploadProfileImage(@PathVariable Long id, MultipartFile profilePicture) {

        return utenteService.uploadProfileImage(id, profilePicture);
    }

    @PutMapping("/update/{id}")
    public void update(@PathVariable Long id, @RequestBody Utente utente) {
        utenteRepository.saveAndFlush(utente);
    }

    @SneakyThrows
    @GetMapping("/downloadProfilePicture/{id}")
    public @ResponseBody byte[] getProfileImage(@PathVariable Long id, HttpServletResponse response) {
        DownloadProfilePictureDTO downloadProfileImageDTO = utenteService.downloadProfilePicture(id);
        String fileName = downloadProfileImageDTO.getUtente().getProfilePicture();
        if (fileName == null) throw new Exception("utente non ha immagine profilo");
        String extension = FilenameUtils.getExtension(fileName);

        switch (extension) {
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
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        return downloadProfileImageDTO.getProfileImage();

    }

    @GetMapping("/getAll")
    public List<Utente> getAll() {
        return utenteRepository.findAll();
    }

    @GetMapping("/getSingle/{id}")
    public Optional<Utente> getSingle(@PathVariable Long id) {
        return utenteRepository.findById(id);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        utenteService.deleteById(id);
        //elimina anche immagine profilo
    }
}
