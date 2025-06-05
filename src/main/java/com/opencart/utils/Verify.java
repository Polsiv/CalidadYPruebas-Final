package com.opencart.utils;

import java.util.ArrayList;
import java.util.List;

public class Verify {
    private static final List<AssertionError> errors = new ArrayList<>();

    public static void verify(Runnable assertion){
        try{
            assertion.run();
        }catch (AssertionError e){
            System.err.println("Verify failed: "+ e.getMessage());
           errors.add(e);
        }
    }

    public static void verifyAll(){
        if(!errors.isEmpty()){
            AssertionError combined = new AssertionError("Se encontraron los siguientes errores en las verificaciones");
            errors.forEach(combined::addSuppressed);
            errors.clear();// limpia cada test
            throw combined;
        }
    }
}
