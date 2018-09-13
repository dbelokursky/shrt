package ru.dbelokursky.shrt.domain;

import lombok.Data;
import org.checkerframework.checker.units.qual.C;

import javax.persistence.*;
import java.sql.Date;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@Entity
@Table(name = "url")
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "url_id")
    private Long id;

    @Column(name = "original_url")
    private String originalUrl;

    @Column(name = "short_url")
    private String shortUrl;

    @Column(name = "publication_date")
    private Date publicationDate;

    @Column(name = "click_counter")
    private AtomicInteger clickCounter;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
