package com.xidian.resource.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.xidian.common.RsaHelper;

@Controller
public class ImageController {
	private Logger logger = Logger.getLogger(getClass());

	@Value("#{config[upload_password]}")
	private String upload_password = "";

	@Value("#{config[private_key_path]}")
	private String private_key_path = "";

	@Value("#{config[image_store_dir]}")
	private String image_store_dir = "";

	@RequestMapping(value = "/upload/pic", method = { RequestMethod.POST })
	@ResponseBody
	public Object uploadPic(HttpServletRequest request,
			@RequestParam(value = "file", required = true) MultipartFile file,
			@RequestParam(value = "password", required = true) String password)
			throws FileNotFoundException, IOException, ClassNotFoundException,
			InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException {
		Map<String, Object> map = new HashMap<String, Object>();
		logger.info("file:" + file.getName());
		RsaHelper rsaHelper = new RsaHelper();
		ObjectInputStream privateKeyOis = new ObjectInputStream(this.getClass()
				.getResourceAsStream(this.private_key_path));
		Key privateKey = (Key) privateKeyOis.readObject();
		privateKeyOis.close();
		String target = rsaHelper.decrypt(password, privateKey);
		if (!this.upload_password.equals(target)) {
			map.put("id", -1);
			return map;
		}
		String id = UUID.randomUUID().toString().replaceAll("-", "");
		String path = request.getSession().getServletContext()
				.getRealPath(this.image_store_dir);
		logger.info("image..." + path.toString());
		File pic = new File(String.format("%s%s%s", path, File.separator, id));
		try {
			file.transferTo(pic);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		map.put("id", id);
		return map;
	}
}