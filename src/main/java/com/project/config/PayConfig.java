package com.project.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.project.utils.ConfigUtils;
import com.project.utils.OthersUtils;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

/**
 * 第三方支付配置
 */
@Configuration
public class PayConfig {
  //支付宝网关
  public static String URL="https://openapi.alipay.com/gateway.do";
  //APPID即创建应用后生成
  public static String APP_ID="2017091308703804";
  //开发者应用私钥，由开发者自己生成
  public static String APP_PRIVATE_KEY="MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDWmpsAzw5+P6++7Z4WAVZAyOf6yOa43+PrdQSwcza8UOa45QFfptRn9g6klGazpM+Say68WPEPLZKHfn5Odo+EHxQYZbTztOHM7zBYzaHr/QYnvfzlysGlWuJw8tRjXzFvovONlJkfA6QnRdUuiW4EiCmOIWFufrZ0vyoJoaCuNOUgB+dTLG32Lw/DHFJu7uOw5Jl/M2izZN1LHlHBBsv7KfPWrCmCYt+9IEUwnf7EtffWBcmenTVn0hZSv7/Hpl+yLQ4s3RNCHxsc/Oh6YQ1lwFk3LAY3dLDH734ZnuEMJilp9pu1CFccZuYMWZ6EWapHu5RCHDl0RzJ2Zbw/iUq/AgMBAAECggEBAKYvvb8HmSKdMPy6yAQkbuXmbvafI0ShZL/Os53s6sM0Jyehd8nZyHzlZ/t9THbrNy8cv2ltuudVFDbG9wrN91+KLaajBFkBhyaLR7ZNL4ovO9gE4VYWCJWDZv2unliBvCfGWbMXEuiYikM7adYK6N6rjvqY0PmfOK3555TjzCl6Mp4PaX1GGMFCQ8COUcfumwUnQ8ZcPH/7VJIv2qLdTyCACPDPTFtsNnsLWzCTNhBQ8Lu6WE7Ia60taJaNwsz6hW5G/Zx3QWEM59mHBk9XexrSsgAGcuJLCOTH4mrzzH6dy1OUam62AujzZNTPRfwl3Yuuj0HqRS7StlOzTEyFi2kCgYEA/XwOog6gorGRHSbh2ngWvn3AlTMcHnANR73G0b7xQD3cn7R2uN8gmzd/EGD+b6OGVQG6YWfbPA7MpjySWYh+51jLXzSiHGQ/b8jcxaf3pcD4UsNOLmtTJb2enrm9piBaxzMO17q6Ag28p/Gl/ALip4Z0vtup2bcI1nIc69gNRssCgYEA2LvG/e3Agtx9HV25k5xEoHtrCPHiheZjR2taAgZU6V3RpErYCkLmp51dGQxDV5rl/rjHIcU/p2QaXhgSfJLkVEtBbmLQ0bq/z3QH7KzdDo0E2sLGvaY9OEurjovo650OusJUTWMbmrK8wYvEiLUWnfF0Ityzf+abWq2M+hIOWV0CgYEAsgv+d0QQ/tX8mN5jy8GIMDtCF0p0GMkZ+udJDLCy4i3Rlvf6YDaRv5TGkmmerinNRE5XbO23J0M56hYnse3XPYa4KuJcb9bWXB1ZPRiTMYEOq4t1kdNe7uZ20QSkOlkFil/JsCv/VUvy1IA+13nrXEJsJFdEwaD3+zDiMaJrCOsCgYBZ/AywnSfSrUzdBm7oVHdB0jwsaJFOWstZeb7THGHgym0iM1jjWcd+TB29KpRDKTLYwUscQFMfYrZ33rK+OhbG0MAH3ssoaMr+jQRFRbg6NF6RnVD6qKy+VNyTwI8iVAErlUd/IejyH8ey7B7bS6RqSzeWyd9U+SADVA82PIzsmQKBgQCKJRKgufdf2TQly9+z4C/7M0kYuA9eZ9Y7+ElCrlO9Gu1kQFRY+Apq2o2fsvM/7jQwPXdMyq8Ek3wcia+U0Jux/DgEdGFqiQ8uO+u6ILFiGexWiQVUjr6lF6yXsWYmG0p7gj+CuhDZ6AOF2nOk5u04QGP02dOzdPLiUOra38XWog==";
  //参数返回格式，只支持json
  public static String FORMAT="json";
  //请求和签名使用的字符编码格式，支持GBK和UTF-8
  public static String CHARSET="UTF-8";
  //支付宝公钥，由支付宝生成
  public static String ALIPAY_PUBLIC_KEY="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhc407Wydnnes1MRqNb7JCUXCwxfIMvRyGDwT6MGrEDb0ZZAjgFiZO/IzV+vwpkttgbrYUaxjdrsVSir0O+q/dywndDo/hCEEPcxdKqyoGxIazRgGYIZqRLSOfNMI14Tt0WN08Uwqs8SvqE3e4xLZmiW+oIT5NGmInNkKyVVDckhO1hENydAj3szb4YTSrxUa12+Tbgkt1cgxKoILI7uXQwdcps9c/goQdSd0v1FFyuifO1Z2mcbYr+KP3agHS6JuP1JDmDAAY6XAtHHnm+5lS2tGiQ8iHdwNoowNGv5oaRAtRIJBQnccG2bv0ItnLtIFdnS8qI8C+UircJbJpcRLEQIDAQAB";
  //商户生成签名字符串所使用的签名算法类型，目前支持RSA2和RSA，推荐使用RSA2
  public static String SIGN_TYPE="RSA2";
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
   * 支付宝请求客户端
   */
  @Bean
  public AlipayClient getAlipayClient(){
    return new DefaultAlipayClient(URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
  }
  /**
   * http-client配置
   */
  @Bean("wechatHttpClient")
  public CloseableHttpClient wechatHttpClient() throws Exception{
    //指定证书类型
    KeyStore wechatKeyStore = KeyStore.getInstance("PKCS12");
    // 读取本机存放的PKCS12证书文件
    // 读取本机存放的PKCS12证书文件
    ClassPathResource classPathResource=new ClassPathResource("wechat_cert.p12");
    InputStream wechatInstream = classPathResource.getInputStream();
    // 加载微信支付商户证书
    wechatKeyStore.load(wechatInstream, ConfigUtils.wx_merchants_id.toCharArray());
    //根据证书生成SSL上下文
    SSLContext sslcontext = SSLContexts.custom()
      .loadKeyMaterial(wechatKeyStore, ConfigUtils.wx_merchants_id.toCharArray())
      .build();
    SSLConnectionSocketFactory sslConnectionSocketFactory =new SSLConnectionSocketFactory(sslcontext, NoopHostnameVerifier.INSTANCE);
    //指定http-client请求参数
    Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
      .register("http", PlainConnectionSocketFactory.getSocketFactory())
      .register("https", sslConnectionSocketFactory)
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
}
