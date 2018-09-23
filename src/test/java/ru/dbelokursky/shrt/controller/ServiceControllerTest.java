package ru.dbelokursky.shrt.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest(ServiceController.class)
public class ServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "user1")
    public void shouldReturnPageNotFound() throws Exception {
        this.mockMvc.perform(get("/404").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("notFound"));
    }

    @Test
    @WithMockUser(username = "user1")
    public void shouldReturnAccessDeniedPage() throws Exception {
        this.mockMvc.perform(get("/403").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("accessDenied"));
    }

    @Test
    @WithMockUser(username = "user1")
    public void shouldReturnIndexPage() throws Exception {
        this.mockMvc.perform(get("/index").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }
}