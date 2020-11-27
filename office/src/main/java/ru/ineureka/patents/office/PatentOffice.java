package ru.ineureka.patents.office;

import com.google.common.flogger.FluentLogger;
import ru.ineureka.patents.office.dto.PatentExtendedDto;
import ru.ineureka.patents.office.exception.OfficeError;
import ru.ineureka.patents.office.exception.PatentOfficeException;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An abstract class for parsing patent office's documents.
 *
 * @since 3.0.0
 */
public abstract class PatentOffice {
    protected final String text;
    protected DateTimeFormatter documentDateFormat;

    public PatentOffice(byte[] bytes) throws PatentOfficeException {
        setDocumentDateFormat();
        try {
            this.text = new String(bytes, getEncoding());
        } catch (UnsupportedEncodingException e) {
            throw new PatentOfficeException(OfficeError.ENCODING, "Кодировка документа не поддерживается.");
        }
    }

    /**
     * Checks if a document has all the necessary data for processing.
     *
     * @return True if document is ok.
     * @throws PatentOfficeException In case of any exceptions.
     * @since 1.0.0
     */
    protected abstract boolean isDocumentAlrightAlrightAlright() throws PatentOfficeException;

    protected abstract void setDocumentDateFormat();

    protected LocalDate toDateFormat(String date) {
        return Objects.isNull(date) || date.isBlank() ? null : LocalDate.parse(date, documentDateFormat);
    }

    public abstract PatentExtendedDto getPatent() throws PatentOfficeException;

    public String getText() {
        return text;
    }

    /**
     * Returns default encoding of the document.
     *
     * @since 3.0.0
     */
    protected String getEncoding() {
        return "UTF-8";
    }

    private Matcher getMatcher(String pattern, int flags) {
        return Pattern.compile(pattern, flags).matcher(text);
    }

    protected Matcher getMatcherI(String pattern) {
        return getMatcher(pattern, Pattern.CASE_INSENSITIVE);
    }

    protected abstract FluentLogger getLogger();

    protected String logInid(int number, String value) {
        getLogger().atFine().log("INID %n [%s]", number, value);

        return value;
    }

    protected String logValue(String name, String value) {
        getLogger().atFine().log("VALUE %s [%s]", name, value);

        return value;
    }

    protected <T extends Optional<?>> T logChunk(String name, T object) {
        getLogger().atFine().log("Search result for %s is [%s]", name, object.map(Object::toString).orElse("-"));

        return object;
    }
}
