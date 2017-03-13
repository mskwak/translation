package kr.co.easymanual.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan("kr.co.easymanual")
@Lazy
@EnableTransactionManagement

//kr.co.easymanual.map 으로 설정하면 에러가 발생한다. 왜일까? -> 답: @MapperScan에서 Mapper는 xml 파일이 아니라 인터페이스로 정의된 java 파일이었다.
//ERROR o.s.web.servlet.DispatcherServlet - Context initialization failed
//org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'uploadBySomething': Injection of autowired dependencies failed; nested exception is org.springframework.beans.factory.BeanCreationException: Could not autowire field: private kr.co.easymanual.service.UploadedFileManager kr.co.easymanual.controller.UploadBySomething.uploadedFileManager; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'uploadedFileManager': Injection of autowired dependencies failed; nested exception is org.springframework.beans.factory.BeanCreationException: Could not autowire field: private kr.co.easymanual.dao.EmAttachmentsMapper kr.co.easymanual.service.UploadedFileManager.emAttachmentsMapper; nested exception is org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type [kr.co.easymanual.dao.EmAttachmentsMapper] found for dependency: expected at least 1 bean which qualifies as autowire candidate for this dependency. Dependency annotations: {@org.springframework.beans.factory.annotation.Autowired(required=true)}
//@MapperScan("kr.co.easymanual")
//@MapperScan("classpath:mapper/*.xml")
@MapperScan("kr.co.easymanual.dao")

//https://www.jayway.com/2014/02/16/spring-propertysource -> @PropertySource 및 @PropertySources 에 대해 쉽게 설명해 두었다. (영어)
//@PropertySource("file:${CONFIG_FILE}") 에서 $CONFIG_FILE은 쉘 환경변수 값을 의미한다.
//스프링4 부터 지원. @PropertySources에 classpath 및 file 을 동시에 2개 이상 설정 가능하도록 한다.
@PropertySources({
	@PropertySource("classpath:easymanual.properties"),
    @PropertySource(value = "file:${CONFIG_DIR}/easymanual.properties", ignoreResourceNotFound = true)
})
public class EnvConfig {

	@Value("${attachments.directory}") private String attachmentsDirectory;
	@Value("${datasource.driver.classname}") private String driverClassName;
	@Value("${datasource.url}") private String url;
	@Value("${datasource.username}") private String userName;
	@Value("${datasource.password}") private String password;
	@Value("${solr.server.address}") private String solrServerAddress;
	@Value("${packages.to.scan}") private String packagesToScan;
	@Value("${hibernate.dialect}") private String dialect;

	// org.apache.ibatis.binding.BindingException: Invalid bound statement (not found):
	// http://stackoverflow.com/questions/30081542/mybatis-spring-mvc-error-invalid-bound-statement-not-found
	@Autowired
	private ResourceLoader resourceLoader;

	@Bean(name = "attachmentsDirectory")
	public String getAttachmentsDirectory() {
		return this.attachmentsDirectory;
	}

	@Bean(name = "solrServerAddress")
	public String getSolrServerAddress() {
		return this.solrServerAddress;
	}

	// Tomcat이 제공하는 org.apache.tomcat.jdbc.pool 패키지 (tomcat-jdbc.jar)는 Apache Commons DBCP connection pool (이하 DBCP)을 대체하는 역할을 한다.
	// 다만 Tomcat 7.0 이상부터만 Tomcat JDBC를 제공하기 때문에 Tomcat 6.0까지는 여전히 DBCP를 사용해야 한다.
	@Bean(destroyMethod = "close")
	public DataSource dataSource() {
		BasicDataSource basicDataSource = new BasicDataSource();

		basicDataSource.setDriverClassName(this.driverClassName);
		basicDataSource.setUrl(this.url);
		basicDataSource.setUsername(this.userName);
		basicDataSource.setPassword(this.password);
		// Method org.postgresql.jdbc4.Jdbc4Connection.isValid(int) is not yet implemented
		basicDataSource.setValidationQuery("SELECT 1");

		return basicDataSource;
	}

//	@Bean(destroyMethod = "close")
//	public DataSource dataSource() {
//		org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
//		//configureDataSource(dataSource);
//		dataSource.setDriverClassName(this.driverClassName);
//		dataSource.setUrl(this.url);
//		dataSource.setUsername(this.userName);
//		dataSource.setPassword(this.password);
//		dataSource.setValidationQuery("SELECT 1");
//		return dataSource;
//	}


// http://zgundam.tistory.com/86
//	@Bean
//	public DataSourceTransactionManager transactionManager() {
//		return new DataSourceTransactionManager(this.dataSource());
//	}

	// 트랜잭션 검증 안된 상태. http://m.blog.naver.com/ck791024/40188108679 -> 검증 완료
	// @Transactional을 private 메소드에 적용해 봤자 동작하지 않는다.
	@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(this.dataSource());
	}

	// 웹에서 등록한 데이터가 데이터베이스에 INSERT 된다.
	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(this.dataSource());
		// org.apache.ibatis.binding.BindingException: Invalid bound statement (not found):
		// http://stackoverflow.com/questions/30081542/mybatis-spring-mvc-error-invalid-bound-statement-not-found
		sqlSessionFactoryBean.setMapperLocations(ResourcePatternUtils.getResourcePatternResolver(this.resourceLoader).getResources("classpath:mapper/*.xml"));
		return sqlSessionFactoryBean.getObject();
	}

	// http://mryong8.blogspot.kr/2014/11/spring-32-properties-message.html
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setDefaultEncoding("UTF-8");
		//messageSource.setBasename("/WEB-INF/messages/message.properties");
		// .properties 확장자가 없어야 한다.
		messageSource.setBasename("classpath:messages/message");
		return messageSource;
	}

	//destroyMethod 메소드는 언제 호출이 되는가? -> destroyMethod = "close" 했어도 명확히 close 됐다는 로그가 없다.
	//@Bean (name = "solrServer", destroyMethod = "close")
	@Bean (name = "solrServer")
	@Scope(value = "prototype")
	public HttpSolrClient getSolrServer() {
		String url = "http://" + this.solrServerAddress + ":8983/solr/tbx";
		return new HttpSolrClient(url);
	}

	//다른 빈에서 @Autowired private ThreadPoolTaskExecutor taskExcutor; 와 같이 사용할 수 있다.
	@Bean
	public ThreadPoolTaskExecutor taskExcutor()  {
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setCorePoolSize(10);
		threadPoolTaskExecutor.setMaxPoolSize(100);
		//threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
		threadPoolTaskExecutor.initialize();
		return threadPoolTaskExecutor;
	}

	@Bean
	public ThreadPoolTaskScheduler taskScheduler() {
		return new ThreadPoolTaskScheduler();
	}

//	@Bean
//	public RequestMappingHandlerAdapter messageConverters() {
//		RequestMappingHandlerAdapter requestMappingHandlerAdapter = new RequestMappingHandlerAdapter();
//		return requestMappingHandlerAdapter;
//	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		Properties jpaProperties = new Properties();
		// http://docs.jboss.org/hibernate/orm/4.3/manual/en-US/html_single/#configuration-optional-dialects 지원 가능한 방언 리스트
		//jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL9Dialect");
		jpaProperties.put("hibernate.dialect", this.dialect);

		// 하이버네이트가 실행한 SQL 쿼리를 출력한다.
		jpaProperties.put("hibernate.show_sql", true);

		// 하이버네이트가 실행한 SQL 쿼리를 출력할 때 보기 쉽게 정렬한다.
		jpaProperties.put("hibernate.format_sql", true);

		// 쿼리를 출력할 때 주석도 함께 출력한다.
		jpaProperties.put("hibernate.use_sql_comments", true);

		// 이 클래스는 테이블 명이나 컬럼 명이 생략되면 자바의 카멜 표기법을 테이블의 언더스코어 표기법으로 매핑한다.
		// -> 설정이 안 먹힌다.
		//jpaProperties.put("hibernate.ejb.naming_strategy", "org.hibernate.cfg.ImprovedNamingStrategy");
		//jpaProperties.put("hibernate.ejb.naming_strategy", "ImprovedNamingStrategy.INSTANCE");

		// JPA 표준에 맞춘 새로운 키 생성 전략을 사용한다.
		jpaProperties.put("hibernate.id.new_generator_mappings", true);

		// p.127 자바 ORM 표준 JPA 프로그래밍
		// create: 기존 테이블을 삭제하고 새로 생성한다. DROP + CREATE
		// create-drop: 애플리케이션을 종료할 때 생성한 DDL을 제거한다. DROP + CREATE + DROP
		// update: 데이터베이스 테이블과 엔티티 매핑정보를 비교해서 변경 사항만 수정한다.
		// validate: 데이터베이스 테이블과 엔티티 매핑정보를 비교해서 차이가 있으면 경고를 남기고 애플리케이션을 실행하지 않는다. 이 설정은 DDL을 수정하지 않는다.
		// none: 자동 생성기능을 사용하지 않는다.
		jpaProperties.put("hibernate.hbm2ddl.auto", "create");

		LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		localContainerEntityManagerFactoryBean.setDataSource(this.dataSource());
		localContainerEntityManagerFactoryBean.setPackagesToScan(this.packagesToScan);
		localContainerEntityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		localContainerEntityManagerFactoryBean.setJpaProperties(jpaProperties);

		return localContainerEntityManagerFactoryBean;
	}
}
