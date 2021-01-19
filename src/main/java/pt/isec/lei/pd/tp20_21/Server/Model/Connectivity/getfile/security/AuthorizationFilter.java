package pt.isec.lei.pd.tp20_21.Server.Model.Connectivity.getfile.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AuthorizationFilter extends OncePerRequestFilter
{
    private final String HEADER = "Authorization";

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException
    {
        String authorizationHeaderValue = httpServletRequest.getHeader(HEADER);

        if (authorizationHeaderValue != null && Token.validateToken(authorizationHeaderValue))
        {
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("USER"));

            UsernamePasswordAuthenticationToken uPAT =
                    new UsernamePasswordAuthenticationToken(
                            Token.getUsernameByToken(authorizationHeaderValue),
                            null, authorities);

            SecurityContextHolder.getContext().setAuthentication(uPAT);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
