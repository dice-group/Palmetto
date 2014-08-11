package org.aksw.palmetto.webapp;

import java.io.IOException;
import java.io.InputStream;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Status;

public class SimpleResourceRetriever extends Restlet {

    @Override
    public void handle(Request request, Response response) {
        String resourceName = request.getResourceRef().getLastSegment();

        InputStream is = this.getClass().getClassLoader().getResourceAsStream(resourceName);
        if (is == null) {
            response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return;
        }
        byte buffer[] = new byte[1024];
        StringBuilder result = new StringBuilder();

        try {
            int length = is.read(buffer);
            while (length > 0) {
                result.append(new String(buffer, 0, length));
                length = is.read(buffer);
            }
        } catch (IOException e) {
            response.setStatus(Status.SERVER_ERROR_INTERNAL);
            return;
        }

        response.setEntity(result.toString(), getMediaType(resourceName));
    }

    private MediaType getMediaType(String resourceName) {
        int pos = resourceName.lastIndexOf('.');
        if (pos < 0) {
            return MediaType.TEXT_PLAIN;
        }
        switch (resourceName.substring(pos + 1)) {
        case "html":
        case "htm": {
            return MediaType.TEXT_HTML;
        }
        case "js": {
            return MediaType.TEXT_JAVASCRIPT;
        }
        case "jpg": {
            return MediaType.IMAGE_JPEG;
        }
        case "png": {
            return MediaType.IMAGE_PNG;
        }
        default: {
            return MediaType.TEXT_PLAIN;
        }
        }
    }
}
