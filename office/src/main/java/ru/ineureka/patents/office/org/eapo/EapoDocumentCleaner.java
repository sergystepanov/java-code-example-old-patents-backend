package ru.ineureka.patents.office.org.eapo;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.StackSize;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.ineureka.patents.office.org.Cleaner;

import java.io.ByteArrayInputStream;

public final class EapoDocumentCleaner {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    /**
     * Cleans input HTML:
     * - makes absolute paths for images and styles.
     * - removes <script> blocks form the source, which blocks PDF generation.
     *
     * @param data An array of bytes of HTML data.
     * @return The clean resulting bytes of HTML.
     * @since 3.0.0
     */
    public static byte[] clean(byte[] data) {
        try {
            var doc = Jsoup.parse(new ByteArrayInputStream(data), Eapo.ENCODING, Eapo.URL);
            doc.outputSettings(new Document.OutputSettings().prettyPrint(false));
            doc.select("script").remove();

            Cleaner.convertRelativeToAbsolute(doc);

            return doc.outerHtml().getBytes(Eapo.ENCODING);
        } catch (Exception e) {
            logger.atSevere().withStackTrace(StackSize.FULL).log(e.getMessage());
        }

        return data;
    }
}
