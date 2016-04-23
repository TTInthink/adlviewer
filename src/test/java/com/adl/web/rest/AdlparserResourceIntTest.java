package com.adl.web.rest;

import com.adl.AdlviewerApp;
import com.adl.domain.Adlparser;
import com.adl.repository.AdlparserRepository;
import com.adl.service.AdlparserService;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the AdlparserResource REST controller.
 *
 * @see AdlparserResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AdlviewerApp.class)
@WebAppConfiguration
@IntegrationTest
public class AdlparserResourceIntTest {

    private static final String DEFAULT_FILENAME = "AAAAA";
    private static final String UPDATED_FILENAME = "BBBBB";
    private static final String DEFAULT_CONTENT = "AAAAA";
    private static final String UPDATED_CONTENT = "BBBBB";

    @Inject
    private AdlparserRepository adlparserRepository;

    @Inject
    private AdlparserService adlparserService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAdlparserMockMvc;

    private Adlparser adlparser;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AdlparserResource adlparserResource = new AdlparserResource();
        ReflectionTestUtils.setField(adlparserResource, "adlparserService", adlparserService);
        this.restAdlparserMockMvc = MockMvcBuilders.standaloneSetup(adlparserResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        adlparserRepository.deleteAll();
        adlparser = new Adlparser();
        adlparser.setFilename(DEFAULT_FILENAME);
        adlparser.setContent(DEFAULT_CONTENT);
    }

    @Test
    public void createAdlparser() throws Exception {
        int databaseSizeBeforeCreate = adlparserRepository.findAll().size();

        // Create the Adlparser

        restAdlparserMockMvc.perform(post("/api/adlparsers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(adlparser)))
                .andExpect(status().isCreated());

        // Validate the Adlparser in the database
        List<Adlparser> adlparsers = adlparserRepository.findAll();
        assertThat(adlparsers).hasSize(databaseSizeBeforeCreate + 1);
        Adlparser testAdlparser = adlparsers.get(adlparsers.size() - 1);
        assertThat(testAdlparser.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testAdlparser.getContent()).isEqualTo(DEFAULT_CONTENT);
    }

    @Test
    public void getAllAdlparsers() throws Exception {
        // Initialize the database
        adlparserRepository.save(adlparser);

        // Get all the adlparsers
        restAdlparserMockMvc.perform(get("/api/adlparsers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(adlparser.getId())))
                .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }

    @Test
    public void getAdlparser() throws Exception {
        // Initialize the database
        adlparserRepository.save(adlparser);

        // Get the adlparser
        restAdlparserMockMvc.perform(get("/api/adlparsers/{id}", adlparser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(adlparser.getId()))
            .andExpect(jsonPath("$.filename").value(DEFAULT_FILENAME.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()));
    }

    @Test
    public void getNonExistingAdlparser() throws Exception {
        // Get the adlparser
        restAdlparserMockMvc.perform(get("/api/adlparsers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateAdlparser() throws Exception {
        // Initialize the database
        adlparserService.save(adlparser);

        int databaseSizeBeforeUpdate = adlparserRepository.findAll().size();

        // Update the adlparser
        Adlparser updatedAdlparser = new Adlparser();
        updatedAdlparser.setId(adlparser.getId());
        updatedAdlparser.setFilename(UPDATED_FILENAME);
        updatedAdlparser.setContent(UPDATED_CONTENT);

        restAdlparserMockMvc.perform(put("/api/adlparsers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAdlparser)))
                .andExpect(status().isOk());

        // Validate the Adlparser in the database
        List<Adlparser> adlparsers = adlparserRepository.findAll();
        assertThat(adlparsers).hasSize(databaseSizeBeforeUpdate);
        Adlparser testAdlparser = adlparsers.get(adlparsers.size() - 1);
        assertThat(testAdlparser.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testAdlparser.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    public void deleteAdlparser() throws Exception {
        // Initialize the database
        adlparserService.save(adlparser);

        int databaseSizeBeforeDelete = adlparserRepository.findAll().size();

        // Get the adlparser
        restAdlparserMockMvc.perform(delete("/api/adlparsers/{id}", adlparser.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Adlparser> adlparsers = adlparserRepository.findAll();
        assertThat(adlparsers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
