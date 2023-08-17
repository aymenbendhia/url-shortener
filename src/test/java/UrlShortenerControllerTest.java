import com.notarius.UrlShortenerApplication;
import com.notarius.service.UrlShortenerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = UrlShortenerApplication.class)
@AutoConfigureMockMvc
public class UrlShortenerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlShortenerService service;

    @Test
    public void originalUrlShouldReturnMessageFromService() throws Exception {
        when(service.getOriginalUrl("http://fakedomain.com/full-url")).thenReturn("http://fakedomain.com/1");
        this.mockMvc.perform(post("/api/originalurl").content("http://fakedomain.com/full-url")).andExpect(status().isOk())
                .andExpect(content().string(containsString("http://fakedomain.com/1")));
    }


    @Test
    public void originalUrlShouldGenerateBadRequest() throws Exception {
        when(service.getOriginalUrl("http://fakedomain.com/full-url-loooooong")).thenThrow(new RuntimeException("Shortened URL handled part length should not be greater than 10 characters"));
        this.mockMvc.perform(post("/api/originalurl").content("http://fakedomain.com/full-url-loooooong")).andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Shortened URL handled part length should not be greater than 10 characters")));
    }

    @Test
    public void generateShortUrlShouldReturnMessageFromService() throws Exception {
        when(service.generateShortUrl("http://fakedomain.com/1")).thenReturn("http://fakedomain.com/full-url");
        this.mockMvc.perform(post("/api/shorturl").content("http://fakedomain.com/1")).andExpect(status().isOk())
                .andExpect(content().string(containsString("http://fakedomain.com/full-url")));
    }

    @Test
    public void generateShortUrlShouldGenerateBadRequest() throws Exception {
        when(service.generateShortUrl("fakeprotocol://fakedomain.com/1")).thenThrow(new RuntimeException("Invalid URL"));
        this.mockMvc.perform(post("/api/shorturl").content("fakeprotocol://fakedomain.com/1")).andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Invalid URL")));
    }

}
