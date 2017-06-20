package com.xidian.resource.controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ImageController {
	private Logger logger = Logger.getLogger(getClass());

	private String server_url = "http://localhost:8080/resource/upload/pic";

	@RequestMapping(value = "/sample", method = { RequestMethod.POST })
	@ResponseBody
	public Object sample(
			@RequestParam(value = "file", required = false) MultipartFile file)
			throws IOException {
		logger.info("server_url:" + this.server_url);

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(this.server_url);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addBinaryBody("file", file.getInputStream(),
				ContentType.MULTIPART_FORM_DATA, file.getOriginalFilename());// 文件流
		builder.addTextBody("filename", file.getOriginalFilename());// 类似浏览器表单提交，对应input的name和value
		HttpEntity entity = builder.build();
		httpPost.setEntity(entity);
		HttpResponse response = httpClient.execute(httpPost);// 执行提交
		HttpEntity responseEntity = response.getEntity();
		logger.info("responseEntity:" + responseEntity.toString());
		// 将响应内容转换为字符串
		String result = EntityUtils.toString(responseEntity,
				Charset.forName("UTF-8"));
		logger.info("result:" + result);
		JSONObject jsonBean = JSONObject.fromObject(result);
		httpClient.close();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", jsonBean.get("id"));
		return map;
	}

	@RequestMapping(value = "/upload/pic", method = { RequestMethod.POST })
	@ResponseBody
	public Object uploadPic(
			@RequestParam(value = "file", required = false) MultipartFile file) {
		logger.info("file:" + file.getName());
		String id = UUID.randomUUID().toString();
		File pic = new File(id);
		try {
			file.transferTo(pic);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		return map;
	}
}