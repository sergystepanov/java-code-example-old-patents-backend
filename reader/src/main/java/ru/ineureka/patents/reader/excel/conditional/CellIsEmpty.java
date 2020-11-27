package ru.ineureka.patents.reader.excel.conditional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import java.util.function.Predicate;

public final class CellIsEmpty implements Predicate<Cell> {
    @Override
    public boolean test(Cell cell) {
        return cell.getCellType() == CellType.BLANK;
    }
}
