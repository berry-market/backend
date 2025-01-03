package com.berry.bid.infrastructure.config;


import com.querydsl.sql.Configuration;
import com.querydsl.sql.MySQLTemplates;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.codegen.MetaDataExporter;
import com.querydsl.sql.spring.SpringConnectionProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.io.File;
import java.sql.SQLException;

@org.springframework.context.annotation.Configuration
@RequiredArgsConstructor
public class QuerydslConfig {

    private final DataSource dataSource;

    @Bean
    public SQLQueryFactory queryFactory() throws SQLException {
        Configuration configuration = new Configuration(new MySQLTemplates()); // MySQL Dialect 설정
        return new SQLQueryFactory(configuration, new SpringConnectionProvider(dataSource));
    }

    @Bean
    public MetaDataExporter exporter() throws SQLException {
        MetaDataExporter exporter = new MetaDataExporter();
        exporter.setPackageName("com.berry.bid.querydsl");
        exporter.setTargetFolder(new File("src/main/generated"));
        exporter.export(dataSource.getConnection().getMetaData());
        return exporter;
    }

}
