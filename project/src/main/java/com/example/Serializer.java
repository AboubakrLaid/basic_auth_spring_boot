package com.example;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;



@Service
public class Serializer {
    @Autowired
    private Validator validator;

    public ResponseEntity<?> serialize(Object obj, String objName) {
        Errors errors = new BeanPropertyBindingResult(obj, objName);
        validator.validate(obj, errors);

        if (errors.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            errors.getAllErrors().forEach(error -> errorMap.put(((FieldError) error).getField(), error.getDefaultMessage()));
            return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
