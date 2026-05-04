package uiktp.team19.service.import_and_export;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uiktp.team19.model.location.Location;
import uiktp.team19.model.weather_api.FullWeatherData;
import uiktp.team19.service.location.LocationService;
import uiktp.team19.service.weather_api.WeatherDataService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportAndExportService {
    private final LocationService locationService;
    private final WeatherDataService weatherDataService;

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

    public ByteArrayOutputStream exportWeatherDataForLocation(Long locationId) throws IOException {
        List<FullWeatherData> weatherDataList = weatherDataService.getYearlyFullWeatherDataFromDB(locationId);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Weather Data");

            // Header row
            String[] headers = {
                    "Date/Time",
                    "Temperature 2m (°C)", "Relative Humidity 2m (%)", "Wind Speed 10m (m/s)",
                    "Vapour Pressure Deficit", "Cloud Cover (%)",
                    "Precipitation Probability (%)", "Rain (mm)", "Evapotranspiration",
                    "ET0 FAO Evapotranspiration",
                    "Soil Temp 0cm", "Soil Temp 6cm", "Soil Temp 18cm", "Soil Temp 54cm",
                    "Soil Moisture 0-1cm", "Soil Moisture 1-3cm", "Soil Moisture 3-9cm",
                    "Soil Moisture 9-27cm", "Soil Moisture 27-81cm"
            };

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontName("Arial");
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data rows
            CellStyle dateStyle = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-mm-dd hh:mm"));

            int rowNum = 1;
            for (FullWeatherData wd : weatherDataList) {
                Row row = sheet.createRow(rowNum++);
                int col = 0;

                Cell dateCell = row.createCell(col++);
                if (wd.getDateTime() != null) {
                    dateCell.setCellValue(wd.getDateTime());
                    dateCell.setCellStyle(dateStyle);
                }

                row.createCell(col++).setCellValue(nullSafeDouble(wd.getTemperature2m()));
                row.createCell(col++).setCellValue(nullSafeDouble(wd.getRelativeHumidity2m()));
                row.createCell(col++).setCellValue(nullSafeDouble(wd.getWindSpeed10m()));
                row.createCell(col++).setCellValue(nullSafeDouble(wd.getVapourPressureDeficit()));
                row.createCell(col++).setCellValue(nullSafeDouble(wd.getCloudCover()));
                row.createCell(col++).setCellValue(nullSafeDouble(wd.getPrecipitationProbability()));
                row.createCell(col++).setCellValue(nullSafeDouble(wd.getRain()));
                row.createCell(col++).setCellValue(nullSafeDouble(wd.getEvapotranspiration()));
                row.createCell(col++).setCellValue(nullSafeDouble(wd.getEt0FaoEvapotranspiration()));
                row.createCell(col++).setCellValue(nullSafeDouble(wd.getSoilTemperature0cm()));
                row.createCell(col++).setCellValue(nullSafeDouble(wd.getSoilTemperature6cm()));
                row.createCell(col++).setCellValue(nullSafeDouble(wd.getSoilTemperature18cm()));
                row.createCell(col++).setCellValue(nullSafeDouble(wd.getSoilTemperature54cm()));
                row.createCell(col++).setCellValue(nullSafeDouble(wd.getSoilMoisture0To1cm()));
                row.createCell(col++).setCellValue(nullSafeDouble(wd.getSoilMoisture1To3cm()));
                row.createCell(col++).setCellValue(nullSafeDouble(wd.getSoilMoisture3To9cm()));
                row.createCell(col++).setCellValue(nullSafeDouble(wd.getSoilMoisture9To27cm()));
                row.createCell(col).setCellValue(nullSafeDouble(wd.getSoilMoisture27To81cm()));
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out;
        }
    }

    private double nullSafeDouble(Float value) {
        return value != null ? value.doubleValue() : 0.0;
    }
}
