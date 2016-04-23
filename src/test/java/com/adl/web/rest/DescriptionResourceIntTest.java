package com.adl.web.rest;

import com.adl.AdlviewerApp;
import com.adl.domain.Description;
import com.adl.repository.DescriptionRepository;
import com.adl.service.DescriptionService;

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
 * Test class for the DescriptionResource REST controller.
 *
 * @see DescriptionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AdlviewerApp.class)
@WebAppConfiguration
@IntegrationTest
public class DescriptionResourceIntTest {

    private static final String DEFAULT_CONCEPT = "AAAAA";
    private static final String UPDATED_CONCEPT = "BBBBB";
    private static final String DEFAULT_LONG_CONCEPT = "AAAAA";
    private static final String UPDATED_LONG_CONCEPT = "BBBBB";
    private static final String DEFAULT_AUTHOR = "AAAAA";
    private static final String UPDATED_AUTHOR = "BBBBB";

    @Inject
    private DescriptionRepository descriptionRepository;

    @Inject
    private DescriptionService descriptionService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDescriptionMockMvc;

    private Description description;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DescriptionResource descriptionResource = new DescriptionResource();
        ReflectionTestUtils.setField(descriptionResource, "descriptionService", descriptionService);
        this.restDescriptionMockMvc = MockMvcBuilders.standaloneSetup(descriptionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        descriptionRepository.deleteAll();
        description = new Description();
        description.setConcept(DEFAULT_CONCEPT);
        description.setLongConcept(DEFAULT_LONG_CONCEPT);
        description.setAuthor(DEFAULT_AUTHOR);
    }

    @Test
    public void createDescription() throws Exception {
        int databaseSizeBeforeCreate = descriptionRepository.findAll().size();

        // Create the Description

        restDescriptionMockMvc.perform(post("/api/descriptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(description)))
                .andExpect(status().isCreated());

        // Validate the Description in the database
        List<Description> descriptions = descriptionRepository.findAll();
        assertThat(descriptions).hasSize(databaseSizeBeforeCreate + 1);
        Description testDescription = descriptions.get(descriptions.size() - 1);
        assertThat(testDescription.getConcept()).isEqualTo(DEFAULT_CONCEPT);
        assertThat(testDescription.getLongConcept()).isEqualTo(DEFAULT_LONG_CONCEPT);
        assertThat(testDescription.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
    }

    @Test
    public void getAllDescriptions() throws Exception {
        // Initialize the database
        descriptionRepository.save(description);

        // Get all the descriptions
        restDescriptionMockMvc.perform(get("/api/descriptions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(description.getId())))
                .andExpect(jsonPath("$.[*].concept").value(hasItem(DEFAULT_CONCEPT.toString())))
                .andExpect(jsonPath("$.[*].longConcept").value(hasItem(DEFAULT_LONG_CONCEPT.toString())))
                .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR.toString())));
    }

    @Test
    public void getDescription() throws Exception {
        // Initialize the database
        descriptionRepository.save(description);

        // Get the description
        restDescriptionMockMvc.perform(get("/api/descriptions/{id}", description.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(description.getId()))
            .andExpect(jsonPath("$.concept").value(DEFAULT_CONCEPT.toString()))
            .andExpect(jsonPath("$.longConcept").value(DEFAULT_LONG_CONCEPT.toString()))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR.toString()));
    }

    @Test
    public void getNonExistingDescription() throws Exception {
        // Get the description
        restDescriptionMockMvc.perform(get("/api/descriptions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateDescription() throws Exception {
        // Initialize the database
        descriptionService.save(description);

        int databaseSizeBeforeUpdate = descriptionRepository.findAll().size();

        // Update the description
        Description updatedDescription = new Description();
        updatedDescription.setId(description.getId());
        updatedDescription.setConcept(UPDATED_CONCEPT);
        updatedDescription.setLongConcept(UPDATED_LONG_CONCEPT);
        updatedDescription.setAuthor(UPDATED_AUTHOR);

        restDescriptionMockMvc.perform(put("/api/descriptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDescription)))
                .andExpect(status().isOk());

        // Validate the Description in the database
        List<Description> descriptions = descriptionRepository.findAll();
        assertThat(descriptions).hasSize(databaseSizeBeforeUpdate);
        Description testDescription = descriptions.get(descriptions.size() - 1);
        assertThat(testDescription.getConcept()).isEqualTo(UPDATED_CONCEPT);
        assertThat(testDescription.getLongConcept()).isEqualTo(UPDATED_LONG_CONCEPT);
        assertThat(testDescription.getAuthor()).isEqualTo(UPDATED_AUTHOR);
    }

    @Test
    public void deleteDescription() throws Exception {
        // Initialize the database
        descriptionService.save(description);

        int databaseSizeBeforeDelete = descriptionRepository.findAll().size();

        // Get the description
        restDescriptionMockMvc.perform(delete("/api/descriptions/{id}", description.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Description> descriptions = descriptionRepository.findAll();
        assertThat(descriptions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
