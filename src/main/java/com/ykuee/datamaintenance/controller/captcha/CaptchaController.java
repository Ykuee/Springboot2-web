package com.ykuee.datamaintenance.controller.captcha;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;

import cn.hutool.core.img.ImgUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 图片验证码
 */
@Api(tags = "验证码生成")
@RestController
@RequestMapping("/captcha")
public class CaptchaController {
	@Autowired
	private Producer captchaProducer;

	@Autowired
	private Producer captchaProducerMath;

	/**
	 * 
	  *<p>Title: getKaptchaImage</p>
	  *<p>Description: "验证码生成 默认abcd</p>
	  * @author Ykuee
	  * @date 2021-3-4 
	  * @param request
	  * @param response
	  * @param type
	  * @return
	 */
	@ApiOperation(value = "验证码生成", notes = "验证码生成")
	@GetMapping("/captchaImage")
	public Map<String,String> getKaptchaImage(HttpServletRequest request, HttpServletResponse response, String type) {
		HttpSession session = request.getSession();
		response.setDateHeader("Expires", 0);
		// Set standard HTTP/1.1 no-cache headers.
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		// Set standard HTTP/1.0 no-cache header.
		response.setHeader("Pragma", "no-cache");
		// return a jpeg
		response.setContentType("image/jpeg");
		/// 验证码字符串
		String captStr = null;
		// 答案
		String answer = null;
		BufferedImage images = null;
		if ("math".equals(type)) {
			// 验证码为算数 8*9 类型
			// 验证码加答案8-3=?@5
			String captText = captchaProducerMath.createText();
			// 验证码8-3=?
			captStr = captText.substring(0, captText.lastIndexOf("@"));
			answer = captText.substring(captText.lastIndexOf("@") + 1);
			// 生成图片
			images = captchaProducerMath.createImage(captStr);
		} else {
			// 默认验证码为 abcd类型
			captStr = answer = captchaProducer.createText();
			images = captchaProducer.createImage(captStr);
		}
		String imagesBase64 = ImgUtil.toBase64(images, ImgUtil.IMAGE_TYPE_JPEG);
		session.setAttribute(Constants.KAPTCHA_SESSION_KEY, answer);
		Map<String,String> resMap = new HashMap<String, String>();
		resMap.put("imagesBase64", imagesBase64);
		return resMap;
	}


	/**
	 * 
	  *<p>Title: isVerify</p>
	  *<p>Description: 滚动条验证码</p>
	  * @author Ykuee
	  * @date 2021-3-4 
	  * @param datas
	  * @return
	 */
	@ApiOperation(value = "滚动条验证码", notes = "滚动条验证码")
	@PostMapping("/isVerify")
	@ResponseBody
	public boolean isVerify(@RequestBody List<Integer> datas) {
		int sum = 0;
		for (Integer data : datas) {
			sum += data;
		}
		double avg = sum * 1.0 / datas.size();

		double sum2 = 0.0;
		for (Integer data : datas) {
			sum2 += Math.pow(data - avg, 2);
		}

		double stddev = sum2 / datas.size();
		return stddev != 0;
	}

}