package ru.dbelokursky.shrt.service;

import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.dbelokursky.shrt.domain.Url;
import ru.dbelokursky.shrt.repository.UrlRepository;
import ru.dbelokursky.shrt.repository.UserRepository;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UrlServiceImpl implements UrlService{

    private final UrlRepository urlRepository;

    private final UserRepository userRepository;

    @Autowired
    public UrlServiceImpl(UrlRepository urlRepository, UserRepository userRepository) {
        this.urlRepository = urlRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Url> findAll() {
        return Lists.newArrayList(urlRepository.findAll());
    }

    @Override
    public Optional<Url> findById(Long id) {
        return urlRepository.findById(id);
    }

    /**
     * @see <a href="https://en.wikipedia.org/wiki/MurmurHash#MurmurHash3">MurmurHash3</a>
     */
    @Override
    public void save(Url url) {
        String originalUrl = url.getOriginalUrl();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (new UrlValidator().isValid(originalUrl)) {
            String hash = Hashing.murmur3_32().hashString(originalUrl, StandardCharsets.UTF_8).toString();
            url.setHash(hash);
            url.setClickCounter(0);
            url.setPublicationDate(new Date(System.currentTimeMillis()));
            if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
                url.setUser(userRepository.findByLogin(authentication.getName()));
            }
            urlRepository.save(url);
        }
    }
}
