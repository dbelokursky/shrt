package ru.dbelokursky.shrt.controller;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.dbelokursky.shrt.domain.Url;
import ru.dbelokursky.shrt.service.UrlService;

import java.sql.Date;
import java.util.Arrays;
import java.util.HashSet;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UrlController.class, secure = false)
public class UrlControllerTest {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(document("{method-name}",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .build();
    }

    @MockBean
    private UrlService urlService;

    private final Url url = Url.builder()
            .url("https://habr.com/company/jugru/")
            .redirectCode(302)
            .hash("4d789a4b")
            .clickCounter(0)
            .publicationDate(new Date(System.currentTimeMillis()))
            .build();

    private final String request = "{\"url\": \"https://habr.com/company/jugru/\"}";

    @Test
    public void shouldReturnShortenUrl() throws Exception {
        Url url = Url.builder().url("https://habr.com/company/jugru/").build();

        when(urlService.save(url)).thenReturn(url);

        this.mockMvc.perform(
                post("/register")
                        .accept(MediaType.APPLICATION_JSON).content(request)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        verify(urlService, times(1)).save(url);
    }

    @Test
    public void whenRequestWithShortUrlThanRedirectToOriginalUrl() throws Exception {
        when(urlService.findByHash(url.getHash())).thenReturn(new HashSet<>(Arrays.asList(url)));

        this.mockMvc.perform(get("/" + url.getHash()).accept(MediaType.TEXT_HTML))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(url.getUrl()));
    }
}