package com.ag.ara.configuration;

import java.util.Properties;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.ag.ara")
@EnableTransactionManagement
@PropertySource(value={"classpath:hibernate.properties"})

public class HelloWorldConfiguration extends WebMvcConfigurerAdapter{
	
    
 private static final String PROPERTY_NAME_DATABASE_DRIVER = "db.driver";
 private static final String PROPERTY_NAME_DATABASE_PASSWORD = "db.password";
 private static final String PROPERTY_NAME_DATABASE_URL = "db.url";
 private static final String PROPERTY_NAME_DATABASE_USERNAME = "db.username";
      
 private static final String PROPERTY_NAME_HIBERNATE_DIALECT = "hibernate.dialect";
 private static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL = "hibernate.show_sql";
  
 @Resource
 private Environment env;
  
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		registry.viewResolver(viewResolver);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("/static/");
		registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/META-INF/resources/webjars/").setCachePeriod(31556926);
        registry.addResourceHandler("/css/**").addResourceLocations("/css/").setCachePeriod(31556926);
        registry.addResourceHandler("/img/**").addResourceLocations("/img/").setCachePeriod(31556926);
        registry.addResourceHandler("/js/**").addResourceLocations("/js/").setCachePeriod(31556926);
	}
	
	@Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
	
	
	 @Bean(name = "datasource")
	    public  DataSource dataSource() {
		 DriverManagerDataSource dataSource = new DriverManagerDataSource();
         
         dataSource.setDriverClassName(env.getRequiredProperty(PROPERTY_NAME_DATABASE_DRIVER));
         dataSource.setUrl(env.getRequiredProperty(PROPERTY_NAME_DATABASE_URL));
         dataSource.setUsername(env.getRequiredProperty(PROPERTY_NAME_DATABASE_USERNAME));
         dataSource.setPassword(env.getRequiredProperty(PROPERTY_NAME_DATABASE_PASSWORD));
          
         return dataSource;
	     /*   BasicDataSource ds = new BasicDataSource();
	        ds.setDriverClassName("com.mysql.jdbc.Driver");
	        ds.setUrl("jdbc:mysql://localhost:3306/test");
	        ds.setUsername("root");
	        ds.setPassword("root");

	        return ds;*/
	    }
	 @Bean
	    public LocalSessionFactoryBean sessionFactory(){
	        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
	        sessionFactory.setDataSource(dataSource());
	        sessionFactory.setPackagesToScan(new String[]{"com.ag.ara.model"});
	        sessionFactory.setHibernateProperties(hibernateProperties());
	        return sessionFactory;
	        
	    }
	 private Properties hibernateProperties() {
		 Properties properties = new Properties();
         properties.put(PROPERTY_NAME_HIBERNATE_DIALECT, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_DIALECT));
         properties.put(PROPERTY_NAME_HIBERNATE_SHOW_SQL, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_SHOW_SQL));
         return properties;       
	    }
	 @Bean
	    @Autowired
	    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory){
	        HibernateTransactionManager tx = new HibernateTransactionManager();
	        tx.setSessionFactory(sessionFactory);
	        return tx;
	    }
	 
	 @Override
     public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
             configurer.favorPathExtension(true)
                     .useJaf(false)
                     .ignoreAcceptHeader(true)
                     .mediaType("html", MediaType.TEXT_HTML)
                     .mediaType("json", MediaType.APPLICATION_JSON)
                     .defaultContentType(MediaType.TEXT_HTML);
     }

}