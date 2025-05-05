package org.marno.drugsafetyreport.controller;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.marno.drugsafetyreport.model.DrugSafetyReport;
import org.marno.drugsafetyreport.repository.DrugSafetyReportRepository;
import org.marno.drugsafetyreport.util.DrugSafetyReportNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for Drug Safety Report API
 */
@RestController
public class DrugSafetyReportController {
    private static final Logger logger = LoggerFactory.getLogger(DrugSafetyReportController.class);
    private final DrugSafetyReportRepository repository;

    DrugSafetyReportController(DrugSafetyReportRepository repository) {
        this.repository = repository;
    }

    @Tag(name = "findAll", description = "Find all drug safety reports by descending order")
    @GetMapping("/reports")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            description = "Find all reports",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = DrugSafetyReport.class)))
                            })
            })
    List<DrugSafetyReport> allReports() {
        // add sorting to findAll
        Sort sortBy = Sort.by(Sort.Direction.DESC, "id");

        return repository.findAll(sortBy);
    }


    @Tag(name = "find", description = "Find Drug Safety Report by id")
    @GetMapping("/reports/{id}")
    @ApiResponses({
            @ApiResponse(description = "Found with given id",
                    responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DrugSafetyReport.class))),
            @ApiResponse(description = "Drug safety report not found with given id",
                    responseCode = "404",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DrugSafetyReportNotFoundException.class)))})
    DrugSafetyReport oneReport(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new DrugSafetyReportNotFoundException(id));
    }

    @Tag(name = "create", description = "Create a new drug safety report")
    @PostMapping("/reports")
    @ResponseStatus(HttpStatus.CREATED)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Create a new drug safety report object",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = DrugSafetyReport.class),
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = {
                            @ExampleObject(
                                    name = "An example request with the minimum required fields to create.",
                                    value = "{\"reporterName\" : \"Marno\", \"productName\" : \"Fictional drug\", \"descriptionOfIssue\" : \"Causes irrational fear\", \"timestamp\" : \"2025-05-02T20:00:00\", \"status\" : \"NEW\"}",
                                    summary = "Example Drug Safety Report")}))
    @ApiResponse(
            responseCode = "201",
            description = "Drug safety report successfully created",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = DrugSafetyReport.class)
            )
    )
    ResponseEntity<DrugSafetyReport> newReport(@Valid @RequestBody DrugSafetyReport newReport) {
        logger.info("Received request to create new drug safety report: {}", newReport);

        DrugSafetyReport savedReport = repository.save(newReport);

        logger.info("Successfully created drug safety report with ID: {}", savedReport.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReport);
    }
}
