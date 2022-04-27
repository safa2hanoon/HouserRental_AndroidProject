package edu.berziet.houserental.services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FIleStoreService {
	@Autowired
	ServletContext context;

	public String store(MultipartFile file) {
		Path absolutePath = Paths.get("uploads");
		try {
			if (file.isEmpty()) {
				return "";
			}
			// find extension of the file,png or jpg
			String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());

			// generate a random unique name for the image
			String uploadedFileName = UUID.randomUUID().toString() + "." + extension;
			Files.copy(file.getInputStream(), absolutePath.resolve(uploadedFileName));

			return uploadedFileName;
		} catch (IOException e) {
			e.printStackTrace();
			return "Failed to store file.";
		}
	}

	public Resource load(String filename) {
		try {
			Path root = Paths.get("uploads");
			Path file = root.resolve(filename);
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Could not read the file!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}

	public void deleteImage(String fileName) {
		try {
			Path root = Paths.get("uploads");
			Path file = root.resolve(fileName);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists()) {
				resource.getFile().delete();
			} else {
				throw new RuntimeException("Could not read the file!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		} catch (IOException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}

	}
}
