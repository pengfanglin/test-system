package com.project.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

/**
 * 和JSON相关的配置（包含@ResponseBody序列化规则）
 */
@Configuration
public class JSONConfig {
	/**
	 * jackson对象声明
	 */
	@Bean
	public ObjectMapper objectMapper(){
		ObjectMapper objectMapper=new ObjectMapper();
		// 转换为格式化的json
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		//如果字段类型为空，不报错
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		//如果json中的字段在实体类中未出现，不报错
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return objectMapper;
	}
	/**
	 * 配置springMvc的默认序列化规则，对null对象做特殊处理
	 */
	@Bean
	public MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		//获取springMvc默认的objectMapper
		ObjectMapper objectMapper = converter.getObjectMapper();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		objectMapper.setDateFormat(sdf);
		// 为mapper注册一个带有SerializerModifier的Factory，针对值为null的字段进行特殊处理
		objectMapper.setSerializerFactory(objectMapper.getSerializerFactory().withSerializerModifier(new MyBeanSerializerModifier()));
		return converter;
	}
}

/**
 *自定义序列化处理器
 */
class MyBeanSerializerModifier extends BeanSerializerModifier {
	@Override
	public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
		//循环所有的beanPropertyWriter
		for (BeanPropertyWriter writer : beanProperties) {
			//字段类型
			Class<?> clazz=writer.getMember().getRawType();
			if (clazz.isArray() || clazz.equals(List.class) || clazz.equals(Set.class)) {
				//如果是array，list，set则序列化为[]
				writer.assignNullSerializer(new JsonSerializer<Object>(){
					@Override
					public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException{
						if (value == null) {
							jsonGenerator.writeStartArray();
							jsonGenerator.writeEndArray();
						} else {
							jsonGenerator.writeObject(value);
						}
					}
				});
			}else if(clazz.getSimpleName().equals("Object")){
				//如果是Object类型则序列化为{}
				writer.assignNullSerializer(new JsonSerializer<Object>(){
					@Override
					public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException{
						if (value == null) {
							jsonGenerator.writeStartObject();
							jsonGenerator.writeEndObject();
						} else {
							jsonGenerator.writeObject(value);
						}
					}
				});
			}else{
				//不符合上述规则的全部序列化{}
				writer.assignNullSerializer(new JsonSerializer<Object>(){
					@Override
					public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException{
						jsonGenerator.writeObject(value);
					}
				});
			}
		}
		return beanProperties;
	}
}
