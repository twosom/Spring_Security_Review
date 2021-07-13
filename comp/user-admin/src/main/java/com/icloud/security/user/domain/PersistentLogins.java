package com.icloud.security.user.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * create table persistent_logins ( <br/>
 * username varchar(64) not null, <br/>
 * series varchar(64) primary key, <br/>
 * token varchar(64) not null, <br/>
 * last_used timestamp not null <br/>
 * )
 */
@Entity
@Data
@Table(name = "persistent_logins")
public class PersistentLogins {

    @Id
    @Column(name = "series", length = 64)
    private String series;

    @Column(columnDefinition = "varchar", length = 64, nullable = false)
    private String username;

    @Column(columnDefinition = "varchar", length = 64, nullable = false)
    private String token;

    @Column(columnDefinition = "timestamp", nullable = false, name = "last_used")
    private LocalDateTime lastUsed;
}
