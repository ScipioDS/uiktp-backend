package uiktp.team19.web.import_and_export;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uiktp.team19.service.import_and_export.ImportAndExportService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class ImportAndExportController {
    private final ImportAndExportService importAndExportService;

    @PostMapping("/import/excel")
    @Transactional
    public ResponseEntity<Map<String, String>> importExcel(@RequestParam("file") MultipartFile file) {
        try {
            importAndExportService.importPredefinedLocations(file);
            return ResponseEntity.ok(Map.of("message", "Import successful"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Import failed: " + e.getMessage()));
        }
    }

    @GetMapping("/export/{locationId}")
    public ResponseEntity<byte[]> exportWeatherData(@PathVariable Long locationId) throws IOException {
        ByteArrayOutputStream out = importAndExportService.exportWeatherDataForLocation(locationId);
        if (out == null) return ResponseEntity.notFound().build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "weather_data_" + locationId + ".xlsx");

        return ResponseEntity.ok().headers(headers).body(out.toByteArray());
    }
}
