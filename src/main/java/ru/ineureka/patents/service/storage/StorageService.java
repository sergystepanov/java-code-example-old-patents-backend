package ru.ineureka.patents.service.storage;

import com.google.common.flogger.FluentLogger;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

@Service
public class StorageService {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private static MessageDigest sha1;

    static {
        try {
            sha1 = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            logger.atSevere().log("There's no SHA1 support");
            throw new RuntimeException();
        }
    }

    /**
     * Calculates the SHA-1 stream data checksum.
     *
     * @param in The file to read.
     * @return The hex value of the SHA-1 in uppercase.
     * @throws IOException If an I/O error occurs.
     */
    public String getHash(InputStream in) throws IOException {
        byte[] buffer = new byte[4096];
        int len = in.read(buffer);

        while (len != -1) {
            sha1.update(buffer, 0, len);
            len = in.read(buffer);
        }

        String hexValue;
        try (var formatter = new Formatter()) {
            for (var hex : sha1.digest()) {
                formatter.format("%02x", hex);
            }
            hexValue = formatter.toString();
        }

        return hexValue;
    }

    /**
     * Calculates the SHA-1 stream data checksum.
     * <p>
     * Reads input string as UTF-8 bytes.
     *
     * @param text The text to read.
     * @return The hex value of the SHA-1 in uppercase.
     * @throws IOException If an I/O error occurs.
     */
    public String getHash(String text) throws IOException {
        return getHash(new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8)));
    }
}
