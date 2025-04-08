package com.ecom.service.impl;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.ecom.service.FileService;

@Service
public class FileServiceImpl implements FileService {

	@Autowired
	private Cloudinary cloudinary;

	@Value("${cloudinary.folder.category:agribucket-category}")
	private String categoryBucket;

	@Value("${cloudinary.folder.product:agriproduct}")
	private String productBucket;

	@Value("${cloudinary.folder.profile:agristore-profile}")
	private String profileBucket;

	@Override
	public Boolean uploadFileS3(MultipartFile file, Integer bucketType) {
		String bucketName = null;
		try {
			if (file == null || file.isEmpty()) {
				return false;
			}

			if (bucketType == 1) {
				bucketName = categoryBucket;
			} else if (bucketType == 2) {
				bucketName = productBucket;
			} else {
				bucketName = profileBucket;
			}

			String fileName = file.getOriginalFilename();
			String uniqueFileName = UUID.randomUUID().toString();

			// Upload to Cloudinary
			Map uploadResult = cloudinary.uploader().upload(
					file.getBytes(),
					com.cloudinary.utils.ObjectUtils.asMap(
							"folder", bucketName,
							"public_id", uniqueFileName,
							"resource_type", "auto"));

			if (!ObjectUtils.isEmpty(uploadResult)) {
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
