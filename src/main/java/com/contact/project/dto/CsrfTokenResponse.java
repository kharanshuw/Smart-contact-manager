package com.contact.project.dto;

import org.springframework.security.web.csrf.CsrfToken;

public class CsrfTokenResponse {
    private String token;
    private String headerName;
    private String parameterName;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    @Override
    public String toString() {
        return "CsrfTokenResponse [token=" + token + ", headerName=" + headerName
                + ", parameterName=" + parameterName + "]";
    }

    public CsrfTokenResponse(CsrfToken csrfToken) {
        this.token = csrfToken.getToken();
        this.headerName = csrfToken.getHeaderName();
        this.parameterName = csrfToken.getParameterName();
    }


}
