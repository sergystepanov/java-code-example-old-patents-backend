package ru.ineureka.patents.office.org.fips;

import ru.ineureka.patents.property.Inid;

import java.util.regex.Pattern;

final class FipsParsingPattern {
    static final String DATE_PATTERN = "\\d{2}\\.\\d{2}\\.\\d{4}";
    static final Pattern DOCUMENT_MARKER = Pattern.compile("\\(" + Inid.PUBLISHING_ORGANIZATION_CODE + "\\)<");
    static final Pattern DOCUMENT_ERROR = Pattern.compile("(?:<p>)?([^<>]+)(?:</p>)?");
    static final Pattern DOCUMENT_ERROR2 = Pattern.compile("<title>Ошибка</title>.*[\\s]*.*[\\s].*?p><p>(.*)</p><p");
    static final Pattern PATENT_NUMBER = Pattern.compile("<.*? id=\"top4\".*?>[\\r\\n]*(?:<a.*?>)?([\\d ]+)(?:</a)>");
    static final Pattern PROPERTY_NUMBER_FROM_APPLICATION = Pattern.compile(">Публикация № ?<b><.*?>(.*?)</");
    static final Pattern PUB_ORG = Pattern.compile("<.*? id=\"top2\".*?>([A-Z]*)<");
    static final Pattern STATUS = Pattern.compile("<td id=\"StatusR\">(.*)[\\t\\r\\n ]*\\(.*?: (.*)\\)");
    static final Pattern FEE = Pattern.compile("><td id=\"StatusR\">.*[\\t\\r\\n ]*\\(.*?: .*\\)[\\t\\r\\n]*(?:<br>)(учтена за (\\d{1,2}) год с (" + DATE_PATTERN + ") по (" + DATE_PATTERN + "))<");
    static final Pattern PCT_DATE = Pattern.compile("\\(" + Inid.PCT_DATE + "\\) .*[\\s ]*.*?(?:<[ab].*?>)(" + DATE_PATTERN + ")(?:</[ab]>)");
    static final Pattern PCT_APP = Pattern.compile("\\(" + Inid.PCT_APPLICATION + "\\) .*[\\s ]*.*?(?:<[ab].*?>)(.*?)\\((" + DATE_PATTERN + ")\\)[\\s ]*(?:</[ab]>)");
    static final Pattern PCT_PUB = Pattern.compile("\\(" + Inid.PCT_PUBLICATION_DATA + "\\) .*[\\s ]*[brpnum<> \\s]*(.*?)</pnum> \\((.*)\\)[\\s <]*");
    static final Pattern START_DATE = Pattern.compile("\\(" + Inid.RIGHTS_START_DATE + "\\).*?:.?(?:<br>)*[\\r\\n\\t ]*<b>(" + DATE_PATTERN + ")<");
    static final Pattern PUB_DATE = Pattern.compile("\\(" + Inid.PUBLICATION_DATE + "\\) .*[\\s ]*.*?(?:<[ab].*?>)(" + DATE_PATTERN + ")(?:</[ab]>)");
    static final Pattern APP_DATA = Pattern.compile("\\(" + Inid.APPLICATION_NUMBER + "\\).?\\(" + Inid.APPLICATION_DATE + "\\) Заявка: <b>(?:<a.*?>)?([\\d/]+)(?:</a>)?, (" + DATE_PATTERN + ")</b>");
    static final Pattern APP_DATA_D_APP_NUM = Pattern.compile(">\\(" + Inid.APPLICATION_NUMBER + "\\) .*?: <b>(?:<a.*?>)*([\\d/]+)<");
    static final Pattern APP_DATA_D_APP_DATE = Pattern.compile("\\(" + Inid.APPLICATION_DATE + "\\) .*?:[\\r\\n\\t ]*<b>(?:<a.*?>)*(" + DATE_PATTERN + ")<");
    static final Pattern PROP_CASE1 = Pattern.compile("\\(" + Inid.OWNERS_NAMES + "\\).*?:\\s*<br?>\\s*(.*)<");
    static final Pattern PROP_CASE2 = Pattern.compile("\\(" + Inid.APPLICANTS_NAMES + "\\).*?:[\\r\\n\\t ]*(?:<br>)?[\\r\\n\\t ]*<b>[\\r\\n\\t ]*(.*)<");
    static final Pattern PROP_CASE3 = Pattern.compile("Приобретатель исключительного права:[\\s ]*<b>(.*)<");
    static final Pattern PROP_LIST_SPLIT = Pattern.compile("\\)\\s*[,$]", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    static final Pattern CORRECTIONS = Pattern.compile("<p\\sclass=\"(?:Start|First)Izv\">[\\s ]*?ИЗВЕЩЕНИЯ");
    // Catastrophic backtracking without (?!ФАКСИМИЛЬНЫЕ)
    static final Pattern CORRECTIONS_TEXT = Pattern.compile("(?:<p\\sclass=\"(?:First|Start)Izv\">\\s*(?!ФАКСИМИЛЬНЫЕ).*?\\s*</p>)\\s*([\\S\\s]*?)</div>");
    static final Pattern OL = Pattern.compile("QA(4A|1K) Регистрация открытой лицензии");
    static final Pattern OL_D = Pattern.compile("Дата регистрации открытой лицензии:\\s*<b>(" + DATE_PATTERN + ")</b>");
    static final Pattern CONVENT = Pattern.compile("\\(30\\).*?: ?.*\\s*<[br]+>\\s*((?:<[br]+>" + DATE_PATTERN + "\\s*[() \\r\\n\\t.0-9\\-а-яА-Я]*\\s*[A-Z]{2}[\\s ]*[A-Z.0-9/,\\- ]+)+)</[br]+>");
    static final Pattern PARENT_APP_DATE_NUM = Pattern.compile("\\(" + Inid.PARENT_APP + "\\).*[\\s\\t]*<b><a.*?href=\"(.*?)\" .*?>(.*?)</a> ?(" + DATE_PATTERN + ")</b>");

    private FipsParsingPattern() {
    }
}
