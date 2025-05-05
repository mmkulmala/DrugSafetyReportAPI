package org.marno.drugsafetyreport.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.marno.drugsafetyreport.enums.DrugSafetyReportStatus;
import org.marno.drugsafetyreport.model.DrugSafetyReport;
import org.marno.drugsafetyreport.repository.DrugSafetyReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DrugSafetyReportController.class)
class DrugSafetyReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DrugSafetyReportRepository repository;

    private DrugSafetyReport testReport1;
    private DrugSafetyReport testReport2;

    @BeforeEach
    void setUp() {
        testReport1 = DrugSafetyReport.builder()
                .id(1L)
                .reporterName("John Doe")
                .productName("Test Drug 1")
                .descriptionOfIssue("Test issue 1")
                .timestamp(Timestamp.from(Instant.parse("2025-01-01T10:00:00Z")))
                .status(DrugSafetyReportStatus.NEW)
                .build();

        testReport2 = DrugSafetyReport.builder()
                .id(2L)
                .reporterName("Jane Smith")
                .productName("Test Drug 2")
                .descriptionOfIssue("Test issue 2")
                .timestamp(Timestamp.from(Instant.parse("2025-01-02T10:00:00Z")))
                .status(DrugSafetyReportStatus.IN_REVIEW)
                .build();
    }

    // GET /reports Tests
    
    @Test
    void allReports_ReturnsListOfReportsInDescendingOrder() throws Exception {
        // Given
        when(repository.findAll(any(Sort.class)))
                .thenReturn(Arrays.asList(testReport2, testReport1));

        // When & Then
        mockMvc.perform(get("/reports"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[1].id", is(1)))
                .andExpect(jsonPath("$[0].reporterName", is("Jane Smith")))
                .andExpect(jsonPath("$[1].reporterName", is("John Doe")));
    }

    @Test
    void allReports_WhenEmpty_ReturnsEmptyList() throws Exception {
        // Given
        when(repository.findAll(any(Sort.class)))
                .thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/reports"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // GET /reports/{id} Tests
    
    @Test
    void oneReport_WhenExists_ReturnsReport() throws Exception {
        // Given
        when(repository.findById(1L))
                .thenReturn(Optional.of(testReport1));

        // When & Then
        mockMvc.perform(get("/reports/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.reporterName", is("John Doe")))
                .andExpect(jsonPath("$.productName", is("Test Drug 1")))
                .andExpect(jsonPath("$.descriptionOfIssue", is("Test issue 1")))
                .andExpect(jsonPath("$.status", is("NEW")));
    }

    @Test
    void oneReport_WhenNotExists_Returns404() throws Exception {
        // Given
        when(repository.findById(999L))
                .thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/reports/999"))
                .andDo(print()) // Prints the response for debugging
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Could not find Drug safety report with id 999"));
    }

    // POST /reports Tests
    
    @Test
    void newReport_WithValidData_CreatesReport() throws Exception {
        // Given
        DrugSafetyReport newReport = DrugSafetyReport.builder()
                .reporterName("New Reporter")
                .productName("New Drug")
                .descriptionOfIssue("New issue")
                .timestamp(Timestamp.from(Instant.parse("2025-01-03T10:00:00Z")))
                .status(DrugSafetyReportStatus.NEW)
                .build();

        DrugSafetyReport savedReport = DrugSafetyReport.builder()
                .id(3L)
                .reporterName(newReport.getReporterName())
                .productName(newReport.getProductName())
                .descriptionOfIssue(newReport.getDescriptionOfIssue())
                .timestamp(newReport.getTimestamp())
                .status(newReport.getStatus())
                .build();

        when(repository.save(any(DrugSafetyReport.class)))
                .thenReturn(savedReport);

        // When & Then
        mockMvc.perform(post("/reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newReport)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.reporterName", is("New Reporter")))
                .andExpect(jsonPath("$.productName", is("New Drug")))
                .andExpect(jsonPath("$.descriptionOfIssue", is("New issue")))
                .andExpect(jsonPath("$.status", is("NEW")));
    }

    @Test
    void newReport_WithMissingReporterName_ReturnsBadRequest() throws Exception {
        // Given
        DrugSafetyReport invalidReport = DrugSafetyReport.builder()
                .productName("New Drug")
                .descriptionOfIssue("New issue")
                .timestamp(Timestamp.from(Instant.parse("2025-01-03T10:00:00Z")))
                .status(DrugSafetyReportStatus.NEW)
                .build();

        // When & Then
        mockMvc.perform(post("/reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidReport)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void newReport_WithMissingProductName_ReturnsBadRequest() throws Exception {
        // Given
        DrugSafetyReport invalidReport = DrugSafetyReport.builder()
                .reporterName("New Reporter")
                .descriptionOfIssue("New issue")
                .timestamp(Timestamp.from(Instant.parse("2025-01-03T10:00:00Z")))
                .status(DrugSafetyReportStatus.NEW)
                .build();

        // When & Then
        mockMvc.perform(post("/reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidReport)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void newReport_WithMissingDescription_ReturnsBadRequest() throws Exception {
        // Given
        DrugSafetyReport invalidReport = DrugSafetyReport.builder()
                .reporterName("New Reporter")
                .productName("New Drug")
                .timestamp(Timestamp.from(Instant.parse("2025-01-03T10:00:00Z")))
                .status(DrugSafetyReportStatus.NEW)
                .build();

        // When & Then
        mockMvc.perform(post("/reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidReport)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void newReport_WithMissingTimestamp_ReturnsBadRequest() throws Exception {
        // Given
        DrugSafetyReport invalidReport = DrugSafetyReport.builder()
                .reporterName("New Reporter")
                .productName("New Drug")
                .descriptionOfIssue("New issue")
                .status(DrugSafetyReportStatus.NEW)
                .build();

        // When & Then
        mockMvc.perform(post("/reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidReport)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void newReport_WithMissingStatus_ReturnsBadRequest() throws Exception {
        // Given
        DrugSafetyReport invalidReport = DrugSafetyReport.builder()
                .reporterName("New Reporter")
                .productName("New Drug")
                .descriptionOfIssue("New issue")
                .timestamp(Timestamp.from(Instant.parse("2025-01-03T10:00:00Z")))
                .build();

        // When & Then
        mockMvc.perform(post("/reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidReport)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void newReport_WithClosedStatus_CreatesReport() throws Exception {
        // Given
        DrugSafetyReport newReport = DrugSafetyReport.builder()
                .reporterName("New Reporter")
                .productName("New Drug")
                .descriptionOfIssue("New issue")
                .timestamp(Timestamp.from(Instant.parse("2025-01-03T10:00:00Z")))
                .status(DrugSafetyReportStatus.CLOSED)
                .build();

        DrugSafetyReport savedReport = DrugSafetyReport.builder()
                .id(4L)
                .reporterName(newReport.getReporterName())
                .productName(newReport.getProductName())
                .descriptionOfIssue(newReport.getDescriptionOfIssue())
                .timestamp(newReport.getTimestamp())
                .status(newReport.getStatus())
                .build();

        when(repository.save(any(DrugSafetyReport.class)))
                .thenReturn(savedReport);

        // When & Then
        mockMvc.perform(post("/reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newReport)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.status", is("CLOSED")));
    }
}

