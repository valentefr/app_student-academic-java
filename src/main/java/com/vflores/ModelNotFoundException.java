package com.vflores;


public class ModelNotFoundException extends  RuntimeException
{
    public ModelNotFoundException(String message) {
        super(message);
    }
}
