package ru.ineureka.patents.service;

import com.google.common.flogger.FluentLogger;
import org.springframework.stereotype.Service;
import ru.ineureka.patents.auth.JwtAuth;
import ru.ineureka.patents.config.JwtConfig;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Locale;

@Service
public class Sys {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private final JwtConfig jwtConfig;

    public Sys(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @PostConstruct
    void info() {
        if (jwtConfig.isGenOnStart()) printNewKeyMaybe();
    }

    /**
     * Supplies the default encoding without using Charset.defaultCharset()
     * and without accessing System.getProperty("file.encoding").
     *
     * @return Default encoding (default charset).
     */
    public static String getEncoding() {
        final byte[] bytes = {'D'};

        try (InputStream inputStream = new ByteArrayInputStream(bytes);
             InputStreamReader reader = new InputStreamReader(inputStream)) {
            return reader.getEncoding();
        } catch (IOException e) {
            logger.atSevere().withCause(e).log("Encoding detection in stream error");
            return "Undefined";
        }
    }

    public static void printEncodings() {
        logger.atInfo().log("\n" +
                        "+----------------------------------------------------------------------------------------+\n" +
                        "|                                    ENCODING INFO                                       |\n" +
                        "+----------------------------------------------------------------------------------------+\n" +
                        "| Default Locale | Default Charset | file.encoding | sun.jnu.encoding | Default encoding |\n" +
                        "| %-14s | %-15s | %-13s | %-16s | %-16s |\n" +
                        "+ ---------------------------------------------------------------------------------------+",
                Locale.getDefault(), Charset.defaultCharset(), System.getProperty("file.encoding"),
                System.getProperty("sun.jnu.encoding"), getEncoding()
        );
    }

    public void printNewKeyMaybe() {
        logger.atInfo().log("\n" +
                        "+------------------------------------------------------------------------------------------+\n" +
                        "|                                     RANDOM KEY                                           |\n" +
                        "+------------------------------------------------------------------------------------------+\n" +
                        "| %s |\n" +
                        "+ -----------------------------------------------------------------------------------------+",
                JwtAuth.showMeAKey());
    }
}
