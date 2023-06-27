package com.spandana.batchprocessingdemo.config;

import com.spandana.batchprocessingdemo.entity.Customer;
import com.spandana.batchprocessingdemo.repository.CustomerRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;


@Configuration //@Configuration. This annotation tells Spring that this class contains bean definitions.
//@EnableBatchProcessing //The @EnableBatchProcessing annotation is a convenient way to enable Spring Batch features in a Spring application. // It generates a constructor for a class that includes parameters for all of its fields.
public class SpringbatchConfig extends DefaultBatchConfiguration{


    private CustomerRepository customerRepository;
    @Bean
    public ItemReader<Customer> itemReader() {
        FlatFileItemReader<Customer> reader = new FlatFileItemReader<>();
        // FlatFileItemReader implementation in ItemReader interface to read files (CSV/text files etc)
        reader.setResource(new ClassPathResource("Organization.csv")); // Specify your CSV file path
        reader.setLinesToSkip(1); // Skip the header row
        DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();
        //The line mapper is responsible for parsing each line from the CSV file and mapping it to an object of type Organization.
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        //DelimitedLineTokenizer to tokenize each line based on a delimiter (by default, it uses a comma as the delimiter)
        tokenizer.setNames(new String[]{"Index", "Customer_ID", "First_Name", "Last_Name", "Company"
        , "City", "Phone 1", "Phone 2", "Email", "Subscription_Date", "Website"}); // Specify column names
        BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        //BeanWrapperFieldSetMapper and set its targetType to Customer.class.
        // This mapper is responsible for mapping the tokens to the properties of the Customer class.
        fieldSetMapper.setTargetType(Customer.class);
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        reader.setLineMapper(lineMapper);
        return reader;
    }

    @Bean
    public ItemProcessor<Customer, Customer> itemProcessor() {
        return new ItemProcessor<Customer, Customer>() {
            @Override
            public Customer process(Customer Customer) throws Exception {
                // Perform any data transformation or validation if needed
                return Customer;
            }
        };
    }

    @Bean
    public ItemWriter<Customer> itemWriter(JpaRepository<Customer, Integer> customerRepository) {
        RepositoryItemWriter<Customer> writer = new RepositoryItemWriter<>();
        writer.setRepository(customerRepository);
        writer.setMethodName("save"); // Specify the save method in the repository
        return writer;
    }


    @Bean
    public Step step(ItemReader<Customer> itemReader, ItemProcessor<Customer, Customer> itemProcessor, ItemWriter<Customer> itemWriter) {
        return new StepBuilder("step")
                .<Customer, Customer>chunk(10)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }


    @Bean
    public Job job(Step step, JobRepository jobRepository) throws Exception {
        return new JobBuilder("job", jobRepository)
                .flow(step)
                .end()
                .build();
    }
}
