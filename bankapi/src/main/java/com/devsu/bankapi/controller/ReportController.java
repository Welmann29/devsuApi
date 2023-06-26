package com.devsu.bankapi.controller;

import com.devsu.bankapi.service.ReportService;
import com.devsu.bankapi.utils.dto.response.report.CustomerReport;
import com.devsu.bankapi.utils.functions.decorators.LogDevsu;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(
            ReportService reportService
    ){
        this.reportService = reportService;
    }

    @LogDevsu
    @GetMapping
    @Operation(summary = "Reporte por cliente y rango de fecha, se mostrara la información basica del cliente" +
            " junto a todas sus cuentas (sin importar el estado) y los movimientos asociados en la fecha ingresadas")
    public ResponseEntity<CustomerReport> getReportByCustomer(
            @Parameter(description = "Id interno del cliente") @RequestParam Long customerId,
            @Parameter(description = "Rango de fechas deseadas en el reporte, formato esperado " +
                    "inicio_fin (dd-MM-yyyy_dd-MM-yyyy)", example = "01-02-2023_30-03-2023")
            @RequestParam String dateRange
    ){
        CustomerReport report = reportService.generateReport(customerId, dateRange);
        if (report.getSuccess()){
            return ResponseEntity.ok(report);
        }else{
            return ResponseEntity.badRequest().body(report);
        }
    }

}
