package net.codetip.blog.util;

import net.codetip.blog.dto.GeetestConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Optional;

/**
 * 
 * @author jsonXxxx 极验第二次验证工具类
 */
public class Verification {

	public final static int SUCCESS = 1;
	public final static int FAIL = 0;

	public static int verification(HttpServletRequest request, HttpServletResponse response) {
		GeetestLib gtSdk = new GeetestLib(GeetestConfig.getGeetest_id(), GeetestConfig.getGeetest_key(),
				GeetestConfig.isnewfailback());

		String challenge = request.getParameter(GeetestLib.fn_geetest_challenge);
		String validate = request.getParameter(GeetestLib.fn_geetest_validate);
		String seccode = request.getParameter(GeetestLib.fn_geetest_seccode);

		// 从session中获取gt-server状态

		Optional<Object> status = Optional.ofNullable(request).map(HttpServletRequest::getSession)
				.map(session -> session.getAttribute(gtSdk.gtServerStatusSessionKey));

		int gt_server_status_code = status.isPresent() ? (int) status.get() : 0;

		// int gt_server_status_code = (Integer)
		// request.getSession().getAttribute(gtSdk.gtServerStatusSessionKey);

		// 从session中获取userid
		String userid = (String) request.getSession().getAttribute("userid");

		// 自定义参数,可选择添加
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("user_id", userid); // 网站用户id
		param.put("ip_address", IpUtil.getIpAddr(request)); // 传输用户请求验证时所携带的IP

		int gtResult = 0;

		if (gt_server_status_code == 1) {
			// gt-server正常，向gt-server进行二次验证
			gtResult = gtSdk.enhencedValidateRequest(challenge, validate, seccode, param);
		} else {
			// gt-server非正常情况下，进行failback模式验证
			gtResult = gtSdk.failbackValidateRequest(challenge, validate, seccode);
		}
		return gtResult;
	}
}
