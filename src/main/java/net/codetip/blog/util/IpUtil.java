package net.codetip.blog.util;

import com.fasterxml.jackson.databind.JsonNode;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.*;

public interface IpUtil {
	public static String getIpAddr(HttpServletRequest request) {
		String ipAddress = null;
		try {
			ipAddress = request.getHeader("x-forwarded-for");
			if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
				ipAddress = request.getHeader("Proxy-Client-IP");
			}
			if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
				ipAddress = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
				ipAddress = request.getRemoteAddr();
				if (ipAddress.equals("127.0.0.1")) {
					// 根据网卡取本机配置的IP
					InetAddress inet = null;
					try {
						inet = InetAddress.getLocalHost();
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
					ipAddress = inet.getHostAddress();
				}
			}
			// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
			if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
																// = 15
				if (ipAddress.indexOf(",") > 0) {
					ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
				}
			}
		} catch (Exception e) {
			ipAddress = "";
		}
		// ipAddress = this.getRequest().getRemoteAddr();

		return ipAddress;
	}

	/**
	 * 
	 * 
	 * 
	 * { "code": 0,<br>
	 * "data": {<br>
	 * "ip": "171.221.142.61",<br>
	 * "country": "中国",<br>
	 * "area": "",<br>
	 * "region": "四川",<br>
	 * "city": "成都",<br>
	 * "county": "XX",<br>
	 * "isp": "电信",<br>
	 * "country_id": "CN",<br>
	 * "area_id": "",<br>
	 * "region_id": "510000",<br>
	 * "city_id": "510100",<br>
	 * "county_id": "xx",<br>
	 * "isp_id": "100017"<br>
	 * }<br>
	 * }<br>
	 * 
	 * 
	 * @param ip
	 * @return
	 */
	public static JsonNode getIpInfo(String ip) {
		String path = "http://ip.taobao.com/service/getIpInfo.php?ip=" + ip;
		// String inputline = "";
		// String info = "";
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10 * 1000);
			conn.setRequestMethod("GET");

			// InputStreamReader inStream = new
			// InputStreamReader(conn.getInputStream(), "UTF-8");
			// BufferedReader buffer = new BufferedReader(inStream);
			//
			// while ((inputline = buffer.readLine()) != null) {
			// info += inputline;
			// }
			return JsonUtils.getObjectMapper().readTree(conn.getInputStream());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return JsonUtils.getObjectMapper().createObjectNode();
	}

	public static String getIpInfo(HttpServletRequest request) {
		String ipAddr = getIpAddr(request);
		JsonNode ipInfo = getIpInfo(ipAddr);
		if (ipInfo != null) {
			if (ipInfo.get("code").asInt() == 0) {
				JsonNode jsonNode = ipInfo.get("data");
				StringBuilder info = new StringBuilder();
				info.append(jsonNode.get("country").asText());
				info.append("-");
				info.append(jsonNode.get("region").asText());
				info.append("-");
				info.append(jsonNode.get("city").asText());
				return info.toString();
			}
		}
		return "null";
	}
}
