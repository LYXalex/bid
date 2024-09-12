package com.example.bid;

import com.example.bid.Service.ProjectService;
import com.example.bid.Utilities.Bid;
import com.example.bid.Utilities.Project;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class ProjectServiceTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @Test
    public void createProjectTest() throws Exception {
        Project project1 = new Project();
        project1.setId(1L);
        project1.setName("Project 1");
        project1.setBidingDDL(LocalDateTime.now().minusDays(1)); // Deadline has passed
        project1.setWinningBuyer("John Doe");

        // Register the JavaTimeModule for handling LocalDateTime
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);  // Optional: To avoid timestamps

        when(projectService.CreateProject(any(Project.class))).thenReturn(project1);

        ResultActions res = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/project")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(project1)));

        res.andExpect(MockMvcResultMatchers.status().isOk());

        res.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Project 1"));

        verify(projectService).CreateProject(any(Project.class));

    }

    @Test
    public void createBidTest() throws Exception {
        Project project1 = new Project();
        project1.setId(1L);
        project1.setName("Project 1");
        project1.setBidingDDL(LocalDateTime.now().minusDays(1)); // Deadline has passed
        project1.setWinningBuyer("John Doe");

        Bid bid = new Bid();
        bid.setId(1L);
        bid.setName("Bid 1");
        bid.setProject(project1);
        bid.setAmount(BigDecimal.valueOf(2000));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        when(projectService.CreateBid(any(bid.getClass()),eq(1L))).thenReturn(bid);

        ResultActions res = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/1/bid").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(bid)));

        res.andExpect(MockMvcResultMatchers.status().isOk());
        res.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Bid 1"));
        verify(projectService).CreateBid(any(bid.getClass()),eq(1L));

    }
}
