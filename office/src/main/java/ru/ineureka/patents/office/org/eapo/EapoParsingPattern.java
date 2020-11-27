package ru.ineureka.patents.office.org.eapo;

import ru.ineureka.patents.property.Inid;

import java.util.regex.Pattern;

public final class EapoParsingPattern {
    static final Pattern PROPERTY_NUMBER = Pattern.compile(">\\(" + Inid.PATENT_NUMBER + "\\).*?</td><td.*?>(\\d*)<");
    static final Pattern PROPERTY_PUBLICATION_DATE = Pattern.compile(">\\(" + Inid.PUBLICATION_DATE + "\\).*?</td><td.*?>.*? (\\d{4}.\\d{2}.\\d{2})");
    static final Pattern PAYMENTS = Pattern.compile(
            "<tr>\\s*" +
                    "<td.*?>(.*?)</td>\\s*" + // Год
                    "<td.*?>(.*?)</td>\\s*" + // Период
                    "<td class=\"?c0\"?>(.*?)</td>\\s*" + // AM
                    "<td class=\"?c0\"?>(.*?)</td>\\s*" + // AZ
                    "<td class=\"?c0\"?>(.*?)</td>\\s*" + // BY
                    "<td class=\"?c0\"?>(.*?)</td>\\s*" + // KG
                    "<td class=\"?c0\"?>(.*?)</td>\\s*" + // KZ
                    "<td class=\"?c0\"?>(.*?)</td>\\s*" + // RU
                    "<td class=\"?c0\"?>(.*?)</td>\\s*" + // TJ
                    "<td class=\"?c0\"?>(.*?)</td>\\s*" + // TM
                    "<"
    );
}
