import com.notarius.model.DomainPart;
import com.notarius.model.HandledPart;
import com.notarius.repository.DomainPartRepository;
import com.notarius.repository.HandledPartRepository;
import com.notarius.service.UrlShortenerService;
import com.notarius.service.UrlShortenerServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;


@ContextConfiguration(classes = UrlShortenerServiceImpl.class)
@RunWith(SpringRunner.class)
public class UrlShortenerServiceTest {

    public static final int MAX_HANDLED_PART_LENGTH = 10;
    @Autowired
    private UrlShortenerService service;

    @MockBean
    private DomainPartRepository domainPartRepository;

    @MockBean
    private HandledPartRepository handledPartRepository;

    @Test
    public void testGenerateShortUrl_whenShortenedUrlNotGenerated_thenHandledPartShouldBeCalculatedAndReturned() {
        Mockito.when(handledPartRepository.findHandledPartByOriginalHandledPart("/part1/part2"))
                .thenReturn(null);

        Mockito.when(domainPartRepository.findDomainPartByFixedUrl("http://www.fixedurl.ca"))
                .thenReturn(null);

        HandledPart handledPart = new HandledPart("/part1/part2");
        handledPart.setId(1L);
        Mockito.when(handledPartRepository.save(any(HandledPart.class)))
                .thenReturn(handledPart);

        String shortenedUrl = service.generateShortUrl("http://www.fixedurl.ca/part1/part2");
        Assert.assertEquals(shortenedUrl, "http://www.fixedurl.ca/û");
    }

    @Test
    public void testGenerateShortUrl_whenShortenedUrlNotGenerated_thenHandledPartShouldBeCalculatedAndReturned_MaximumLength() {

        Mockito.when(domainPartRepository.findDomainPartByFixedUrl("http://www.fixedurl.ca"))
                .thenReturn(null);

        Mockito.when(handledPartRepository.findHandledPartByOriginalHandledPart("/part1/part2"))
                .thenReturn(null);

        HandledPart handledPart = new HandledPart("/part1/part2");
        handledPart.setId(9223372036854775807L);
        Mockito.when(handledPartRepository.save(any(HandledPart.class)))
                .thenReturn(handledPart);
        String shortenedUrl = service.generateShortUrl("http://www.fixedurl.ca/part1/part2");
        Assert.assertEquals(shortenedUrl, "http://www.fixedurl.ca/ánBkAÚL<¥ô");
        Assert.assertTrue(shortenedUrl.length() <= "http://www.fixedurl.ca/".length() + MAX_HANDLED_PART_LENGTH);
    }

    @Test
    public void testGenerateShortUrl_whenShortenedUrlNotGenerated_thenHandledPartShouldBeCalculatedAndReturned_EvenLength() {

        Mockito.when(domainPartRepository.findDomainPartByFixedUrl("http://www.fixedurl.ca"))
                .thenReturn(null);

        Mockito.when(handledPartRepository.findHandledPartByOriginalHandledPart("/part1/part2"))
                .thenReturn(null);

        HandledPart handledPart = new HandledPart("/part1/part2");
        handledPart.setId(233720368547758007L);
        Mockito.when(handledPartRepository.save(any(HandledPart.class)))
                .thenReturn(handledPart);
        String shortenedUrl = service.generateShortUrl("http://www.fixedurl.ca/part1/part2");
        Assert.assertEquals(shortenedUrl, "http://www.fixedurl.ca/nBkAÚL<¥7");
        Assert.assertTrue(shortenedUrl.length() <= "http://www.fixedurl.ca/".length() + MAX_HANDLED_PART_LENGTH);
    }


    @Test
    public void testGenerateShortUrl_whenHandledPartAlreadyGeneratedAndDomainPartNotGenerated_thenShortenedUrlShouldBeReturned() {
        HandledPart initialHandledPart = new HandledPart("/part1/part2");
        initialHandledPart.setId(1L);
        Mockito.when(handledPartRepository.findHandledPartByOriginalHandledPart("/part1/part2"))
                .thenReturn(initialHandledPart);

        Mockito.when(domainPartRepository.findDomainPartByFixedUrl("http://www.fixedurl.ca"))
                .thenReturn(null);

        HandledPart handledPart = new HandledPart("/part1/part2");
        handledPart.setId(1L);
        Mockito.when(handledPartRepository.save(any(HandledPart.class)))
                .thenReturn(handledPart);
        String shortenedUrl = service.generateShortUrl("http://www.fixedurl.ca/part1/part2");
        Assert.assertEquals(shortenedUrl, "http://www.fixedurl.ca/û");
    }

    @Test
    public void testGenerateShortUrl_whenHandledPartAlreadyGeneratedAndDomainPartAlreadyGenerated_thenShortenedUrlShouldBeReturned() {
        HandledPart initialHandledPart = new HandledPart("/part1/part2");
        initialHandledPart.setId(1L);

        DomainPart domainPart = new DomainPart("http://www.fixedurl.ca");
        domainPart.setId(1L);

        initialHandledPart.getDomainParts().add(domainPart);

        Mockito.when(handledPartRepository.findHandledPartByOriginalHandledPart("/part1/part2"))
                .thenReturn(initialHandledPart);

        Mockito.when(domainPartRepository.findDomainPartByFixedUrl("http://www.fixedurl.ca"))
                .thenReturn(domainPart);

        HandledPart handledPart = new HandledPart("/part1/part2");
        handledPart.setId(1L);
        Mockito.when(handledPartRepository.save(any(HandledPart.class)))
                .thenReturn(handledPart);
        String shortenedUrl = service.generateShortUrl("http://www.fixedurl.ca/part1/part2");
        Assert.assertEquals(shortenedUrl, "http://www.fixedurl.ca/û");
    }

    @Test
    public void testGenerateShortUrl_whenInvalidUrl_thenInvalidUrlExceptionRaised() {
        Mockito.when(handledPartRepository.findHandledPartByOriginalHandledPart("/part1/part2"))
                .thenReturn(null);

        Mockito.when(domainPartRepository.findDomainPartByFixedUrl("dddfff://www.fixedurl.ca"))
                .thenReturn(null);

        HandledPart handledPart = new HandledPart("/part1/part2");
        handledPart.setId(1L);
        Mockito.when(handledPartRepository.save(any(HandledPart.class)))
                .thenReturn(handledPart);

        try {
            service.generateShortUrl("dddfff://www.fixedurl.ca/part1/part2");
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertEquals(e.getMessage(), "Invalid URL");
        }
    }


    @Test
    public void testGetOriginalUrl_happy_path() {
        HandledPart handledPart = new HandledPart("/part1/part2");
        handledPart.setId(1L);
        DomainPart domainPart = new DomainPart("http://www.fixedurl.ca");
        domainPart.setId(1L);

        handledPart.getDomainParts().add(domainPart);

        Mockito.when(handledPartRepository.findById(1L))
                .thenReturn(Optional.of(handledPart));

        String originalUrl = service.getOriginalUrl("http://www.fixedurl.ca/1");

        Assert.assertEquals(originalUrl, "http://www.fixedurl.ca/part1/part2");

    }

    @Test
    public void testGetOriginalUrl_happyPathWithMaxLength() {
        HandledPart handledPart = new HandledPart("/part1/part2");
        handledPart.setId(9223372036854775807L);
        DomainPart domainPart = new DomainPart("http://www.fixedurl.ca");
        domainPart.setId(1L);

        handledPart.getDomainParts().add(domainPart);

        Mockito.when(handledPartRepository.findById(9223372036854775807L))
                .thenReturn(Optional.of(handledPart));

        String originalUrl = service.getOriginalUrl("http://www.fixedurl.ca/ánBkAÚL<¥ô");

        Assert.assertEquals(originalUrl, "http://www.fixedurl.ca/part1/part2");


    }

    @Test
    public void testGetOriginalUrl_happyPathWithMaxLength_evenLength() {
        HandledPart handledPart = new HandledPart("/part1/part2");
        handledPart.setId(9223372036854775807L);
        DomainPart domainPart = new DomainPart("http://www.fixedurl.ca");
        domainPart.setId(1L);

        handledPart.getDomainParts().add(domainPart);

        Mockito.when(handledPartRepository.findById(233720368547758007L))
                .thenReturn(Optional.of(handledPart));

        String originalUrl = service.getOriginalUrl("http://www.fixedurl.ca/nBkAÚL<¥7");

        Assert.assertEquals(originalUrl, "http://www.fixedurl.ca/part1/part2");


    }

    @Test
    public void testGetOriginalUrl_happy_path_multipleDomainsWithSameHandledPart() {
        HandledPart handledPart = new HandledPart("/part1/part2");
        handledPart.setId(1L);
        DomainPart domainPart = new DomainPart("http://www.fixedurl.ca");
        domainPart.setId(1L);

        DomainPart domainPartTwo = new DomainPart("http://www.fixedurl2.ca");
        domainPartTwo.setId(2L);

        handledPart.getDomainParts().add(domainPart);
        handledPart.getDomainParts().add(domainPartTwo);

        Mockito.when(handledPartRepository.findById(1L))
                .thenReturn(Optional.of(handledPart));

        String originalUrl = service.getOriginalUrl("http://www.fixedurl2.ca/1");

        Assert.assertEquals(originalUrl, "http://www.fixedurl2.ca/part1/part2");

    }

    @Test
    public void testGetOriginalUrl_whenDomainDoesNotExist_thenExceptionRaised() {
        HandledPart handledPart = new HandledPart("/part1/part2");
        handledPart.setId(1L);
        DomainPart domainPart = new DomainPart("http://www.fixedurl.ca");
        domainPart.setId(1L);

        DomainPart domainPartTwo = new DomainPart("http://www.fixedurl2.ca");
        domainPartTwo.setId(2L);

        handledPart.getDomainParts().add(domainPart);
        handledPart.getDomainParts().add(domainPartTwo);

        Mockito.when(handledPartRepository.findById(1L))
                .thenReturn(Optional.of(handledPart));

        try {
            service.getOriginalUrl("http://www.fixedurl3.ca/1");
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertEquals(e.getMessage(), "Short URL not found");
        }


    }

    @Test
    public void testGetOriginalUrl_whenHandledPartDoesNotExist_thenExceptionRaised() {
        HandledPart handledPart = new HandledPart("/part1/part2");
        handledPart.setId(1L);
        DomainPart domainPart = new DomainPart("http://www.fixedurl.ca");
        domainPart.setId(1L);

        DomainPart domainPartTwo = new DomainPart("http://www.fixedurl2.ca");
        domainPartTwo.setId(2L);

        handledPart.getDomainParts().add(domainPart);
        handledPart.getDomainParts().add(domainPartTwo);

        Mockito.when(handledPartRepository.findById(1L))
                .thenReturn(Optional.of(handledPart));

        try {
            service.getOriginalUrl("http://www.fixedurl2.ca/2");
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertEquals(e.getMessage(), "Short URL not found");
        }


    }

    @Test
    public void testGetOriginalUrl_whenHandledPartTooLong_thenExceptionRaised() {
        HandledPart handledPart = new HandledPart("/part1/part2");
        handledPart.setId(1L);
        DomainPart domainPart = new DomainPart("http://www.fixedurl.ca");
        domainPart.setId(1L);

        DomainPart domainPartTwo = new DomainPart("http://www.fixedurl2.ca");
        domainPartTwo.setId(2L);

        handledPart.getDomainParts().add(domainPart);
        handledPart.getDomainParts().add(domainPartTwo);

        Mockito.when(handledPartRepository.findById(1L))
                .thenReturn(Optional.of(handledPart));

        try {
            service.getOriginalUrl("http://www.fixedurl2.ca/0123456789+");
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertEquals(e.getMessage(), "Shortened URL handled part length should not be greater than 10 characters");
        }


    }
}
