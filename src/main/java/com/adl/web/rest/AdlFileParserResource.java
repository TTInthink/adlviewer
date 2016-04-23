package com.adl.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.adl.domain.AdlFile;
import com.adl.domain.AdlFileParser;
import com.adl.domain.AdlOptions;
import com.adl.service.AdlFileParserService;
import com.adl.service.AdlFileService;
import com.adl.web.rest.util.HeaderUtil;
import com.adl.web.rest.util.PaginationUtil;

import org.openehr.am.archetype.Archetype;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

/**
 * REST controller for managing AdlFileParser.
 */
@RestController
@RequestMapping("/api")
public class AdlFileParserResource {

    private final Logger log = LoggerFactory.getLogger(AdlFileParserResource.class);
        
    @Inject
    private AdlFileParserService adlFileParserService;
    
    @Inject
    private AdlFileService adlFileService;
    
    /**
     * POST  /adl-file-parsers : Create a new adlFileParser.
     *
     * @param adlFileParser the adlFileParser to create
     * @return the ResponseEntity with status 201 (Created) and with body the new adlFileParser, or with status 400 (Bad Request) if the adlFileParser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/adl-file-parsers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AdlFileParser> createAdlFileParser(@RequestBody AdlFileParser adlFileParser) throws URISyntaxException {
        log.debug("REST request to save AdlFileParser : {}", adlFileParser);
        if (adlFileParser.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("adlFileParser", "idexists", "A new adlFileParser cannot already have an ID")).body(null);
        }
        AdlFileParser result = adlFileParserService.save(adlFileParser);
        adlFileParserService.loadAdl(adlFileParser.getArchetypeID());
        return ResponseEntity.created(new URI("/api/adl-file-parsers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("adlFileParser", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /adl-file-parsers : Updates an existing adlFileParser.
     *
     * @param adlFileParser the adlFileParser to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated adlFileParser,
     * or with status 400 (Bad Request) if the adlFileParser is not valid,
     * or with status 500 (Internal Server Error) if the adlFileParser couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/adl-file-parsers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AdlFileParser> updateAdlFileParser(@RequestBody AdlFileParser adlFileParser) throws URISyntaxException {
        log.debug("REST request to update AdlFileParser : {}", adlFileParser);
        if (adlFileParser.getId() == null) {
            return createAdlFileParser(adlFileParser);
        }
        AdlFileParser result = adlFileParserService.save(adlFileParser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("adlFileParser", adlFileParser.getId().toString()))
            .body(result);
    }

    /**
     * GET  /adl-file-parsers : get all the adlFileParsers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of adlFileParsers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/adl-file-parsers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<AdlFileParser>> getAllAdlFileParsers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of AdlFileParsers");
        Page<AdlFileParser> page = adlFileParserService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/adl-file-parsers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /adl-file-parsers/:id : get the "id" adlFileParser.
     *
     * @param id the id of the adlFileParser to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the adlFileParser, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/adl-file-parsers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AdlFileParser> getAdlFileParser(@PathVariable String id) {
        log.debug("REST request to get AdlFileParser : {}", id);
        AdlFileParser adlFileParser = adlFileParserService.findOne(id);
        return Optional.ofNullable(adlFileParser)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /adl-file-parsers/:id : delete the "id" adlFileParser.
     *
     * @param id the id of the adlFileParser to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/adl-file-parsers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAdlFileParser(@PathVariable String id) {
        log.debug("REST request to delete AdlFileParser : {}", id);
        adlFileParserService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("adlFileParser", id.toString())).build();
    }
    
    
    @RequestMapping(value = "/adl-file-parsers/getAdlFileOption",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
        @Timed
        public ResponseEntity<List<AdlOptions>> getAdlFileOption() {
            log.debug("REST request to get AdlFileParser Option ");
            Page<AdlFile> page = adlFileService.findAll(null); 
            List<AdlFile> adlFiles=page.getContent();
            List<AdlOptions> list=new ArrayList<AdlOptions>();
            for(AdlFile adlFile:adlFiles){
            	list.add(new AdlOptions(adlFile.getId(),adlFile.getFilename().replace(".adl", "")));
            }

            return new ResponseEntity<>(list, HttpStatus.OK);
        }


}
