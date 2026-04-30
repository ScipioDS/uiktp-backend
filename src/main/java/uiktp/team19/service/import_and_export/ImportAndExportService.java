package uiktp.team19.service.import_and_export;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uiktp.team19.model.location.Location;
import uiktp.team19.service.location.LocationService;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportAndExportService {
    private final LocationService locationService;

    public List<Location> importPredefinedLocations(MultipartFile file) throws IOException {
        try (InputStream is = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(is)) {
            List<Location> locations = new ArrayList<>();
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // skip header
                Row row = sheet.getRow(i);

                if (row == null) continue;

                String name = getString(row.getCell(0));
                Double latitude = getDouble(row.getCell(1));
                Double longitude = getDouble(row.getCell(2));

                Location location = new Location();
                location.setName(name);
                location.setLatitude(latitude);
                location.setLongitude(longitude);
                location.setIsPredefined(true);

                locations.add(location);
            }
            return this.locationService.saveAll(locations);
        }
    }

    private String getString(Cell cell) {
        if (cell == null) return null;

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> null;
        };
    }

    private Double getDouble(Cell cell) {
        if (cell == null) return null;

        Double value = switch (cell.getCellType()) {
            case NUMERIC -> cell.getNumericCellValue();
            case STRING -> {
                try {
                    String raw = cell.getStringCellValue().trim();
                    // Strip trailing .0 from numeric-looking strings
                    if (raw.endsWith(".0")) {
                        raw = raw.substring(0, raw.length() - 2);
                    }
                    yield Double.parseDouble(raw);
                } catch (NumberFormatException e) {
                    yield null;
                }
            }
            default -> null;
        };

        if (value == null) return null;

        // Convert implied decimal: 410319 -> 41.0319
        if (value > 90 || value < -90) {
            value = value / 10000.0;
        }

        // Clamp to [-90, 90] range as safety net
        value = Math.max(-90.0, Math.min(90.0, value));

        return value;
    }
}
