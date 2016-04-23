package com.adl.web.rest;

import com.adl.AdlviewerApp;
import com.adl.domain.AdlFile;
import com.adl.repository.AdlFileRepository;
import com.adl.service.AdlFileService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the AdlFileResource REST controller.
 *
 * @see AdlFileResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AdlviewerApp.class)
@WebAppConfiguration
@IntegrationTest
public class AdlFileResourceIntTest {

    private static final String DEFAULT_FILENAME = "AAAAA";
    private static final String UPDATED_FILENAME = "BBBBB";

    private static final Long DEFAULT_SIZE = 1L;
    private static final Long UPDATED_SIZE = 2L;

    private static final LocalDate DEFAULT_DATE_UPLOAD = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_UPLOAD = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_COMMENT = "AAAAA";
    private static final String UPDATED_COMMENT = "BBBBB";

    @Inject
    private AdlFileRepository adlFileRepository;

    @Inject
    private AdlFileService adlFileService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAdlFileMockMvc;

    private AdlFile adlFile;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AdlFileResource adlFileResource = new AdlFileResource();
        ReflectionTestUtils.setField(adlFileResource, "adlFileService", adlFileService);
        this.restAdlFileMockMvc = MockMvcBuilders.standaloneSetup(adlFileResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        adlFileRepository.deleteAll();
        adlFile = new AdlFile();
        adlFile.setFilename(DEFAULT_FILENAME);
        adlFile.setSize(DEFAULT_SIZE);
        adlFile.setDateUpload(DEFAULT_DATE_UPLOAD);
        adlFile.setComment(DEFAULT_COMMENT);
    }

    @Test
    public void createAdlFile() throws Exception {
        int databaseSizeBeforeCreate = adlFileRepository.findAll().size();

        // Create the AdlFile

        restAdlFileMockMvc.perform(post("/api/adl-files")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(adlFile)))
                .andExpect(status().isCreated());

        // Validate the AdlFile in the database
        List<AdlFile> adlFiles = adlFileRepository.findAll();
        assertThat(adlFiles).hasSize(databaseSizeBeforeCreate + 1);
        AdlFile testAdlFile = adlFiles.get(adlFiles.size() - 1);
        assertThat(testAdlFile.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testAdlFile.getSize()).isEqualTo(DEFAULT_SIZE);
        assertThat(testAdlFile.getDateUpload()).isEqualTo(DEFAULT_DATE_UPLOAD);
        assertThat(testAdlFile.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    public void getAllAdlFiles() throws Exception {
        // Initialize the database
        adlFileRepository.save(adlFile);

        // Get all the adlFiles
        restAdlFileMockMvc.perform(get("/api/adl-files?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(adlFile.getId())))
                .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
                .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE.intValue())))
                .andExpect(jsonPath("$.[*].dateUpload").value(hasItem(DEFAULT_DATE_UPLOAD.toString())))
                .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())));
    }

    @Test
    public void getAdlFile() throws Exception {
        // Initialize the database
        adlFileRepository.save(adlFile);

        // Get the adlFile
        restAdlFileMockMvc.perform(get("/api/adl-files/{id}", adlFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(adlFile.getId()))
            .andExpect(jsonPath("$.filename").value(DEFAULT_FILENAME.toString()))
            .andExpect(jsonPath("$.size").value(DEFAULT_SIZE.intValue()))
            .andExpect(jsonPath("$.dateUpload").value(DEFAULT_DATE_UPLOAD.toString()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()));
    }

    @Test
    public void getNonExistingAdlFile() throws Exception {
        // Get the adlFile
        restAdlFileMockMvc.perform(get("/api/adl-files/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateAdlFile() throws Exception {
        // Initialize the database
        adlFileService.save(adlFile);

        int databaseSizeBeforeUpdate = adlFileRepository.findAll().size();

        // Update the adlFile
        AdlFile updatedAdlFile = new AdlFile();
        updatedAdlFile.setId(adlFile.getId());
        updatedAdlFile.setFilename(UPDATED_FILENAME);
        updatedAdlFile.setSize(UPDATED_SIZE);
        updatedAdlFile.setDateUpload(UPDATED_DATE_UPLOAD);
        updatedAdlFile.setComment(UPDATED_COMMENT);

        restAdlFileMockMvc.perform(put("/api/adl-files")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAdlFile)))
                .andExpect(status().isOk());

        // Validate the AdlFile in the database
        List<AdlFile> adlFiles = adlFileRepository.findAll();
        assertThat(adlFiles).hasSize(databaseSizeBeforeUpdate);
        AdlFile testAdlFile = adlFiles.get(adlFiles.size() - 1);
        assertThat(testAdlFile.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testAdlFile.getSize()).isEqualTo(UPDATED_SIZE);
        assertThat(testAdlFile.getDateUpload()).isEqualTo(UPDATED_DATE_UPLOAD);
        assertThat(testAdlFile.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    public void deleteAdlFile() throws Exception {
        // Initialize the database
        adlFileService.save(adlFile);

        int databaseSizeBeforeDelete = adlFileRepository.findAll().size();

        // Get the adlFile
        restAdlFileMockMvc.perform(delete("/api/adl-files/{id}", adlFile.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<AdlFile> adlFiles = adlFileRepository.findAll();
        assertThat(adlFiles).hasSize(databaseSizeBeforeDelete - 1);
    }
}
