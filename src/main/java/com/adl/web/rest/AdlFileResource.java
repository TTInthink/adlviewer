package com.adl.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.adl.domain.AdlFile;
import com.adl.domain.Adlparser;
import com.adl.service.AdlFileService;
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
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing AdlFile.
 */
@RestController
@RequestMapping("/api")
public class AdlFileResource {

    private final Logger log = LoggerFactory.getLogger(AdlFileResource.class);
        
    @Inject
    private AdlFileService adlFileService;
    
    /**
     * POST  /adl-files : Create a new adlFile.
     *
     * @param adlFile the adlFile to create
     * @return the ResponseEntity with status 201 (Created) and with body the new adlFile, or with status 400 (Bad Request) if the adlFile has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/adl-files",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AdlFile> createAdlFile(@RequestBody AdlFile adlFile) throws URISyntaxException {
        log.debug("REST request to save AdlFile : {}", adlFile);
        if (adlFile.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("adlFile", "idexists", "A new adlFile cannot already have an ID")).body(null);
        }
        AdlFile result = adlFileService.save(adlFile);
        return ResponseEntity.created(new URI("/api/adl-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("adlFile", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /adl-files : Updates an existing adlFile.
     *
     * @param adlFile the adlFile to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated adlFile,
     * or with status 400 (Bad Request) if the adlFile is not valid,
     * or with status 500 (Internal Server Error) if the adlFile couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/adl-files",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AdlFile> updateAdlFile(@RequestBody AdlFile adlFile) throws URISyntaxException {
        log.debug("REST request to update AdlFile : {}", adlFile);
        if (adlFile.getId() == null) {
            return createAdlFile(adlFile);
        }
        AdlFile result = adlFileService.save(adlFile);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("adlFile", adlFile.getId().toString()))
            .body(result);
    }

    /**
     * GET  /adl-files : get all the adlFiles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of adlFiles in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/adl-files",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<AdlFile>> getAllAdlFiles(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of AdlFiles");
        Page<AdlFile> page = adlFileService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/adl-files");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /adl-files/:id : get the "id" adlFile.
     *
     * @param id the id of the adlFile to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the adlFile, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/adl-files/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AdlFile> getAdlFile(@PathVariable String id) {
        log.debug("REST request to get AdlFile : {}", id);
        AdlFile adlFile = adlFileService.findOne(id);
        return Optional.ofNullable(adlFile)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /adl-files/:id : delete the "id" adlFile.
     *
     * @param id the id of the adlFile to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/adl-files/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAdlFile(@PathVariable String id) {
        log.debug("REST request to delete AdlFile : {}", id);
        adlFileService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("adlFile", id.toString())).build();
    }
    
    @RequestMapping(value = "/adl-files/upload",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AdlFile>  uploadAdlparser(final HttpServletRequest request,
            final HttpServletResponse response,@RequestParam("comment") String comment,@RequestParam("dateUpload") Date dateUpload,
    		@RequestParam("file") MultipartFile file) throws URISyntaxException {
            log.debug("REST request to upload Adlparser : ");
            if (!file.isEmpty()) {
            	String filename="";
    			try {
    				filename=file.getOriginalFilename();
    				BufferedOutputStream stream = new BufferedOutputStream(
    						new FileOutputStream(new File( "upload/"+filename)));
                    FileCopyUtils.copy(file.getInputStream(), stream);
    				stream.close();
    				
    				AdlFile adlfile=new AdlFile();
    				Instant instant = dateUpload.toInstant();
    				ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
    				LocalDate date = zdt.toLocalDate();
        			adlfile.setDateUpload(date);
        			adlfile.setSize(file.getSize());
        			adlfile.setFilename(filename);
        			adlfile.setComment(comment);

        			AdlFile result = adlFileService.save(adlfile);
        			return ResponseEntity.created(new URI("/api/adl-files/" + result.getId()))
        		            .headers(HeaderUtil.createEntityCreationAlert("adlFile", result.getId().toString()))
        		            .body(result);

    			}
    			catch (Exception e) {
    				e.printStackTrace();
    				return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("adlparser", "idexists", "Exception occurs when uploading the file")).body(null);
    			}
    			

    		}
            
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("adlparser", "idexists", "Error uploading file")).body(null);
        }

}
