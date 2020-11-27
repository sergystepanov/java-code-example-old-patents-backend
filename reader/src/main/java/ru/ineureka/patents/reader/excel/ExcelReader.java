package ru.ineureka.patents.reader.excel;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.StackSize;
import org.apache.poi.OldFileFormatException;
import org.apache.poi.ss.usermodel.*;
import ru.ineureka.patents.reader.Reader;
import ru.ineureka.patents.reader.excel.conditional.CellIsEmpty;
import ru.ineureka.patents.reader.excel.exception.ExcelReaderException;
import ru.ineureka.patents.reader.excel.exception.ExcelReaderExceptionType;

import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Microsoft Excel file format reader representation class.
 *
 * @since 1.0.0
 */
public final class ExcelReader implements Reader<InputStream> {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    /**
     * Reads an Excel file.
     *
     * @param in A stream to read.
     * @return An {@link ExcelTable} object.
     * @throws ExcelReaderException In case of any exceptions.
     * @since 1.0.0
     */
    @Override
    public ExcelTable read(InputStream in) {
        var start = Instant.now();

        final Map<Integer, Map<Integer, String>> rows = new LinkedHashMap<>();
        Set<Integer> hiddenColumnIndexes;
        Set<Integer> hiddenRowIndexes = new HashSet<>();

        try (var workbook = WorkbookFactory.create(in)) {
            final var sheet = workbook.getSheetAt(0);
            final var formatter = new DataFormatter();
            final var formula = workbook.getCreationHelper().createFormulaEvaluator();

            // Finding hidden columns in the first row
            hiddenColumnIndexes = StreamSupport.stream(sheet.getRow(sheet.getFirstRowNum()).spliterator(), true)
                    .filter(c -> sheet.isColumnHidden(c.getColumnIndex()))
                    .map(Cell::getColumnIndex)
                    .collect(Collectors.toSet());

            // Process table's row iterator as a stream
            StreamSupport.stream(sheet.spliterator(), false).forEach(row -> {
                // Shift the row index not to 0
                final var rowIndex = row.getRowNum() + 1;

                // Skips row if it's hidden
                if (row.getZeroHeight()) {
                    hiddenRowIndexes.add(rowIndex);
                    return;
                }

                final Map<Integer, String> cells = StreamSupport.stream(row.spliterator(), false)
                        // By default we skip hidden cells
                        .filter(Predicate.not(new CellIsEmpty())
                                .and(cell -> !hiddenColumnIndexes.contains(cell.getColumnIndex())))
                        // This will evaluate the cell, and any type of cell will return string value
                        .peek(formula::evaluate)
                        .collect(Collectors.toMap(Cell::getColumnIndex, c -> {
                            // Explicitly format the dates with a normal date format rather the default 'MM/dd/yy'
                            return (isDateCell(c) ? DateTimeFormatter.ISO_LOCAL_DATE
                                    .format(c.getDateCellValue().toInstant().atZone(ZoneId.systemDefault())) :
                                    formatter.formatCellValue(c))
                                    .trim();
                        }));

                rows.put(rowIndex, cells);
            });
        } catch (OldFileFormatException old) {
            logger.atSevere().withCause(old).withStackTrace(StackSize.SMALL);
            throw new ExcelReaderException(ExcelReaderExceptionType.TOO_OLD);
        } catch (Exception e) {
            logger.atSevere().withCause(e).withStackTrace(StackSize.SMALL).log("Couldn't read an Excel file");
            throw new ExcelReaderException(ExcelReaderExceptionType.AN_ERROR, e.getMessage());
        } finally {
            logger.atInfo().log("File read time: %s", Duration.between(start, Instant.now()));
        }

        final ExcelTable table = new ExcelTable(rows);

        if (hiddenColumnIndexes.size() > 0) {
            table.addWarning("Hidden cols: " + hiddenColumnIndexes.size());
            logger.atFine().log("Hidden columns: %d", hiddenColumnIndexes.size());
        }
        if (hiddenRowIndexes.size() > 0) {
            table.addWarning("Hidden rows: " + hiddenRowIndexes.size());
            logger.atFine().log("Hidden rows: %d", hiddenColumnIndexes.size());
        }

        return table;
    }

    private static boolean isDateCell(Cell cell) {
        return cell.getCellType().equals(CellType.NUMERIC) && DateUtil.isCellDateFormatted(cell);
    }
}
