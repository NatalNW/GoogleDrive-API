package com.example.Main.Controller;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.Main.Model.GoogleDriveModel;

@Controller
public class IndexController {
	//private GoogleDriveModel upload = new GoogleDriveModel();

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String uploadImages(Model model, @RequestParam(value = "files") MultipartFile[] files) throws IOException, GeneralSecurityException{
	//	System.out.println("To Aqui");

		for (MultipartFile file : files) {
			GoogleDriveModel.uploadFile(file);
		}
		
		model.addAttribute("msg", "Successfully uploaded file");
		return "upload";
	}	
}
