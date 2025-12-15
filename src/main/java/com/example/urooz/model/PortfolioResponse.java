package com.example.urooz.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PortfolioResponse {
    private String html;
    private String css;
    private String js;
}