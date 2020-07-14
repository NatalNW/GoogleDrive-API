package com.googledrive.api.Config;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

//import org.springframework.web.multipart.MultipartFile;

//import javax.swing.JFileChooser;

public class GoogleDriveModel {
	private static final String APPLICATION_NAME = "ProjectLostPets";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final String TOKENS_DIRECTORY_PATH = "tokens";
	private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
	private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
	private static Drive service;

	private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
		// Load client secrets.
		InputStream in = GoogleDriveModel.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES)
						.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
						.setAccessType("offline").build();
		LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
		return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
	}

	public static void uploadFile(MultipartFile file) throws IOException, GeneralSecurityException {
		java.io.File image = convert(file);
		Drive service = getService();
		File fileMetadata = new File();
		fileMetadata.setName(image.getName());
		fileMetadata.setMimeType("image/*");
		java.io.File filePath = new java.io.File(image.getAbsolutePath());
		FileContent mediaContent = new FileContent("image/*", filePath);
		File imgFile = service.files().create(fileMetadata, mediaContent).setFields("id").execute();
		
		createPublicPermission(imgFile.getId());
		
		System.out.println("IMG File ID: " + imgFile.getPermissionIds());
		System.out.println("IMG File ID: " + imgFile.getPermissions());
	//	System.out.println("IMG File ID: " + imgFile.get);
		System.out.println("IMG File ID: " + imgFile.getShared());
		System.out.println("IMG File ID: " + imgFile.getId());
	}

//	public static void listFiles(Drive service) throws IOException {
//		// Print the names and IDs for up to 10 files.
//		FileList result = service.files().list().setPageSize(10).setFields("nextPageToken, files(id, name)").execute();
//		List<File> files = result.getFiles();
//		if (files == null || files.isEmpty()) {
//			System.out.println("No files found.");
//		} else {
//			System.out.println("Files:");
//			for (File file : files) {
//				System.out.printf("%s (%s)\n", file.getName(), file.getId());
//			}
//		}

//	}
	
	 public static Permission createPublicPermission(String googleFileId) throws IOException, GeneralSecurityException {
	        // All values: user - group - domain - anyone
	        String permissionType = "anyone";
	        // All values: organizer - owner - writer - commenter - reader
	        String permissionRole = "reader";
	 
	        Permission newPermission = new Permission();
	        newPermission.setType(permissionType);
	        newPermission.setRole(permissionRole);
	 
	        Drive driveService = getService();
	        return driveService.permissions().create(googleFileId, newPermission).execute();
	    }

	private static java.io.File convert(MultipartFile file) throws IOException {
		java.io.File convFile = new java.io.File(file.getOriginalFilename());
		convFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	public static Drive getService() throws GeneralSecurityException, IOException {
		// Build a new authorized API client service.
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

		return service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
				.setApplicationName(APPLICATION_NAME).build();
	}

//	public static void main(String... args) throws IOException, GeneralSecurityException {
//
//		JFileChooser fc = new JFileChooser();
//		int res = fc.showOpenDialog(null);
//
//		if (res == JFileChooser.APPROVE_OPTION) {
//			java.io.File arquivo = fc.getSelectedFile();
//			UploadFile(arquivo, getService());
//		} else
//			System.out.println("Voce nao selecionou o arquivo.");
//
//		listFiles(getService());
//	}
}
