package com.project.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bean.others.LocationBean;

import java.util.HashMap;
import java.util.Map;

/**
 * 高德地图
 */
public class GaoDeUtils {
	public static LocationBean addressToLocation(String address) {
		LocationBean locationBean = null;
		Map<String, Object> params = new HashMap<>();
		params.put("key", "4a4775afef857b08ca7e3ed2407a91ba");
		params.put("address", address);
		try {
			String result = HttpUtils.get("http://restapi.amap.com/v3/geocode/geo", params);
			JsonNode jsonNode = new ObjectMapper().readTree(result);
			if (jsonNode.findValue("status").textValue().equals("1")) {
				String location = jsonNode.findValue("location").textValue();
				if (location != null && !location.equals("")) {
					locationBean = new LocationBean().setLongitude(location.split(",")[0]).setLatitude(location.split(",")[1]);
					if(locationBean.getLongitude()==null&&locationBean.getLatitude()==null){
						throw new RuntimeException();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("地址无效，请检查地址是否有误!");
		}
		return locationBean;
	}
}
