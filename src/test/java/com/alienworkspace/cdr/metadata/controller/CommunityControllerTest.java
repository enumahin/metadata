package com.alienworkspace.cdr.metadata.controller;

import com.alienworkspace.cdr.metadata.exception.ResourceNotFoundException;
import com.alienworkspace.cdr.metadata.service.CommunityService;
import com.alienworkspace.cdr.model.dto.metadata.CommunityDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.alienworkspace.cdr.metadata.helpers.Constants.COMMUNITY_BASE_URL;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommunityController.class)
public class CommunityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CommunityService communityService;

    private CommunityDto.CommunityDtoBuilder communityDtoBuilder = CommunityDto.builder();

    @BeforeEach
    public void setUp() {
        communityDtoBuilder
                .communityId(1)
                .communityName("Test Community")
                .locale("en_US")
                .localePreferred(true)
                .communityCode("COM123")
                .communityGeoCode("GEO123")
                .communityPhoneCode(234);
    }

    @DisplayName("Test Create Community")
    @Test
    public void testCreateCommunity() throws Exception {
        CommunityDto communityDto = communityDtoBuilder.build();
        when(communityService.createCommunity(communityDto)).thenReturn(communityDtoBuilder.communityId(1).build());
        ResultActions result = mockMvc.perform(
                post(COMMUNITY_BASE_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(communityDto))
        );
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.communityId").value(1))
                .andExpect(jsonPath("$.communityName").value(communityDto.getCommunityName()))
                .andExpect(jsonPath("$.locale").value(communityDto.getLocale()))
                .andExpect(jsonPath("$.localePreferred").value(communityDto.isLocalePreferred()));
    }

    @DisplayName("Test Update Community")
    @Test
    public void testUpdateCommunity() throws Exception {
        CommunityDto communityDto = communityDtoBuilder.build();
        CommunityDto.CommunityDtoBuilder updatedCommunityDto = communityDtoBuilder
                .communityName("Updated Community")
                .locale("fr_FR")
                .localePreferred(false);
        CommunityDto updatedCommunity = updatedCommunityDto.communityId(1).build();
        when(communityService.updateCommunity(anyInt(), any(CommunityDto.class))).thenReturn(updatedCommunity);
        ResultActions result = mockMvc.perform(
                put(COMMUNITY_BASE_URL + "/{id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(communityDto))
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.communityId").value(1))
                .andExpect(jsonPath("$.communityName").value(updatedCommunity.getCommunityName()))
                .andExpect(jsonPath("$.locale").value(updatedCommunity.getLocale()))
                .andExpect(jsonPath("$.localePreferred").value(updatedCommunity.isLocalePreferred()));
    }

    @DisplayName("Test Update Non Existing Community")
    @Test
    public void testUpdateNonExistingCommunity() throws Exception {
        CommunityDto communityDto = communityDtoBuilder.build();
        when(communityService.updateCommunity(1, communityDto)).thenThrow(new ResourceNotFoundException("Community not found"));
        ResultActions result = mockMvc.perform(
                put(COMMUNITY_BASE_URL + "/{id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(communityDto))
        );
        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.errorMessage").value(CoreMatchers.containsString("Community not found")));
    }

    @DisplayName("Test Get Community")
    @Test
    public void testGetCommunity() throws Exception {
        CommunityDto communityDto = communityDtoBuilder.build();
        when(communityService.getCommunity(1)).thenReturn(communityDtoBuilder.communityId(1).build());
        ResultActions result = mockMvc.perform(
                get(COMMUNITY_BASE_URL + "/{id}", 1)
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.communityId").value(1))
                .andExpect(jsonPath("$.communityName").value(communityDto.getCommunityName()))
                .andExpect(jsonPath("$.locale").value(communityDto.getLocale()))
                .andExpect(jsonPath("$.localePreferred").value(communityDto.isLocalePreferred()));
    }

    @DisplayName("Test Get All Communities")
    @Test
    public void testGetAllCommunities() throws Exception {
        List<CommunityDto> communities = List.of(communityDtoBuilder.communityId(1).build());
        when(communityService.getAllCommunities()).thenReturn(communities);
        ResultActions result = mockMvc.perform(
                get(COMMUNITY_BASE_URL)
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].communityId").value(1))
                .andExpect(jsonPath("$.[0].communityName").value(communities.get(0).getCommunityName()))
                .andExpect(jsonPath("$.[0].locale").value(communities.get(0).getLocale()))
                .andExpect(jsonPath("$.[0].localePreferred").value(communities.get(0).isLocalePreferred()));
    }

    @DisplayName("Test Delete Community")
    @Test
    public void testDeleteCommunity() throws Exception {
        RecordVoidRequest recordVoidRequest = RecordVoidRequest.builder()
                .voidReason("Test Void Reason")
                .build();
        doNothing().when(communityService).deleteCommunity(1, recordVoidRequest);
        ResultActions result = mockMvc.perform(
                delete(COMMUNITY_BASE_URL + "/{id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recordVoidRequest))
        );
        result.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("Test Delete NonExisting Community")
    @Test
    public void testDeleteNonExistingCommunity() throws Exception {
        RecordVoidRequest recordVoidRequest = RecordVoidRequest.builder()
                .voidReason("Test Void Reason")
                .build();
        doThrow(new ResourceNotFoundException("Community not found")).when(communityService).deleteCommunity(1, recordVoidRequest);
        ResultActions result = mockMvc.perform(
                delete(COMMUNITY_BASE_URL + "/{id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recordVoidRequest))
        );
        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.errorMessage").value(CoreMatchers.containsString("Community not found")));
    }
} 