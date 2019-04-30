package com.example.Main.Controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.Main.Model.GoogleDriveModel;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.model.File;

@Controller
public class IndexController {
	//private GoogleDriveModel upload = new GoogleDriveModel();

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String uploadImages(Model model, @RequestParam(value = "files") MultipartFile[] files) {
	//	System.out.println("To Aqui");
		for (MultipartFile file : files) {
			try {
				File fileMetadata = new File();
	        	fileMetadata.setName(file.getOriginalFilename());
	        	fileMetadata.setMimeType("image/*");
	        	java.io.File filePath = this.convert(file);
	        	FileContent mediaContent = new FileContent("image/*", filePath);
	        	GoogleDriveModel.getService().files().create(fileMetadata, mediaContent).setFields("id").execute();
	        	filePath.delete();
			} catch (IOException | GeneralSecurityException e) {
				e.printStackTrace();
			}
		}
		model.addAttribute("msg", "Successfully uploaded file");
		return "upload";
	}
	
	   private java.io.File convert(MultipartFile file) throws IOException {
	    	java.io.File convFile = new java.io.File(file.getOriginalFilename());
	        convFile.createNewFile();
	        FileOutputStream fos = new FileOutputStream(convFile);
	        fos.write(file.getBytes());
	        fos.close();
	        return convFile;
	    }
	
}
