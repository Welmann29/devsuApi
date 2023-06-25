package com.devsu.bankapi.controller;

import com.devsu.bankapi.service.ReportService;
import com.devsu.bankapi.utils.dto.response.report.CustomerReport;
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

    @GetMapping
    public ResponseEntity<CustomerReport> getReportByCustomer(
            @RequestParam Long customerId,
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
