package net.codetip.blog.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * @author 孟熙武
 *
 */
public class JsonUtils {
	private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);
	private static final ObjectMapper objectMapper;
	private static XmlMapper xmlMapper = new XmlMapper();

	static {
		objectMapper = new ObjectMapper();
		// 去掉默认的时间戳格式
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		// 设置为中国上海时区
		objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		// objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES,
		// false);
		// 空值不序列化
		// objectMapper.setSerializationInclusion(Include.NON_NULL);
		// 将空值序列化为空串
		objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
			@Override
			public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
					throws IOException, JsonProcessingException {
				gen.writeString("");
			}
		});
		// 反序列化时，属性不存在的兼容处理
		objectMapper.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		// 序列化时，日期的统一格式
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// 单引号处理
		objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
	}

	public static ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public static ObjectWriter getObjectWriter(Include inc, Class<?>[] cs, String... excludes)
			throws JsonProcessingException {
		// ObjectMapper mapper = new ObjectMapper();
		if (inc != null) {
			objectMapper.setSerializationInclusion(inc);
		}
		SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter.serializeAllExcept(excludes);
		FilterProvider filters = new SimpleFilterProvider().addFilter("ExcludesFilter", theFilter);
		for (int i = 0; cs != null && i < cs.length; i++) {
			objectMapper.addMixIn(cs[i], ExcludesFilteMixIn.class);
		}
		return objectMapper.writer(filters);
	}

	public static ObjectWriter getObjectWriter(Include inc, Class<?> c, String... excludes)
			throws JsonProcessingException {
		return getObjectWriter(inc, new Class[] { c }, excludes);
	}

	public static ObjectWriter getObjectWriter(Class<?> c, String... excludes) throws JsonProcessingException {
		return getObjectWriter(null, new Class[] { c }, excludes);
	}

	public static ObjectWriter getObjectWriter(Class<?>[] cs, String... excludes) throws JsonProcessingException {
		return getObjectWriter(null, cs, excludes);
	}

	/**
	 * 将对象序列化成JSON字符串
	 * 
	 * @param obj
	 * @param inc
	 *            Include.NON_NULL/NON_EMPTY/ALWAYS/....
	 * @param c
	 *            哪些class中的属性需要排除，只能是Bean中的属性，注意：Map中的key无法排除
	 * @param excluds
	 *            排除对象中的哪些属性
	 * @return
	 * @throws JsonProcessingException
	 */
	public static String write(Object obj, Include inc, Class<?> c, String... excludes) throws JsonProcessingException {
		return getObjectWriter(inc, c, excludes).writeValueAsString(obj);
	}

	public static String write(Object obj, Include inc, Class<?>[] cs, String... excludes)
			throws JsonProcessingException {
		return getObjectWriter(inc, cs, excludes).writeValueAsString(obj);
	}

	public static String write(Object obj, Class<?> c, String... excluds) throws JsonProcessingException {
		return write(obj, Include.ALWAYS, c, excluds);
	}

	public static String write(Object obj, Class<?>[] cs, String... excluds) throws JsonProcessingException {
		return write(obj, Include.ALWAYS, cs, excluds);
	}

	public static String write(Object obj) throws JsonProcessingException {
		return write(obj, new Class[] {});
	}

	public static <T> T jsonToBean(String json, Class<T> clazz) {
		try {
			return objectMapper.readValue(json, clazz);
		} catch (JsonParseException e) {
			logger.error(e.getMessage(), e);
		} catch (JsonMappingException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public static <T> String BeanToJson(T entity) {
		try {
			return objectMapper.writeValueAsString(entity);
		} catch (JsonGenerationException e) {
			logger.error(e.getMessage(), e);
		} catch (JsonMappingException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public static <T> T jsonToCollection(String json, TypeReference<T> typeReference) {
		try {
			return objectMapper.readValue(json, typeReference);
		} catch (JsonParseException e) {
			logger.error(e.getMessage(), e);
		} catch (JsonMappingException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * json string convert to map
	 */
	public static <T> Map<String, Object> jsonToMap(String jsonStr) throws Exception {
		return objectMapper.readValue(jsonStr, Map.class);
	}

	/**
	 * json string convert to map with javaBean
	 */
	public static <T> Map<String, T> jsonToMap(String jsonStr, Class<T> clazz) throws Exception {
		Map<String, Map<String, Object>> map = objectMapper.readValue(jsonStr, new TypeReference<Map<String, T>>() {
		});
		Map<String, T> result = new HashMap<String, T>();
		for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
			result.put(entry.getKey(), mapToBean(entry.getValue(), clazz));
		}
		return result;
	}

	/**
	 * json array string convert to list with javaBean
	 */
	public static <T> List<T> jsonToList(String jsonArrayStr, Class<T> clazz) throws Exception {
		List<Map<String, Object>> list = objectMapper.readValue(jsonArrayStr, new TypeReference<List<T>>() {
		});
		List<T> result = new ArrayList<T>();
		for (Map<String, Object> map : list) {
			result.add(mapToBean(map, clazz));
		}
		return result;
	}

	/**
	 * map convert to javaBean
	 */
	public static <T> T mapToBean(Map map, Class<T> clazz) {
		return objectMapper.convertValue(map, clazz);
	}

	public static <T> List<T> jsonTolist(String jsonArrayStr, Class<T> clazz) throws Exception {
		List<Map<String, Object>> list = objectMapper.readValue(jsonArrayStr, new TypeReference<List<T>>() {
		});
		List<T> result = new ArrayList<>();
		for (Map<String, Object> map : list) {
			result.add(mapToBean(map, clazz));
		}
		return result;
	}

	public static JsonNode toJsonNode(Object object) {

		try {
			return objectMapper.readTree(write(object));
		} catch (IOException e) {

			e.printStackTrace();
		}
		return null;

	}

	public static String jsonToXml(String jsonStr) throws Exception {
		JsonNode root = objectMapper.readTree(jsonStr);
		String xml = xmlMapper.writeValueAsString(root);
		return xml;
	}

	public static String jsonToXml(JsonNode root) throws Exception {
		String xml = xmlMapper.writeValueAsString(root);
		return xml;
	}

	public static String xmlToJson(String xml) {
		try {
			StringWriter w = new StringWriter();
			JsonParser jp = xmlMapper.getFactory().createParser(xml);
			JsonGenerator jg = objectMapper.getFactory().createGenerator(w);
			while (jp.nextToken() != null) {
				jg.copyCurrentEvent(jp);
			}
			jp.close();
			jg.close();
			return w.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "{}";
	}
}
