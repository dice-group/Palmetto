package org.aksw.palmetto.webapp;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

// Ignored until there is a good way to use the index for tests in the CI
//@Ignore
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
        ResponseEntity<byte[]> response = controller.requestDocFreq("dog dog_ cat");
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

}
