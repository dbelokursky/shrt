package ru.dbelokursky.shrt.service;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.dbelokursky.shrt.domain.Url;
import ru.dbelokursky.shrt.repository.UrlRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UrlServiceImpl implements UrlService{

    private final UrlRepository urlRepository;

    @Autowired
    public UrlServiceImpl(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Override
    public List<Url> findAll() {
        return Lists.newArrayList(urlRepository.findAll());
    }

    @Override
    public Optional<Url> findById(Long id) {
        return urlRepository.findById(id);
    }

    @Override
    public void save(Url url) {
        urlRepository.save(url);
    }
}
