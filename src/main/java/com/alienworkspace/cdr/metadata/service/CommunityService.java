package com.alienworkspace.cdr.metadata.service;

import com.alienworkspace.cdr.model.dto.metadata.CommunityDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import java.util.List;

/**
 * Service interface for Community operations.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
public interface CommunityService {

    /**
     * Creates a new community.
     *
     * @param communityDto the community to create
     * @return the created community
     */
    CommunityDto createCommunity(CommunityDto communityDto);

    /**
     * Updates an existing community.
     *
     * @param communityId the ID of the community to update
     * @param communityDto the community to update
     * @return the updated community
     */
    CommunityDto updateCommunity(int communityId, CommunityDto communityDto);

    /**
     * Deletes a community.
     *
     * @param id the ID of the community to delete
     * @param request the request object containing the voided by user
     */
    void deleteCommunity(int id, RecordVoidRequest request);

    /**
     * Retrieves a community by its ID.
     *
     * @param id the ID of the community to retrieve
     * @return the community
     */
    CommunityDto getCommunity(int id);

    /**
     * Retrieves all communities.
     *
     * @return a list of communities
     */
    List<CommunityDto> getAllCommunities();
} 