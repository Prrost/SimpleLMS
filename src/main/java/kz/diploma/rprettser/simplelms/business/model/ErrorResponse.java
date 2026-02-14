package kz.diploma.rprettser.simplelms.business.model;


public record ErrorResponse(String code, String message, String stackTrace) { }