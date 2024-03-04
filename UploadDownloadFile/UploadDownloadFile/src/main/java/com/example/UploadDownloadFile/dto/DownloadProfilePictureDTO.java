package com.example.UploadDownloadFile.dto;

import com.example.UploadDownloadFile.entities.Utente;
import lombok.Data;

@Data

public class DownloadProfilePictureDTO {
    private Utente utente;
    private byte[] profileImage;

}
