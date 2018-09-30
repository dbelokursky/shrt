package ru.dbelokursky.shrt.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.dbelokursky.shrt.domain.Url;
import ru.dbelokursky.shrt.service.UrlService;

import java.sql.Date;
import java.util.Arrays;
import java.util.HashSet;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UrlController.class, secure = false)
public class UrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

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