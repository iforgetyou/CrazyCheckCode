package com.nothing;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class HttpClientTest {
    static String imageUrl = "https://kyfw.12306.cn/otn/passcodeNew/getPassCodeNew?module=login&rand=rrrand";
    static String localUrl = "d:/12306/1.jpeg";

    public  void main(String args[]) throws IOException {
        BufferedOutputStream out = null;
        try {

            HttpClient httpclient = new DefaultHttpClient();
            //Secure Protocol implementation.
            SSLContext ctx = SSLContext.getInstance("SSL");
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
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);

            ClientConnectionManager ccm = httpclient.getConnectionManager();
            //register https protocol in httpClient's scheme registry
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", 443, ssf));

            HttpGet httpget = new HttpGet(imageUrl);
            HttpParams params = httpclient.getParams();

            //            params.setParameter("param1", "paramValue1");

            httpget.setParams(params);
            System.out.println("REQUEST:" + httpget.getURI());

            HttpResponse response = httpclient.execute(httpget);
            Header[] headers = response.getAllHeaders();
            for (Header header : headers) {
                System.out.println(header.getName() + ":" + header.getValue());
            }

            // 生成图片
            File file = new File(localUrl);
            if (!file.exists()){
                file.createNewFile();
            }
            out = new BufferedOutputStream(
                    new FileOutputStream(file));

            HttpEntity entity = response.getEntity();
            entity.writeTo(out);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
}


