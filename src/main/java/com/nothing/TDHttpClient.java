package com.nothing;

import com.nothing.pojo.Position;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.*;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * Created by zyan.zhang on 2015/3/20.
 * 12306的http客户端服务
 */
public class TDHttpClient {
    String DOMAIN_NAME = "kyfw.12306.cn";
    String imageUrl = "https://kyfw.12306.cn/otn/passcodeNew/getPassCodeNew?module=login&rand=sjrand&0.394159632967785";
    String checkCodeUrl = "https://kyfw.12306.cn/otn/passcodeNew/checkRandCodeAnsyn";
    Map<String, String> cookieMap = new LinkedHashMap<String, String>();

    CookieStore cookieStore;
    HttpClient httpClient;

    public HttpClient buildClient() {
        SSLContext ctx = null;

        //          HttpClient        httpClient = new DefaultHttpClient() ;
        try {
            //            ctx = SSLContext.getInstance("SSL");
            ctx = SSLContext.getInstance("TLS");

            //Implementation of a trust manager for X509 certificates
            X509TrustManager tm = new X509TrustManager() {

                public void checkClientTrusted(X509Certificate[] xcs,
                        String string) throws CertificateException {

                }

                public void checkServerTrusted(X509Certificate[] xcs,
                        String string) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[] { tm }, null);

            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(ctx);

            cookieStore = new BasicCookieStore();
            // Populate cookies if needed
            //            BasicClientCookie cookie = new BasicClientCookie("name", "value");
            //            cookie.setDomain("kyfw.12306.cn");
            //            cookie.setPath("/");
            //            cookieStore.addCookie(cookie);

            httpClient = HttpClients.custom()
                    .setSSLSocketFactory(sslsf)
                    .setDefaultCookieStore(cookieStore)
                    .build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        return httpClient;
    }


    public void checkCode(Position... positions) throws IOException, URISyntaxException {

        StringBuilder randCode = new StringBuilder();
        for (Position position : positions) {
            randCode.append(position.getCoordinateX()).append(",").append(position.getCoordinateY()).append(",");
        }
        if (randCode.length() > 1) {
            randCode.deleteCharAt(randCode.length() - 1);
        }

        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("randCode", randCode.toString()));
        formparams.add(new BasicNameValuePair("rand", "sjrand"));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
        HttpPost post = new HttpPost(checkCodeUrl);
        post.setHeader("Accept", "image/webp,*/*;q=0.8");
        post.setEntity(entity);
        System.out.println("Post request" + post + ",entity:" + entity + ",formparam:" + formparams);

        HttpResponse postResponse = httpClient.execute(post);

        System.out.println(EntityUtils.toString(postResponse.getEntity(), "UTF-8"));
        EntityUtils.consume(postResponse.getEntity());
    }

    // 生成图片函数
    public void downloadImage(String fileURL) throws IOException {
        buildClient();

        BufferedOutputStream out = null;
        try {
            HttpGet httpget = new HttpGet(imageUrl);

            RequestConfig localConfig = RequestConfig.custom()
                    .setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY)
                    .build();
            httpget.setConfig(localConfig);

            HttpResponse response = httpClient.execute(httpget);
            changeCookies(response.getAllHeaders());
            //            System.out.println(response + " response headers------------------------------");
            Header[] headers = response.getAllHeaders();
            for (Header header : headers) {
                System.out.println(header.getName() + ":" + header.getValue());
            }

            // 生成图片
            File file = new File(fileURL);
            if (!file.exists()) {
                file.createNewFile();
            }
            out = new BufferedOutputStream(
                    new FileOutputStream(file));

            response.getEntity().writeTo(out);
            EntityUtils.consume(response.getEntity());

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }


    /**
     * 初始化登录用户Cookie数据
     *
     * @return
     */
    public Map<String, String> initCookie() {
        try {
            HttpGet httpGet = new HttpGet("https://kyfw.12306.cn/otn/login/init");
            httpGet.setHeader("Connection", "keep-alive");
            HttpClient httpClient = buildClient();
            HttpResponse response = httpClient.execute(httpGet);
            // 获取消息头的信息
            changeCookies(response.getAllHeaders());
            EntityUtils.consume(response.getEntity());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return cookieMap;
        }
    }

    public void changeCookies(Header[] headers) {
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].getName().equals("Set-Cookie")) {
                String cookie = headers[i].getValue();
                String cookieName = cookie.split("=")[0];
                String cookieValue = cookie.split("=")[1].split(";")[0];
                cookieMap.put(cookieName, cookieValue);
            }
        }
    }

    public String getCookies() {
        StringBuilder sb = new StringBuilder();
        // key=value;key=value
        for (Map.Entry<String, String> entry : cookieMap.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append(";");
        }
        if (sb.length() > 1) {
            sb.deleteCharAt(sb.length() - 1);
        }
        System.out.println("获取新cookies:" + sb.toString());
        return sb.toString();
    }


    public void printRequestCookies(AbstractHttpClient httpclient) {
        List<Cookie> cookies = ((AbstractHttpClient) httpclient).getCookieStore().getCookies();
        for (Cookie cookie : cookies)
            //System.out.println(cookie.getName() + "=" + cookie.getValue() + ";");
            System.out.println(cookie);
        // 除了HttpClient自带的Cookie，自己还可以增加自定义的Cookie
        // 增加代码...

    }

    public void printCookies() {
        List<Cookie> cookies = cookieStore.getCookies();
        for (Cookie cookie : cookies) {
            System.out.println(cookie);
        }
    }
}
