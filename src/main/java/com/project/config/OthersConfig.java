package com.project.config;

import cn.jpush.api.JPushClient;
import com.project.utils.OthersUtils;
import com.project.utils.PayUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.Properties;


/**
 * 其他全局对象的声明配置
 */
@Configuration
public class OthersConfig {
    //极光推送key
    public static String JPUSH_APP_KEY="4903776f21cfd4500575f126";
    //极光推送秘钥
    public static String MASTER_SECRET="6bb6c08b5964257aea24c9fb";
    //从连接池获取到连接的超时时间，如果是非连接池的话，该参数无效
    public static int CONNECTION_REQUEST_TIMEOUT=10*1000;
    //建立连接的超时时间
    public static int CONNECT_TIMEOUT=10*1000;
    //客户端和服务进行数据交互的超时时间
    public static int SOCKET_TIMEOUT=10*1000;
    //最大连接数
    public static int MAX_TOTAL=500;
    //连接池按route配置的最大连接数
    public static int DEFAULT_MAX_PER_ROUTE=200;
    /**
     * 自定义错误界面
     */
    @Bean
    public WebServerFactoryCustomizer containerCustomizer() {
        return (WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>) factory -> {
            ErrorPage page404 = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
            ErrorPage page500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/404.html");
            factory.addErrorPages(page404,page500);
        };
    }
    /**
     * mybatis通用mapper配置
     */
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        //配置tkMapper包扫描位置
        mapperScannerConfigurer.setBasePackage("com.project.dao");
        Properties properties = new Properties();
        properties.setProperty("mappers", "tk.mybatis.mapper.common.Mapper");
        properties.setProperty("notEmpty", "false");
        properties.setProperty("IDENTITY", "MYSQL");
        mapperScannerConfigurer.setProperties(properties);
        return mapperScannerConfigurer;
    }

    /**
     * http-client配置
     */
    @Bean("httpClient")
    public CloseableHttpClient httpClient(){
        //指定http-client请求参数
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
          .register("http", PlainConnectionSocketFactory.getSocketFactory())
          .register("https", SSLConnectionSocketFactory.getSocketFactory())
          .build();
        PoolingHttpClientConnectionManager gcm = new PoolingHttpClientConnectionManager(registry);
        gcm.setMaxTotal(MAX_TOTAL);
        gcm.setDefaultMaxPerRoute(DEFAULT_MAX_PER_ROUTE);
        RequestConfig requestConfig = RequestConfig.custom()
          .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
          .setConnectTimeout(CONNECT_TIMEOUT)
          .setSocketTimeout(SOCKET_TIMEOUT)
          .build();
        return HttpClients.custom().setConnectionManager(gcm).setDefaultRequestConfig(requestConfig).build();
    }
    /**
     * 极光推送客户端
     */
    @Bean
    public JPushClient jPushClient(){
        return new JPushClient(MASTER_SECRET, JPUSH_APP_KEY);
    }
}