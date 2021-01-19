package pt.isec.lei.pd.tp20_21.Server.Model.Connectivity.getfile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pt.isec.lei.pd.tp20_21.Server.Model.Connectivity.getfile.security.AuthorizationFilter;


@ComponentScan(basePackages = {"pt.isec.lei.pd.tp20_21.Server.Model.Connectivity.getfile.controllers"})
@SpringBootApplication
public class RestApiGetFileApplication {
    
    public static String directoryPath;

    public static void main(String[] args) {
            
        if(args.length < 1){
            System.out.println("The base directory must be provided as the first command line argument!");
            System.exit(1);
        }
            
        directoryPath = args[0];
        
	SpringApplication.run(RestApiGetFileApplication.class, args);
    }
    
    @EnableWebSecurity
    @Configuration
    class WebSecurityConfig extends WebSecurityConfigurerAdapter
    {
        @Override
        protected void configure(HttpSecurity http) throws Exception
        {
            http.csrf().disable()
                    .addFilterAfter(new AuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                    .authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/user/login").permitAll()
                    .anyRequest().authenticated().and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }
    }
}

