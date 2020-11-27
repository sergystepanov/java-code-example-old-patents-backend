package ru.ineureka.patents.property;

/**
 * INID codes according to ST.9 WIPO standard (June 2013).
 *
 * @since 1.0.0
 */
public class Inid {
    // (10) Identification of the patent, SPC or patent document
    // (11) Number of the patent, SPC or patent document
    public static final int PATENT_NUMBER = 11;
    // (19) WIPO Standard ST.3 code, or other identification, of the office or organization publishing the document
    // Notes: (i) For an SPC, data regarding the basic patent should be coded by using code (68).
    // (ii) ** Minimum data element for patent documents only.
    // (iii) With the proviso that when data coded (11) and (13), or (19), (11) and (13), are used together and
    // on a single line, category (10) can be used, if so desired.
    // (iv) Data to be given under code (15) should be presented in accordance with the provisions set out in
    // WIPO Standard ST.50.
    public static final int PUBLISHING_ORGANIZATION_CODE = 19;

    // (20) Data concerning the application for a patent or SPC
    // (21) Number(s) assigned to the application(s), e.g., “Numéro d’enregistrement national”, “Aktenzeichen”
    public static final int APPLICATION_NUMBER = 21;
    // (22) Date(s) of filing the application(s)
    public static final int APPLICATION_DATE = 22;
    // (24) Date from which industrial property rights may have effect
    public static final int RIGHTS_START_DATE = 24;

    // (30) Data relating to priority under the Paris Convention or the Agreement on Trade-Related Aspects
    // of Intellectual
    // Property Rights (TRIPS Agreement)
    public static final int PRIORITIES = 30;
    // (33) WIPO Standard ST.3 code identifying the national industrial property office allotting the priority
    // application number or the organization allotting the regional priority application number; for international
    // applications filed under the PCT, the code “WO” is to be used
    public static final int NATIONAL_OFFICE = 33;

    // (40) Date(s) of making available to the public
    // (45) Date of making available to the public by printing or similar process of a patent document on which grant
    // has taken place on or before the said date
    public static final int PUBLICATION_DATE = 45;

    // (60) References to other legally or procedurally related domestic or previously domestic patent documents
    // including unpublished applications therefor
    // (62) Number and, if possible, filing date of the earlier application from which the present patent document has
    // been divided up
    public static final int PARENT_APP = 62;

    // (70) Identification of parties concerned with the patent or SPC
    // (71) Name(s) of applicant(s)
    public static final int APPLICANTS_NAMES = 71;
    // (72) Name(s) of inventor(s) if known to be such
    public static final int INVENTORS_NAMES = 72;
    // (73) Name(s) of grantee(s), holder(s), assignee(s) or owner(s)
    public static final int OWNERS_NAMES = 73;

    // (80) (90) Identification of data related to International Conventions other than the Paris Convention, and to
    // legislation with respect to SPCs
    // (85) Date of commencement of the national phase pursuant to PCT Article 23(1) or 40(1)
    public static final int PCT_DATE = 85;
    // (86) Filing data of the PCT international application, i.e., international filing date, international application
    // number, and, optionally, the language in which the published international application was originally filed;
    // or, in the case of design patents, registration data of the Hague Agreement international application, i.e.,
    // international registration date and international registration number
    public static final int PCT_APPLICATION = 86;
    // (87) Publication data of the PCT international application, i.e., international publication date, international
    // publication number, and, optionally, the language in which the international application is published
    public static final int PCT_PUBLICATION_DATA = 87;

    private Inid() {
    }
}
