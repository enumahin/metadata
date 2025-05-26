package com.alienworkspace.cdr.metadata.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA Auditing Configuration.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class JpaAuditingConfiguration {
}

