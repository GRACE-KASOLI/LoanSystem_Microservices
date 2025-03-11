package com.example.UserService;

import org.apache.poi.ss.usermodel.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/export")
public class ExcelExportController {

    private final UserRepository userRepository;
    private final ExcelExportService excelExportService;

    public ExcelExportController(UserRepository userRepository, ExcelExportService excelExportService) {
        this.userRepository = userRepository;
        this.excelExportService = excelExportService;
    }

    @GetMapping("/users")
    public void exportUsersToExcel(HttpServletResponse response) throws IOException {
        List<User> users = userRepository.findAll();
        excelExportService.exportToExcel(response, users);
    }

    // ✅ New Upload Endpoint with Missing Cell Handling
    @PostMapping("/upload")
    public String uploadExcelFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "File is empty";
        }

        try {
            excelExportService.importFromExcel(file);
            return "File uploaded and processed successfully";
        } catch (Exception e) {
            return "Error processing file: " + e.getMessage();
        }
    }

    // Utility method to handle missing cells
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null; // Return null if cell is missing
        }
        return cell.getCellType() == CellType.STRING ? cell.getStringCellValue().trim() : null;
    }
}
