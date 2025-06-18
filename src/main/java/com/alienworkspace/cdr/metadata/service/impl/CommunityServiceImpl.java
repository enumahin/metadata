package com.alienworkspace.cdr.metadata.service.impl;

import com.alienworkspace.cdr.metadata.exception.ResourceNotFoundException;
import com.alienworkspace.cdr.metadata.helpers.CurrentUser;
import com.alienworkspace.cdr.metadata.model.mapper.CommunityMapper;
import com.alienworkspace.cdr.metadata.repository.CommunityRepository;
import com.alienworkspace.cdr.metadata.service.CommunityService;
import com.alienworkspace.cdr.model.dto.metadata.CommunityDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the CommunityService interface.
 *
 * @author Ikenumah
 * @version 1.0
 * @since 1.0
 */
@Service
@AllArgsConstructor
public class CommunityServiceImpl implements CommunityService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommunityServiceImpl.class);
    private final CommunityRepository communityRepository;
    private final CommunityMapper communityMapper;

    /**
     * Create a new community.
     *
     * @param communityDto the community to create
     * @return the created community
     */
    @Transactional
    @Override
    public CommunityDto createCommunity(CommunityDto communityDto) {
        try {
            return communityMapper.toDto(communityRepository.save(communityMapper.toEntity(communityDto)));
        } catch (Exception e) {
            LOGGER.error("Failed to create community", e);
            throw new IllegalArgumentException("Failed to create community", e);
        }
    }

    /**
     * Update an existing community.
     *
     * @param communityId the id of the community to update
     * @param communityDto the community to update
     * @return the updated community
     */
    @Transactional
    @Override
    public CommunityDto updateCommunity(int communityId, CommunityDto communityDto) {
        return communityRepository.findById(communityId)
                .map(community -> {
                    try {
                        community.setCommunityName(communityDto.getCommunityName());
                        community.setLocale(communityDto.getLocale());
                        community.setLocalePreferred(communityDto.isLocalePreferred());
                        community.setCommunityCode(communityDto.getCommunityCode());
                        community.setCommunityGeoCode(communityDto.getCommunityGeoCode());
                        community.setCommunityPhoneCode(communityDto.getCommunityPhoneCode());
                        community.setLastModifiedBy(CurrentUser.getCurrentUser().getPersonId());
                        community.setLastModifiedAt(LocalDateTime.now());
                        return communityMapper.toDto(communityRepository.save(community));
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Failed to update community: " + e.getMessage(), e);
                    }
                }).orElseThrow(() -> new ResourceNotFoundException("Community not found with id: " + communityId));
    }

    /**
     * Delete a community.
     *
     * @param id the id of the community to delete
     * @param recordVoidRequest the record void request
     */
    @Transactional
    @Override
    public void deleteCommunity(int id, RecordVoidRequest recordVoidRequest) {
        communityRepository.findById(id)
                .map(community -> {
                    try {
                        community.setVoided(true);
                        community.setVoidedAt(LocalDateTime.now());
                        community.setVoidedBy(CurrentUser.getCurrentUser().getPersonId());
                        community.setVoidReason(recordVoidRequest.getVoidReason());
                        return communityMapper.toDto(communityRepository.save(community));
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Failed to delete community: " + e.getMessage(), e);
                    }
                }).orElseThrow(() -> new ResourceNotFoundException("Community not found with id: " + id));
    }

    /**
     * Get a community by its id.
     *
     * @param id the id of the community to get
     * @return the community
     */
    @Transactional
    @Override
    public CommunityDto getCommunity(int id) {
        return communityMapper.toDto(communityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Community not found with id: " + id)));
    }

    /**
     * Get all communities.
     *
     * @return a list of communities
     */
    @Transactional
    @Override
    public List<CommunityDto> getAllCommunities() {
        return communityRepository.findAll().stream().map(communityMapper::toDto).toList();
    }
} 