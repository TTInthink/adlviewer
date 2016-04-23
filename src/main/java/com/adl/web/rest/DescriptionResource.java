package com.adl.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.adl.domain.Description;
import com.adl.service.DescriptionService;
import com.adl.web.rest.util.HeaderUtil;
import com.adl.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Description.
 */
@RestController
@RequestMapping("/api")
public class DescriptionResource {

    private final Logger log = LoggerFactory.getLogger(DescriptionResource.class);
        
    @Inject
    private DescriptionService descriptionService;
    
    /**
     * POST  /descriptions : Create a new description.
     *
     * @param description the description to create
     * @return the ResponseEntity with status 201 (Created) and with body the new description, or with status 400 (Bad Request) if the description has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/descriptions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Description> createDescription(@RequestBody Description description) throws URISyntaxException {
        log.debug("REST request to save Description : {}", description);
        if (description.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("description", "idexists", "A new description cannot already have an ID")).body(null);
        }
        Description result = descriptionService.save(description);
        return ResponseEntity.created(new URI("/api/descriptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("description", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /descriptions : Updates an existing description.
     *
     * @param description the description to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated description,
     * or with status 400 (Bad Request) if the description is not valid,
     * or with status 500 (Internal Server Error) if the description couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/descriptions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Description> updateDescription(@RequestBody Description description) throws URISyntaxException {
        log.debug("REST request to update Description : {}", description);
        if (description.getId() == null) {
            return createDescription(description);
        }
        Description result = descriptionService.save(description);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("description", description.getId().toString()))
            .body(result);
    }

    /**
     * GET  /descriptions : get all the descriptions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of descriptions in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/descriptions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Description>> getAllDescriptions(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Descriptions");
        Page<Description> page = descriptionService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/descriptions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /descriptions/:id : get the "id" description.
     *
     * @param id the id of the description to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the description, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/descriptions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Description> getDescription(@PathVariable String id) {
        log.debug("REST request to get Description : {}", id);
        Description description = descriptionService.findOne(id);
        return Optional.ofNullable(description)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /descriptions/:id : delete the "id" description.
     *
     * @param id the id of the description to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/descriptions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDescription(@PathVariable String id) {
        log.debug("REST request to delete Description : {}", id);
        descriptionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("description", id.toString())).build();
    }

}
