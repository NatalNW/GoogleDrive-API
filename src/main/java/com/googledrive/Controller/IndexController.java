package com.googledrive.Controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.services.drive.model.File;
import com.googledrive.Config.GoogleDriveConfig;

@RestController
public class IndexController {

	// private JSONObject obj;

	@RequestMapping(value = "/upload-image", method = RequestMethod.POST)
	public File uploadImage(Model model, @RequestParam(value = "image") MultipartFile image) throws IOException, GeneralSecurityException {
		File img = GoogleDriveConfig.uploadImage(image);

		return img;
	}

	@RequestMapping(value = "/upload-images", method = RequestMethod.POST)
	public String uploadImages(Model model, @RequestParam(value = "images") MultipartFile[] images) throws IOException, GeneralSecurityException {
		List<File> imgs = new ArrayList<>();
		// obj = new JSONObject();

		for (MultipartFile image : images) {
			imgs.add(GoogleDriveConfig.uploadImage(image));
		}

		// for (File img : imgs) {}

		return "";
	}

	@RequestMapping(value = "/files", method = RequestMethod.GET)
	public List<File> listFiles() throws IOException, GeneralSecurityException {
		List<File> files = GoogleDriveConfig.listFiles();

		return files;
	}
}
