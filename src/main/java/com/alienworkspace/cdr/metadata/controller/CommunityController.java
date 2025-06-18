package com.alienworkspace.cdr.metadata.controller;

import static com.alienworkspace.cdr.metadata.helpers.Constants.COMMUNITY_BASE_URL;

import com.alienworkspace.cdr.metadata.service.CommunityService;
import com.alienworkspace.cdr.model.dto.metadata.CommunityDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Community Controller.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Tag(name = "Community", description = "Community API")
@RestController
@RequestMapping(COMMUNITY_BASE_URL)
@AllArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class CommunityController {

    private final CommunityService communityService;

    /**
     * Creates a new community.
     *
     * @param communityDto the community to create
     * @return the created community
     */
    @Operation(summary = "Create a new community", description = "Creates a new community")
    @ApiResponse(responseCode = "201", description = "Community created successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CommunityDto.class)))
    @PostMapping
    public ResponseEntity<CommunityDto> createCommunity(@RequestBody CommunityDto communityDto) {
        return new ResponseEntity<>(communityService.createCommunity(communityDto), HttpStatus.CREATED);
    }

    /**
     * Updates an existing community.
     *
     * @param id Community ID
     * @param communityDto the community to update
     * @return the updated community
     */
    @Operation(summary = "Update an existing community", description = "Updates an existing community")
    @ApiResponse(responseCode = "200", description = "Community updated successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CommunityDto.class)))
    @ApiResponse(responseCode = "404", description = "Community not found")
    @PutMapping("/{id}")
    public ResponseEntity<CommunityDto> updateCommunity(@PathVariable int id, @RequestBody CommunityDto communityDto) {
        return ResponseEntity.ok(communityService.updateCommunity(id, communityDto));
    }

    /**
     * Deletes a community.
     *
     * @param recordVoidRequest the request object containing the voided by user
     */
    @Operation(summary = "Delete a community", description = "Deletes a community")
    @ApiResponse(responseCode = "200", description = "Community deleted successfully")
    @ApiResponse(responseCode = "404", description = "Community not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommunity(@PathVariable int id,
                                                @RequestBody RecordVoidRequest recordVoidRequest) {
        communityService.deleteCommunity(id, recordVoidRequest);
        return ResponseEntity.ok().build();
    }

    /**
     * Retrieves a community by its ID.
     *
     * @param id the ID of the community to retrieve
     * @return the community
     */
    @Operation(summary = "Get a community by ID", description = "Retrieves a community by its ID")
    @ApiResponse(responseCode = "200", description = "Community retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommunityDto.class)))
    @ApiResponse(responseCode = "404", description = "Community not found")
    @GetMapping("/{id}")
    public ResponseEntity<CommunityDto> getCommunity(@PathVariable int id) {
        return ResponseEntity.ok(communityService.getCommunity(id));
    }

    /**
     * Retrieves all communities.
     *
     * @return a list of communities
     */
    @Operation(summary = "Get all communities", description = "Retrieves all communities")
    @ApiResponse(responseCode = "200", description = "Communities retrieved successfully",
            content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = CommunityDto.class)))
    @GetMapping
    public ResponseEntity<List<CommunityDto>> getAllCommunities() {
        return ResponseEntity.ok(communityService.getAllCommunities());
    }
} 