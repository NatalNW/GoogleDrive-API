package com.googledrive.Controller;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.googledrive.Config.GoogleDriveConfig;

@Controller
public class IndexController {

	@RequestMapping(value = "/upload-image", method = RequestMethod.POST)
	public String uploadImages(Model model, @RequestParam(value = "files") MultipartFile[] files) throws IOException, GeneralSecurityException{

		for (MultipartFile file : files) {
			GoogleDriveConfig.uploadFile(file);
		}
		
		return "upload";
	}	
}
