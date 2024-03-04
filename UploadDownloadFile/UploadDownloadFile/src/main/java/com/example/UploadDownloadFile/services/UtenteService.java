package com.example.UploadDownloadFile.services;

import com.example.UploadDownloadFile.dto.DownloadProfilePictureDTO;
import com.example.UploadDownloadFile.entities.Utente;
import com.example.UploadDownloadFile.repositories.UtenteRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class UtenteService {
    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private FileStorageService fileStorageService;

    @SneakyThrows
    private Utente getUtente(Long id) {
        Optional<Utente> checkUtente = utenteRepository.findById(id);
        if (!checkUtente.isPresent()) throw new Exception("utente non trovato");
        return checkUtente.get();
    }

    @SneakyThrows
    public Utente uploadProfileImage(Long id, MultipartFile profilePicture) {
        Utente utente = getUtente(id);
        if (utente.getProfilePicture() != null) {
            fileStorageService.remove(utente.getProfilePicture());
        }
        String fileName = fileStorageService.upload(profilePicture);
        utente.setProfilePicture(fileName);
        return utenteRepository.saveAndFlush(utente);
    }

    @SneakyThrows
    public DownloadProfilePictureDTO downloadProfilePicture(Long id) {
        Utente utente = getUtente(id);

        DownloadProfilePictureDTO dto = new DownloadProfilePictureDTO();
        dto.setUtente(utente);
        if (utente.getProfilePicture() == null) return dto;

        byte[] profilePicBiytes = fileStorageService.download(utente.getProfilePicture());
        dto.setProfileImage(profilePicBiytes);
        return dto;
    }

    @SneakyThrows
    public void deleteById(Long id) {
        Utente utente = getUtente(id);
        if(utente.getProfilePicture() != null){
            fileStorageService.remove(utente.getProfilePicture());
        }
        utenteRepository.deleteById(id);
    }
}
