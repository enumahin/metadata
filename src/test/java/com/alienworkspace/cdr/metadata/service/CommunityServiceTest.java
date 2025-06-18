package com.alienworkspace.cdr.metadata.service;

import com.alienworkspace.cdr.metadata.model.Community;
import com.alienworkspace.cdr.metadata.model.mapper.CommunityMapper;
import com.alienworkspace.cdr.metadata.repository.CommunityRepository;
import com.alienworkspace.cdr.metadata.service.impl.CommunityServiceImpl;
import com.alienworkspace.cdr.model.dto.metadata.CommunityDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommunityServiceTest {
    @Mock
    CommunityRepository communityRepository;
    @Mock
    CommunityMapper communityMapper;
    @InjectMocks
    CommunityServiceImpl communityService;

    private final CommunityDto.CommunityDtoBuilder communityDtoBuilder = CommunityDto.builder();
    private final Community.CommunityBuilder communityBuilder = Community.builder();

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
        communityBuilder
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
    public void testCreateCommunity() {
        CommunityDto communityDto = communityDtoBuilder.build();
        Community community = communityBuilder.build();
        Community savedCommunity = communityBuilder.build();
        when(communityMapper.toEntity(communityDto)).thenReturn(community);
        when(communityRepository.save(community)).thenReturn(savedCommunity);
        when(communityMapper.toDto(savedCommunity)).thenReturn(communityDto);
        CommunityDto actual = communityService.createCommunity(communityDto);
        assertNotNull(actual);
        assertEquals(communityDto.getCommunityId(), actual.getCommunityId());
        verify(communityRepository).save(communityMapper.toEntity(communityDto));
        verifyNoMoreInteractions(communityRepository);
    }

    @DisplayName("Test Update Community")
    @Test
    public void testUpdateCommunity() {
        CommunityDto communityDto = communityDtoBuilder.build();
        Community existingCommunity = communityBuilder.build();
        Community updatedCommunity = communityBuilder.build();
        updatedCommunity.setCommunityName("Updated");
        CommunityDto expectedDto = communityDtoBuilder.communityName("Updated").build();
        when(communityRepository.findById(communityDto.getCommunityId())).thenReturn(Optional.of(existingCommunity));
        when(communityRepository.save(existingCommunity)).thenReturn(updatedCommunity);
        when(communityMapper.toDto(updatedCommunity)).thenReturn(expectedDto);
        CommunityDto actual = communityService.updateCommunity(communityDto.getCommunityId(), communityDtoBuilder.communityName("Updated").build());
        assertNotNull(actual);
        assertEquals("Updated", actual.getCommunityName());
        verify(communityRepository).save(existingCommunity);
        verifyNoMoreInteractions(communityRepository);
    }

    @DisplayName("Test Delete Community")
    @Test
    public void testDeleteCommunity() {
        RecordVoidRequest request = new RecordVoidRequest();
        request.setVoidReason("Test");
        Community existingCommunity = communityBuilder.build();
        Community voidedCommunity = communityBuilder.build();
        voidedCommunity.setVoided(true);
        voidedCommunity.setVoidedBy(3L);
        voidedCommunity.setVoidedAt(LocalDateTime.now());
        when(communityRepository.findById(1)).thenReturn(Optional.of(existingCommunity));
        when(communityRepository.save(existingCommunity)).thenReturn(voidedCommunity);
        when(communityMapper.toDto(voidedCommunity)).thenReturn(communityDtoBuilder.build());
        communityService.deleteCommunity(1, request);
        verify(communityRepository).save(existingCommunity);
        verifyNoMoreInteractions(communityRepository);
        assertTrue(existingCommunity.isVoided());
        assertNotNull(existingCommunity.getVoidedAt());
    }

    @DisplayName("Test Get Community")
    @Test
    public void testGetCommunity() {
        int id = 1;
        Community existingCommunity = communityBuilder.build();
        CommunityDto expectedDto = communityDtoBuilder.build();
        when(communityRepository.findById(id)).thenReturn(Optional.of(existingCommunity));
        when(communityMapper.toDto(existingCommunity)).thenReturn(expectedDto);
        CommunityDto actual = communityService.getCommunity(id);
        assertNotNull(actual);
        assertEquals(expectedDto, actual);
        verify(communityRepository).findById(id);
        verifyNoMoreInteractions(communityRepository);
    }

    @DisplayName("Test Get All Communities")
    @Test
    public void testGetAllCommunities() {
        List<Community> communities = List.of(communityBuilder.build());
        List<CommunityDto> expectedDtos = List.of(communityDtoBuilder.build());
        when(communityRepository.findAll()).thenReturn(communities);
        when(communityMapper.toDto(any(Community.class))).thenReturn(communityDtoBuilder.build());
        List<CommunityDto> actual = communityService.getAllCommunities();
        assertNotNull(actual);
        assertEquals(expectedDtos.size(), actual.size());
        verify(communityRepository).findAll();
        verifyNoMoreInteractions(communityRepository);
    }
} 