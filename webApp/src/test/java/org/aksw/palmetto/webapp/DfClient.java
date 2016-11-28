package org.aksw.palmetto.webapp;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;

import org.apache.commons.io.Charsets;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

// Ignored until there is a good way to use the index for tests in the CI
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = org.aksw.palmetto.webapp.config.RootConfig.class)
public class DfClient {

    @Autowired
    private PalmettoApplication controller;

    @Test
    public void testSucces() throws Exception {
        ResponseEntity<byte[]> response = controller.requestDocFreq("dog cat");
        ByteBuffer buffer = ByteBuffer.wrap(response.getBody());

        int length, docId;
        length = buffer.getInt();
        Assert.assertTrue(length > 0);
        Assert.assertTrue(length <= buffer.remaining());
        for (int i = 0; i < length; ++i) {
            docId = buffer.getInt();
            Assert.assertTrue(docId >= 0);
        }
        length = buffer.getInt();
        Assert.assertTrue(length > 0);
        Assert.assertTrue(length <= buffer.remaining());
        for (int i = 0; i < length; ++i) {
            docId = buffer.getInt();
            Assert.assertTrue(docId >= 0);
        }
        Assert.assertEquals(0, buffer.remaining());
    }

    @Test
    public void testUnderscore() throws Exception {
        ResponseEntity<byte[]> response = controller.requestDocFreq("dog foundation_year cat");
        ByteBuffer buffer = ByteBuffer.wrap(response.getBody());

        int length, docId;
        length = buffer.getInt();
        Assert.assertTrue(length > 0);
        Assert.assertTrue(length <= buffer.remaining());
        for (int i = 0; i < length; ++i) {
            docId = buffer.getInt();
            Assert.assertTrue(docId >= 0);
        }
        // dog_ should create an empty result
        length = buffer.getInt();
        Assert.assertEquals(0, length);

        length = buffer.getInt();
        Assert.assertTrue(length > 0);
        Assert.assertTrue(length <= buffer.remaining());
        for (int i = 0; i < length; ++i) {
            docId = buffer.getInt();
            Assert.assertTrue(docId >= 0);
        }
        Assert.assertEquals(0, buffer.remaining());
    }

    @Test
    public void testFail() throws Exception {
        ResponseEntity<byte[]> response = controller.requestDocFreq("unknownword123");
        ByteBuffer buffer = ByteBuffer.wrap(response.getBody());

        int length = buffer.getInt();
        Assert.assertEquals(0, length);
        Assert.assertEquals(0, buffer.remaining());
    }

    public static void main(String[] args) throws Exception {
        String url = "http://palmetto.aksw.org/palmetto-webapp/service/df";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // Send post request
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        OutputStream out = con.getOutputStream();
        out.write("words=cat foundation_year dog".getBytes(Charsets.UTF_8));
        out.flush();
        out.close();

        System.out.println("Sending 'POST' request to URL : " + url);
        System.out.println("Response Code : " + con.getResponseCode());

        InputStream is = new BufferedInputStream(con.getInputStream());
        ByteBuffer buffer;
        byte bytes[];
        int length;
        while (is.available() > 0) {
            bytes = new byte[4];
            is.read(bytes);
            buffer = ByteBuffer.wrap(bytes);
            length = buffer.getInt();
            System.out.println(length);
            if (length > 0) {
                buffer = ByteBuffer.allocate(length * 4);
                is.read(buffer.array());
            }
        }
        is.close();
    }
}
