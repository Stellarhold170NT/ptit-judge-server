package vn.ptit.judge.config;

import java.util.List;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class SoapConfig extends WsConfigurerAdapter {

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        ServletRegistrationBean<MessageDispatcherServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(servlet);
        registration.setUrlMappings(List.of("/ws/*"));
        registration.setLoadOnStartup(1);
        return registration;
    }

    @Bean
    public XsdSchema dataSchema() {
        return new SimpleXsdSchema(new ClassPathResource("schemas/data.xsd"));
    }

    @Bean
    public XsdSchema characterSchema() {
        return new SimpleXsdSchema(new ClassPathResource("schemas/character.xsd"));
    }

    @Bean
    public XsdSchema objectSchema() {
        return new SimpleXsdSchema(new ClassPathResource("schemas/object.xsd"));
    }

    @Bean(name = "DataService")
    public DefaultWsdl11Definition dataServiceWsdl(XsdSchema dataSchema) {
        DefaultWsdl11Definition wsdl = new DefaultWsdl11Definition();
        wsdl.setPortTypeName("DataService");
        wsdl.setLocationUri("/ws");
        wsdl.setTargetNamespace("http://ptit.vn/judge/soap/data");
        wsdl.setSchema(dataSchema);
        return wsdl;
    }

    @Bean(name = "CharacterService")
    public DefaultWsdl11Definition characterServiceWsdl(XsdSchema characterSchema) {
        DefaultWsdl11Definition wsdl = new DefaultWsdl11Definition();
        wsdl.setPortTypeName("CharacterService");
        wsdl.setLocationUri("/ws");
        wsdl.setTargetNamespace("http://ptit.vn/judge/soap/character");
        wsdl.setSchema(characterSchema);
        return wsdl;
    }

    @Bean(name = "ObjectService")
    public DefaultWsdl11Definition objectServiceWsdl(XsdSchema objectSchema) {
        DefaultWsdl11Definition wsdl = new DefaultWsdl11Definition();
        wsdl.setPortTypeName("ObjectService");
        wsdl.setLocationUri("/ws");
        wsdl.setTargetNamespace("http://ptit.vn/judge/soap/object");
        wsdl.setSchema(objectSchema);
        return wsdl;
    }
}
