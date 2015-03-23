package com.nothing;

import com.nothing.pojo.Position;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

public class TDHttpClientTest {
    TDHttpClient client = new TDHttpClient();

    @Test
    public void testDownLoadImage() throws IOException, InterruptedException {
        String localDir = "d:/12306/";

        TDHttpClient client = new TDHttpClient();
        while (true) {
            String localUrl = localDir + System.currentTimeMillis() + ".jpeg";
            client.downloadImage(localUrl);
            Thread.sleep(1000);
            System.out.println("休息一会");
        }
    }

    @Test
    public void getImage() throws IOException, URISyntaxException, InterruptedException {

        String localDir = "d:/12306/";
        String localUrl = localDir + System.currentTimeMillis() + ".jpeg";
        //        client.initCookie();
        client.downloadImage(localUrl);


        client.checkCode(
                new Position(1, 2),
                new Position(4, 2)
        );
        client.checkCode(
                new Position(2, 1),
                new Position(3, 2)
        );
    }

    @Test
    public void testPosition() {
        Position position = new Position(1, 1);
        System.out.println(position.getCoordinateX() + ":" + position.getCoordinateY());
    }


}