package ru.ineureka.patents.office.org.fips;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.StackSize;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import ru.ineureka.patents.office.org.Cleaner;
import ru.ineureka.patents.office.org.fips.cache.FipsLogo;

import java.io.ByteArrayInputStream;

public final class FipsDocumentCleaner {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    /**
     * Cleans input HTML:
     * - removes <script>, <noscript> blocks form the source, which blocks PDF generation.
     * - embeds the logo because it's often hold HTTP requests.
     *
     * @param data An array of bytes of HTML data.
     * @return The clean resulting bytes of HTML.
     * @since 3.0.0
     */
    public static byte[] clean(byte[] data) {
        try {
            final var doc = Jsoup.parse(new ByteArrayInputStream(data), Fips.ENCODING, Fips.URL);
            doc.outputSettings(new Document.OutputSettings().prettyPrint(false));
            doc.select("script, noscript").remove();

            Cleaner.convertRelativeToAbsolute(doc);

            final Element logo = doc.select("img[src~=RFP_LOGO\\.gif$]").first();
            if (logo != null) logo.attr("src", FipsLogo.data);

            final Element style = doc.select("style").first();
            if (style != null) {
                style.html(style.html().replace("http://www.fips.ru/cdfi/Fonts/WipoUni.ttf", "WipoUni.ttf"));
            }

            return doc.outerHtml().getBytes(Fips.ENCODING);
        } catch (Exception e) {
            logger.atSevere().withStackTrace(StackSize.FULL).log(e.getMessage());
        }

        return data;
    }

    private FipsDocumentCleaner() {
    }
}
