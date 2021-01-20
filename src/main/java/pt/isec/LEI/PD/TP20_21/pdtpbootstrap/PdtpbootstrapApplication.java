package pt.isec.LEI.PD.TP20_21.pdtpbootstrap;


//@SpringBootApplication
//public class PdtpbootstrapApplication {
//    public static void main(String[] args) {
//        SpringApplication.run(PdtpbootstrapApplication.class, args);
//    }
//}

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import pt.isec.LEI.PD.TP20_21.pdtpbootstrap.security.AuthorizationFilter;
//import pt.isec.LEI.PD.TP20_21.pdtpbootstrap.security.AuthorizationFilter;

@SpringBootApplication
public class PdtpbootstrapApplication {
    public static String directoryPath;
    public PdtpbootstrapApplication (){}
    public static void main(String[] args) {
//        if (args.leng.println("The base directory must be provided as the first command line argument!");
////            System.exit(1);
////        }
////        directoryPath = args[0];th < 1) {
//            System.out
        SpringApplication.run(PdtpbootstrapApplication.class, args);
    }

//    @EnableWebSecurity
//    @Configuration
//    class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http.csrf().disable()
//                    .addFilterAfter(new AuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
//                    .authorizeRequests()
//                    .antMatchers(HttpMethod.POST, "/user/login").permitAll()
//                    .anyRequest().authenticated().and()
//                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
//        }
//    }
}