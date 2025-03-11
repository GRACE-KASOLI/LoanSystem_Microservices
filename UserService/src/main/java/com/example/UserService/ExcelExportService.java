package com.example.UserService;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelExportService {

    private final UserRepository userRepository;

    public ExcelExportService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ✅ Export Users from Database to Excel File
    public void exportToExcel(HttpServletResponse response, List<User> users) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Users");

        // Create Header Row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Name");
        headerRow.createCell(2).setCellValue("Email");
        headerRow.createCell(3).setCellValue("Phone");
        headerRow.createCell(4).setCellValue("Role");

        // Fill Data Rows
        int rowNum = 1;
        for (User user : users) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(user.getId());
            row.createCell(1).setCellValue(user.getName());
            row.createCell(2).setCellValue(user.getEmail());
            row.createCell(3).setCellValue(user.getPhone());
            row.createCell(4).setCellValue(user.getRole() != null ? user.getRole().name() : "");
        }

        // Set Response Headers for File Download
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=users.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }

    // ✅ Import Users from Excel File to Database
    public void importFromExcel(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            List<User> users = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                User user = new User();
                user.setName(getCellValueAsString(row.getCell(1)));
                user.setEmail(getCellValueAsString(row.getCell(2)));
                user.setPhone(getCellValueAsString(row.getCell(3)));

                String roleValue = getCellValueAsString(row.getCell(4));
                if (roleValue == null || roleValue.isEmpty()) {
                    System.out.println("Skipping row " + row.getRowNum() + " due to missing role.");
                    continue; // Skip users with missing roles
                }

                try {
                    Role role = Role.valueOf(roleValue.toUpperCase()); // Convert role to uppercase for consistency
                    user.setRole(role);
                } catch (IllegalArgumentException e) {
                    System.out.println("Skipping row " + row.getRowNum() + " due to invalid role: " + roleValue);
                    continue; // Skip users with invalid roles
                }

                users.add(user);
            }

            userRepository.saveAll(users);
        }
    }

    // ✅ Helper Method to Handle Missing or Different Cell Types
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return null; // Handle missing cells

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue()); // Convert numeric values
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return null;
        }
    }
}
