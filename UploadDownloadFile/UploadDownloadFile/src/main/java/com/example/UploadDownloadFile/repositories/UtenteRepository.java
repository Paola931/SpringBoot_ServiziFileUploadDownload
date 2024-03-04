package com.example.UploadDownloadFile.repositories;

import com.example.UploadDownloadFile.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtenteRepository extends JpaRepository<Utente,Long> {
}
