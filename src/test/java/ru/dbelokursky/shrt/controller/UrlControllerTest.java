package ru.dbelokursky.shrt.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.view.RedirectView;
import ru.dbelokursky.shrt.domain.Url;
import ru.dbelokursky.shrt.service.UrlService;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UrlController.class, secure = false)
public class UrlControllerTest {

    private final Url url = Url.builder().url("https://habr.com/company/jugru/").redirectCode(302).build();

    private final String request = "{" +
                    "\"url\": \"https://habr.com/company/jugru/\"" +
                    "}";

    private final String response = "{" +
                    "\"shortUrl\": \"http://localhost:8080/4d789a4b\"" +
                    "}";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlController urlController;

    @MockBean
    private UrlService urlService;

    @Test
    public void shouldReturnShortenUrl() throws Exception {
        given(this.urlService.save(url)).willReturn(url);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/register")
                .accept(MediaType.APPLICATION_JSON).content(request)
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
//        verify(urlService, times(1)).save(url);
    }

    //        request:
//            http://localhost/4d789a4b
//        response:
//            https://habr.com/company/jugru/
    @Test
    public void shouldReturnRedirect() throws Exception {
        String originalUrl = "https://habr.com/company/jugru/";
        String hash = "4d789a4b";
        when(urlController
                .redirect(hash))
                .thenReturn(new RedirectView(originalUrl));

        this.mockMvc.perform(get("/" + hash).accept(MediaType.TEXT_HTML))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(originalUrl));

    }
}