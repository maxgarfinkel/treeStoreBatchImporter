package com.maxgarfinkel.treestorebatchimporter;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.IncorrectTokenCountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    // tag::readerwriterprocessor[]
    @Bean
    public FlatFileItemReader<GLA_Tree> reader(){
        FlatFileItemReader<GLA_Tree> flatFileItemReader = new FlatFileItemReaderBuilder<GLA_Tree>()
            .name("glaTreeReader")
            .resource(new ClassPathResource("london_street_trees_gla_20180214.csv"))
            .delimited()
            .names(new String[]{"gla_id",
                    "borough",
                    "species_name",
                    "common_name",
                    "display_name",
                    "load_date",
                    "easting",
                    "northing",
                    "longitude",
                    "latitude"})
            .fieldSetMapper(new BeanWrapperFieldSetMapper<GLA_Tree>() {{
                setTargetType(GLA_Tree.class);
            }})
            .build();
        flatFileItemReader.setLinesToSkip(1);
        return flatFileItemReader;
    }

    @Bean
    public GLA_TreeProcessor processor() {
        return new GLA_TreeProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Tree> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Tree>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO tree (uuid, latinName, commonName, lat, lon) VALUES (:uuid, :latinName, :commonName, :lat, :lon)")
                .dataSource(dataSource)
                .build();
    }
    // end::readerwriterprocessor[]

    // tag::jobstep[]
    @Bean
    public Job importGLA_TreeJob(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importGLA_TreeJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<Tree> writer) {
        return stepBuilderFactory.get("step1")
                .<GLA_Tree, Tree> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(1000000)
                .build();
    }
    // end::jobstep[]
}

