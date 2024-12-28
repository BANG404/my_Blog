package com.ibat.myblog.Model;

import java.util.Date;
import lombok.Data;

@Data
public class TokenValidationResult {
    private boolean isValid;
    private String error;
    private String username;
    private Date expirationDate;
    private Date issuedAt;
}
