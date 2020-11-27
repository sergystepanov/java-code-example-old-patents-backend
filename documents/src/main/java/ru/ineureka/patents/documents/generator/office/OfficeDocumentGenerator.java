package ru.ineureka.patents.documents.generator.office;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.StackSize;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public class OfficeDocumentGenerator implements DocumentGenerator {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    @Override
    public void generate(InputStream templateStream, OutputStream dest, Map<String, String> what) throws GeneratorException {
        if (templateStream == null || dest == null) throw new GeneratorException("A template file should be provided");

        try (var document = open(templateStream)) {
            replace(document, what);
            document.write(dest);
        } catch (Exception e) {
            logger.atSevere().withStackTrace(StackSize.SMALL).log(e.getMessage());
            throw new GeneratorException("Couldn't generate the file");
        }
    }

    /**
     * Returns a Word document (docx) was read from disk into memory.
     *
     * @param stream The template stream.
     * @throws IOException In case of IO errors.
     */
    private XWPFDocument open(InputStream stream) throws IOException {
        return new XWPFDocument(new ByteArrayInputStream(stream.readAllBytes()));
    }

    /**
     * Replaces all text occurrences in the doc by the provided map.
     * I like quadratic algorithms.
     *
     * @param doc          The document where replace will be present.
     * @param replacements A map of replacement words.
     */
    private void replace(XWPFDocument doc, Map<String, String> replacements) {
        for (var paragraph : doc.getParagraphs()) {
            for (var run : paragraph.getRuns()) {
                final var text = run.getText(0);
                logger.atFine().log("%s", text);

                if (text == null) continue;

                for (var replacement : replacements.entrySet()) {
                    var placeholder = replacement.getKey();
                    if (text.contains(placeholder)) {
                        final var newText = text.replace(placeholder, replacement.getValue());
                        run.setText(newText, 0);
                    }
                }
            }
        }
    }
}
