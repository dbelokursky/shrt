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
import ru.dbelokursky.shrt.domain.User;
import ru.dbelokursky.shrt.repository.UrlRepository;
import ru.dbelokursky.shrt.repository.UserRepository;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;

    private final UserRepository userRepository;

    @Autowired
    public UrlServiceImpl(UrlRepository urlRepository, UserRepository userRepository) {
        this.urlRepository = urlRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Set<Url> findByUserId(Long userId) {
        return urlRepository.findByUserId(userId);
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
    public Url save(Url url) {
        Url result = Url.builder().build();
        if (new UrlValidator().isValid(url.getUrl())) {
            String hash = Hashing.murmur3_32().hashString(url.getUrl(), StandardCharsets.UTF_8).toString();

            //If authenticated user is present.
            if (getAuthenticatedUser().isPresent()) {
                User user = getAuthenticatedUser().get();
                if (urlRepository.findByHashAndUser(hash, user).isPresent()) {
                    result = urlRepository.findByHashAndUser(hash, user).get();
                } else {
                    setUpUrl(url, result, hash, user);
                    urlRepository.save(result);
                }
                //If anonymous user.
            } else {
                if (!urlRepository.findByHash(hash).isEmpty()) {
                    result = urlRepository.findByHash(hash).iterator().next();
                } else {
                    setUpUrl(url, result, hash, null);
                    urlRepository.save(result);
                }
            }
        }
        return result;
    }

    private void setUpUrl(Url url, Url result, String hash, User user) {
        result.setUrl(url.getUrl());
        result.setHash(hash);
        result.setClickCounter(0);
        result.setPublicationDate(new Date(System.currentTimeMillis()));
        result.setUser(user);
        if (url.getRedirectCode() == null) {
            result.setRedirectCode(HttpServletResponse.SC_MOVED_TEMPORARILY);
        } else {
            result.setRedirectCode(url.getRedirectCode());
        }
    }

    @Override
    public Url incrementCounter(Url url) {
        url.setClickCounter(url.getClickCounter() + 1);
        urlRepository.save(url);
        return url;
    }

    @Override
    public Set<Url> findByHash(String hash) {
        return urlRepository.findByHash(hash);
    }

    @Override
    public Optional<Url> findByHashAndLogin(String hash, User user) {
        return urlRepository.findByHashAndUser(hash, user);
    }

    private Optional<User> getAuthenticatedUser() {
        Optional<User> user = Optional.empty();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            user = userRepository.findByLogin(authentication.getName());
        }
        return user;
    }
}
