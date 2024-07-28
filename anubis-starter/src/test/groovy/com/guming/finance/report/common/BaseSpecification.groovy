package com.finance.anubis.report.common

import com.github.springtestdbunit.DbUnitTestExecutionListener
import com.github.springtestdbunit.annotation.DbUnitConfiguration
import org.spockframework.spring.SpringMockTestExecutionListener
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import org.springframework.test.context.support.DirtiesContextTestExecutionListener
import org.springframework.test.context.transaction.TransactionalTestExecutionListener
import spock.lang.Specification
import com.finance.anubis.report.common.CommonConfiguration
import com.finance.anubis.report.common.CsvDataSetLoader
/**
 * @author baocheng
 * @date 2022/12/26 17:08
 */
@ActiveProfiles("loc")
@Configuration
@SpringBootTest(properties = "spring.cloud.nacos.config.enabled=false",
        classes = [CommonConfiguration])
@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader)
@TestExecutionListeners([DependencyInjectionTestExecutionListener, DbUnitTestExecutionListener,
        SpringMockTestExecutionListener, DirtiesContextTestExecutionListener, TransactionalTestExecutionListener])
@SuppressWarnings("unused")
class BaseSpecification extends Specification {


}
