package com.adl.web.rest;

import com.adl.AdlviewerApp;
import com.adl.domain.AdlFileParser;
import com.adl.repository.AdlFileParserRepository;
import com.adl.service.AdlFileParserService;

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
 * Test class for the AdlFileParserResource REST controller.
 *
 * @see AdlFileParserResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AdlviewerApp.class)
@WebAppConfiguration
@IntegrationTest
public class AdlFileParserResourceIntTest {

    private static final String DEFAULT_ARCHETYPE_ID = "AAAAA";
    private static final String UPDATED_ARCHETYPE_ID = "BBBBB";

    @Inject
    private AdlFileParserRepository adlFileParserRepository;

    @Inject
    private AdlFileParserService adlFileParserService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAdlFileParserMockMvc;

    private AdlFileParser adlFileParser;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AdlFileParserResource adlFileParserResource = new AdlFileParserResource();
        ReflectionTestUtils.setField(adlFileParserResource, "adlFileParserService", adlFileParserService);
        this.restAdlFileParserMockMvc = MockMvcBuilders.standaloneSetup(adlFileParserResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        adlFileParserRepository.deleteAll();
        adlFileParser = new AdlFileParser();
        adlFileParser.setArchetypeID(DEFAULT_ARCHETYPE_ID);
    }

    @Test
    public void createAdlFileParser() throws Exception {
        int databaseSizeBeforeCreate = adlFileParserRepository.findAll().size();

        // Create the AdlFileParser

        restAdlFileParserMockMvc.perform(post("/api/adl-file-parsers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(adlFileParser)))
                .andExpect(status().isCreated());

        // Validate the AdlFileParser in the database
        List<AdlFileParser> adlFileParsers = adlFileParserRepository.findAll();
        assertThat(adlFileParsers).hasSize(databaseSizeBeforeCreate + 1);
        AdlFileParser testAdlFileParser = adlFileParsers.get(adlFileParsers.size() - 1);
        assertThat(testAdlFileParser.getArchetypeID()).isEqualTo(DEFAULT_ARCHETYPE_ID);
    }

    @Test
    public void getAllAdlFileParsers() throws Exception {
        // Initialize the database
        adlFileParserRepository.save(adlFileParser);

        // Get all the adlFileParsers
        restAdlFileParserMockMvc.perform(get("/api/adl-file-parsers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(adlFileParser.getId())))
                .andExpect(jsonPath("$.[*].archetypeID").value(hasItem(DEFAULT_ARCHETYPE_ID.toString())));
    }

    @Test
    public void getAdlFileParser() throws Exception {
        // Initialize the database
        adlFileParserRepository.save(adlFileParser);

        // Get the adlFileParser
        restAdlFileParserMockMvc.perform(get("/api/adl-file-parsers/{id}", adlFileParser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(adlFileParser.getId()))
            .andExpect(jsonPath("$.archetypeID").value(DEFAULT_ARCHETYPE_ID.toString()));
    }

    @Test
    public void getNonExistingAdlFileParser() throws Exception {
        // Get the adlFileParser
        restAdlFileParserMockMvc.perform(get("/api/adl-file-parsers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateAdlFileParser() throws Exception {
        // Initialize the database
        adlFileParserService.save(adlFileParser);

        int databaseSizeBeforeUpdate = adlFileParserRepository.findAll().size();

        // Update the adlFileParser
        AdlFileParser updatedAdlFileParser = new AdlFileParser();
        updatedAdlFileParser.setId(adlFileParser.getId());
        updatedAdlFileParser.setArchetypeID(UPDATED_ARCHETYPE_ID);

        restAdlFileParserMockMvc.perform(put("/api/adl-file-parsers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAdlFileParser)))
                .andExpect(status().isOk());

        // Validate the AdlFileParser in the database
        List<AdlFileParser> adlFileParsers = adlFileParserRepository.findAll();
        assertThat(adlFileParsers).hasSize(databaseSizeBeforeUpdate);
        AdlFileParser testAdlFileParser = adlFileParsers.get(adlFileParsers.size() - 1);
        assertThat(testAdlFileParser.getArchetypeID()).isEqualTo(UPDATED_ARCHETYPE_ID);
    }

    @Test
    public void deleteAdlFileParser() throws Exception {
        // Initialize the database
        adlFileParserService.save(adlFileParser);

        int databaseSizeBeforeDelete = adlFileParserRepository.findAll().size();

        // Get the adlFileParser
        restAdlFileParserMockMvc.perform(delete("/api/adl-file-parsers/{id}", adlFileParser.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<AdlFileParser> adlFileParsers = adlFileParserRepository.findAll();
        assertThat(adlFileParsers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
