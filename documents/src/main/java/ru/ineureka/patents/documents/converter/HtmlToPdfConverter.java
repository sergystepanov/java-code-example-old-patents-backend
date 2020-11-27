package ru.ineureka.patents.documents.converter;

import com.google.common.flogger.FluentLogger;
import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.Options;
import io.webfolder.cdp.session.Session;
import io.webfolder.cdp.session.SessionFactory;
import io.webfolder.cdp.type.constant.PdfTransferMode;

import java.nio.file.Path;
import java.util.List;

public final class HtmlToPdfConverter implements Converter {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private final Launcher launcher;
    private final SessionFactory factory;
    private final String context;
    private final Session session;

    public HtmlToPdfConverter() {
        launcher = new Launcher(
                Options.builder()
                        .headless(true)
                        .arguments(List.of("--disable-gpu"))
                        .build()
        );
        factory = launcher.launch();
        context = factory.createBrowserContext();
        session = factory.create(context);
    }

    @Override
    public String convert(Path input) throws ConverterException {
        logger.atInfo().log("Starting convert %s to PDF", input);

        if (input == null || !input.toFile().exists()) {
            throw new ConverterException("Input file doesn't exist");
        }

        String content;

        logger.atFine().log("Opening: %s", input);
        synchronized (this) {
            session.navigate("file:///" + input);
            session.waitDocumentReady(30000);
            content = session.getCommand()
                    .getPage()
                    .printToPDF(
                            false,
                            false,
                            true,
                            1.0,
                            8.5,
                            11.0,
                            0.0,
                            0.0,
                            0.0,
                            0.0,
                            "1",
                            true,
                            "",
                            "",
                            false,
                            PdfTransferMode.ReturnAsBase64
                    )
                    .getData();
        }
        logger.atFine().log("End of generation for: %s", input);

        return content;
    }

    public synchronized void shutdown() {
        session.close();
        factory.disposeBrowserContext(context);
        launcher.kill();
        logger.atInfo().log("The Chrome converter was terminated successfully");
    }
}
