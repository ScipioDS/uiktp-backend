package uiktp.team19.web.import_and_export;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uiktp.team19.service.import_and_export.ImportAndExportService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ImportAndExportController {
    private final ImportAndExportService importAndExportService;

    @PostMapping("/import/excel")
    @Transactional
    public ResponseEntity<String> importExcel(@RequestParam("file") MultipartFile file) {
        try {
            importAndExportService.importPredefinedLocations(file);
            return ResponseEntity.ok("Import successful");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Import failed: " + e.getMessage());
        }
    }
}
