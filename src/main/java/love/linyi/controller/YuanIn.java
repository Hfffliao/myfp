package love.linyi.controller;

import love.linyi.domin.ShiJian;
import love.linyi.service.ShiJianService;
import love.linyi.service.impl.AcquisitionTimeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * 处理 /yuan 路径请求的控制器
 */
@Controller
@RequestMapping("/yuan")
public class YuanIn {
	@Autowired
	private ShiJianService shiJianService;

	@Autowired
	private AcquisitionTimeImpl acquisitionTime;


	ShiJian shijian;
	YuanIn() {
		shijian = new ShiJian();
	}
	@PostMapping
	@ResponseBody
	public String doPost(HttpServletRequest request, HttpServletResponse response) {
		String inputLine;
		try (BufferedReader reader = request.getReader()) {
			inputLine = reader.readLine();
		} catch (IOException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return "Error reading request";
		}

		String distanceStr = inputLine != null ? inputLine : "-1";
		try {
			int distance = Integer.parseInt(distanceStr);
			if (shiJianService.save(shijian)) {
				return "suc";
			}
		} catch (NumberFormatException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return "Invalid distance format";
		}

		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		return "Save failed";
	}

	@GetMapping
	@ResponseBody
	public String doGet(HttpServletRequest request, HttpServletResponse response) {
		return doPost(request, response);
	}
}
