package ru.ineureka.patents.reader.excel;

import ru.ineureka.patents.reader.DataTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * An object that holds Excel table's data.
 *
 * @since 3.0.0
 */
public final class ExcelTable implements DataTable {
    private final Map<Integer, Map<Integer, String>> data;
    private final List<String> warnings;

    public ExcelTable(Map<Integer, Map<Integer, String>> data) {
        this.data = data;
        this.warnings = new ArrayList<>();
    }

    /**
     * Returns all the table data as a map.
     * We assume that a table should have a header in the first row,
     * which we'll be skipped in this method.
     *
     * @return A map of table rows and cells.
     * @since 3.0.0
     */
    public Map<Integer, Map<Integer, String>> getData() {
        return data.size() > 1 ?
                data.entrySet().stream()
                        .skip(1)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                : Collections.emptyMap();
    }

    /**
     * Normalizes the header row (cells) of a table.
     * It's being used in various mappings.
     *
     * @return A list of normalized cell values of the header.
     * @since 3.0.0
     */
    public Map<Integer, String> getNormalizedHeader() {
        if (data.isEmpty()) return Collections.emptyMap();

        return data.entrySet().iterator()
                // Get the first row
                .next().getValue().entrySet().stream()
                // Collect it again
                .collect(Collectors.toMap(
                        // A simple normalization
                        Map.Entry::getKey,
                        e -> e.getValue()
                                .replaceAll("\\P{Print}", "")
                                .strip()
                                .toLowerCase()
                                .replace(" ", "_")
                ));
    }

    public void addWarning(String... warnings) {
        Collections.addAll(this.warnings, warnings);
    }

    public List<String> getWarnings() {
        return warnings;
    }

    @Override
    public String toString() {
        return "ExcelTable{" + "data=" + data + '}';
    }
}
