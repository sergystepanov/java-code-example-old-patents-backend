package ru.ineureka.patents.office.org;

import org.jsoup.nodes.Document;

public final class Cleaner {

    public static void convertRelativeToAbsolute(Document doc) {
        for (var anchor : doc.select("a")) {
            anchor.attr("href", anchor.absUrl("href"));
        }

        for (var link : doc.select("link")) {
            link.attr("href", link.absUrl("href"));
        }

        for (var image : doc.select("img")) {
            image.attr("src", image.absUrl("src"));
        }
    }

    private Cleaner() {
    }
}
