package com.adl.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.adl.domain.Adlparser;
import com.adl.service.AdlparserService;
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
import javax.servlet.http.HttpServletRequest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.FileCopyUtils;
/**
 * REST controller for managing Adlparser.
 */
@RestController
@RequestMapping("/api")
public class AdlparserResource {

    private final Logger log = LoggerFactory.getLogger(AdlparserResource.class);
        
    @Inject
    private AdlparserService adlparserService;
    
    /**
     * POST  /adlparsers : Create a new adlparser.
     *
     * @param adlparser the adlparser to create
     * @return the ResponseEntity with status 201 (Created) and with body the new adlparser, or with status 400 (Bad Request) if the adlparser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/adlparsers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Adlparser> createAdlparser(@RequestBody Adlparser adlparser) throws URISyntaxException {
        log.debug("REST request to save Adlparser : {}", adlparser);
        if (adlparser.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("adlparser", "idexists", "A new adlparser cannot already have an ID")).body(null);
        }
        Adlparser result = adlparserService.save(adlparser);
        return ResponseEntity.created(new URI("/api/adlparsers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("adlparser", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /adlparsers : Updates an existing adlparser.
     *
     * @param adlparser the adlparser to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated adlparser,
     * or with status 400 (Bad Request) if the adlparser is not valid,
     * or with status 500 (Internal Server Error) if the adlparser couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/adlparsers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Adlparser> updateAdlparser(@RequestBody Adlparser adlparser) throws URISyntaxException {
        log.debug("REST request to update Adlparser : {}", adlparser);
        if (adlparser.getId() == null) {
            return createAdlparser(adlparser);
        }
        Adlparser result = adlparserService.save(adlparser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("adlparser", adlparser.getId().toString()))
            .body(result);
    }

    /**
     * GET  /adlparsers : get all the adlparsers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of adlparsers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/adlparsers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Adlparser>> getAllAdlparsers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Adlparsers");
        Page<Adlparser> page = adlparserService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/adlparsers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /adlparsers/:id : get the "id" adlparser.
     *
     * @param id the id of the adlparser to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the adlparser, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/adlparsers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Adlparser> getAdlparser(@PathVariable String id) {
        log.debug("REST request to get Adlparser : {}", id);
        Adlparser adlparser = adlparserService.findOne(id);
        return Optional.ofNullable(adlparser)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /adlparsers/:id : delete the "id" adlparser.
     *
     * @param id the id of the adlparser to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/adlparsers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAdlparser(@PathVariable String id) {
        log.debug("REST request to delete Adlparser : {}", id);
        adlparserService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("adlparser", id.toString())).build();
    }
    
    @RequestMapping(value = "/adlparsers/upload",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
        @Timed
    public ResponseEntity<Adlparser>  uploadAdlparser(@RequestParam("file") MultipartFile file) throws URISyntaxException {
            log.debug("REST request to upload Adlparser : ");
            
            if (!file.isEmpty()) {
            	String filename="";
    			try {
    				filename=file.getOriginalFilename();
    				BufferedOutputStream stream = new BufferedOutputStream(
    						new FileOutputStream(new File( "upload/"+filename)));
                    FileCopyUtils.copy(file.getInputStream(), stream);
    				stream.close();

    			}
    			catch (Exception e) {
    				e.printStackTrace();
    				return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("adlparser", "idexists", "Exception occurs when uploading the file")).body(null);
    			}
    			Adlparser adlparser=new Adlparser();
    			adlparser.setFilename(filename);
    			adlparser.setContent("Click on View Details");
    			Adlparser result = adlparserService.save(adlparser);
                return ResponseEntity.created(new URI("/api/adlparsers/" + result.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert("adlparser", result.getId().toString()))
                    .body(result);
    		}
            
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("adlparser", "idexists", "Error uploading file")).body(null);
        }

}
