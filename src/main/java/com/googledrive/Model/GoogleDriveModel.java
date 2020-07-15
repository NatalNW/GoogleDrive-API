package com.googledrive.Model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import com.googledrive.Config.GoogleDriveConfig;

import org.springframework.web.multipart.MultipartFile;

public class GoogleDriveModel {
    
    private static Drive service;    

    private static java.io.File convert(MultipartFile file) throws IOException {
		java.io.File convFile = new java.io.File(file.getOriginalFilename());
		convFile.createNewFile();

		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();

		return convFile;
	}

	public static File uploadImage(MultipartFile file) throws IOException, GeneralSecurityException {
		java.io.File image = convert(file);
		service = GoogleDriveConfig.getService();

		File fileMetadata = new File();
		fileMetadata.setName(image.getName());
		fileMetadata.setMimeType("image/*");

		java.io.File filePath = new java.io.File(image.getAbsolutePath());
		FileContent mediaContent = new FileContent("image/*", filePath);
		File imgFile = service.files().create(fileMetadata, mediaContent).setFields("id").execute();

		return imgFile;
	}

	public static Permission createPublicPermission(String googleFileId) throws IOException, GeneralSecurityException {
		// All values: user - group - domain - anyone
		String permissionType = "anyone";
		// All values: organizer - owner - writer - commenter - reader
		String permissionRole = "reader";

		Permission newPermission = new Permission();
		newPermission.setType(permissionType);
		newPermission.setRole(permissionRole);

        service = GoogleDriveConfig.getService();
        
		return service.permissions().create(googleFileId, newPermission).execute();
	}

	public static List<File> listFiles() throws IOException, GeneralSecurityException {
		service = GoogleDriveConfig.getService();
		FileList result = service.files().list().setPageSize(10).setFields("nextPageToken, files(id, name)").execute();
		List<File> files = result.getFiles();

		return files;
	}
}